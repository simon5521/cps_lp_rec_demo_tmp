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

FROM CV2_STAGE AS TF_STAGE
RUN echo "deb https://packages.cloud.google.com/apt coral-edgetpu-stable main" | tee /etc/apt/sources.list.d/coral-edgetpu.list && \
    curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add - && \
    apt update && \
    apt install -y libedgetpu1-std && \
    apt install -y python3-pycoral

FROM TF_STAGE  AS PIP_STAGE
RUN apt-get update && apt-get install python3-numpy -y && apt-get install python3-pandas -y && apt-get install python3-pil -y
COPY ./LP_Detection_Service/requirements.txt .
RUN pip3 install -r requirements.txt

FROM PIP_STAGE AS LP_Detection_Service

COPY ./LP_Detection_Service/main.py .

#RUN ls -a
#RUN ls LP_Detection_Srevice -a
#RUN ls LP_Detection_Srevice/ssd_mobilenet_v2_quantized_TFLite_model -a
#RUN cat LP_Detection_Srevice/ssd_mobilenet_v2_quantized_TFLite_model/labelmap.txt

RUN cat DDS_config.xml

CMD ["python3", "main.py"]