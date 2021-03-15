#!/usr/bin/python3
from loggingUtils.DDS_Log_Reader import start_dds_log_reader
from time import sleep

if __name__ == "__main__":    
    start_dds_log_reader()
    while(1):
        sleep(1.0)