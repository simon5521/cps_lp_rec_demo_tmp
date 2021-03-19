from ctypes import *
import time

import numpy as np
import cv2 as cv

try:
    add_lib = CDLL("RawImage_subscriber")
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


sub =subscriber()
sub.startListening()



with open('test2.jpg', 'wb') as f:
    f.write(sub.getPixels())
