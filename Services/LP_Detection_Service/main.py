#!/usr/bin/python3
import importlib.util
import os
import sys
import time
import uuid
import json

import cv2
import numpy as np

sys.path.append('.')
from videoUtils.DDS_streamer import start_dds_streamer
from videoUtils.encode_decode import start_decoder, start_encoder
from videoUtils.mjpeg_streamer import start_mjpeg_server
from loggingUtils.DDS_Logging import start_dds_logger
from videoUtils.mqtt_streamer import start_mqtt_streamer

# pip3 install https://dl.google.com/coral/python/tflite_runtime-2.1.0.post1-cp37-cp37m-linux_armv7l.whl

MODEL_NAME = 'LP_Detection_Service/ssd_mobilenet_v2_quantized_TFLite_model'
GRAPH_NAME = 'detect.tflite'
EDGETPU_GRAPH_NAME = 'detect_edgetpu.tflite'
LABELMAP_NAME = 'labelmap.txt'
min_conf_threshold = float(0.75)
use_TPU = True
headlessMode = True

base_path = 'LP_Detection_Service/'
with open( base_path + 'nodeid.json') as json_file:
    nodeid = json.load(json_file)['nodeid']
if nodeid == '':
    nodeid = str(uuid.uuid1())
    with open(base_path + 'nodeid.json', 'w') as json_file:
        json.dump({'nodeid': nodeid}, json_file)

logging_buffer = start_dds_logger(nodeid, 'LP_Detection')
time.sleep(1)

# initialize mjpeg streamers
camera_imageBuffer, mjpeg_camera_process = start_mjpeg_server(
    port=8080, buffer_size=3)

with open('config.json') as json_file:
    config = json.load(json_file)

if config['protocol'] == 'MQTT':
    streamer_input_buffer, streamer_output_buffer = start_mqtt_streamer(nodeid, broker = config['mqtt_host'], port = 1883, topic_pub = 'detected_image', topic_sub = 'raw_image', logging_buffer=logging_buffer)
else:

    streamer_input_buffer, streamer_output_buffer = start_dds_streamer(
        nodeid, "DDS_config.xml",
        data_writer="MyPublisher::ImageWriter",
        data_reader="MySubscriber::RawReader",
        input_buffer_size=10, output_buffer_size=10,
        logging_buffer=logging_buffer)
        
encoder_input_buffer = start_encoder(streamer_output_buffer, encoder_input_buffer_size=10, logging_buffer=logging_buffer)
decoder_output_buffer = start_decoder(streamer_input_buffer, decoder_output_buffer_size=10, logging_buffer=logging_buffer)


time.sleep(1)

# Import TensorFlow libraries
# If tflite_runtime is installed, import interpreter from tflite_runtime, else import from regular tensorflow
# If using Coral Edge TPU, import the load_delegate library
pkg = importlib.util.find_spec('tflite_runtime')
if pkg:
    from tflite_runtime.interpreter import Interpreter
    if use_TPU:
        from tflite_runtime.interpreter import load_delegate
else:
    from tensorflow.lite.python.interpreter import Interpreter
    if use_TPU:
        from tensorflow.lite.python.interpreter import load_delegate

# If using Edge TPU, assign filename for Edge TPU model
if use_TPU:
    GRAPH_NAME = EDGETPU_GRAPH_NAME

# Get path to current working directory
CWD_PATH = os.getcwd()

# Path to .tflite file, which contains the model that is used for object detection
PATH_TO_CKPT = os.path.join(CWD_PATH, MODEL_NAME, GRAPH_NAME)

# Path to label map file
PATH_TO_LABELS = os.path.join(CWD_PATH, MODEL_NAME, LABELMAP_NAME)

# Load the label map
with open(PATH_TO_LABELS, 'r') as f:
    labels = [line.strip() for line in f.readlines()]

# Have to do a weird fix for label map if using the COCO "starter model" from
# https://www.tensorflow.org/lite/models/object_detection/overview
# First label is '???', which has to be removed.
if labels[0] == '???':
    del(labels[0])

# Load the Tensorflow Lite model.
# If using Edge TPU, use special load_delegate argument
if use_TPU:
    interpreter = Interpreter(model_path=PATH_TO_CKPT,
                              experimental_delegates=[load_delegate('libedgetpu.so.1.0')])
    print(PATH_TO_CKPT)
else:
    interpreter = Interpreter(model_path=PATH_TO_CKPT)

interpreter.allocate_tensors()

# Get model details
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()
height = input_details[0]['shape'][1]
width = input_details[0]['shape'][2]

floating_model = (input_details[0]['dtype'] == np.float32)

input_mean = 127.5
input_std = 127.5

# Initialize frame rate calculation
frame_rate_calc = 1
freq = cv2.getTickFrequency()


def preprocessImage(image):
    # Acquire frame and resize to expected shape [1xHxWx3]
    frame = image.copy()
    x_ofs = int(( frame.shape[1]-frame.shape[0])/2)
    if x_ofs > 0:
        frame = frame[0:, x_ofs:-x_ofs]
    frame_gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    frame_rgb = cv2.cvtColor(frame_gray, cv2.COLOR_GRAY2RGB)
    frame_resized = cv2.resize(frame_rgb, (width, height))
    input_data = np.expand_dims(frame_resized, axis=0)
    # Normalize pixel values if using a floating model (i.e. if model is non-quantized)
    if floating_model:
        input_data = (np.float32(input_data) - input_mean) / input_std
    return input_data


def detect(input_data):
    # Perform the actual detection by running the model with the image as input
    interpreter.set_tensor(input_details[0]['index'], input_data)
    interpreter.invoke()

    # Retrieve detection results
    # Bounding box coordinates of detected objects
    boxes = interpreter.get_tensor(output_details[0]['index'])[0]
    classes = interpreter.get_tensor(output_details[1]['index'])[
        0]  # Class index of detected objects
    scores = interpreter.get_tensor(output_details[2]['index'])[
        0]  # Confidence of detected objects
    return boxes, classes, scores


def DrawBoxesandSendCroppedImages(boxes, classes, scores, data, send=True):
    frame = data['pixels'].copy()
    height, width, channels = frame.shape
    x_ofs = int(( frame.shape[1]-frame.shape[0])/2)
    # Loop over all detections and draw detection box if confidence is above minimum threshold
    for i in range(len(scores)):
        if ((scores[i] > min_conf_threshold) and (scores[i] <= 1.0)):

            # Get bounding box coordinates and draw box
            # Interpreter can return coordinates that are outside of image dimensions, need to force them to be within image using max() and min()
            ymin = int(max(1, (boxes[i][0] * height)))
            xmin = int(max(1, (boxes[i][1] * height)))+x_ofs
            ymax = int(min(height, (boxes[i][2] * height)))
            xmax = int(min(height, (boxes[i][3] * height)))+x_ofs

            cv2.rectangle(frame, (xmin, ymin), (xmax, ymax), (10, 255, 0), 2)

            # Draw label
            # Look up object name from "labels" array using class index
            object_name = labels[int(classes[i])]
            label = '%s: %d%%' % (object_name, int(
                scores[i]*100))  # Example: 'person: 72%'
            labelSize, baseLine = cv2.getTextSize(
                label, cv2.FONT_HERSHEY_SIMPLEX, 0.7, 2)  # Get font size
            # Make sure not to draw label too close to top of window
            label_ymin = max(ymin, labelSize[1] + 10)
            # Draw white box to put label text in
            cv2.rectangle(frame, (xmin, label_ymin-labelSize[1]-10), (
                xmin+labelSize[0], label_ymin+baseLine-10), (255, 255, 255), cv2.FILLED)
            cv2.putText(frame, label, (xmin, label_ymin-7),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 0), 2)  # Draw label text

            if(send):
                # send cropped images
                cut_factor = 0.3 / 2
                v_cut = (ymax - ymin) * cut_factor
                h_cut = (xmax - xmin) * cut_factor
                h_cut2 = (ymax - ymin) * (cut_factor * 0.2)

                croppedframe = frame[int(
                    ymin + v_cut):int(ymax - v_cut), int(xmin + h_cut):int(xmax - h_cut2)]
                data['pixels'] = croppedframe
                data['position'] = {
                    'ymin': ymin,
                    'xmin': xmin,
                    'ymax': ymax,
                    'xmax': xmax
                }

                try:
                    encoder_input_buffer.put_nowait(data)
                except:
                    encoder_input_buffer.get()
                    encoder_input_buffer.put(data)
                    #videoUtils.db_manager.save_car_det_loss()
                    logging_buffer.put({'measurement': 'detection_loss', 'component': 'detector', 'time': time.time(), 'data': 'encoder_input_buffer'})

    return frame


try:
    while True:
        data = decoder_output_buffer.get()
        data['cameraid'] = int(data["source"])

        # Start timer (for calculating frame rate)
        t1 = cv2.getTickCount()

        frame = data["pixels"]

        readtime = cv2.getTickCount() - t1
        #videoUtils.db_manager.save_det_net_dly(readtime)
        #logging_buffer.put({'measurement': 'net_delay', 'component': 'detector', 'data': str(readtime)})

        t1 = cv2.getTickCount()

        input_data = preprocessImage(frame)

        boxes, classes, scores = detect(input_data)

        frame = DrawBoxesandSendCroppedImages(
            boxes, classes, scores, data, data["validdata"])

        # Draw framerate in corner of frame
        cv2.putText(frame, 'FPS: {0:.2f}'.format(
            frame_rate_calc), (30, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 0), 2, cv2.LINE_AA)

        try:
            if data["debug"]:
                camera_imageBuffer.put_nowait(frame)
        except:
            pass

        # Calculate framerate
        t2 = cv2.getTickCount()
        time1 = (t2-t1)/freq
        frame_rate_calc = 1/time1

        # videoUtils.db_manager.save_det_rt(time1)
        logging_buffer.put({'measurement': 'runtime', 'component': 'detector', 'data': str(time1)})

        if not headlessMode:
            cv2.imshow('Licence Plate Detector', frame)
            # Press 'q' to quit
            if cv2.waitKey(1) == ord('q'):
                break

finally:
    mjpeg_camera_process.terminate()

# Clean up
mjpeg_camera_process.terminate()

if not headlessMode:
    cv2.destroyAllWindows()
