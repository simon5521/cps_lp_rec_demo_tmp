# text recognition
import datetime

import cv2
import pytesseract
import numpy as np
import time
import re
import matplotlib.pyplot as plt
from torch.distributions.utils import lazy_property
from multiprocessing import Process, Queue

from DBManagement.db_manager import save_car_rec_loss,save_lp,save_rec_rt,save_rec_net_dly

print("server has been started")

#url='http://192.168.0.73:8081/cam.mjpg'
#url='http://192.168.1.51:8081/cam.mjpg'
#url='http://192.168.1.248:8080/cam.mjpg'
#url='http://192.168.1.231:8081/cam.mjpg'
url='http://192.168.2.3:8081/cam.mjpg'
config = ('--oem 1 --psm 13')

lp_pattern="[A-Z]\s?[A-Z]\s?[A-Z]\s?\-?\s?\d\s?\d\s?\d"

use_preprocess=False





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


def lp_rcv_proc(lp_queue):
    print("create capture")
    cap = cv2.VideoCapture(url)
    print("capture has been created")
    while True:
        while not cap.isOpened():
            print("cap object is closed trying to reopen...")
            cap = cv2.VideoCapture(url)
        t1 = time.time()
        ret, frame = cap.read()
        t2 = time.time()
        nettime_s=t2-t1
        if not ret :
            print("communication error")
            cap = cv2.VideoCapture(url)
            continue
        else:
            print("get-")
            save_rec_net_dly(nettime_s)

        try:
            lp_queue.put(frame,True,0)
        except:
            save_car_rec_loss()
            print("dataloss")
        time.sleep(0.001)

def lp_rec_proc(lp_queue,diag_queue):
    while True:
        try:
            frame=lp_queue.get(True,20)
        except:
            print("lp queue is empty, timeout happend")
            continue
        t3 = time.time()
        # pytessercat
        text = pytesseract.image_to_string(frame, config=config)
        text = re.sub(r'[^a-zA-Z0-9-]', '', text)
        text=text.upper()
        t4 = time.time()
        rectime_s=t4-t3
        save_rec_rt(rectime_s)
        lp=re.findall(lp_pattern,text)
        if len(lp)>0 :
            lp_num=lp[0]
            save_lp(lp_num)
            print("LP found: ", lp_num)
        else:
            print("No LP found",text)

        print(datetime.datetime.now())

        try:
            diag_queue.put((frame,text),False)
        except:
            continue




if __name__ == '__main__':
    print("creating queue")
    lp_queue = Queue(20)
    print("queue has been created")
    diag_queue=Queue(1)
    print("creating processes")
    p_recv = Process(target=lp_rcv_proc, args=(lp_queue,))
    p_rec_1 = Process(target=lp_rec_proc, args=(lp_queue,diag_queue))
    p_rec_2 = Process(target=lp_rec_proc, args=(lp_queue,diag_queue))
    p_rec_3 = Process(target=lp_rec_proc, args=(lp_queue,diag_queue))
    p_rec_4 = Process(target=lp_rec_proc, args=(lp_queue,diag_queue))
    p_rec_5 = Process(target=lp_rec_proc, args=(lp_queue,diag_queue))
    p_rec_6 = Process(target=lp_rec_proc, args=(lp_queue,diag_queue))
    print("starting processes")
    p_rec_1.start()
    time.sleep(0.3)
    #p_rec_2.start()
    #time.sleep(0.3)
    #p_rec_3.start()
    #time.sleep(0.3)
    #p_rec_4.start()
    #time.sleep(0.3)
    #p_rec_5.start()
    #time.sleep(0.3)
    #p_rec_6.start()
    time.sleep(2.5)
    p_recv.start()
    print("all processes has been started")

    while True:
        frame,text=diag_queue.get()
        h,w,c=frame.shape
        frame=cv2.resize(frame,(w*4,h*4))
        frame=cv2.rectangle(frame,(0,0),(w*3,30),(255,255,255),-1)
        frame=cv2.putText(frame,text,org=(10,27),fontFace=1,fontScale=2,color=(0,50,150),thickness=2)
        cv2.imshow("Output",frame)
        cv2.waitKey(1)

"""
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
    #cv2.imshow('Video', frame)
    print("done")

    t3 = time.time_ns()

    if use_preprocess :
        frame = image_preprocess(frame)

    # pytessercat
    text = pytesseract.image_to_string(frame, config=config)

    text=text.upper()

    t4 = time.time_ns()

    rectime_ns=t4-t3

    save_rec_rt(rectime_ns)

    lp=re.findall(lp_pattern,text)

    if len(lp)>0 :
        lp_num=lp[0]
        save_lp(lp_num)
        print("LP found: ", lp_num)
    else:
        print("No LP found",text)

    print(datetime.datetime.now())
"""
