#!/usr/bin/env python3
import base64
from multiprocessing import Process, Queue

import cv2
import numpy as np


def encode_immage(encoder_input_buffer, encoder_output_buffer):
    while(True):
        frame = encoder_input_buffer.get()
        _, numpy_jpeg_array = cv2.imencode('.jpg', frame['pixels'], [
                                           int(cv2.IMWRITE_JPEG_QUALITY), 30])
        im_bytes = numpy_jpeg_array.tobytes()
        im_b64 = base64.b64encode(im_bytes)
        base64_message = im_b64.decode('ascii')

        frame['pixels'] = base64_message

        try:
            encoder_output_buffer.put_nowait(frame)
        except:
            encoder_output_buffer.get()
            encoder_output_buffer.put(frame)


def decode_immage(decoder_input_buffer, decoder_output_buffer):
    while True:
        frame = decoder_input_buffer.get()

        base64_message = frame['pixels']
        im_b64 = base64_message.encode('ascii')
        im_bytes = base64.b64decode(im_b64)
        im_arr = np.frombuffer(im_bytes, dtype=np.uint8)  # im_arr is one-dim Numpy array
        frame['pixels'] = cv2.imdecode(im_arr, flags=cv2.IMREAD_COLOR)

        try:
            decoder_output_buffer.put_nowait(frame)
        except:
            decoder_output_buffer.get()
            decoder_output_buffer.put(frame)


def start_encoder(encoder_output_buffer, encoder_input_buffer_size=10):
    encoder_input_buffer = Queue(encoder_input_buffer_size)

    encoder_process = Process(name='image_encoder', target=encode_immage,
                              args=(encoder_input_buffer, encoder_output_buffer))

    encoder_process.daemon = True
    encoder_process.start()

    return encoder_input_buffer


def start_decoder(decoder_input_buffer, decoder_output_buffer_size=10):
    decoder_output_buffer = Queue(decoder_output_buffer_size)

    decoder_process = Process(name='image_decoder', target=decode_immage,
                              args=(decoder_input_buffer, decoder_output_buffer))

    decoder_process.daemon = True
    decoder_process.start()

    return decoder_output_buffer
