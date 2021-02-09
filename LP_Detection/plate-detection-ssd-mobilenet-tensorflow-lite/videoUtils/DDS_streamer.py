#!/usr/bin/env python3
import time
import rticonnextdds_connector as rti
from multiprocessing import Process, Queue

dds_streamer_input_buffer = Queue(3)
dds_streamer_output_buffer = Queue(3)

def start_publisher(host_id, config_xml, domain_participant_pub, data_writer):
    global dds_streamer_input_buffer, dds_streamer_output_buffer
    with rti.open_connector(
            config_name=domain_participant_pub,
            url=config_xml) as connector:

        output = connector.get_output(data_writer)
        output.instance["source"] = str(host_id)

        while(True):
            data_to_send = dds_streamer_output_buffer.get()
            output.instance.set_dictionary(data_to_send)
            output.write()

def start_subscriber(host_id, config_xml, domain_participant_sub, data_reader):
    global dds_streamer_input_buffer, dds_streamer_output_buffer
    with rti.open_connector(
            config_name=domain_participant_sub,
            url=config_xml) as connector:

        input = connector.get_input(data_reader)

        while(True):
            input.wait() # wait for data on this input
            input.take()
            for sample in input.samples.valid_data_iter:
                # You can get all the fields in a get_dictionary()
                data = sample.get_dictionary()
                if((data["source"] != host_id) and ((data["destination"] == "") or (data["destination"] == host_id))):
                    try:
                        dds_streamer_input_buffer.put_nowait(data)
                    except:
                        dds_streamer_input_buffer.get()
                        dds_streamer_input_buffer.put(data)

def start_dds_streamer(host_id, config_xml, domain_participant_pub = "MyParticipantLibrary::ImagePubParticipant", data_writer = None, domain_participant_sub = "MyParticipantLibrary::ImageSubParticipant", data_reader = None, input_buffer_size = 10, output_buffer_size = 10):
    global dds_streamer_input_buffer, dds_streamer_output_buffer
    dds_streamer_input_buffer = Queue(input_buffer_size)
    dds_streamer_output_buffer = Queue(output_buffer_size)
    
    if(data_writer != None):
        dds_publisher_process = Process(name='dds_streamer_' + str(host_id), target=start_publisher, args=(host_id, config_xml, domain_participant_pub, data_writer))
        dds_publisher_process.daemon = True
        dds_publisher_process.start()

    if(data_reader != None):
        dds_subscriber_process = Process(name='dds_streamer_' + str(host_id), target=start_subscriber, args=(host_id, config_xml, domain_participant_sub, data_reader))
        dds_subscriber_process.daemon = True
        dds_subscriber_process.start()

    return dds_streamer_input_buffer, dds_streamer_output_buffer

