package hu.bme.mit.gamma.casestudy.camea.paralellqueuemanager;

import hu.bme.mit.gamma.casestudy.camea.*;

public class ReflectiveParalellQueueManager implements ReflectiveComponentInterface {
	
	private ParalellQueueManager wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveParalellQueueManager() {
		wrappedComponent = new ParalellQueueManager();
	}
	
	public ReflectiveParalellQueueManager(ParalellQueueManager wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public ParalellQueueManager getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "DataLoss", "InStream", "FromHW", "ToHW", "OutStream" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "DataLoss":
				return new String[] { "NewData" };
			case "InStream":
				return new String[] { "NewData" };
			case "FromHW":
				return new String[] { "NewData" };
			case "ToHW":
				return new String[] { "NewData" };
			case "OutStream":
				return new String[] { "NewData" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "InStream.NewData":
				wrappedComponent.getInStream().raiseNewData();
				break;
			case "FromHW.NewData":
				wrappedComponent.getFromHW().raiseNewData();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "DataLoss.NewData":
				if (wrappedComponent.getDataLoss().isRaisedNewData()) {
					return true;
				}
				break;
			case "ToHW.NewData":
				if (wrappedComponent.getToHW().isRaisedNewData()) {
					return true;
				}
				break;
			case "OutStream.NewData":
				if (wrappedComponent.getOutStream().isRaisedNewData()) {
					return true;
				}
				break;
			default:
				throw new IllegalArgumentException("Not known port-out event combination: " + portEvent);
		}
		return false;
	}
	
	public boolean isStateActive(String region, String state) {
		return wrappedComponent.isStateActive(region, state);
	}
	
	public String[] getRegions() {
		return new String[] { "main_region" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "main_region":
				return new String[] { "Full", "AllProcWorking", "NotAllProcWorking" };
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
	public void schedule(String instance) {
		wrappedComponent.runCycle();
	}
	
	public String[] getVariables() {
		return new String[] { "act_proc_num", "proc_num", "q_size", "b_size" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
			case "act_proc_num":
				return wrappedComponent.getAct_proc_num();
			case "proc_num":
				return wrappedComponent.getProc_num();
			case "q_size":
				return wrappedComponent.getQ_size();
			case "b_size":
				return wrappedComponent.getB_size();
		}
		throw new IllegalArgumentException("Not known variable: " + variable);
	}
	
	public String[] getComponents() {
		return new String[] { };
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			// If the class name is given, then it will return itself
			case "ParalellQueueManager":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
