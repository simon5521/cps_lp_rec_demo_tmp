from influxdb import InfluxDBClient
#client = InfluxDBClient(host='192.168.1.68', port=8086)
client = InfluxDBClient(host='192.168.0.207', port=8086)
client.create_database('smartcity')
client.create_user("grafana","")