FROM debian:buster AS PRE_STAGE

RUN apt-get update; apt-get install -y apt-utils
RUN apt-get update; apt-get install curl -y
RUN apt-get update && apt-get install -y gnupg2

#RUN echo "deb https://packages.cloud.google.com/apt coral-edgetpu-stable main" | tee /etc/apt/sources.list.d/coral-edgetpu.list
#RUN curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
#RUN apt-get update
#RUN apt-get install python3-tflite-runtime -y


FROM PRE_STAGE AS CV2_STAGE
RUN apt-get update; apt-get install -y python3
RUN apt-get update; apt-get install  -y python3-setuptools && apt-get install -y python3-pip
RUN apt-get update; apt-get install -y python3-opencv

FROM CV2_STAGE  AS PIP_STAGE
RUN apt-get update && apt-get install python3-numpy -y
COPY ./LP_Detection_Service/requirements.txt .
RUN pip3 install -r requirements.txt

FROM PIP_STAGE AS Recorded_Virtual_Camera_Service

COPY ./Virtual_Camera_Service/main_replay.py .
COPY ./DDS_config.xml ./videoUtils/DDS_config.xml
COPY ./videoUtils ./videoUtils
COPY ./Virtual_Camera_Service/carla_video_1.avi .

CMD ["python3", "main_recorded.py"]