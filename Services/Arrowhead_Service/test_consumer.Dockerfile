FROM python:alpine3.15 AS PIP_STAGE

COPY requirements.txt .

RUN pip3 install -r requirements.txt


FROM PIP_STAGE AS AHT_CONSUMER

COPY consumer.py .

CMD ["python3", "consumer.py" ]