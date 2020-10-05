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
    measurement_name = 'car_data'

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



from pinform.client import InfluxClient

cli = InfluxClient(host="localhost", port=8086, database_name="smartcity")
locid="virtual_loc_1"
nodeid="virtual_tester_1"
tag="pre_test_1"
dp1=LP_DATA(time_point=datetime.datetime.now(),nodeid=nodeid,locid=locid,lp="AVR123",tag=tag)
dp2=LP_DATA(time_point=datetime.datetime.now(),nodeid=nodeid,locid=locid,lp="AVR124",tag=tag)
dp3=LP_DATA(time_point=datetime.datetime.now(),nodeid=nodeid,locid=locid,lp="AVR125",tag=tag)
dp4=LP_DATA(time_point=datetime.datetime.now(),nodeid=nodeid,locid=locid,lp="AVR126",tag=tag)
dp5=LP_DATA(time_point=datetime.datetime.now(),nodeid=nodeid,locid=locid,lp="AVR128",tag=tag)
dp6=LP_DATA(time_point=datetime.datetime.now(),nodeid=nodeid,locid=locid,lp="AVR127",tag=tag)
dp7=LP_DATA(time_point=datetime.datetime.now(),nodeid=nodeid,locid=locid,lp="AVR129",tag=tag)

#cli.save_points([dp1,dp2,dp3,dp5,dp4,dp6,dp7])

ohlc_points = cli.load_points(LP_DATA, {'tag':tag})

for p in ohlc_points:
  print(p.lp," | ",p.time_point," | ",p.tag)