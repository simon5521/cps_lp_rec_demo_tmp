#!/usr/bin/python3
import os
import cv2
import numpy as np
import sys
import time
import importlib.util
import videoUtils.mjpeg_streamer
import videoUtils.videoStream


#pip3 install https://dl.google.com/coral/python/tflite_runtime-2.1.0.post1-cp37-cp37m-linux_armv7l.whl

MODEL_NAME = 'ssd_mobilenet_v2_quantized_TFLite_model'
GRAPH_NAME = 'detect.tflite'
LABELMAP_NAME = 'labelmap.txt'
min_conf_threshold = float(0.6)
use_TPU = False
headlessMode = False

# Initialize video stream
videostream = videoUtils.videoStream.VideoStream(resolution=(640, 480),framerate=30).start()
time.sleep(1)

#initialize mjpeg streamers
camera_imageBuffer, mjpeg_camera_process = videoUtils.mjpeg_streamer.start_mjpeg_server(port=8080, buffer_size = 3)
plate_imageBuffer, mjpeg_plate_process = videoUtils.mjpeg_streamer.start_mjpeg_server(port=8081, buffer_size = 15)
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
    # If user has specified the name of the .tflite file, use that name, otherwise use default 'edgetpu.tflite'
    if (GRAPH_NAME == 'detect.tflite'):
        GRAPH_NAME = 'edgetpu.tflite'       

# Get path to current working directory
CWD_PATH = os.getcwd()

# Path to .tflite file, which contains the model that is used for object detection
PATH_TO_CKPT = os.path.join(CWD_PATH,MODEL_NAME,GRAPH_NAME)

# Path to label map file
PATH_TO_LABELS = os.path.join(CWD_PATH,MODEL_NAME,LABELMAP_NAME)

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
    frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    frame_resized = cv2.resize(frame_rgb, (width, height))
    input_data = np.expand_dims(frame_resized, axis=0)
    # Normalize pixel values if using a floating model (i.e. if model is non-quantized)
    if floating_model:
        input_data = (np.float32(input_data) - input_mean) / input_std
    return input_data

def detect(input_data):
    # Perform the actual detection by running the model with the image as input
    interpreter.set_tensor(input_details[0]['index'],input_data)
    interpreter.invoke()

    # Retrieve detection results
    boxes = interpreter.get_tensor(output_details[0]['index'])[0] # Bounding box coordinates of detected objects
    classes = interpreter.get_tensor(output_details[1]['index'])[0] # Class index of detected objects
    scores = interpreter.get_tensor(output_details[2]['index'])[0] # Confidence of detected objects
    return boxes, classes, scores

def drawBoxes(boxes, classes, scores, image):
    frame = image.copy()
    height, width, channels = frame.shape
    # Loop over all detections and draw detection box if confidence is above minimum threshold
    for i in range(len(scores)):
        if ((scores[i] > min_conf_threshold) and (scores[i] <= 1.0)):

            # Get bounding box coordinates and draw box
            # Interpreter can return coordinates that are outside of image dimensions, need to force them to be within image using max() and min()
            ymin = int(max(1,(boxes[i][0] * height)))
            xmin = int(max(1,(boxes[i][1] * width)))
            ymax = int(min(height,(boxes[i][2] * height)))
            xmax = int(min(width,(boxes[i][3] * width)))
            
            cv2.rectangle(frame, (xmin,ymin), (xmax,ymax), (10, 255, 0), 2)

            # Draw label
            object_name = labels[int(classes[i])] # Look up object name from "labels" array using class index
            label = '%s: %d%%' % (object_name, int(scores[i]*100)) # Example: 'person: 72%'
            labelSize, baseLine = cv2.getTextSize(label, cv2.FONT_HERSHEY_SIMPLEX, 0.7, 2) # Get font size
            label_ymin = max(ymin, labelSize[1] + 10) # Make sure not to draw label too close to top of window
            cv2.rectangle(frame, (xmin, label_ymin-labelSize[1]-10), (xmin+labelSize[0], label_ymin+baseLine-10), (255, 255, 255), cv2.FILLED) # Draw white box to put label text in
            cv2.putText(frame, label, (xmin, label_ymin-7), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 0), 2) # Draw label text
    return frame

def sendCroppedImages(boxes, classes, scores, image):
    frame = image.copy()
    height, width, channels = frame.shape
    # Loop over all detections and send cropped images if confidence is above minimum threshold
    for i in range(len(scores)):
        if ((scores[i] > min_conf_threshold) and (scores[i] <= 1.0)):

            # Get coordinates and send cropped images
            # Interpreter can return coordinates that are outside of image dimensions, need to force them to be within image using max() and min()
            ymin = int(max(1,(boxes[i][0] * height)))
            xmin = int(max(1,(boxes[i][1] * width)))
            ymax = int(min(height,(boxes[i][2] * height)))
            xmax = int(min(width,(boxes[i][3] * width)))
            
            cut_factor = 0.3 /2
            v_cut = (ymax - ymin) * cut_factor
            h_cut = (xmax - xmin) * cut_factor
            
            croppedframe = frame[int(ymin + v_cut):int(ymax - v_cut), int(xmin + h_cut):int(xmax - h_cut)]
            
            try:
                plate_imageBuffer.put_nowait(croppedframe)
            except:
                plate_imageBuffer.get()
                plate_imageBuffer.put(croppedframe)
            

try:
    while True:
        # Start timer (for calculating frame rate)
        t1 = cv2.getTickCount()

        # Grab frame from video stream
        frame = videostream.read()

        input_data = preprocessImage(frame)

        boxes, classes, scores = detect(input_data)
        
        sendCroppedImages(boxes, classes, scores, frame)
        
        frame = drawBoxes(boxes, classes, scores, frame)

        # Draw framerate in corner of frame
        cv2.putText(frame,'FPS: {0:.2f}'.format(frame_rate_calc),(30,50),cv2.FONT_HERSHEY_SIMPLEX,1,(255,255,0),2,cv2.LINE_AA)
        
        try:
            camera_imageBuffer.put_nowait(frame)
        except:
            pass

        # Calculate framerate
        t2 = cv2.getTickCount()
        time1 = (t2-t1)/freq
        frame_rate_calc= 1/time1

        if(not headlessMode):
            cv2.imshow('Licence Plate Detector', frame)
            # Press 'q' to quit
            if cv2.waitKey(1) == ord('q'):
                break
        
finally:
    mjpeg_camera_process.terminate()
    mjpeg_plate_process.terminate()
    
# Clean up
mjpeg_camera_process.terminate()
mjpeg_plate_process.terminate()
if(not headlessMode):
    cv2.destroyAllWindows()
    videostream.stop()
