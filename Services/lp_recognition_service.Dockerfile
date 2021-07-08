FROM nvcr.io/nvidia/l4t-base:r32.5.0 AS PYTHON_STAGE

RUN apt update && apt install -y locales locales-all
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8

RUN apt update && apt install -y --no-install-recommends \
    git build-essential \
    python3.7 python3.7-dev python3-pip python3-setuptools


RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.7 1

#RUN pip3 -q install pip --upgrade
RUN python3 -m pip install --upgrade pip


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
    apt-get update && apt-get install python3-opencv -y

FROM CV2_STAGE AS  PYTORCH_STAGE

#RUN python3 -m pip install torch torchvision -f https://download.pytorch.org/whl/lts/1.8/torch_lts.html

ARG PYTORCH_URL=https://nvidia.box.com/shared/static/lufbgr3xu2uha40cs9ryq1zn4kxsnogl.whl
ARG PYTORCH_WHL=torch-1.2.0-cp36-cp36m-linux_aarch64.whl

RUN wget --quiet --show-progress --progress=bar:force:noscroll --no-check-certificate ${PYTORCH_URL} -O ${PYTORCH_WHL} && \
    pip3 install ${PYTORCH_WHL} --verbose && \
    rm ${PYTORCH_WHL}


#
# torchvision 0.4
#
ARG TORCHVISION_VERSION=v0.4.0
ARG PILLOW_VERSION=pillow<7
ARG TORCH_CUDA_ARCH_LIST="5.3;6.2;7.2"

RUN printenv && echo "torchvision version = $TORCHVISION_VERSION" && echo "pillow version = $PILLOW_VERSION" && echo "TORCH_CUDA_ARCH_LIST = $TORCH_CUDA_ARCH_LIST"

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
		  git \
		  build-essential \
            libjpeg-dev \
		  zlib1g-dev \
    && rm -rf /var/lib/apt/lists/*

RUN git clone -b ${TORCHVISION_VERSION} https://github.com/pytorch/vision torchvision && \
    cd torchvision && \
    python3 setup.py install && \
    cd ../ && \
    rm -rf torchvision && \
    pip3 install "${PILLOW_VERSION}"


FROM PYTORCH_STAGE AS PIP_STAGE

COPY ./LP_Recognition_Service/requirements2 .

RUN export DEBIAN_FRONTEND=noninteractive && \
    python3 -m pip install -r requirements2

FROM PIP_STAGE AS lp_recognition_service_pc_gpu


COPY ./LP_Recognition_Service/lp3.jpeg .
COPY ./LP_Recognition_Service/easy_ocr_test.py .



CMD ["python3", "./easy_ocr_test.py"]




