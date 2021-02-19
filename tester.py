from time import sleep

import videoUtils.DDS_streamer

if __name__ == '__main__':




    print("initizing DDS connection")

    sleep(10)
    dds_streamer_input_buffer, dds_streamer_output_buffer = videoUtils.DDS_streamer.start_dds_streamer(str("traffic_sym"), "ImagesExample.xml", data_writer = "MyPublisher::CameraControllWriter", output_buffer_size = 10)

    print("DDS connection has established")


    while True:
        sleep(0.5)
        print("cycle")
        try:
            dds_streamer_output_buffer.put_nowait({"destination": str(0),"debug": None, "validdata": True})
        except:
            print("buffer is full"+str(i))


