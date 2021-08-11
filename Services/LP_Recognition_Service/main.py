# text recognition👽️
import datetime
import json

import easyocr

import cv2

import numpy as np
import time
import re
import matplotlib.pyplot as plt
from torch.distributions.utils import lazy_property
from multiprocessing import Process, Queue
from queue import Full


from videoUtils.DDS_streamer import start_dds_streamer
from videoUtils.encode_decode import start_decoder, start_encoder

from loggingUtils.DDS_Logging import start_dds_logger

import uuid

import rticonnextdds_connector as rti

from videoUtils.mqtt_streamer import start_mqtt_streamer



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
    plt.imshow(gray)
    plt.show()
    return img


def lp_rec_proc(lp_queue,out_queue):
    lp=""
    print("initializing easy ocr reader")
    reader = easyocr.Reader(['en'])
    print("easy ocr reader is initialized")
    while True:
        try:
            data=lp_queue.get(True,20)
            print("new incoming image...")
        except:
            print("lp queue is empty, timeout happend")
            continue

        image=data["image"]
        data=data["data"]

        for lp_data in data["license_plate"]:

            xmax=data["position"]["xmax"]
            xmin=data["position"]["xmin"]
            ymax=data["position"]["ymax"]
            ymin=data["position"]["ymin"]

            cut_factor = 0.3 / 2
            v_cut = (ymax - ymin) * cut_factor
            h_cut = (xmax - xmin) * cut_factor
            h_cut2 = (ymax - ymin) * (cut_factor * 0.2)

            frame=image[int(
                ymin + v_cut):int(ymax - v_cut), int(xmin + h_cut):int(xmax - h_cut2)]
            t3 = time.time()

            # pytessercat
            #frame=image_preprocess(frame)
            #text = pytesseract.image_to_string(frame, config=config)
            text=''
            try:
                result = reader.readtext(frame)
                print(result)
                text = str(result[0][0][1])
                print(text)
                text = re.sub(r'[^a-zA-Z0-9-]', '', text)
                text=text.upper()
                t4 = time.time()
                rectime_s=t4-t3
                #save_rec_rt(rectime_s)
                """logging_buffer.put({'measurement': 'runtime',
                                    'component': 'recognition',
                                    'time': time.time(),
                                    'data': str(rectime_s)})"""
                si=0
                for r in result:
                    text = r[1]
                    text = re.sub(r'[^a-zA-Z0-9-]', '', text)
                    if len(text) > 5 and len(text) < 9:
                        print("LP found ",si," : ", text)
                        lp_data["lp_text"]=text
                        break
                    else:
                        print("No LP found ",si," : ", text)

                """lp=text
                #lp=re.findall(lp_pattern,text)
                if len(lp)>0 :
                    lp_num=lp[0]
                    #save_lp(lp_num)
                    logging_buffer.put({'measurement': 'detection_loss',
                                        'component': 'recognition',
                                        'time': time.time(),
                                        'data': lp_num})

                    print("LP found: ", lp_num)
                else:
                    print("No LP found",text)"""
            except:

                lp_data["lp_text"] = ""
                print("No LP found at all :",result)

        try:
            out_queue.put_nowait(data)
        except Full:
            print("-------------------------------------------------")
            print("out queue error: Full")
            continue




if __name__ == '__main__':

    print("recognition server has been started")

    nodeid = "recognition-pc"

    #logging_buffer = start_dds_logger(nodeid, 'LP_Recognition')
    time.sleep(1)
    print("logger has been started")

    with open('config.json') as json_file:
        config = json.load(json_file)

    if config['protocol'] == 'MQTT':
        streamer_input_buffer , streamer_output_buffer = start_mqtt_streamer(nodeid, broker=config['mqtt_host'], topic_sub='detected_image', topic_pub='recognized_image')  # , logging_buffer=logging_buffer)
    else:
        streamer_input_buffer, dds_streamer_output_buffer = start_dds_streamer(
            uuid.uuid1(), "DDS_config.xml",
            data_reader="MySubscriber::ImageReader",
            input_buffer_size=15, output_buffer_size=10)
    decoder_output_buffer = start_decoder(streamer_input_buffer, decoder_output_buffer_size=10)
    diag_queue=Queue(1)
    print("creating processes")
    p_rec_1 = Process(target=lp_rec_proc, args=(decoder_output_buffer,streamer_output_buffer))
    #p_rec_2 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    #p_rec_3 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    #p_rec_4 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    #p_rec_5 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
    #p_rec_6 = Process(target=lp_rec_proc, args=(decoder_output_buffer,diag_queue))
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
        print('>>>>getframe for diag>>>>>')
        h,w,c=frame.shape
        frame=cv2.resize(frame,(w*4,h*4))
        frame=cv2.rectangle(frame,(0,0),(w*3,30),(255,255,255),-1)
        frame=cv2.putText(frame,text,org=(10,27),fontFace=1,fontScale=2,color=(0,50,150),thickness=2)

        cv2.imshow("Output",frame)
        print('>>>>putframe for diag>>>>')
        cv2.waitKey(1)
        print('>>>>end>>>>')


