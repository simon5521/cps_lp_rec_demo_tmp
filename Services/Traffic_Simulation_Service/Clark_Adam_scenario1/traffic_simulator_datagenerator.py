import os, sys

#tools = os.path.join('/usr/share/sumo', 'tools')
tools = os.path.join('C:\\Users\\simon5521\\Programs\\Sumo', 'tools')
sys.path.append(tools)
import traci

locid1="Alagut"
locid2="Hunyadi_Janos_utca"

#speed of the simulation
sim_speed=10.0 #sim_step/sec
#location of the sinulation configuration files
#sim_loc="Clark_Adam_scenario1/"
sim_loc="Clark_Adam_scenario1\\"

print("starting sumo gui")
sumoBinary = "sumo-gui"
sumoCmd = [sumoBinary, "-c", "osm.sumocfg"]
traci.start(sumoCmd)
print("sumo gui has been started")

print("starting the simulation")

# warming up the simulation (because the simulation start without car)
traci.simulationStep(200)
step=0
time=0.0
ptime=0.0
data=[]

while step<3200 :
        step=step+1
        time=step*1.0/sim_speed
        #wait 20 ms
        #sleep(1/sim_speed)

        #execute a simulation step
        traci.simulationStep()

        if traci.lanearea.getLastStepVehicleNumber("Camera1")>0:
            data.append(time)

        ptime=time

print("Data:",data)


