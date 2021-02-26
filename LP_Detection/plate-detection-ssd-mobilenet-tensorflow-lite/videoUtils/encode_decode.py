#!/usr/bin/env python3
from multiprocessing import Process, Queue

import cv2
import numpy as np


def encode_immage(encoder_input_buffer, encoder_output_buffer):
    hexlist = ["{:#04x}".format(x)[2:] for x in range(256)]
    lut = np.array(hexlist)
    inverse_lut = {}
    for index, hex_num in enumerate(hexlist):
        inverse_lut[hex_num] = index

    while(True):
        frame = encoder_input_buffer.get()
        _, numpy_jpeg_array = cv2.imencode('.jpg', frame['pixels'], [
                                           int(cv2.IMWRITE_JPEG_QUALITY), 30])
        im_str = ''.join(lut[numpy_jpeg_array].reshape(-1).tolist())

        frame['pixels'] = im_str

        try:
            encoder_output_buffer.put_nowait(frame)
        except:
            encoder_output_buffer.get()
            encoder_output_buffer.put(frame)


def decode_immage(decoder_input_buffer, decoder_output_buffer):
    hexlist = ["{:#04x}".format(x)[2:] for x in range(256)]
    lut = np.array(hexlist)
    inverse_lut = {}
    for index, hex_num in enumerate(hexlist):
        inverse_lut[hex_num] = index

    while True:
        frame = decoder_input_buffer.get()

        im_str = frame['pixels']
        chunks = [im_str[i:i+2] for i in range(0, len(im_str), 2)]
        jpeg_array = list(map(lambda x: inverse_lut[x], chunks))
        numpy_jpeg_array = (
            (np.array(jpeg_array, dtype=np.uint8)).reshape((len(jpeg_array), 1)))
        frame['pixels'] = cv2.imdecode(numpy_jpeg_array, flags=1)

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
