import os
import matplotlib.pyplot as plt
import torch

import pyro
import pyro.contrib.gp as gp
import pyro.distributions as dist


pyro.enable_validation(True)       # can help with debugging
pyro.set_rng_seed(0)


data = [ 0.385598354
,
0.433279884
,
0.37271063
,
0.375004129
,
0.374148976
,
0.414905539
,
0.449342833
,
0.417252785
,
0.375906291
,
0.376523907
,
0.384315855
,
0.422228566
,
0.393512498
,
0.487940845
,
0.43313064
,
0.455842658
,
0.435982453
,
0.38017915
,
0.370259989
,
0.375193262
,
0.38290653
,
0.37860512
,
0.371140274
,
0.374240253
,
0.378837725
,
0.363760596
,
0.37481114
,
0.36158728
,
0.430623713
,
0.497260872
,
0.365633055
,
0.380153094
,
0.360322586
,
0.350919709
,
0.347418106
,
0.356888565
,
0.371029653
,
0.420338379
,
0.426273484
,
0.398732321
,
0.482129626
,
0.468244635
,
0.364010845
,
0.375366183
,
0.382710137
,
0.383458758
,
0.376729561
,
0.358895385
,
0.371226728
,
0.371478026
,
0.411612734
,
0.400365241
,
0.439301671
,
0.391265562
,
0.375832821
,
0.401609055
,
0.384666349
,
0.389605398
,
0.375913848
,
0.376081044
,
0.375819936
,
0.385576585
,
0.377482597
,
0.451733649
,
0.451782042
,
0.46253263
,
0.39920852
,
0.394922806
,
0.398238955
,
0.399691089
,
0.397536095
,
0.392962602
,
0.403694205
,
0.416412967
,
0.40052556
,
0.405804048
,
0.470180261
,
0.473970118
,
0.624919671
,
0.454910676
,
0.391113556
,
0.441224508
,
0.427863176
,
0.403000396
,
0.401705085
,
0.405285496
,
0.408991468
,
0.393828758
,
0.418005334
,
0.45609891
,
0.41933651
,
0.403618483
,
0.405554735
,
0.391253077
,
0.384529525
,
0.415441636
,
0.454038651
,
0.392876632
,
0.365837239
,
0.36510953
,
0.368331376
,
0.373943911
,
0.380320387
,
0.362739191
,
0.382770722
,
0.37736349
,
0.370303811
,
0.37134394
,
0.369484911
,
0.384034484
,
0.3798936
,
0.43435589
,
0.369887759
,
0.36807706
,
0.382272614
,
0.381230766
,
0.382437164
,
0.380777291
,
0.376921343
,
0.380188731
,
0.410277585
,
0.426626843
,
0.411238167,

0.395350477,

0.374494207,

0.374091565,

0.381272638,

0.381225226,

0.381530078,

0.38170088,

0.379063134,

0.377809605,

0.405414362,

0.380107313,

0.427483978,

0.447298629,

0.397276134,

0.456332106,

0.419099722,

0.427935351,

0.418212689,

0.452266353,

0.385394187,

0.394680986,

0.407141895,

0.417351714,

0.410763469,

0.427124092,

0.377698404,

0.42723714,

0.394072201,

0.356867691,

0.350943927,

0.352670597,

0.364506774,

0.36891671,

0.365970239,

0.35392319,

0.362367163,

0.364973492,

0.395918951,

0.391875714,

0.386056215,

0.405662795,

0.393483885,

0.379904729,

0.375926034,

0.38029669,

0.39835831,

0.383498747,

0.388075451,

0.385921934,

0.453421934,

0.450431002,

0.413600008,

0.425869592,

0.433775391,

0.37856269,

0.376492896,

0.394494326,

0.393016178,

0.388074266,

0.382953963,

0.401378292,

0.401188441,

0.401308875,

0.403656543,

0.397672116,

0.397175015,

0.383891743,

0.374149103,

0.373358406,

0.374285036,

0.373967481,

0.373875972,

0.375169522,

0.373787445,

0.374896553,

0.384731383,

0.403840459
]


X = torch.tensor([float(i) for i in range(200)])
X = torch.linspace(0.0,2.5,200)
y = torch.tensor(data)

# note that this helper function does three different things:
# (i) plots the observed data;
# (ii) plots the predictions from the learned GP after conditioning on data;
# (iii) plots samples from the GP prior (with no conditioning on observed data)

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


# initialize the inducing inputs
Xu = torch.arange(20.) / 4.0

# initialize the kernel and model
pyro.clear_param_store()
kernel = gp.kernels.Sum(gp.kernels.Periodic(input_dim=1),gp.kernels.Periodic(input_dim=1))
# we increase the jitter for better numerical stability
sgpr = gp.models.SparseGPRegression(X, y, kernel, Xu=Xu, jitter=1.0e-5)

# the way we setup inference is similar to above
optimizer = torch.optim.Adam(sgpr.parameters(), lr=0.005)
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

plt.plot(losses);
plt.show()
# let's look at the inducing points we've learned
print("inducing points:\n{}".format(sgpr.Xu.data.numpy()))
# and plot the predictions from the sparse GP
plot(model=sgpr, plot_observed_data=True, plot_predictions=True)