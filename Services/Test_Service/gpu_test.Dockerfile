FROM nvidia/cuda:10.2-runtime AS CORE

WORKDIR /usr/src/app

RUN apt update && apt install -y --no-install-recommends \
    git build-essential \
    python3-dev python3-pip python3-setuptools

RUN pip3 -q install pip --upgrade

COPY ./requirements.txt .

RUN apt-get update \
    && apt-get install -y \
        build-essential \
        cmake \
        git \
        wget \
        unzip \
        yasm \
        pkg-config \
        libswscale-dev \
        libtbb2 \
        libtbb-dev \
        libjpeg-dev \
        libpng-dev \
        libtiff-dev \
        libavformat-dev \
        libpq-dev \
        libgl1-mesa-glx \
    && rm -rf /var/lib/apt/lists/*

RUN pip install -r requirements.txt

FROM CORE

COPY ./lp3.jpeg .
COPY ./easy_ocr_test.py .
COPY ./start.sh .

#RUN apt install -y locales locales-all
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8

RUN dpkg-reconfigure locales

CMD ["python3", "./easy_ocr_test.py"]


