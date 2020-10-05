package hu.bme.mit.gamma.casestudy.camea.queuemanager;

import hu.bme.mit.gamma.casestudy.camea.*;

public class ReflectiveQueueManager implements ReflectiveComponentInterface {
	
	private QueueManager wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveQueueManager() {
		wrappedComponent = new QueueManager();
	}
	
	public ReflectiveQueueManager(QueueManager wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public QueueManager getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "InStream", "ToHW", "FromHW", "OutStream", "DataLoss" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "InStream":
				return new String[] { "NewData" };
			case "ToHW":
				return new String[] { "NewData" };
			case "FromHW":
				return new String[] { "NewData" };
			case "OutStream":
				return new String[] { "NewData" };
			case "DataLoss":
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
			case "DataLoss.NewData":
				if (wrappedComponent.getDataLoss().isRaisedNewData()) {
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
				return new String[] { "Full", "Working", "NoData" };
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
	public void schedule(String instance) {
		wrappedComponent.runCycle();
	}
	
	public String[] getVariables() {
		return new String[] { "q_size", "b_size" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
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
			case "QueueManager":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
