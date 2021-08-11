#!/usr/bin/env python3
import base64
from multiprocessing import Process, Queue

import cv2
import numpy as np


def encode_immage(encoder_input_buffer, encoder_output_buffer, logging_buffer):
    while(True):
        data = encoder_input_buffer.get()
        _, numpy_jpeg_array = cv2.imencode('.jpg', data['pixels'], [
                                           int(cv2.IMWRITE_JPEG_QUALITY), 30])
        im_bytes = numpy_jpeg_array.tobytes()
        im_b64 = base64.b64encode(im_bytes)
        base64_message = im_b64.decode('ascii')

        data['pixels'] = base64_message

        encoder_output_buffer.put(data)


def decode_immage(decoder_input_buffer, decoder_output_buffer, logging_buffer):
    while True:
        data = decoder_input_buffer.get()

        base64_message = data['pixels']
        im_b64 = base64_message.encode('ascii')
        im_bytes = base64.b64decode(im_b64)
        # im_arr is one-dim Numpy array
        im_arr = np.frombuffer(im_bytes, dtype=np.uint8)
        image = cv2.imdecode(im_arr, flags=cv2.IMREAD_COLOR)

        decoder_output_buffer.put({'data': data, 'image': image})


def start_encoder(encoder_output_buffer, encoder_input_buffer_size=10, logging_buffer=None):
    encoder_input_buffer = Queue(encoder_input_buffer_size)

    encoder_process = Process(name='image_encoder', target=encode_immage,
                              args=(encoder_input_buffer, encoder_output_buffer, logging_buffer))

    encoder_process.daemon = True
    encoder_process.start()

    return encoder_input_buffer


def start_decoder(decoder_input_buffer, decoder_output_buffer_size=10, logging_buffer=None):
    decoder_output_buffer = Queue(decoder_output_buffer_size)

    decoder_process = Process(name='image_decoder', target=decode_immage,
                              args=(decoder_input_buffer, decoder_output_buffer, logging_buffer))

    decoder_process.daemon = True
    decoder_process.start()

    return decoder_output_buffer
