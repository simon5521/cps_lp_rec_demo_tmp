from pinform import Measurement
from pinform.fields import FloatField,StringField
from pinform.tags import Tag
import datetime


from pinform.client import InfluxClient

locid="virtual_loc_1"
nodeid="virtual_tester_1"
tag="pre_test_1"
dburl="localhost"
#dburl="192.168.1.68"

timeFormat = '%Y-%m-%dT%H:%M:%SZ'



cli = InfluxClient(host=dburl, port=8086, database_name="smartcity")


def save(data):
    cli.save_points([data])

def save_det_rt(rt):
    save(LP_DET_RT(time_point=datetime.datetime.now(),tag=tag,nodeid=nodeid,runtime=rt))

def save_rec_rt(rt):
    save(LP_REC_RT(time_point=datetime.datetime.now(),tag=tag, nodeid=nodeid, runtime=rt))

def save_rec_net_dly(delay):
    save(NET_REC_DELAY(time_point=datetime.datetime.now(),tag=tag, nodeid=nodeid, delay=delay))

def save_det_net_dly(delay):
    save(NET_DET_DELAY(time_point=datetime.datetime.now(),tag=tag, nodeid=nodeid, delay=delay))

def save_lp(lp):
  save(LP_DATA(time_point=datetime.datetime.now(),tag=tag,nodeid=nodeid,locid=locid,lp=lp))

def save_car_data(locid=locid):
  save(CAR_DATA(time_point=datetime.datetime.now(),locid=locid,nodeid=nodeid,tag=tag))

def save_car_loss():
  save(CAR_LOSS(time_point=datetime.datetime.now(),nodeid=nodeid,tag=tag))

def save_car_net_loss():
  save(CAR_NET_LOSS(time_point=datetime.datetime.now(),nodeid=nodeid,tag=tag))


def save_car_rec_loss():
  save(CAR_REC_LOSS(time_point=datetime.datetime.now(),locid=locid,nodeid=nodeid,tag=tag))


def save_car_det_loss():
  save(CAR_DET_LOSS(time_point=datetime.datetime.now(),locid=locid,nodeid=nodeid,tag=tag))

def close():
  cli.close()

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

class NET_REC_DELAY(Measurement):
  class Meta:
    measurement_name = 'rec_net_dly'
  tag = Tag(null=False)
  nodeid=StringField(null=False)
  delay=FloatField(null=False)


class NET_DET_DELAY(Measurement):
  class Meta:
    measurement_name = 'det_net_dly'
  tag = Tag(null=False)
  nodeid=StringField(null=False)
  delay=FloatField(null=False)

class LP_DATA(Measurement):
  class Meta:
    measurement_name = 'lp_data'
  tag = Tag(null=False)
  lp=StringField(null=False)
  nodeid=StringField(null=False)
  locid=StringField(null=False)


class CAR_DATA(Measurement):
  class Meta:
    measurement_name = 'car_data'

  tag = Tag(null=False)
  nodeid = StringField(null=False)
  locid = StringField(null=False)


class CAR_LOSS(Measurement):
  class Meta:
    measurement_name = 'car_loss'

  tag = Tag(null=False)
  nodeid = StringField(null=False)

class CAR_DET_LOSS(Measurement):
  class Meta:
    measurement_name = 'car_det_loss'

  tag = Tag(null=False)
  nodeid = StringField(null=False)

class CAR_REC_LOSS(Measurement):
  class Meta:
    measurement_name = 'car_rec_loss'

  tag = Tag(null=False)
  nodeid = StringField(null=False)

class CAR_NET_LOSS(Measurement):
  class Meta:
    measurement_name = 'car_net_loss'

  tag = Tag(null=False)
  nodeid = StringField(null=False)

def load_det_rt(tag=tag):
  points = cli.load_points(LP_DET_RT, {'tag': tag})
  t=list()
  rt=list()
  for p in points:
    t.append(p.time_point)
    rt.append(p.runtime)
  return t,rt


def load_rec_rt(tag=tag):
  points = cli.load_points(LP_REC_RT, {'tag': tag})
  t=list()
  rt=list()
  for p in points:
    t.append(p.time_point)
    rt.append(p.runtime)
  return t,rt


def load_net_det_dly(tag=tag):
  points = cli.load_points(NET_DET_DELAY, {'tag': tag})
  t=list()
  d=list()
  for p in points:
    t.append(p.time_point)
    d.append(p.delay)
  return t,d


def load_net_rec_rt(tag=tag):
  points = cli.load_points(NET_REC_DELAY, {'tag': tag})
  t=list()
  d=list()
  for p in points:
    t.append(p.time_point)
    d.append(p.delay)
  return t,d