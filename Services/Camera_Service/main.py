#!/usr/bin/env python3
import uuid

import cv2
import numpy as np

from videoUtils.DDS_streamer import start_dds_streamer
from videoUtils.encode_decode import start_encoder

CID = 0
dds_streamer_input_buffer, dds_streamer_output_buffer = start_dds_streamer(
                str(CID), 'DDS_config.xml',
                data_writer='MyPublisher::RawWriter',
                data_reader='MySubscriber::CameraControllReader',
                input_buffer_size=10, output_buffer_size=10)

encoder_input_buffer = start_encoder(dds_streamer_output_buffer, encoder_input_buffer_size=10)



data = None
debug = True
validdata = False

while True:

    if debug:
        try:
            data = dds_streamer_input_buffer.get_nowait()
        except:
            data = {'destination': str(
                CID), 'validdata': True, 'debug': 'True'}
    else:
        data = dds_streamer_input_buffer.get()

    if data['debug'] == 'True':
        debug = True
    if data['debug'] == 'False':
        debug = False
        
    validdata = data['validdata']

    if(debug or validdata):

        encoder_input_buffer.put(
            {"pixels": frame, 'debug': debug, 'validdata': validdata})
