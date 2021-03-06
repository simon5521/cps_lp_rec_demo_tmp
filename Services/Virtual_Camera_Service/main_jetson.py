import glob
import json
import os
import sys
from copy import copy

#import matplotlib.pyplot as plt

import carla

import random
import time
import numpy as np
import cv2
import os
import uuid

import cv2
import numpy as np
from numpy import record

sys.path.append('.')
from videoUtils.DDS_streamer import start_dds_streamer
from videoUtils.encode_decode import start_encoder
from videoUtils.mqtt_streamer import start_mqtt_streamer

carla_ip="192.168.1.10"

debug=True
record=False
out=0
k=0


x=246
y=-166
z=3
yaw=186.2
pitch=-5.4

id=0

spawn_points = [
    carla.Transform(
        carla.Location(x=250, y=-165.8, z=3),
        carla.Rotation(yaw=186.2, pitch=-5.4)),
    carla.Transform(
      carla.Location(x=15.5, y=4.5, z=1.6),
      carla.Rotation(yaw=90,pitch=-6.0)),
    carla.Transform(
        carla.Location(x=236, y=-166.8, z=3),
        carla.Rotation(yaw=549.0, pitch=-7.8)),
    carla.Transform(
        carla.Location(x=15.5, y=4.5, z=2.1),
        carla.Rotation(yaw=90, pitch=-6.0)),
    carla.Transform(
        carla.Location(x=240, y=-166, z=3),
        carla.Rotation(yaw=550.0, pitch=-7))
]
"""
x=250
y=-165.8
z=3
yaw=186.2
pitch=-5.4
"""

def process_img(image,encoder_input_buffer):
    global debug, k, id
    i = np.array(image.raw_data)  # convert to an array
    i2 = i.reshape((IM_HEIGHT, IM_WIDTH, 4))  # was flattened, so we're going to shape it.
    i3 = i2[:, :, :3]  # remove the alpha (basically, remove the 4th index  of every pixel. Converting RGBA to RGB)
    #cv2.imshow("", i3)  # show it.
    #cv2.waitKey(1)
    k=k+1
    #if record and k<30*120 and record:
        # write the flipped frame
        #out.write(i3)

    if k>30*120 and record:
        out.release()
        print("out release")
    try:
        encoder_input_buffer.put(
            {"pixels": i3, 'debug': True, 'validdata': True,'frame_id':id})
        id=id+1
    except:
        print("communication error : no reciever")
    return i3/255.0  # normalize

def is_port_in_use(port):
    import socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        return s.connect_ex(('localhost', port)) == 0



CID = 0
nodenum=2
nodeid=["virtual_camera_"+str(i) for i in range(nodenum)]

with open('config.json') as json_file:
    config = json.load(json_file)

if config['protocol'] == 'MQTT':
    print('MQTT config')
    streamer_input_buffer=[0 for i in range(nodenum)]
    streamer_output_buffer=[0 for i in range(nodenum)]
    for i in range(nodenum):
        print("nodeid: ",nodeid[i])
        streamer_input_buffer[i], streamer_output_buffer[i] = start_mqtt_streamer(nodeid[i], broker = config['mqtt_host'], port = 1883, topic_pub = 'raw_image', topic_sub = 'camera_controll')#, logging_buffer=logging_buffer)
else:
    print('DDS config')
    streamer_input_buffer, streamer_output_buffer = start_dds_streamer(
                    str(CID), 'DDS_config.xml',
                    data_writer='MyPublisher::RawWriter',
                    data_reader='MySubscriber::CameraControllReader',
                    input_buffer_size=10, output_buffer_size=50#,
                    #logging_buffer=logging_buffer
    )

encoder_input_buffer=[0 for i in range(nodenum)]
for i in range(nodenum):
    encoder_input_buffer[i] = start_encoder(streamer_output_buffer[i], encoder_input_buffer_size=10)

if record:
    fourcc = cv2.VideoWriter_fourcc(*'XVID')
    out = cv2.VideoWriter('~/output.avi', fourcc, 30.0, (640, 480))
    print("outout open")

data = None
debug = True
validdata = False

if __name__ == '__main__':
    actor_list = []
    try:

        if False : # not is_port_in_use(2000):

            print("start carla")
            os.system("DISPLAY= /opt/carla-simulator/CarlaUE4.sh -opengl -quality-level=Epic > ~/Documents/carlalog.txt &")

            #os.system("DISPLAY= /opt/carla-simulator/CarlaUE4.sh -opengl -quality-level=Epic > ~/Documents/carlalog.txt &")

            time.sleep(20.0)

        else:
            print("carla has been already started")

        print("load Town4")
        os.system("python3 /mnt/sdcard/carla/carla/PythonAPI/util/config.py --host "+carla_ip+" --map Town04")
        time.sleep(10.0)
        print("start Sumo")
        os.system("cd /mnt/sdcard/carla/carla/PythonAPI/examples && python3 spawn_npc.py -n 200 --host "+carla_ip+" > ~/Documents/sumolog.txt &")
        print("start camera client")
        time.sleep(10.0)
        client=carla.Client(carla_ip,2000)
        print("get world")
        world = client.get_world()
        print("get blueprint")
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



        # spawn the sensor and attach to vehicle.

        #sensors = [world.spawn_actor(blueprint, spawn_points[i]) for i in range(nodenum)]
        sensor0=world.spawn_actor(blueprint, spawn_points[0])
        sensor1=world.spawn_actor(blueprint, spawn_points[1])
        #sensor2=world.spawn_actor(blueprint, spawn_points[2])
        #sensor3=world.spawn_actor(blueprint, spawn_points[3])
        #sensor4=world.spawn_actor(blueprint, spawn_points[4])
        #print("sensors: ", sensors)

        print("listening")
        sensor0.listen(lambda data: process_img(data,encoder_input_buffer[0]))
        sensor1.listen(lambda data: process_img(data,encoder_input_buffer[1]))
        #sensor2.listen(lambda data: process_img(data,encoder_input_buffer[2]))
        #sensor3.listen(lambda data: process_img(data,encoder_input_buffer[3]))
        #sensor4.listen(lambda data: process_img(data,encoder_input_buffer[4]))
        """for i in range(nodenum):
            print("listening: ", i)
            sensors[i].listen(lambda data: process_img(data,encoder_input_buffer[i]))"""
    except:
        print("error")
    while (1):
        """try:
            for i in range(nodenum):
                data = streamer_input_buffer[i].get()
                if data['debug'] == 'True':
                    debug = True
                if data['debug'] == 'False':
                    debug = False
        except:
            pass"""
        time.sleep(1.0)

