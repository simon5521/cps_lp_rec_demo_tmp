from ctypes import *
import time

import numpy as np
import cv2 as cv

try:
    add_lib = CDLL("./images_publisher")
    print("Successfully loaded ", add_lib)
except Exception as e:
    print(e)

image = cv.imread('crop.jpg')
_, numpy_jpeg_array = cv.imencode('.jpg', image, [int(cv.IMWRITE_JPEG_QUALITY), 90])
image_bytes = numpy_jpeg_array.tobytes()


class publisher(object):
    def __init__(self):
        add_lib.createimagepublisher.restype = c_uint64
        self.obj = add_lib.createimagepublisher()

    def writesample(self, image_bytes, source):
        source=bytes(source, "utf-8")
        param = (c_ubyte * len(image_bytes))()
        param[0:len(image_bytes)] = image_bytes
        add_lib.sendimagesample((self.obj),c_int32(len(image_bytes)),param,c_char_p(source))




p = publisher()
print(image_bytes[0:10])
while(1):
	p.writesample(image_bytes, "asd")
	input()




