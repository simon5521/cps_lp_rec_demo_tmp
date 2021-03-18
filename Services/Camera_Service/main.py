#!/usr/bin/env python3
import uuid
import json
import sys

import cv2
import numpy as np

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

CID = nodeid
logging_buffer = start_dds_logger(nodeid, 'Camera_Service')

with open('config.json') as json_file:
    config = json.load(json_file)

if config['protocol'] == 'MQTT':
    streamer_input_buffer, streamer_output_buffer = start_mqtt_streamer(nodeid, broker = config['mqtt_host'], port = 1883, topic_pub = 'raw_image', topic_sub = 'camera_controll', logging_buffer=logging_buffer)
else:
    streamer_input_buffer, streamer_output_buffer = start_dds_streamer(
                    str(CID), 'DDS_config.xml',
                    data_writer='MyPublisher::RawWriter',
                    data_reader='MySubscriber::CameraControllReader',
                    input_buffer_size=10, output_buffer_size=10, 
                    logging_buffer=logging_buffer)

encoder_input_buffer = start_encoder(streamer_output_buffer, encoder_input_buffer_size=10, logging_buffer=logging_buffer)



data = None
debug = True
validdata = True
cap = cv2.VideoCapture(0)

while True:

    if debug:
        try:
            data = streamer_input_buffer.get_nowait()
        except:
            data = {'destination': str(
                CID), 'validdata': True, 'debug': 'True'}
    else:
        data = streamer_input_buffer.get()

    if data['debug'] == 'True':
        debug = True
    if data['debug'] == 'False':
        debug = False
        
    validdata = data['validdata']

    if(debug or validdata):
        _, frame = cap.read()
        encoder_input_buffer.put(
            {"pixels": frame, 'debug': debug, 'validdata': validdata})
