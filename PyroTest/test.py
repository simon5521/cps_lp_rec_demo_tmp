import math
import os
import torch
import torch.distributions.constraints as constraints
import pyro
from pyro.optim import Adam
from pyro.infer import SVI, Trace_ELBO
import pyro.distributions as dist
from influxdb import InfluxDBClient
import matplotlib.pyplot as plt
import numpy as np
from math import exp

class Dataset():
    def __init__(self, dbname, ip, port, query=None):
        if query is not None:
            client = InfluxDBClient(ip, int(port), database=dbname)
            result = client.query(query)
            points = result.get_points()
            self.points = points


data=Dataset(
    "smartcity",
    "localhost",
    8086,
    """
    SELECT "data" FROM "runtime" WHERE ("nodeid" = '252f78f2-9716-11eb-adde-dca632b81f1b') LIMIT 200
    """
)


data=[float(p["data"]) for p in list(data.points)]
md=min(data)
data=[d-md for d in data]
a=-0.022
def guide(data):
    pass

def model(data):
    alpha_q = pyro.param("alpha_q", torch.tensor(1.0),
                         constraint=constraints.positive)
    beta_q = pyro.param("beta_q", torch.tensor(0.0),
                        constraint=constraints.positive)
    for i in range(len(data)):
            # observe datapoint i using the bernoulli likelihood
            #pyro.sample("obs_{}".format(i), dist.Normal(alpha_q,beta_q), obs=torch.tensor(data[i]))
            pyro.sample("obs_{}".format(i), dist.Exponential(alpha_q), obs=(torch.tensor(data[i])))


adam_params = {"lr": 0.05, "betas": (0.99, 0.999)}
optimizer = Adam(adam_params)
n_steps=2000
# setup the inference algorithm
svi = SVI(model, guide, optimizer, loss=Trace_ELBO())

losses=list()
ploss=100000000.0
# do gradient steps
for step in range(n_steps):
    loss=svi.step(data)
    losses.append(loss)
    if step % 10 == 0:
        print("loss=",loss)
    if ploss-loss<0.001:
        break
    ploss=loss

print("haho")
# grab the learned variational parameters
m = pyro.param("alpha_q").item()
s = pyro.param("beta_q").item()
print(m)
print(s)

#xx = list(np.arange(m-2*s, m+2*s, s/10))
xx = list(np.arange(0, 4/m, 0.1/m))
yy = []
#d=dist.Normal(loc=torch.tensor(m),scale=torch.tensor(s))
d=dist.Exponential(m)
print(d)
print(d.log_prob(torch.tensor(m)))
for x in xx:
    #y=float(exp(dist.Normal(loc=torch.tensor(m),scale=torch.tensor(s)).log_prob(torch.tensor(x))))
    y=float(exp(dist.Exponential(m).log_prob(torch.tensor(x))))
    yy.append(y)


fig1, a1 = plt.subplots()
fig2, a2 = plt.subplots()

a1.plot(losses)
a1.set_ylabel('loss')
a1.set_xlabel('step')
a1.set_title("model fitting: losses")
a2.hist(data,bins=10)
a2.plot(xx,yy)
a2.set_ylabel('frequency')
a2.set_xlabel('delay (s)')
a2.set_title("model fitting: models")
plt.show()

