import glob
import os
import sys


import matplotlib.pyplot as plt


"""
try:
    sys.path.append(glob.glob('../carla/dist/carla-*%d.%d-%s.egg' % (
        sys.version_info.major,
        sys.version_info.minor,
        'win-amd64' if os.name == 'nt' else 'linux-x86_64'))[0])
except IndexError:
    print("import error")
    pass
"""
import carla

import random
import time
import numpy as np
import cv2
import os


def process_img(image):
    i = np.array(image.raw_data)  # convert to an array
    i2 = i.reshape((IM_HEIGHT, IM_WIDTH, 4))  # was flattened, so we're going to shape it.
    i3 = i2[:, :, :3]  # remove the alpha (basically, remove the 4th index  of every pixel. Converting RGBA to RGB)
    cv2.imshow("", i3)  # show it.
    cv2.waitKey(1)
    return i3/255.0  # normalize

def is_port_in_use(port):
    import socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        return s.connect_ex(('localhost', port)) == 0

if __name__ == '__main__':

    actor_list = []
    try:

        if not is_port_in_use(2000):

            print("start carla")
            os.system("DISPLAY= /opt/carla-simulator/CarlaUE4.sh -opengl -quality-level=Epic > ~/Documents/carlalog.txt &")
            time.sleep(20.0)

        else:
            print("carla has been already started")

        print("load Town4")
        os.system("python3 /opt/carla-simulator/PythonAPI/util/config.py --map Town04")

        print("start Sumo")
        os.system("cd /opt/carla-simulator/Co-Simulation/Sumo && python3 run_synchronization.py examples/Town04.sumocfg > ~/Documents/sumolog.txt &")
        print("start camera client")
        time.sleep(10.0)
        client=carla.Client('localhost',2000)
        print("get world")
        world = client.get_world()
        print("get world")
        blueprint_library = world.get_blueprint_library()

        IM_WIDTH = 640
        IM_HEIGHT = 480

        # https://carla.readthedocs.io/en/latest/cameras_and_sensors
        # get the blueprint for this sensor
        blueprint = blueprint_library.find('sensor.camera.rgb')
        # change the dimensions of the image
        blueprint.set_attribute('image_size_x', f'{IM_WIDTH}')
        blueprint.set_attribute('image_size_y', f'{IM_HEIGHT}')
        blueprint.set_attribute('fov', '20')

        # Adjust sensor relative to vehicle
        spawn_point = carla.Transform(carla.Location(x=15.5, y=4.5, z=0.6), carla.Rotation(yaw=90))

        # spawn the sensor and attach to vehicle.
        sensor = world.spawn_actor(blueprint, spawn_point)
        print("listening")
        sensor.listen(lambda data: process_img(data))
    except:
        print("error")
    while (1):
        time.sleep(1.0)

