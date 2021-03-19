from ctypes import *
import time

import numpy as np
import cv2 as cv

try:
    add_lib = CDLL("RGBImage_publisher")
    print("Successfully loaded ", add_lib)
except Exception as e:
    print(e)


class position:
    def __init__(self, ymin: float, xmin: float, ymax: float, xmax: float):
        self.ymin = ymin
        self.xmin = xmin
        self.ymax = ymax
        self.xmax = xmax


class publisher(object):
    def __init__(self):
        add_lib.createimagepublisher.restype = c_uint64
        self.obj = add_lib.createimagepublisher()

    def writesample(self, source: str, destination: str, debug: bool, validdata: bool, image_bytes_: bytes,
                    cameraid: int, position: position, platenumber: str):
        source = bytes(source, "utf-8")
        destination = bytes(destination, "utf-8")
        platenumber = bytes(platenumber, "utf-8")
        param = (c_ubyte * len(image_bytes_))()
        param[0:len(image_bytes)] = image_bytes_
        add_lib.sendimagesample(c_uint64(self.obj), c_char_p(source), c_char_p(destination), c_bool(debug),
                                c_bool(validdata), param,
                                c_int32(len(image_bytes)), c_int(cameraid), c_float(position.ymin),
                                c_float(position.xmin), c_float(position.ymax), c_float(position.xmax),
                                c_char_p(platenumber))


source: str = "scr"
cameraid = 10
p = publisher()
pos = position(4.0, 1.0, 1.0, 1.0)
# starttime=time.time()
image = cv.imread('test.jpg')
# print("time neded for reading iamge:", time.time()-starttime)
# starttime=time.time()
_, numpy_jpeg_array = cv.imencode('.jpg', image, [int(cv.IMWRITE_JPEG_QUALITY), 90])
image_bytes = numpy_jpeg_array.tobytes()
print(len(image_bytes))
# print("time neded for coding iamge into bytes:", time.time()-starttime)
# starttime=time.time()
while 1:
    p.writesample(source, "destination", 2, 0, image_bytes, cameraid, pos, "dc6-kro")
    cameraid = cameraid + 1
    time.sleep(0.0005)
