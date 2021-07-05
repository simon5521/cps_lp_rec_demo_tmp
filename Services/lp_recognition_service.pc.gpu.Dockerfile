FROM nvidia/cuda:10.1-base-ubuntu18.04 AS PYTHON_STAGE


RUN apt-get update && apt-get install -y --no-install-recommends git

RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    python3-dev python3-pip python3-setuptools

#RUN apt-get update && apt-get install -y --no-install-recommends \
#    git build-essential \
 #   python3-dev python3-pip python3-setuptools

RUN pip3 -q install pip --upgrade

FROM PYTHON_STAGE AS CV2_STAGE


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

RUN export DEBIAN_FRONTEND=noninteractive && \
    apt-get update && apt-get install python3-opencv -y

FROM CV2_STAGE AS  PYTORCH_STAGE

RUN pip install torch torchvision torchaudio -f https://download.pytorch.org/whl/lts/1.8/torch_lts.html

FROM PYTORCH_STAGE AS PIP_STAGE

COPY ./LP_Recognition_Service/requirements.txt .

RUN export DEBIAN_FRONTEND=noninteractive && \
    pip install -r requirements2

FROM PIP_STAGE AS lp_recognition_service_pc_gpu

RUN apt update && apt install -y locales locales-all
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8

COPY ./LP_Recognition_Service/lp3.jpeg .
COPY ./LP_Recognition_Service/easy_ocr_test.py .



CMD ["python3", "./easy_ocr_test.py"]




