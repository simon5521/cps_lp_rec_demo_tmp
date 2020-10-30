#!/usr/bin/env python3
import time
import rticonnextdds_connector as rti
from multiprocessing import Process, Queue

dds_streamer_input_buffer = Queue(3)
dds_streamer_output_buffer = Queue(3)

def start_server(host_id):
    global dds_streamer_input_buffer, dds_streamer_output_buffer
    with rti.open_connector(
            config_name="MyParticipantLibrary::ImagePubParticipant",
            url="ImagesExample.xml") as connector:

        output = connector.get_output("MyPublisher::ImageWriter")
        output.instance["source"] = str(host_id)

        while(True):
            data_to_send = dds_streamer_output_buffer.get()
            output.instance.set_dictionary(data_to_send)
            output.write()

def  start_dds_streamer(host_id, input_buffer_size = 3, output_buffer_size = 3):
    global dds_streamer_input_buffer, dds_streamer_output_buffer
    dds_streamer_input_buffer = Queue(input_buffer_size)
    dds_streamer_output_buffer = Queue(output_buffer_size)
    dds_streamer_process = Process(name='dds_streamer_' + str(host_id), target=start_server, args=(host_id,))
    dds_streamer_process.daemon = True
    dds_streamer_process.start()
    return dds_streamer_input_buffer, dds_streamer_output_buffer

