
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



print("python script is called")

gateway = JavaGateway(callback_server_parameters=CallbackServerParameters())
sctmodel = gateway.entry_point.getEntryPoint().getDetModel()


simTime=10.0

actualTime=0.0


print("connected to gateway")


class Dataset():

	def __init__(self,dbname,ip,port,query):

		client = InfluxDBClient(ip, port, database=dbname)

		result = client.query(query)

		points = result.get_points()

		self.points=points




class PeriodicEventSource():
	def __init__(self, name, dist, ecalls, dataset=None, kernel=None):
		self.name=name
		self.dist=dist
		self.ecalls=ecalls
		if dataset is not None:
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

			kernel = gp.kernels.Sum(gp.kernels.Periodic(input_dim=1), gp.kernels.Brownian(input_dim=1))
			# we increase the jitter for better numerical stability
			sgpr = gp.models.SparseGPRegression(X=X, y=y, kernel=kernel, Xu=Xu, jitter=1.0e-5)

			# the way we setup inference is similar to above
			optimizer = torch.optim.Adam(sgpr.parameters(), lr=0.05)
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
		else:
			self.gp=None

	def sample(self,faults):
		if self.gp is None:
			event_cntr=0
			act_time=0.0
			while act_time<simTime:
				time=pyro.sample(self.name+"_sample_"+str(event_cntr),self.dist).item()
				event_cntr=event_cntr+1
				act_time=act_time+time
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
		EventSource(
		"uC1env_ucsct__UCFault_0",
		pyro.distributions.Normal(torch.tensor(0.8),torch.tensor(0.05))
		,
		[lambda:sctmodel.getUC1env().getUcsct().getHWFault().raiseShutdown(),]
		),
		EventSource(
		"uC2env_ucsct__UCFault_1",
		pyro.distributions.Normal(torch.tensor(0.8),torch.tensor(0.05))
		,
		[lambda:sctmodel.getUC2env().getUcsct().getHWFault().raiseShutdown(),]
		),
		EventSource(
		"epas__SensorFault_2",
		pyro.distributions.Exponential(torch.tensor(0.9))
		,
		[lambda:sctmodel.getEpas().getS1AFault().raiseDet(),]
		),
		EventSource(
		"epas__SensorFault_3",
		pyro.distributions.Exponential(torch.tensor(0.9))
		,
		[lambda:sctmodel.getEpas().getS2AFault().raiseDet(),]
		),
		EventSource(
		"epas__SensorFault_4",
		pyro.distributions.Exponential(torch.tensor(0.9))
		,
		[lambda:sctmodel.getEpas().getS3AFault().raiseDet(),]
		),
		EventSource(
		"epas__SensorFault_5",
		pyro.distributions.Exponential(torch.tensor(0.9))
		,
		[lambda:sctmodel.getEpas().getS1BFault().raiseDet(),]
		),
		EventSource(
		"epas__SensorFault_6",
		pyro.distributions.Exponential(torch.tensor(0.9))
		,
		[lambda:sctmodel.getEpas().getS2BFault().raiseDet(),]
		),
		EventSource(
		"epas__SensorFault_7",
		pyro.distributions.Exponential(torch.tensor(0.9))
		,
		[lambda:sctmodel.getEpas().getS3BFault().raiseDet(),]
		),
]


print("failure sources have been created")


def simulateUntilStop():
	faults=generateFaults(sources)
	gateway.entry_point.getEntryPoint().reset()
	for fault in faults:
		if fault.eventSource.name == "uC1env_ucsct__UCFault_0":
			sctmodel.getUC1env().getUcsct().getHWFault().raiseShutdown()
		if fault.eventSource.name == "uC2env_ucsct__UCFault_1":
			sctmodel.getUC2env().getUcsct().getHWFault().raiseShutdown()
		if fault.eventSource.name == "epas__SensorFault_2":
			sctmodel.getEpas().getS1AFault().raiseDet()
		if fault.eventSource.name == "epas__SensorFault_3":
			sctmodel.getEpas().getS2AFault().raiseDet()
		if fault.eventSource.name == "epas__SensorFault_4":
			sctmodel.getEpas().getS3AFault().raiseDet()
		if fault.eventSource.name == "epas__SensorFault_5":
			sctmodel.getEpas().getS1BFault().raiseDet()
		if fault.eventSource.name == "epas__SensorFault_6":
			sctmodel.getEpas().getS2BFault().raiseDet()
		if fault.eventSource.name == "epas__SensorFault_7":
			sctmodel.getEpas().getS3BFault().raiseDet()
		if gateway.entry_point.getEntryPoint().getState() == "stop":
			return fault.failureTime
	return -1
	

#definition of delay classes


#definition of delay objects
delays=[
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


print("Haho!!!")

for i in range(10):
	print(simulateUntilStop())
		
prior_data=list()
for i in range(1000):
    prior_data.append(simulateUntilStop())

plt.hist(prior_data)
plt.show()

print("Haho!!!")

gateway.shutdown()

