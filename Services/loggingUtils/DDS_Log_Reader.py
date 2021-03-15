#!/usr/bin/env python3
import time
import rticonnextdds_connector as rti
from multiprocessing import Process

def start_subscriber(config_xml, domain_participant_sub, data_reader):
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
                # write data to database
                print(data)

def start_dds_log_reader(config_xml = "DDS_config.xml", domain_participant_sub = "MyParticipantLibrary::ImageSubParticipant", data_reader = "MySubscriber::LogReader"):
    dds_subscriber_process = Process(name='dds_streamer_' + str("logging_process"), target=start_subscriber, args=(config_xml, domain_participant_sub, data_reader))
    dds_subscriber_process.daemon = True
    dds_subscriber_process.start()

