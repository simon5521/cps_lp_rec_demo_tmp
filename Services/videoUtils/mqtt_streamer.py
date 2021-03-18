#!/usr/bin/python3
import time
import json
import paho.mqtt.client as paho
from multiprocessing import Process, Queue

mqtt_input_buffer = Queue()
mqtt_output_buffer = Queue()
mqtt_topic_sub = None
host_id = ''
mqtt_logging_buffer = None

#define callback
def on_message(client, userdata, message):
    global mqtt_input_buffer
    top = message.topic
    msg = str(message.payload.decode("utf-8"))
    data = json.loads(msg)
    if((data["source"] != host_id) and ((data["destination"] == "") or (data["destination"] == host_id))):
        try:
            mqtt_input_buffer.put_nowait(data)
        except:
            mqtt_input_buffer.get()
            mqtt_input_buffer.put(data)
            if mqtt_logging_buffer != None:
                mqtt_logging_buffer.put({'measurement': '', 'component': 'mqtt_streamer', 'data': 'mqtt_input_buffer'})

def on_connect(client, userdata, flags, rc):
    global mqtt_topic_sub
    if rc==0:
        client.subscribe(mqtt_topic_sub)
    else:
        print("Bad connection Returned code=",rc)

def start_subscriber(broker, port, username, password):
    client = paho.Client("client sub at " + broker) 
    client.on_connect=on_connect
    client.on_message=on_message
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    client.loop_forever()

def start_publisher(broker, port, topic_pub, username, password):
    global mqtt_output_buffer
    client = paho.Client("client pub at " + broker) 
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    while True:
        data = mqtt_output_buffer.get()
        data["source"] = str(host_id)
        if 'destination' not in data:
            data['destination'] = ''
        message = json.dumps(data)
        client.publish(topic_pub,message)
        time.sleep(0.01)

def  start_mqtt_streamer(nodeid, broker = "127.0.0.1", port = 1883, topic_pub = None, topic_sub = None, username = None, password= None, input_buffer_size = 10, output_buffer_size = 10, logging_buffer=None):
    global mqtt_input_buffer, mqtt_output_buffer, mqtt_topic_sub, host_id, mqtt_logging_buffer
    host_id = nodeid
    mqtt_topic_sub = topic_sub
    mqtt_input_buffer = Queue(input_buffer_size)
    mqtt_output_buffer = Queue(output_buffer_size)
    mqtt_logging_buffer = logging_buffer
    if topic_sub != None:
        mqtt_sub_client_process = Process(name='mqtt client sub' + str(broker), target=start_subscriber, args=(broker, port, username, password))
        mqtt_sub_client_process.daemon = True
        mqtt_sub_client_process.start()
    if topic_pub != None:
        mqtt_pub_client_process = Process(name='mqtt client pub' + str(broker), target=start_publisher, args=(broker, port, topic_pub, username, password))
        mqtt_pub_client_process.daemon = True
        mqtt_pub_client_process.start()
    return mqtt_input_buffer, mqtt_output_buffer
