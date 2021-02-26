#!/usr/bin/python3
import time
import paho.mqtt.client as paho
from multiprocessing import Process, Queue

mqtt_client_Buffer = Queue(10)
mqtt_topics = []
#define callback
def on_message(client, userdata, message):
    global mqtt_client_Buffer
    top = message.topic
    msg = str(message.payload.decode("utf-8"))
    #ide kell rakni a vezérlőt !!!!!
    mqtt_client_Buffer.put(msg)

def on_connect(client, userdata, flags, rc):
    global mqtt_topics
    if rc==0:
        for topic in mqtt_topics:
            client.subscribe(topic)
    else:
        print("Bad connection Returned code=",rc)

def start_server(broker, port, username, password):
    client = paho.Client("client at " + broker) 
    client.on_connect=on_connect
    client.on_message=on_message
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    client.loop_forever()

def  start_mqtt_client(broker = "io.adafruit.com", port = 1883, topics = ["nukleari/feeds/camera"], username = None, password= None, buffer_size = 20):
    global mqtt_client_Buffer, mqtt_topics
    mqtt_topics = topics
    mqtt_client_Buffer = Queue(buffer_size)
    mqtt_client_process = Process(name='mqtt client' + str(broker), target=start_server, args=(broker, port, username, password))
    mqtt_client_process.daemon = True
    mqtt_client_process.start()
    return mqtt_client_Buffer