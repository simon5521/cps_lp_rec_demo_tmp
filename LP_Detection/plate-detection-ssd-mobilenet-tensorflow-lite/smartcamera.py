#!/usr/bin/env python3
import uuid
import videoUtils.DDS_streamer
import numpy as np
import cv2

CID = 0
dds_streamer_input_buffer, dds_streamer_output_buffer = videoUtils.DDS_streamer.start_dds_streamer(str(CID), "ImagesExample.xml", data_writer = "MyPublisher::RawWriter", data_reader = "MySubscriber::CameraControllReader", input_buffer_size = 10, output_buffer_size = 10)

cap = cv2.VideoCapture(0)
ret = cap.set(3,1440)
ret = cap.set(4,1080)
resolution=(640,480)
pos_x = 300
pos_y = 600

data = None
debug = True
validdata = True

while(True):

    if(debug):
        try:
            data = dds_streamer_input_buffer.get_nowait()
        except:
            data = {"destination":str(CID), "validdata":False}
    else:
        data = dds_streamer_input_buffer.get_nowait()
    
    try:
        debug = data["debug"]
    except:
        pass

    try:
        validdata = data["validdata"]
    except:
        validdata = False

    if(debug or validdata): 
        ret, frame = cap.read()
        if(ret):
            frame = frame[pos_y:(resolution[1] + pos_y), pos_x:(resolution[0] + pos_x)] #crop and resize image

            jpeglist = cv2.imencode(".jpg",frame, [int(cv2.IMWRITE_JPEG_QUALITY), 60])[1].reshape(-1).tolist()

            dds_streamer_output_buffer.put({"pixels":jpeglist, "debug":debug, "validdata":validdata}) 
    