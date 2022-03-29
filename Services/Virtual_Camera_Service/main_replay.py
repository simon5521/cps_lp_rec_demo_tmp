#!/usr/bin/env python3
import uuid
import json
import sys

import time
import cv2
import os

fps=30.0
T=1.0/fps


sys.path.append('.')
from videoUtils.DDS_streamer import start_dds_streamer
from videoUtils.encode_decode import start_encoder
from loggingUtils.DDS_Logging import start_dds_logger
from videoUtils.mqtt_streamer import start_mqtt_streamer


base_path = 'Camera_Service/'
with open( base_path + 'nodeid.json') as json_file:
    nodeid = json.load(json_file)['nodeid']
if nodeid == '':
    nodeid = str(uuid.uuid1())
    with open(base_path + 'nodeid.json', 'w') as json_file:
        json.dump({'nodeid': nodeid}, json_file)


CID = 0
nodenum=1
nodeid=["virtual_camera_"+str(i) for i in range(nodenum)]

with open('config.json') as json_file:
    config = json.load(json_file)

if config['protocol'] == 'MQTT':
    print('MQTT config')
    streamer_input_buffer=[0 for i in range(nodenum)]
    streamer_output_buffer=[0 for i in range(nodenum)]
    for i in range(nodenum):
        print("nodeid: ",nodeid[i])
        streamer_input_buffer[i], streamer_output_buffer[i] = start_mqtt_streamer(nodeid[i], broker = config['mqtt_host'], port = 1883, topic_pub = 'raw_image', topic_sub = 'camera_controll')#, logging_buffer=logging_buffer)
else:
    print('DDS config')
    streamer_input_buffer, streamer_output_buffer = start_dds_streamer(
                    str(CID), 'DDS_config.xml',
                    data_writer='MyPublisher::RawWriter',
                    data_reader='MySubscriber::CameraControllReader',
                    input_buffer_size=10, output_buffer_size=50#,
                    #logging_buffer=logging_buffer
    )

encoder_input_buffer=[0 for i in range(nodenum)]
for i in range(nodenum):
    encoder_input_buffer[i] = start_encoder(streamer_output_buffer[i], encoder_input_buffer_size=10)

id=1#int(time.time())

while True:

    vidcap = cv2.VideoCapture("carla_video_1.avi")

    starttime = time.time()
    success=True
    while vidcap.isOpened():
        success, image = vidcap.read()
        time.sleep(T - ((time.time() - starttime) % T))
        if success:
            encoder_input_buffer.put(
                {"pixels": image, 'debug': True, 'validdata': True, 'frame_id': id})
            id = id + 1
            #cv2.imshow("", image)
            #cv2.waitKey(1)
        else:
            break

    vidcap.release()