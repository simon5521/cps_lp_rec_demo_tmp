import glob
import json
import os
import sys


import matplotlib.pyplot as plt

import carla

import random
import time
import numpy as np
import cv2
import os
import uuid

import cv2
import numpy as np
from dateutil.parser import parser
from numpy import record
from pynput.keyboard import Listener




sensor=0
debug=True
record=False
out=0
k=0

x=246
y=-166.0
z=4
yaw=546.6
pitch=-6.8
"""
x=250
y=-165.8
z=3
yaw=186.2
pitch=-5.4
"""

"""
x=15.5
y=4.5
z=2.1
yaw=90
pitch=-5.4
"""

def on_press(key):
    global x,y,z,yaw,pitch
    konst=0.2
    mod=False
    if hasattr(key, 'char'):
        key=key.char
    key=str(key)
    if(key=='w'):
        x=x+konst
        mod=True
    if(key=='s'):
        x=x-konst
        mod=True
    if(key=='a'):
        y=y-konst
        mod=True
    if(key=='d'):
        y=y+konst
        mod=True
    if(key=='r'):
        z=z+konst
        mod=True
    if(key=='f'):
        z=z-konst
        mod=True
    if(key=='y'):
        yaw=yaw+konst
        mod=True
    if(key=='x'):
        yaw=yaw-konst
        mod=True
    if(key=='c'):
        pitch=pitch-konst
        mod=True
    if(key=='v'):
        pitch=pitch+konst
        mod=True
    if mod:
        print("(",x,",",y,",",z,") [",yaw,', ',pitch,' ]')
        #loc=carla.Location(x=x, y=y, z=z)
        tr=carla.Transform(carla.Location(x=x, y=y, z=z),carla.Rotation(yaw=yaw,pitch=pitch))
        #sensor.set_location(loc)
        sensor.set_transform(tr)


def on_release(key):
    1


def process_img(image):
    global debug, k
    i = np.array(image.raw_data)  # convert to an array
    i2 = i.reshape((IM_HEIGHT, IM_WIDTH, 4))  # was flattened, so we're going to shape it.
    i3 = i2[:, :, :3]  # remove the alpha (basically, remove the 4th index  of every pixel. Converting RGBA to RGB)
    cv2.imshow("", i3)  # show it.
    cv2.waitKey(1)
    k=k+1
    if record and k<30*120 and record:
        # write the flipped frame
        out.write(i3)

    return i3/255.0  # normalize

def is_port_in_use(port):
    import socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        return s.connect_ex(('localhost', port)) == 0



CID = 0
nodeid=0



data = None
debug = True
validdata = False

if __name__ == '__main__':
    actor_list = []
    try:

        if not is_port_in_use(2000):

            print("start carla")
            #.system("DISPLAY= /opt/carla-simulator/CarlaUE4.sh -opengl -quality-level=Epic > ~/Documents/carlalog.txt &")

            os.system("DISPLAY= /opt/carla-simulator/CarlaUE4.sh -opengl -quality-level=Epic > ~/Documents/carlalog.txt &")

            time.sleep(20.0)

        else:
            print("carla has been already started")

        print("load Town4")
        os.system("python3 /opt/carla-simulator/PythonAPI/util/config.py --map Town04")

        print("start Sumo")
        os.system(    "cd /opt/carla-simulator/Co-Simulation/Sumo && python3 run_synchronization.py examples/Town04.sumocfg --sumo-gui > ~/Documents/sumolog.txt &")

        #os.system("cd /opt/carla-simulator/Co-Simulation/Sumo && python3 run_synchronization.py examples/Town04.sumocfg > ~/Documents/sumolog.txt &")

        print("start camera client")
        time.sleep(10.0)
        client=carla.Client('localhost',2000)
        print("get world")
        world = client.get_world()
        print("get world")
        blueprint_library = world.get_blueprint_library()

        IM_WIDTH = 850
        IM_HEIGHT = 500

        #IM_WIDTH = 1920
        #IM_HEIGHT = 1080

        # https://carla.readthedocs.io/en/latest/cameras_and_sensors
        # get the blueprint for this sensor
        blueprint = blueprint_library.find('sensor.camera.rgb')
        # change the dimensions of the image
        blueprint.set_attribute('image_size_x', f'{IM_WIDTH}')
        blueprint.set_attribute('image_size_y', f'{IM_HEIGHT}')
        blueprint.set_attribute('fov', '5')
        #blueprint.set_attribute('fov', '10')
        #blueprint.set_attribute('fov', '30')

        # Adjust sensor relative to vehicle
        spawn_point = carla.Transform(carla.Location(x=x, y=y, z=z), carla.Rotation(yaw=yaw,pitch=pitch))
        #spawn_point = carla.Transform(carla.Location(x=15.5, y=4.5, z=1.6), carla.Rotation(yaw=90,pitch=-6.0))

        # spawn the sensor and attach to vehicle.
        sensor = world.spawn_actor(blueprint, spawn_point)



        print("listening")
        sensor.listen(lambda data: process_img(data))

        with Listener(on_press=on_press, on_release=on_release) as listener:  # Create an instance of Listener
            listener.join()
    except:
        print("error")
    while (1):
        time.sleep(1.0)

