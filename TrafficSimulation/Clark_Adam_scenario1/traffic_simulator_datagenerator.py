import os, sys
import paho.mqtt.client as mqtt
from time import sleep

tools = os.path.join('/usr/share/sumo', 'tools')
sys.path.append(tools)
import traci

#speed of the simulation
sim_speed=10.0 #sim_step/sec
#location of the sinulation configuration files
sim_loc="Clark_Adam_scenario1/"


print("starting sumo gui")
sumoBinary = "sumo-gui"
sumoCmd = [sumoBinary, "-c", sim_loc+"osm.sumocfg"]
traci.start(sumoCmd)
print("sumo gui has been started")

print("starting the simulation")
while True :
    #warming up the simulation (because the simulation start without car)
    traci.simulationStep(600)
    for step in range(3000):

        #wait 20 ms
        sleep(1/sim_speed)

        #execute a simulation step
        traci.simulationStep()

        if traci.lanearea.getLastStepVehicleNumber("Camera1")>0:


        if traci.lanearea.getLastStepVehicleNumber("Camera2")>0:


