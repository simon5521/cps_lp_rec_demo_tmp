ARG BASE_IMG=nvcr.io/nvidia/l4t-pytorch:r32.5.0-pth1.7-py3
#ARG BASE_IMG=nvcr.io/nvidia/l4t-base:r32.5.0

FROM  ${BASE_IMG} AS PYTHON_STAGE

RUN apt update && apt install -y locales locales-all
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8

#RUN apt update && apt install -y --no-install-recommends \
#    git build-essential \
#    python3.7 python3.7-dev python3-pip python3-setuptools


#RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.7 1

#RUN pip3 -q install pip --upgrade
#RUN python3 -m pip install --upgrade pip


RUN python3 --version

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
    apt-get update && apt-get install -y python3-opencv

FROM CV2_STAGE AS  PYTORCH_STAGE

#RUN python3 -m pip install torch torchvision -f https://download.pytorch.org/whl/lts/1.8/torch_lts.html



FROM PYTORCH_STAGE AS PIP_STAGE

RUN apt update && apt install -y python3-skimage && apt install python-opencv

RUN python3 -m pip install scikit-build
COPY ./LP_Recognition_Service/requirements2 .

RUN export DEBIAN_FRONTEND=noninteractive && \
    python3 -m pip install -r requirements2

FROM PIP_STAGE AS lp_recognition_service_pc_gpu


COPY ./LP_Recognition_Service/lp3.jpeg .
COPY ./LP_Recognition_Service/easy_ocr_test.py .



CMD ["python3", "./easy_ocr_test.py"]




