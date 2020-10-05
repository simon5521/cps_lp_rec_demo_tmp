
import pyro
import torch
from py4j.java_gateway import JavaGateway, CallbackServerParameters
import matplotlib.pyplot as plt
import matplotlib
from pyro.infer import SVI, Trace_ELBO
from pyro.optim import Adam
import pyro.distributions as dist
import torch.distributions.constraints as constraints
from pyro.distributions.distribution import Distribution
import math
from influxdb import InfluxDBClient

import pyro.contrib.gp as gp

import datetime

print("python script is called")

print("creating Py4J gateway")

gateway = JavaGateway(callback_server_parameters=CallbackServerParameters())
sctmodel = gateway.entry_point.getEntryPoint().getDetModel()


simTime=20.0
simNumber=50

actualTime=0.0


print("connected to gateway")


class Dataset():

	def __init__(self,dbname,ip,port,query=None,script=None):
		if query is not None:
			client = InfluxDBClient(ip, int(port), database=dbname)
			result = client.query(query)
			points = result.get_points()
			self.points=points
		elif script is not None:
			exec(script)



class PeriodicEventSource():
	def __init__(self, name, dist, ecalls, mlgp=None, dirac=None):
		self.name=name
		self.dist=dist
		self.ecalls=ecalls
		if mlgp is not None:
			self.gp=mlgp.gp
			self.dirac=None
		elif dirac is not None:
			self.dirac=dirac
			self.gp=None
		else:
			self.gp=None
			self.dirac=None

	def sample(self,faults):
		if self.gp is None and self.dirac is None:
			event_cntr=0
			act_time=0.0
			while act_time<simTime:
				time=pyro.sample(self.name+"_sample_"+str(event_cntr),self.dist).item()
				event_cntr=event_cntr+1
				act_time=act_time+time
				for call in self.ecalls:
					faults.append(FailureEvent(self,act_time,call))
		elif self.dirac is not None:
			event_cntr=0
			act_time=0.0
			while act_time<simTime and event_cntr<len(self.dirac.points):
				act_time=self.dirac.points[event_cntr]
				event_cntr=event_cntr+1
				for call in self.ecalls:
					faults.append(FailureEvent(self,act_time,call))
		else:
			event_cntr=0
			act_time=0.0
			while act_time<simTime:
				mu,sig=self.gp.forward(torch.tensor([act_time]), full_cov=False, noiseless=False)
				time=pyro.sample(self.name+"_sample_GP_"+str(event_cntr),pyro.distributions.Normal(mu,sig)).item()
				event_cntr=event_cntr+1
				act_time=act_time+time
				for call in self.ecalls:
					faults.append(FailureEvent(self,act_time,call))


class EventSource():
	def __init__(self, name, dist, ecalls):
		self.name=name
		self.dist=dist
		self.ecalls=ecalls
	def sample(self,faults):
		time=pyro.sample(self.name+"_sample",self.dist).item()
		for call in self.ecalls:
			faults.append(FailureEvent(self,time,call))


class ZeroEventSource():
		def __init__(self,name,dist,calls):
			self.name=name
			self.dist=dist
		def sample(self,faults):
			exists=pyro.sample(self.name+"_sample",self.dist).item()
			if exists==1.0:
				for call in calls:
					faults.append(FailureEvent(self,time,call))


class MLGaussProcess():
	def __init__(self,dataset, kernel, lr):
		points=dataset.points
		x = []
		t = []
		y = []
		i = 0
		t0 = 0
		for p in points:
			if i == 0:
				t0 = datetime.datetime.strptime(p.pop("time"), '%Y-%m-%dT%H:%M:%S.%fZ')
				t.append(t0)
			else:
				t.append(datetime.datetime.strptime(p.pop("time"), '%Y-%m-%dT%H:%M:%S.%fZ'))
			t[i] = t[i] - t0
			x.append(t[i].total_seconds())
			yi = list(p.values())
			if len(yi) is 1:
				y.append(yi[0])
			else:
				y.append(yi)
			i = i + 1
	
		x = torch.tensor(x)
		y = torch.tensor(y)
		X = x
	
		# initialize the inducing inputs
		Xu = torch.arange(1.) / 6.0
	
		#kernel = gp.kernels.Sum(gp.kernels.Periodic(input_dim=1), gp.kernels.Brownian(input_dim=1))
		# we increase the jitter for better numerical stability
		sgpr = gp.models.SparseGPRegression(X=X, y=y, kernel=kernel, Xu=Xu, jitter=1.0e-5)
	
		# the way we setup inference is similar to above
		optimizer = torch.optim.Adam(sgpr.parameters(), lr=lr)
		loss_fn = pyro.infer.Trace_ELBO().differentiable_loss
		losses = []
		num_steps = 2000
		for i in range(num_steps):
			optimizer.zero_grad()
			loss = loss_fn(sgpr.model, sgpr.guide)
			if i % 20 == 0:
				print("Step: ", i, " Loss: ", loss)
			loss.backward()
			optimizer.step()
			losses.append(loss.item())
		self.gp=sgpr



class FailureEvent():
	def __init__(self,eventSource,failureTime):
		self.eventSource=eventSource
		self.failureTime=failureTime
	def __init__(self,eventSource,failureTime,eventCall):
		self.eventSource=eventSource
		self.failureTime=failureTime
		self.eventCall=eventCall




faults=[]
def generateFaults(eventSources):
	global faults
	for eventSource in eventSources:
		eventSource.sample(faults)
	filter(lambda f: f.failureTime>=0.0,faults)
	faults.sort(key=lambda f: f.failureTime)
	return faults




lifetime = 0.001



sources = [
		PeriodicEventSource(
name="LPDetection__Stream_0",
dist=pyro.distributions.Exponential(torch.tensor(10.0)),dirac=None,mlgp=None
		,
		ecalls=[lambda:sctmodel.getLPDetection().getInStream().raiseNewData(),]
		),
]


print("failure sources have been created")


def simulateUntilStop():
	faults=generateFaults(sources)
	gateway.entry_point.getEntryPoint().reset()
	for fault in faults:
		if fault.eventSource.name == "LPDetection__Stream_0":
			sctmodel.getLPDetection().getInStream().raiseNewData()
		if gateway.entry_point.getEntryPoint().getState() == "stop":
			return fault.failureTime
	return -1
	

#definition of delay classes
class Delay_DetectionDelay_DS_0():
	def __init__(self,name,dist,gateway,faults,detmodel,dirac=None,mlgp=None):
		self.gateway = gateway
		self.name=name
		self.dist=dist
		self.faults=faults
		self.event_cntr=0
		self.detmodel=detmodel
		self.firstCall=True
		detmodel.getLPDetection().getToHW().registerListener(self)

	#definition of the interface functions
	def raiseNewData(self):
		time=pyro.sample(self.name+"_sample_delay_"+str(self.event_cntr),self.dist).item()
		self.event_cntr=self.event_cntr+1
		failureTime=time+actualTime
		callEvent=lambda:self.detmodel.getLPDetection().getFromHW().raiseNewData();
		self.faults.append(FailureEvent(self,failureTime,callEvent))


	def notify(self, obj):
		self.faults.append(FailureEvent(self,time+actualTime))
		self.faults.sort(key=lambda f: f.failureTime)

	class Java:
		implements = ["hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface$Listener$Provided"]
class Delay_NetDelay_DS_1():
	def __init__(self,name,dist,gateway,faults,detmodel,dirac=None,mlgp=None):
		self.gateway = gateway
		self.name=name
		self.dist=dist
		self.faults=faults
		self.event_cntr=0
		self.detmodel=detmodel
		self.firstCall=True
		detmodel.getLPNetworking().getToHW().registerListener(self)

	#definition of the interface functions
	def raiseNewData(self):
		time=pyro.sample(self.name+"_sample_delay_"+str(self.event_cntr),self.dist).item()
		self.event_cntr=self.event_cntr+1
		failureTime=time+actualTime
		callEvent=lambda:self.detmodel.getLPNetworking().getFromHW().raiseNewData();
		self.faults.append(FailureEvent(self,failureTime,callEvent))


	def notify(self, obj):
		self.faults.append(FailureEvent(self,time+actualTime))
		self.faults.sort(key=lambda f: f.failureTime)

	class Java:
		implements = ["hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface$Listener$Provided"]
class Delay_RecognitionDelay_DS_2():
	def __init__(self,name,dist,gateway,faults,detmodel,dirac=None,mlgp=None):
		self.gateway = gateway
		self.name=name
		self.dist=dist
		self.faults=faults
		self.event_cntr=0
		self.detmodel=detmodel
		self.firstCall=True
		detmodel.getLPRecognition().getToHW().registerListener(self)

	#definition of the interface functions
	def raiseNewData(self):
		time=pyro.sample(self.name+"_sample_delay_"+str(self.event_cntr),self.dist).item()
		self.event_cntr=self.event_cntr+1
		failureTime=time+actualTime
		callEvent=lambda:self.detmodel.getLPRecognition().getFromHW().raiseNewData();
		self.faults.append(FailureEvent(self,failureTime,callEvent))


	def notify(self, obj):
		self.faults.append(FailureEvent(self,time+actualTime))
		self.faults.sort(key=lambda f: f.failureTime)

	class Java:
		implements = ["hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface$Listener$Provided"]


#definition of delay objects
delays=[
Delay_DetectionDelay_DS_0(
	name="DetectionDelay_DS_0",
	dist=pyro.distributions.Normal(
				torch.tensor(0.03),
				torch.tensor(0.005)
			),dirac=None,mlgp=None,
	gateway=gateway,
	faults=faults,
	detmodel=sctmodel
),

Delay_NetDelay_DS_1(
	name="NetDelay_DS_1",
	dist=pyro.distributions.Normal(
				torch.tensor(0.001),
				torch.tensor(1.0E-4)
			),dirac=None,mlgp=None,
	gateway=gateway,
	faults=faults,
	detmodel=sctmodel
),

Delay_RecognitionDelay_DS_2(
	name="RecognitionDelay_DS_2",
	dist=pyro.distributions.Normal(
				torch.tensor(1.0),
				torch.tensor(0.2)
			),dirac=None,mlgp=None,
	gateway=gateway,
	faults=faults,
	detmodel=sctmodel
),

]


print("random delays have been created")

def simulateUntilTime():
	global actualTime
	actualTime=0
	faults=generateFaults(sources)
	gateway.entry_point.getEntryPoint().reset()
	while len(faults)>0 and actualTime<simTime:
		filter(lambda f: f.failureTime>=0.0,faults)
		faults.sort(key=lambda f: f.failureTime)
		fault=faults.pop(0)
		actualTime=fault.failureTime
		
		#print(fault.eventSource.name+" at time: "+str(actualTime))
		fault.eventCall()
		for i in range(10):
				sctmodel.runCycle()
		
	return gateway.entry_point.getEntryPoint().getFreq()


def simulate():
	return simulateUntilTime()

print("testing the simulator")

for i in range(1):
	print(simulate())
		

print("start simulator")

prior_data=list()
for i in range(simNumber):
	if i % 5 == 0:
		print("Simulation step: ", i)
	prior_data.append(simulate())

plt.hist(prior_data)
plt.show()

print("simulation has been finished")
print("shuting down the Py4J gateway")
gateway.shutdown()
print("exit")

