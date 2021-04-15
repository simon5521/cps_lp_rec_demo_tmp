from ctypes import *
import time

import numpy as np
import cv2 as cv

try:
    add_lib = CDLL("./images_subscriber")
    print("Successfully loaded ", add_lib)
except Exception as e:
    print(e)


class subscriber(object):
    def __init__(self):
        add_lib.createimagesubscriber.restype = c_uint64
        self.obj = add_lib.createimagesubscriber()

    def startListening(self):
        add_lib.startListening(c_uint64(self.obj))

    def getMeta(self):
        add_lib.getMeta.restype = c_char_p
        return add_lib.getMeta(c_uint64(self.obj))

    def getSize(self):
        return  add_lib.getSize(c_uint64(self.obj))


    def getPixels(self):
        add_lib.getPixels.restype = POINTER(c_ubyte)
        param = add_lib.getPixels(c_uint64(self.obj))
        size = self.getSize()
        pp = bytes(param[:size])
        return pp

print("subscriber betoltese")
sub = subscriber()
print("subscriber betoltve, start listening")
sub.startListening()
print("started listening")
input()

with open('test2.jpg', 'wb') as f:
    f.write(sub.getPixels())
