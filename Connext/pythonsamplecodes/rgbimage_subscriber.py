from ctypes import *
import time

import numpy as np
import cv2 as cv

try:
    add_lib = CDLL("RGBImage_subscriber")
    print("Successfully loaded ", add_lib)
except Exception as e:
    print(e)


class subscriber(object):
    def __init__(self):
        add_lib.createimagesubscriber.restype = c_uint64
        self.obj = add_lib.createimagesubscriber()

    def startListening(self):
        add_lib.startListening(c_uint64(self.obj))

    def getDebug(self):
        return add_lib.getDebug(c_uint64(self.obj))

    def getValiddata(self):
        return add_lib.getValiddata(c_uint64(self.obj))

    def getDestination(self):
        add_lib.getDestination.restype=c_char_p
        return add_lib.getDestination(c_uint64(self.obj)).decode("utf-8")

    def getSource(self):
        add_lib.getSource.restype = c_char_p
        return add_lib.getSource(c_uint64(self.obj)).decode("utf-8")

    def getSize(self):
        return add_lib.getSize(c_uint64(self.obj))


    def getPixels(self):
        add_lib.getPixels.restype = POINTER(c_ubyte)
        param = add_lib.getPixels(c_uint64(self.obj))
        size = self.getSize()
        pp = bytes(param[:size])
        return pp
    def getCameraid(self):
        return add_lib.getCameraid(c_uint64(self.obj))
    def getYmin(self):
        add_lib.getYmin.restype=c_float
        return add_lib.getYmin(c_uint64(self.obj))
    def getXmin(self):
        add_lib.getXmin.restype = c_float
        return add_lib.getXmin(c_uint64(self.obj))
    def getYmax(self):
        add_lib.getYmax.restype = c_float
        return add_lib.getYmax(c_uint64(self.obj))
    def getXmyx(self):
        add_lib.getXmax.restype = c_float
        return add_lib.getXmax(c_uint64(self.obj))
    def getPlatenumber(self):
        add_lib.getPlatenumber.restype = c_char_p
        return add_lib.getPlatenumber(c_uint64(self.obj)).decode("utf-8")


sub =subscriber()
sub.startListening()

while 1:
    new=sub.getCameraid()
    time.sleep(1)
    print(sub.getCameraid()-new)
    #print(sub.getCameraid())


with open('test2.jpg', 'wb') as f:
    f.write(sub.getPixels())
