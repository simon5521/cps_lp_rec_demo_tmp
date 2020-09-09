
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


gateway = JavaGateway(callback_server_parameters=CallbackServerParameters())
sctmodel = gateway.entry_point.getEntryPoint().getDetModel()

rate=5.0

simTime=1000.0/rate

actualTime=0.0

lostVikings=0.0


class PeriodicEventSource():
	def __init__(self, name, dist, ecalls):
		self.name=name
		self.dist=dist
		self.ecalls=ecalls
	def sample(self,faults):
		event_cntr=0
		act_time=0.0
		while act_time<simTime:
			time=pyro.sample(self.name+"_sample_"+str(event_cntr),self.dist).item()
			event_cntr=event_cntr+1
			act_time=act_time+time
			for call in self.ecalls:
				faults.append(FailureEvent(self,act_time,call))
		print(event_cntr)

class ContEventSource():
	def __init__(self,name,dist):
		self.name=name
		self.dist=dist
	def sample(self,faults):
		time=pyro.sample(self.name+"_sample",self.dist).item()
		faults.append(FailureEvent(self,time))

class EventSource():
	def __init__(self, name, dist, ecalls):
		self.name=name
		self.dist=dist
		self.ecalls=ecalls
	def sample(self,faults):
		time=pyro.sample(self.name+"_sample",self.dist).item()
		for call in self.ecalls:
			faults.append(FailureEvent(self,time,call))


class DiscEventSource():
		def __init__(self,name,dist):
			self.name=name
			self.dist=dist
			self.calls=calls
		def sample(self,faults):
			exists=pyro.sample(self.name+"_sample",self.dist).item()
			if exists==1.0:
				faults.append(FailureEvent(self,0))

class ZeroEventSource():
		def __init__(self,name,dist,calls):
			self.name=name
			self.dist=dist
		def sample(self,faults):
			exists=pyro.sample(self.name+"_sample",self.dist).item()
			if exists==1.0:
				for call in calls:
					faults.append(FailureEvent(self,time,call))

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




#sources = [
#]

sources = [
		PeriodicEventSource(
		"LPDetQueueManager__Traffic_0",
		pyro.distributions.Exponential(torch.tensor(rate))
		
		,
		[lambda:sctmodel.getLPDetQueueManager().getTraffic().raiseNewCar(),]
		),
]



def simulateUntilStop():
	faults=generateFaults(sources)
	gateway.entry_point.getEntryPoint().reset()
	for fault in faults:
		if fault.eventSource.name == "LPDetQueueManager__Traffic_0":
			sctmodel.getLPDetQueueManager().getTraffic().raiseNewCar()
		if gateway.entry_point.getEntryPoint().getState() == "stop":
			return fault.failureTime
	return -1

#definition of delay classes
class Delay_DetectionDelay_DS_0():
	def __init__(self,name,dist,gateway,faults,detmodel):
		self.gateway = gateway
		self.name=name
		self.dist=dist
		self.faults=faults
		self.event_cntr=0
		self.detmodel=detmodel
		self.firstCall=True
		detmodel.getLPDetQueueManager().getImg().registerListener(self)

	#definition of the interface functions
	def raiseNewData(self):
		global lostVikings
		if self.firstCall:
			lostVikings=lostVikings+1
			time=pyro.sample(self.name+"_sample_delay_"+str(self.event_cntr),self.dist).item()
			self.event_cntr=self.event_cntr+1
			failureTime=time+actualTime
			callEvent=lambda:self.detmodel.getLPDetQueueManager().getPreProcessedImg().raiseNewData();
			self.faults.append(FailureEvent(self,failureTime,callEvent))
			callEvent=lambda:self.detmodel.getSmartControl().getInPicStream().raiseNewData();
			self.faults.append(FailureEvent(self,failureTime,callEvent))
			self.firstCall=False
		else:
			self.firstCall=True


	def notify(self, obj):
		self.faults.append(FailureEvent(self,time+actualTime))
		self.faults.sort(key=lambda f: f.failureTime)

	class Java:
		implements = ["hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StreamInterface$Listener$Provided"]
class Delay_RecognitionDelay_DS_1():
	def __init__(self,name,dist,gateway,faults,detmodel):
		self.gateway = gateway
		self.name=name
		self.dist=dist
		self.faults=faults
		self.event_cntr=0
		self.detmodel=detmodel
		self.firstCall=True
		detmodel.getLPRecQueueManager().getPreProcessedImgOut().registerListener(self)

	#definition of the interface functions
	def raiseNewData(self):
		if self.firstCall:
			time=pyro.sample(self.name+"_sample_delay_"+str(self.event_cntr),self.dist).item()
			self.event_cntr=self.event_cntr+1
			failureTime=time+actualTime
			callEvent=lambda:self.detmodel.getLPRecQueueManager().getProcessedImg().raiseNewData();
			self.faults.append(FailureEvent(self,failureTime,callEvent))
			self.firstCall=False
		else:
			self.firstCall=True


	def notify(self, obj):
		self.faults.append(FailureEvent(self,time+actualTime))
		self.faults.sort(key=lambda f: f.failureTime)

	class Java:
		implements = ["hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StreamInterface$Listener$Provided"]

#definition of delay objects
delays=[
Delay_DetectionDelay_DS_0(
	name="DetectionDelay_DS_0",
	dist=
		pyro.distributions.Normal(torch.tensor(0.0333),torch.tensor(0.001)),
	gateway=gateway,
	faults=faults,
	detmodel=sctmodel
),

Delay_RecognitionDelay_DS_1(
	name="RecognitionDelay_DS_1",
	dist=
		pyro.distributions.Exponential(torch.tensor(0.5)),
	gateway=gateway,
	faults=faults,
	detmodel=sctmodel
),

]


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

for i in range(1):
	lostVikings=0.0
	print(simulateUntilTime())
	print(lostVikings)
		

gateway.shutdown()

