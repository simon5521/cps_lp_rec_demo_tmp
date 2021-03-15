#!/usr/bin/env python3
import time
import rticonnextdds_connector as rti
from multiprocessing import Process, Queue
import datetime

dds_logging_output_buffer = Queue(3)
TIME_FORMAT = '%Y-%m-%dT%H:%M:%SZ'

def start_publisher(host_id, service_type, config_xml, domain_participant_pub, data_writer):
    global dds_logging_output_buffer
    with rti.open_connector(
            config_name=domain_participant_pub,
            url=config_xml) as connector:

        output = connector.get_output(data_writer)
        output.instance["source"] = str(host_id)

        while(True):
            raw_data = dds_logging_output_buffer.get()

            data_to_send = {
                                "measurement": raw_data["measurement"],
                                "tags": {
                                    "nodeid": host_id
                                    "service_type": service_type
                                    "component": raw_data["component"]
                                },
                                "time": datetime.datetime.now().strftime(TIME_FORMAT),
                                "fields": {
                                    "data": raw_data["data"]
                                }
                            }
            output.instance.set_dictionary(data_to_send)
            output.write()

def start_dds_logger(host_id, service_type, config_xml = "DDS_config.xml", domain_participant_pub = "MyParticipantLibrary::ImagePubParticipant", data_writer = "MyPublisher::LogWriter", buffer_size = 100):
    global dds_logging_output_buffer
    dds_logging_output_buffer = Queue(buffer_size)
    
    dds_publisher_process = Process(name='dds_streamer_' + str(host_id), target=start_publisher, args=(host_id, service_type, config_xml, domain_participant_pub, data_writer))
    dds_publisher_process.daemon = True
    dds_publisher_process.start()

    return dds_logging_output_buffer

