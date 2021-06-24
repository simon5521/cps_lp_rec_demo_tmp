FROM debian:buster AS PRE_STAGE

RUN apt-get update; apt-get install -y apt-utils
RUN apt-get update; apt-get install curl -y
RUN apt-get update && apt-get install -y gnupg2

FROM PRE_STAGE AS CV2_STAGE
RUN apt-get update; apt-get install -y python3
RUN apt-get update; apt-get install  -y python3-setuptools && apt-get install -y python3-pip
RUN apt-get update; apt-get install -y python3-opencv

FROM CV2_STAGE  AS PIP_STAGE
RUN apt-get update && apt-get install python3-numpy -y && apt-get install python3-pandas -y && apt-get install python3-pil -y
COPY ./LP_Detection_Service/requirements.txt .
RUN pip3 install -r requirements.txt

FROM PIP_STAGE AS LP_Detection_Service

COPY ./Camera_Service/main.py .
COPY ./DDS_config.xml ./videoUtils/DDS_config.xml
COPY . .

RUN cat DDS_config.xml