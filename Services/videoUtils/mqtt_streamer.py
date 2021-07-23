#!/usr/bin/python3
import json
import time
from multiprocessing import Process, Queue

import paho.mqtt.client as paho


# define callback
def on_message(client, userdata, message, host_id, input_buffer, logging_buffer):
    top = message.topic
    msg = str(message.payload.decode("utf-8"))
    data = json.loads(msg)
    if((data["source"] != host_id) and ((data["destination"] == "") or (data["destination"] == host_id))):
        try:
            input_buffer.put_nowait(data)
        except:
            input_buffer.get()
            input_buffer.put(data)
            if logging_buffer != None:
                logging_buffer.put(
                    {'measurement': '', 'component': 'mqtt_streamer', 'data': 'mqtt_input_buffer'})


def on_connect(client, userdata, flags, rc, host_id, topic_sub):
    if rc == 0:
        client.subscribe(topic_sub)
        client.subscribe(f'{topic_sub}-{host_id}')
    else:
        print("Bad connection Returned code=", rc)


def start_subscriber(host_id, broker, port, topic_sub, username, password, input_buffer, logging_buffer):
    client = paho.Client()
    client.on_connect = lambda client, userdata, flags, rc: on_connect(
        client, userdata, flags, rc, host_id, topic_sub)
    client.on_message = lambda client, userdata, message: on_message(
        client, userdata, message, host_id, input_buffer, logging_buffer)
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    client.loop_forever()


def start_telemetry_publisher(host_id, broker, port, topic_sub, username, password, input_buffer):
    client = paho.Client()
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    while True:
        data = {'qsize': input_buffer.qsize(),
                'topic': f'{topic_sub}-{host_id}'}
        message = json.dumps(data)
        client.publish(f'{topic_sub}-telemetry', message)
        time.sleep(.5)


def on_message_telemetry(client, userdata, message, host_id, telemetry_buffer, telemetry_table):
    top = message.topic
    msg = str(message.payload.decode("utf-8"))
    data = json.loads(msg)
    telemetry_table[data['topic']] = data
    telemetry_table[data['topic']]['last_update_time'] = time.time()
    while True:
        min_key = min(telemetry_table,
                      key=lambda k: telemetry_table[k]['qsize'])
        if(time.time() - telemetry_table[min_key]['last_update_time'] > 10):
            telemetry_table.pop(min_key)
        else:
            try:
                telemetry_buffer.put_nowait(telemetry_table[min_key])
            except:
                try:
                    telemetry_buffer.get_nowait()
                except:
                    pass
                telemetry_buffer.put(telemetry_table[min_key])
            break
        if(len(telemetry_table.keys()) == 0):
            break


def on_connect_telemetry(client, userdata, flags, rc, host_id, topic_pub):
    if rc == 0:
        client.subscribe(f'{topic_pub}-telemetry')
    else:
        print("Bad connection Returned code=", rc)


def start_telemetry_subscriber(host_id, broker, port, topic_pub, username, password, output_buffer, telemetry_buffer):
    client = paho.Client()
    telemetry_table = {}
    client.on_connect = lambda client, userdata, flags, rc: on_connect_telemetry(
        client, userdata, flags, rc, host_id, topic_pub)
    client.on_message = lambda client, userdata, message: on_message_telemetry(
        client, userdata, message, host_id, telemetry_buffer, telemetry_table)
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    client.loop_forever()


def start_publisher(host_id, broker, port, topic_pub, username, password, output_buffer, telemetry_buffer):
    client = paho.Client()
    if(username != None):
        client.username_pw_set(username, password)
    client.connect(broker, port)
    topic = None
    last_update_time = 0
    while True:
        try:
            telemetry = telemetry_buffer.get_nowait()
            topic = telemetry['topic']
            last_update_time = telemetry['last_update_time']
        except:
            pass

        if(time.time() - last_update_time < 10):
            data = output_buffer.get()
            data["source"] = str(host_id)
            if 'destination' not in data:
                data['destination'] = ''
            message = json.dumps(data)
            client.publish(topic, message)

        time.sleep(0.01)


def start_mqtt_streamer(nodeid, broker="127.0.0.1", port=1883, topic_pub=None, topic_sub=None, username=None, password=None, input_buffer_size=10, output_buffer_size=10, logging_buffer=None):
    global mqtt_logging_buffer
    host_id = nodeid
    input_buffer = Queue(input_buffer_size)
    output_buffer = Queue(output_buffer_size)
    telemetry_buffer = Queue(1)
    if topic_sub != None:
        mqtt_sub_client_process = Process(name='mqtt client sub' + str(broker), target=start_subscriber, args=(
            host_id, broker, port, topic_sub, username, password, input_buffer, logging_buffer))
        mqtt_sub_client_process.daemon = True
        mqtt_sub_client_process.start()
        mqtt_telemetry_publisher_process = Process(name='mqtt start telemetry publisher' + str(
            broker), target=start_telemetry_publisher, args=(host_id, broker, port, topic_sub, username, password, input_buffer))
        mqtt_telemetry_publisher_process.daemon = True
        mqtt_telemetry_publisher_process.start()
    if topic_pub != None:
        mqtt_pub_client_process = Process(name='mqtt client pub' + str(broker), target=start_publisher, args=(
            host_id, broker, port, topic_pub, username, password, output_buffer, telemetry_buffer))
        mqtt_pub_client_process.daemon = True
        mqtt_pub_client_process.start()
        mqtt_telemetry_subscriberr_process = Process(name='mqtt start telemetry subscriber' + str(
            broker), target=start_telemetry_subscriber, args=(host_id, broker, port, topic_pub, username, password, output_buffer, telemetry_buffer))
        mqtt_telemetry_subscriberr_process.daemon = True
        mqtt_telemetry_subscriberr_process.start()
    return input_buffer, output_buffer
