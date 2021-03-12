import os
import matplotlib.pyplot as plt
import torch

import pyro
import pyro.contrib.gp as gp
import pyro.distributions as dist
from influxdb import InfluxDBClient
from loggingUtils.db_manager import *
import datetime
"""
save_det_rt(0.12)
save_det_rt(0.23)
save_det_rt(0.21)
save_det_rt(0.19)
save_det_rt(0.24)
save_det_rt(0.30)
save_det_rt(0.32)
save_det_rt(0.18)
save_det_rt(0.26)
save_det_rt(0.19)
save_det_rt(0.31)
save_det_rt(0.23)
save_det_rt(0.12)
"""
close()

client = InfluxDBClient("localhost", 8086, database="smartcity")

query="select runtime, time from lp_det_rt where time> now()-3d-3h-25m"

result = client.query(query)

print("Result: {0}".format(result))

points=result.get_points()
print(points)

"""for p in points:
    t=p.pop("time")
    print(t," - ",datetime.datetime.strptime(t,'%Y-%m-%dT%H:%M:%S.%fZ').timestamp())
    print(list(p.values()))"""


x = []
t = []
y = []
i=0
t0=0
for p in points:
    if i==0:
        t0=datetime.datetime.strptime(p.pop("time"),'%Y-%m-%dT%H:%M:%S.%fZ')
        t.append(t0)
    else:
        t.append(datetime.datetime.strptime(p.pop("time"),'%Y-%m-%dT%H:%M:%S.%fZ'))
    t[i]=t[i]-t0
    x.append(t[i].total_seconds())
    yi=list(p.values())
    if len(yi) is 1:
        y.append(yi[0])
    else:
        y.append(yi)
    i=i+1

x = torch.tensor(x)
y = torch.tensor(y)
X=x
print("x=",x)
print("y=",y)

def plot(plot_observed_data=False, plot_predictions=False, n_prior_samples=0,
         model=None, kernel=None, n_test=800):

    plt.figure(figsize=(12, 6))
    if plot_observed_data:
        plt.plot(X.numpy(), y.numpy(), 'kx')
    if plot_predictions:
        Xtest = torch.linspace(0, 10, n_test)  # test inputs
        # compute predictive mean and variance
        with torch.no_grad():
            if type(model) == gp.models.VariationalSparseGP:
                mean, cov = model(Xtest, full_cov=True)
            else:
                mean, cov = model(Xtest, full_cov=True, noiseless=False)
        sd = cov.diag().sqrt()  # standard deviation at each input point x
        plt.plot(Xtest.numpy(), mean.numpy(), 'r', lw=2)  # plot the mean
        plt.fill_between(Xtest.numpy(),  # plot the two-sigma uncertainty about the mean
                         (mean - 2.0 * sd).numpy(),
                         (mean + 2.0 * sd).numpy(),
                         color='C0', alpha=0.3)
    if n_prior_samples > 0:  # plot samples from the GP prior
        Xtest = torch.linspace(0, 10, n_test)  # test inputs
        noise = (model.noise if type(model) != gp.models.VariationalSparseGP
                 else model.likelihood.variance)
        cov = kernel.forward(Xtest) + noise.expand(n_test).diag()
        samples = dist.MultivariateNormal(torch.zeros(n_test), covariance_matrix=cov)\
                      .sample(sample_shape=(n_prior_samples,))
        plt.plot(Xtest.numpy(), samples.numpy().T, lw=2, alpha=0.4)

    plt.xlim(0.5, 5.)
    plt.show()

pyro.enable_validation(True)       # can help with debugging
pyro.set_rng_seed(0)


# initialize the inducing inputs
Xu = torch.arange(1.) / 6.0

# initialize the kernel and model
pyro.clear_param_store()
kernel = gp.kernels.Sum(gp.kernels.Periodic(input_dim=1),gp.kernels.Brownian(input_dim=1))
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
    if i % 20 ==0:
        print("Step: ",i," Loss: ",loss)
    loss.backward()
    optimizer.step()
    losses.append(loss.item())

#plt.plot(losses);
#plt.show()
# let's look at the inducing points we've learned
print("inducing points:\n{}".format(sgpr.Xu.data.numpy()))
# and plot the predictions from the sparse GP
#plot(model=sgpr, plot_observed_data=True, plot_predictions=True)

for i in range(10):
    print(sgpr.forward(torch.tensor([i/10.0]), full_cov=False, noiseless=False))

