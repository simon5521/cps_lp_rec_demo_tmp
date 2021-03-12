from influxdb import InfluxDBClient
from pinform import Measurement
from pinform.fields import FloatField,StringField
from pinform.tags import Tag
import datetime

timeFormat = '%Y-%m-%dT%H:%M:%SZ'

class OHLC(Measurement):
  class Meta:
    measurement_name = 'ohlc'

  symbol = Tag(null=False)
  open = FloatField(null=False)
  high = FloatField(null=False)
  low = FloatField(null=False)
  close = FloatField(null=False)

class LP_DET_RT(Measurement):
  class Meta:
    measurement_name = 'lp_det_rt'
  tag = Tag(null=False)
  nodeid=StringField(null=False)
  runtime=FloatField(null=False)

class LP_REC_RT(Measurement):
  class Meta:
    measurement_name = 'lp_rec_rt'
  tag = Tag(null=False)
  nodeid=StringField(null=False)
  runtime=FloatField(null=False)

class LP_DATA(Measurement):
  class Meta:
    measurement_name = 'lp_data'
  tag = Tag(null=False)
  lp=StringField(null=False)
  nodeid=StringField(null=False)
  locid=StringField(null=False)


class CAR_LOSS(Measurement):
  class Meta:
    measurement_name = 'car_data'

  tag = Tag(null=False)
  nodeid = StringField(null=False)

class CAR_DET_LOSS(Measurement):
  class Meta:
    measurement_name = 'car_data'

  tag = Tag(null=False)
  nodeid = StringField(null=False)

class CAR_REC_LOSS(Measurement):
  class Meta:
    measurement_name = 'car_rec_loss'

  tag = Tag(null=False)
  nodeid = StringField(null=False)

class CAR_NET_LOSS(Measurement):
  class Meta:
    measurement_name = 'car_data'

  tag = Tag(null=False)
  nodeid = StringField(null=False)


class CAR_DATA(Measurement):
  class Meta:
    measurement_name = 'car_data'

  tag = Tag(null=False)
  nodeid = StringField(null=False)
  locid = StringField(null=False)

"""
today_ohlc = OHLC(time_point=datetime.datetime.now(), symbol='AAPL', open=80.2, high=86.0, low=78.9, close=81.25)
from pinform.client import InfluxClient

cli = InfluxClient(host="192.168.1.68", port=8086, database_name="pyexample")

ohlc = OHLC(time_point=datetime.datetime.now(), symbol='AAPL', open=100.6, high=102.5, low=90.4, close=94.2)
cli.save_points([ohlc])
ohlc_points = cli.load_points(OHLC, {'symbol':'AAPL'})
print(ohlc_points)
for p in ohlc_points:
  print(p.time_point)
tag_values = cli.get_distinct_existing_tag_values('symbol', measurement=OHLC)
print(tag_values)
"""


import numpy as np
import matplotlib.pyplot as plt

from pinform.client import InfluxClient, AggregationMode

cli = InfluxClient(host="localhost", port=8086, database_name="smartcity")
client = InfluxDBClient(host='localhost', port=8086,database="smartcity")
tag="tag"
import pytz

utc=pytz.UTC
while True:
  print("cycle")
  #results=client.query("""SELECT count("locid") FROM "car_data" WHERE ("tag" = 'realtest2_rp1_tr_20_0_5') AND time >= now() - 5m GROUP BY time(10s) fill(null)""")
  x = [i for i in range(60)]
  y = [0] * 60
  """
  series_dict  = cli.get_fields_as_series(CAR_REC_LOSS, tags={'tag': tag},
                                          field_aggregations={'nodeid': [AggregationMode.COUNT]},
                                          time_range=(datetime.datetime.now()-datetime.timedelta(days=5),datetime.datetime.now()),
                                          group_by_time_interval='10s')
  """
  p=cli.load_points(CAR_REC_LOSS, {'tag':tag})
  #p=results.get_points()
  #print(results.raw)
  i=0
  for pi in p:
    x[pi.time_point.second]=i
    #print(pi.nodeid," | ",pi.time_point.second)
    if(pi.time_point >utc.localize(datetime.datetime.now()-datetime.timedelta(minutes=1))):
      y[pi.time_point.second]=y[pi.time_point.second]+1
  plt.plot(y,x,"b*")
  plt.pause(1)