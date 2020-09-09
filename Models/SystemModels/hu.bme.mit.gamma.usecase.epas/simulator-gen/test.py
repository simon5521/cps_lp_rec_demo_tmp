from py4j.java_gateway import JavaGateway, CallbackServerParameters

import pyro
import torch
import matplotlib.pyplot as plt
import matplotlib
from pyro.infer import SVI, Trace_ELBO
from pyro.optim import Adam
#import pyro.distributions as dist
import torch.distributions.constraints as constraints
from pyro.distributions.distribution import Distribution
import math



class DelayEventSourceuC1env_ucsct__UCFault_DS_0(object):
    """def __init__(self,name,dist,gateway,faults,detmodel):
        self.gateway = gateway
        self.name=name
        self.dist=dist
        self.faults=faults
        self.event_cntr=0
        detmodel.getUC1env().getUcsct().getFault().registerListemer(self)"""
    def __init__(self,name,dist,gateway,faults,detmodel):
        self.gateway = gateway
        self.name=name
        self.dist=dist
        self.faults=faults
        self.event_cntr=0
        detmodel.getUC1env().getUcsct().getFault().registerListener(self)
        
    #definition of the interface functions
    def raiseShutdown(self):
        time=pyro.sample(self.name+"_sample_delay_"+str(event_cntr),self.dist).item()
        event_cntr=event_cntr+1
        failureTime=time+actualTime
        callEvent=lambda:detmodel.getUC1env().getUcsct().getHWFault().raiseShutdown();
        self.faults.append(FailureEvent(self,failureTime,callEvent))


    def notify(self, obj):
        self.faults.append(FailureEvent(self,time+actualTime))
        self.faults.sort(key=lambda f: f.failureTime)

    class Java:
        implements = ["hu.bme.mit.gamma.usecase.epas.interfaces.UCFaultInterface$Listener$Provided"]






if __name__ == "__main__":
    faults=[]
        
    gateway = JavaGateway(
        callback_server_parameters=CallbackServerParameters())
    #gateway.entry_point.registerListener(listener)
    epas=gateway.entry_point.getEpas()
    listener = DelayEventSourceuC1env_ucsct__UCFault_DS_0("bela",pyro.distributions.Exponential(torch.tensor(0.01)),gateway,faults,epas)
    sctmodel=epas    


    
    epas.getUC1env().getUcsct().getHWFault().raiseShutdown()
    gateway.entry_point.notifyAllListeners()
    gateway.shutdown()