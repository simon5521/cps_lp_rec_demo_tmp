#!/usr/bin/python3
import cv2
from threading import Thread
import time
class VideoStream:
    """Camera object that controls video streaming from the Picamera"""
    def __init__(self,src = 0, resolution=(640,480), cropx = 0, cropy = 0):
        # Initialize the PiCamera and the camera image stream
        self.stream = cv2.VideoCapture(src)
        #ret = self.stream.set(3,resolution[0])
        #ret = self.stream.set(4,resolution[1])
        time.sleep(1)
        # Read first frame from the stream
        (self.grabbed, self.frame) = self.stream.read()
        self.cropx = cropx 
        self.cropy = cropy 
        height, width, channels = self.frame.shape
        self.resolution = resolution
        if(cropy == 0):
            self.cropy = height
        if(cropx == 0):
            self.cropx = width
        self.frame = self.frame[self.cropy:(self.resolution[1] + self.cropy), self.cropx:(self.resolution[0] + self.cropx)]

    # Variable to control when the camera is stopped
        self.stopped = False

    def start(self):
    # Start the thread that reads frames from the video stream
        Thread(target=self.update,args=()).start()
        return self

    def update(self):
        # Keep looping indefinitely until the thread is stopped
        while True:
            # If the camera is stopped, stop the thread
            if self.stopped:
                # Close camera resources
                self.stream.release()
                return

            # Otherwise, grab the next frame from the stream
            (self.grabbed, self.frame) = self.stream.read()
            self.frame = self.frame[self.cropy:(self.resolution[1] + self.cropy), self.cropx:(self.resolution[0] + self.cropx)]

    def read(self):
    # Return the most recent frame
        return self.frame

    def stop(self):
    # Indicate that the camera and thread should be stopped
        self.stopped = True
