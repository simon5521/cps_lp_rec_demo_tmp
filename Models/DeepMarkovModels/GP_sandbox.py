import datetime

import matplotlib.pyplot as plt
import torch

import pyro
import pyro.contrib.gp as gp
import pyro.distributions as dist
from influxdb import InfluxDBClient



class Dataset():

	def __init__(self,dbname,ip,port,query):

		client = InfluxDBClient(ip, port, database=dbname)

		result = client.query(query)

		points = result.get_points()

		self.points=points





simTime=0.12


class FailureEvent():
	def __init__(self,eventSource,failureTime):
		self.eventSource=eventSource
		self.failureTime=failureTime
	def __init__(self,eventSource,failureTime,eventCall):
		self.eventSource=eventSource
		self.failureTime=failureTime
		self.eventCall=eventCall

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