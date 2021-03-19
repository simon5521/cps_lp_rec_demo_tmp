from ctypes import *
import time

import numpy as np
import cv2 as cv

try:
    add_lib = CDLL("RawImage_publisher")
    print("Successfully loaded ", add_lib)
except Exception as e:
    print(e)
image = cv.imread('test.jpg')
_, numpy_jpeg_array = cv.imencode('.jpg', image, [int(cv.IMWRITE_JPEG_QUALITY), 90])
image_bytes = numpy_jpeg_array.tobytes()


class publisher(object):
    def __init__(self):
        add_lib.createimagepublisher.restype = c_uint64
        self.obj = add_lib.createimagepublisher()

    def writesample(self, source, destination, debug, validdata, image_bytes):
        source=bytes(source, "utf-8")
        destination = bytes(destination, "utf-8")
        param = (c_ubyte * len(image_bytes))()
        param[0:len(image_bytes)] = image_bytes
        add_lib.sendimagesample(c_uint64(self.obj), c_char_p(source), c_char_p(destination), c_bool(debug), c_bool(validdata), param,
                                c_int32(len(image_bytes)))



p = publisher()

p.writesample("src", "destination", 2, 0, image_bytes)




