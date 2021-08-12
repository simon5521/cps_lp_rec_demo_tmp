
import paho.mqtt.client as mqtt
import influxdb_client
from influxdb_client.client.write_api import SYNCHRONOUS
import json

id="Logger-Service"

tag="test1"

with open('config.json') as json_file:
    config = json.load(json_file)




bucket = config["bucket"]
org = config["org"]
url = config["url"]
token = config["token"]

points=list()
batch_size=100


host=config['mqtt_host']

topics=[
    "raw_image",
    "detected_image",
    "recognized_image"
]


telemetry="-telemetry"

data_topics=set()



def on_message(client, userdata, message):
    payload=str(message.payload.decode("utf-8"))
    #print("message received " ,playload)
    payload=json.loads(payload)
    #print("message topic=",message.topic)
    #print("message qos=",message.qos)
    #print("message retain flag=",message.retain)

    if telemetry in message.topic:
        if payload["topic"] not in data_topics:
            data_topics.add(payload["topic"])
            client.subscribe(payload["topic"])
        #save telemetry
        service=message.topic.replace(telemetry,"")
        p = influxdb_client.Point("telemetry").tag("test",tag)\
            .tag("service_type",service)\
            .field("id",payload["topic"].replace(service+"-",""))\
            .field("queue_len",payload["qsize"])
        write_api.write(bucket=bucket, org=org, record=p,token=token)


    else:
        for topic in topics:
            if topic in message.topic:
                del payload['pixels']
                #print( message.topic," : ",payload)
                #save data
                p = influxdb_client.Point("message").tag("test",tag) \
                    .tag("service_type", topic) \
                    .field("id", message.topic.replace(topic + "-", ""))
                
                for key in payload:
                    if key == "licence_plate":
                        for lp in payload["licence_plate"]:
                            lp_point = influxdb_client.Point("licese_plate").tag("test",tag) \
                                .tag("service_type", topic) \
                                .field("id", message.topic.replace(topic + "-", "")) \
                                .tag("frame_id",payload["frame_id"])
                            for key in lp:
                                if key != "position":
                                    lp_point.field(key,lp[key])
                            write_api.write(bucket=bucket, org=org, record=lp_point,token=token)
                    else:
                        p.field(key,payload[key])
                write_api.write(bucket=bucket, org=org, record=p,token=token)
                break









print("creating Influxdb client...")
iclient = influxdb_client.InfluxDBClient(
   url=url,
   token=token,
   org=org,
   username='admin',
   password='LaborImage',
   ssl=True, 
   verify_ssl=True
)
write_api = iclient.write_api(write_options=SYNCHRONOUS)

print("creating MQTT client...")
client = mqtt.Client(client_id=id)
client.on_message=on_message


print("connecting MQTT client...")
client.connect(host, port=1883, keepalive=60, bind_address="")


print("subscribing to the telemetry topics")
for topic in topics:
    client.subscribe(topic+telemetry)

client.loop_forever()