import os, sys
from time import sleep

import videoUtils.DDS_streamer

if __name__ == '__main__':

    #tools = os.path.join('/usr/share/sumo', 'tools')
    tools = os.path.join('C:\\Users\\simon5521\\Programs\\Sumo', 'tools')
    sys.path.append(tools)
    import traci

    from DBManagement.db_manager import save_car_data
    locid1="Alagut"
    locid2="Hunyadi_Janos_utca"

    #speed of the simulation
    sim_speed=20.0 #sim_step/sec
    #location of the sinulation configuration files
    #sim_loc="Clark_Adam_scenario1/"
    sim_loc="Clark_Adam_scenario1\\"

    print("initizing DDS connection")

    sleep(10)
    dds_streamer_input_buffer, dds_streamer_output_buffer = videoUtils.DDS_streamer.start_dds_streamer(str("traffic_sym"), "ImagesExample.xml", data_writer = "MyPublisher::CameraControllWriter", output_buffer_size = 10)

    print("DDS connection has established")


    while True:
        print("starting sumo gui")
        sumoBinary = "sumo-gui"
        sumoCmd = [sumoBinary, "-c", "osm.sumocfg"]#,"--scale",'0.9'
        traci.start(sumoCmd)
        print("sumo gui has been started")

        print("starting the simulation")

        # warming up the simulation (because the simulation start without car)
        traci.simulationStep(1500)
        print("warm up has been finished")

        for i in range(6000):
            #wait 20 ms
            sleep(1/sim_speed)
            #execute a simulation step
            traci.simulationStep()

            if traci.lanearea.getLastStepVehicleNumber("Camera1")>0:
                try:
                    dds_streamer_output_buffer.put_nowait({"destination": str(0), "validdata": True})
                except:
                    print("buffer is full"+str(i))
                save_car_data(locid1)


            if traci.lanearea.getLastStepVehicleNumber("Camera2")>0:
                try:
                    dds_streamer_output_buffer.put_nowait({"destination": str(0), "validdata": True})
                except:
                    print("buffer is full"+str(i))
                save_car_data(locid2)

        traci.close()

