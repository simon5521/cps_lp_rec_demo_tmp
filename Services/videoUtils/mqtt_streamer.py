#!/usr/bin/python3
import time
import json
import paho.mqtt.client as paho
from multiprocessing import Process, Queue

mqtt_input_buffer = Queue()
mqtt_output_buffer = Queue()
mqtt_telemetry_buffer = Queue(1)
mqtt_topic_sub = None
mqtt_topic_pub = None
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
        client.subscribe(f'{mqtt_topic_sub}-{host_id}')
    else:
        print("Bad connection Returned code=",rc)

def start_subscriber(broker, port, username, password):
    client = paho.Client() 
    client.on_connect=on_connect
    client.on_message=on_message
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    client.loop_forever()

def start_telemetry_publisher(broker, port, username, password):
    global mqtt_input_buffer
    client = paho.Client() 
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    while True:
        data = {'qsize' : mqtt_input_buffer.qsize(),
                'topic' : f'{mqtt_topic_sub}-{host_id}'}
        message = json.dumps(data)
        client.publish(f'{mqtt_topic_sub}-telemetry', message)
        time.sleep(.5)


telemetry_table = {}
def on_message_telemetry(client, userdata, message):
    global mqtt_telemetry_buffer, telemetry_table
    top = message.topic
    msg = str(message.payload.decode("utf-8"))
    data = json.loads(msg)
    telemetry_table[data['topic']] = data
    telemetry_table[data['topic']]['last_update_time'] = time.time()
    while True:
        min_key = min(telemetry_table, key=lambda k: telemetry_table[k]['qsize'])
        if(time.time() - telemetry_table[min_key]['last_update_time'] > 10):
            telemetry_table.pop(min_key)
        else:
            try:
                mqtt_telemetry_buffer.put_nowait(telemetry_table[min_key])
            except:
                try:
                    mqtt_telemetry_buffer.get_nowait()
                except:
                    pass
                mqtt_telemetry_buffer.put(telemetry_table[min_key])
            break
        if(len(telemetry_table.keys()) == 0):
            break

def on_connect_telemetry(client, userdata, flags, rc):
    global mqtt_topic_pub
    if rc==0:
        client.subscribe(f'{mqtt_topic_pub}-telemetry')
    else:
        print("Bad connection Returned code=",rc)

def start_telemetry_subscriber(broker, port, username, password):
    client = paho.Client() 
    client.on_connect=on_connect_telemetry
    client.on_message=on_message_telemetry
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    client.loop_forever()

def start_publisher(broker, port, topic_pub, username, password):
    global mqtt_output_buffer, mqtt_telemetry_buffer
    client = paho.Client() 
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    topic = None
    last_update_time = 0
    while True:
        try:
            telemetry = mqtt_telemetry_buffer.get_nowait()
            topic = telemetry['topic']
            last_update_time = telemetry['last_update_time']
        except:
            pass

        if(time.time() - last_update_time < 10):
            data = mqtt_output_buffer.get()
            data["source"] = str(host_id)
            if 'destination' not in data:
                data['destination'] = ''
            message = json.dumps(data)
            client.publish(topic,message)

        time.sleep(0.01)

def  start_mqtt_streamer(nodeid, broker = "127.0.0.1", port = 1883, topic_pub = None, topic_sub = None, username = None, password= None, input_buffer_size = 10, output_buffer_size = 10, logging_buffer=None):
    global mqtt_input_buffer, mqtt_output_buffer, mqtt_topic_sub, mqtt_topic_pub, host_id, mqtt_logging_buffer
    host_id = nodeid
    mqtt_topic_sub = topic_sub
    mqtt_topic_pub = topic_pub
    mqtt_input_buffer = Queue(input_buffer_size)
    mqtt_output_buffer = Queue(output_buffer_size)
    mqtt_logging_buffer = logging_buffer
    if topic_sub != None:
        mqtt_sub_client_process = Process(name='mqtt client sub' + str(broker), target=start_subscriber, args=(broker, port, username, password))
        mqtt_sub_client_process.daemon = True
        mqtt_sub_client_process.start()
        mqtt_telemetry_publisher_process = Process(name='mqtt start telemetry publisher' + str(broker), target=start_telemetry_publisher, args=(broker, port, username, password))
        mqtt_telemetry_publisher_process.daemon = True
        mqtt_telemetry_publisher_process.start()
    if topic_pub != None:
        mqtt_pub_client_process = Process(name='mqtt client pub' + str(broker), target=start_publisher, args=(broker, port, topic_pub, username, password))
        mqtt_pub_client_process.daemon = True
        mqtt_pub_client_process.start()
        mqtt_telemetry_subscriberr_process = Process(name='mqtt start telemetry subscriber' + str(broker), target=start_telemetry_subscriber, args=(broker, port, username, password))
        mqtt_telemetry_subscriberr_process.daemon = True
        mqtt_telemetry_subscriberr_process.start()
    return mqtt_input_buffer, mqtt_output_buffer
