import glob
import os
import sys





import carla

import random
import time
import numpy as np
import cv2



def process_img(image):
    i = np.array(image.raw_data)  # convert to an array
    i2 = i.reshape((IM_HEIGHT, IM_WIDTH, 4))  # was flattened, so we're going to shape it.
    i3 = i2[:, :, :3]  # remove the alpha (basically, remove the 4th index  of every pixel. Converting RGBA to RGB)
    cv2.imshow("", i3)  # show it.
    cv2.waitKey(1)
    return i3/255.0  # normalize

actor_list = []
try:
    print("connect to carla")
    client = carla.Client('localhost', 2000)
    client.set_timeout(2.0)
    print("get word")

    world = client.get_world()
    print("get blueprints")
    blueprint_library = world.get_blueprint_library()

    IM_WIDTH = 640
    IM_HEIGHT = 480

    # https://carla.readthedocs.io/en/latest/cameras_and_sensors
    # get the blueprint for this sensor
    blueprint = blueprint_library.find('sensor.camera.rgb')
    # change the dimensions of the image
    blueprint.set_attribute('image_size_x', f'{IM_WIDTH}')
    blueprint.set_attribute('image_size_y', f'{IM_HEIGHT}')
    blueprint.set_attribute('fov', '30')
    blueprint.set_attribute('sensor_tick', '1.0/20.0')

    # Adjust sensor relative to vehicle
    spawn_point = carla.Transform(carla.Location(x=15,y=-2, z=0.7,),carla.Rotation(0,90,0))
    print("spawn sensor")
    # spawn the sensor and attach to vehicle.
    sensor = world.spawn_actor(blueprint, spawn_point)
    print("listen...")
    sensor.listen(lambda data: process_img(data))
    while(1):
        1


finally:

    print('destroying actors')
    for actor in actor_list:
        actor.destroy()
    print('done.')