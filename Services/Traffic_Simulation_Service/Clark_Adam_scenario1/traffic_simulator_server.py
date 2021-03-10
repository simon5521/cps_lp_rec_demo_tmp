import os, sys
import paho.mqtt.client as mqtt
from time import sleep

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

print("initizing MQTT connection")
client = mqtt.Client("Sumo Traffic Simulator")
print("connecting to broker")
#client.connect("192.168.1.68", 1883)
client.connect("localhost", 1883)
client.loop_start()
print("MQTT connection has established")


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
                client.publish("sumo/camera0","0")
                save_car_data(locid1)

            if traci.lanearea.getLastStepVehicleNumber("Camera2")>0:
                client.publish("sumo/camera1","1")
                save_car_data(locid2)

    traci.close()

