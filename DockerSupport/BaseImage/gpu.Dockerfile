FROM nvidia/cuda:10.2-runtime

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
    && rm -rf /var/lib/apt/lists/*

RUN pip install -r requirements.txt