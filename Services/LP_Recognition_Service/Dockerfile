FROM python:3 AS SUPERIMAGE

WORKDIR /usr/src/lp_rec_server


COPY . .


RUN pip install pytesseract
RUN pip install easyocr
RUN pip install rticonnextdds_connector
RUN pip install opencv-python
RUN pip install numpy
RUN pip install influxdb

FROM python:3

COPY . .

RUN pip install

