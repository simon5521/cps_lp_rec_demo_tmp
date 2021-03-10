#FROM simon5521/cps_demo_lp_rec_server_1:cps
FROM simon5521/cps_demo_gpu_base_image:initial
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


COPY ./lp3.jpeg .
COPY ./easy_ocr_test.py .

#RUN apt-get install python3-opencv -y

CMD ["python3", "./easy_ocr_test.py"]