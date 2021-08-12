
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
    playload=str(message.payload.decode("utf-8"))
    #print("message received " ,playload)
    playload=json.loads(playload)
    #print("message topic=",message.topic)
    #print("message qos=",message.qos)
    #print("message retain flag=",message.retain)

    if telemetry in message.topic:
        data_topics.add(playload["topic"])
        #save telemetry
        service=message.topic.replace(telemetry,"")
        p = influxdb_client.Point("telemetry").tag("test",tag)\
            .tag("service_type",service)\
            .field("id",playload["topic"].replace(service+"-",""))\
            .field("queue_len",playload["qsize"])
        write_api.write(bucket=bucket, org=org, record=p)


    else:
        for topic in topics:
            if topic in message.topic:
                #save data
                p = influxdb_client.Point("message").tag("test",tag) \
                    .tag("service_type", topic) \
                    .field("id", message.topic.replace(topic + "-", ""))
                playload.pop('pixels')
                for key in payload:
                    p.field(key,playload[key])
                write_api.write(bucket=bucket, org=org, record=p)
                break









print("creating Influxdb client...")
client = influxdb_client.InfluxDBClient(
   url=url,
   token=token,
   org=org
)
write_api = client.write_api(write_options=SYNCHRONOUS)


print("creating MQTT client...")
client = Client(client_id=id, clean_session=True, userdata=None, protocol=MQTTv311, transport="tcp")
client.on_message=on_message


print("connecting MQTT client...")
client.connect(host, port=1883, keepalive=60, bind_address="")


print("subscribing to the telemetry topics")
for topic in topics:
    client.subscribe(topic+telemetry)

client.loop_start()