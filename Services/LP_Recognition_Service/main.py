# text recognition👽️
import datetime
import easyocr

import cv2

import numpy as np
import time
import re
import matplotlib.pyplot as plt
from torch.distributions.utils import lazy_property
from multiprocessing import Process, Queue


from videoUtils.DDS_streamer import start_dds_streamer
from videoUtils.encode_decode import start_decoder, start_encoder

from loggingUtils.db_manager import save_car_rec_loss,save_lp,save_rec_rt,save_rec_net_dly

import uuid

import rticonnextdds_connector as rti

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
    img = cv2.adaptiveThreshold(img,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C,cv2.THRESH_BINARY,11,2)
    #img = cv2.bitwise_not(img)
    kernel = np.ones((2, 2), np.uint8)
    #img = cv2.erode(img, kernel, iterations=1)
    #img = cv2.dilate(img, kernel, iterations=1)
    #plt.imshow(gray)
    #plt.show()
    return img


def lp_rec_proc(lp_queue,diag_queue):
    lp=""
    reader = easyocr.Reader(['en'])
    while True:
        try:
            frame=lp_queue.get(True,20)['pixels']
        except:
            print("lp queue is empty, timeout happend")
            continue
        t3 = time.time()
        # pytessercat
        #frame=image_preprocess(frame)
        result = reader.readtext(frame)
        #text = pytesseract.image_to_string(frame, config=config)
        if(len(result)>0):
            print(result)
            text = str(result[0][0][1])
            print(text)
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
        else:
            print("No LP found",result)


        try:
            diag_queue.put((frame,text),False)
        except:
            print("diag error")
            continue




if __name__ == '__main__':
    dds_streamer_input_buffer, dds_streamer_output_buffer = start_dds_streamer(
        uuid.uuid1(), "DDS_config.xml",
        data_reader="MySubscriber::ImageReader",
        input_buffer_size=10, output_buffer_size=10)
    decoder_output_buffer = start_decoder(dds_streamer_input_buffer, decoder_output_buffer_size=10)
    diag_queue=Queue(1)
    print("creating processes")
    p_rec_1 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    p_rec_2 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    p_rec_3 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    p_rec_4 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    p_rec_5 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    p_rec_6 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    print("starting processes")
    p_rec_1.start()
    #time.sleep(0.3)
    #p_rec_2.start()
    #time.sleep(0.3)
    #p_rec_3.start()
    #time.sleep(0.3)
    #p_rec_4.start()
    #time.sleep(0.3)
    #p_rec_5.start()
    #time.sleep(0.3)
    #p_rec_6.start()
    #time.sleep(2.5)
    #p_recv.start()
    print("all processes has been started")

    while True:
        frame,text=diag_queue.get()
        h,w,c=frame.shape
        frame=cv2.resize(frame,(w*4,h*4))
        frame=cv2.rectangle(frame,(0,0),(w*3,30),(255,255,255),-1)
        frame=cv2.putText(frame,text,org=(10,27),fontFace=1,fontScale=2,color=(0,50,150),thickness=2)
        cv2.imshow("Output",frame)
        cv2.waitKey(1)


