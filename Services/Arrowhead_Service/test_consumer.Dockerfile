FROM debian:latest AS PRE_STAGE

RUN apt-get update; apt-get install -y apt-utils
RUN apt-get update; apt-get install curl -y
RUN apt-get update; apt-get install -y python3
RUN apt-get update; apt-get install  -y python3-setuptools && apt-get install -y python3-pip

FROM PRE_STAGE AS PIP_STAGE

COPY requirements.txt .

RUN pip3 install -r requirements.txt


FROM PIP_STAGE AS AHT_CONSUMER

COPY consumer.py .

COPY core_services_defaults.py ./usr/local/lib/python3.9/dist-packages/arrowhead_client/client/core_services_defaults.py

CMD ["python3", "consumer.py" ]