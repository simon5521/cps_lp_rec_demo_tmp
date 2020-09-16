# text recognition
import datetime

import cv2
import pytesseract
import numpy as np
import time
import re
import matplotlib.pyplot as plt
from torch.distributions.utils import lazy_property
import multiprocessing
import queue


from DBManagement.db_manager import save_car_rec_loss,save_lp,save_rec_rt,save_rec_net_dly


#url='http://192.168.0.73:8081/cam.mjpg'
#url='http://192.168.1.51:8081/cam.mjpg'
#url='http://192.168.1.248:8080/cam.mjpg'
url='http://192.168.1.231:8081/cam.mjpg'
config = ('--oem 1 --psm 11')

lp_pattern="[A-Z][A-Z][A-Z]\s?\-\s?\d\d\d"

use_preprocess=True

cap = cv2.VideoCapture(url)


def image_preprocess(img):
    gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
    # gray, img_bin = cv2.threshold(gray,128,255,cv2.THRESH_BINARY | cv2.THRESH_OTSU)
    img_bin = cv2.adaptiveThreshold(gray, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 11, 2)
    gray = cv2.bitwise_not(img_bin)
    kernel = np.ones((3, 3), np.uint8)
    img = gray
    img = cv2.erode(gray, kernel, iterations=1)
    img = cv2.dilate(img, kernel, iterations=1)
    #plt.imshow(gray)
    #plt.show()
    return img




while True:
    print("cycle")
    t1=time.time_ns()

    ret, frame = cap.read()

    t2 = time.time_ns()

    nettime_ns=t2-t1


    if not ret :
        continue

    save_rec_net_dly(nettime_ns)

    print("found frame")
    # cv2.imshow('Video', frame)
    print("done")

    t3 = time.time_ns()

    if use_preprocess :
        frame = image_preprocess(frame)

    # pytessercat
    text = pytesseract.image_to_string(frame, config=config)

    t4 = time.time_ns()

    rectime_ns=t4-t3

    save_rec_rt(rectime_ns)

    lp=re.findall(lp_pattern,text)

    if len(lp)>0 :
        lp_num=lp[0]
        save_lp(lp_num)
        print("LP found: ", lp_num)
    else:
        print("No LP found")

    print(datetime.datetime.now())

