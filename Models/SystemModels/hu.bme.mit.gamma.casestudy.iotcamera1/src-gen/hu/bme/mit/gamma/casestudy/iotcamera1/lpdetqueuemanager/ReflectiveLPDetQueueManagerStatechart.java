package hu.bme.mit.gamma.casestudy.iotcamera1.lpdetqueuemanager;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;

public class ReflectiveLPDetQueueManagerStatechart implements ReflectiveComponentInterface {
	
	private LPDetQueueManagerStatechart wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveLPDetQueueManagerStatechart() {
		wrappedComponent = new LPDetQueueManagerStatechart();
	}
	
	public ReflectiveLPDetQueueManagerStatechart(LPDetQueueManagerStatechart wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public LPDetQueueManagerStatechart getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Traffic", "Status", "Img", "PreProcessedImg" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Traffic":
				return new String[] { "NewCar" };
			case "Status":
				return new String[] { "Full", "Free" };
			case "Img":
				return new String[] { "NewData" };
			case "PreProcessedImg":
				return new String[] { "NewData" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Traffic.NewCar":
				wrappedComponent.getTraffic().raiseNewCar();
				break;
			case "PreProcessedImg.NewData":
				wrappedComponent.getPreProcessedImg().raiseNewData();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Status.Full":
				if (wrappedComponent.getStatus().isRaisedFull()) {
					return true;
				}
				break;
			case "Status.Free":
				if (wrappedComponent.getStatus().isRaisedFree()) {
					return true;
				}
				break;
			case "Img.NewData":
				if (wrappedComponent.getImg().isRaisedNewData()) {
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
				return new String[] { "NotFull", "Full" };
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
	public void schedule(String instance) {
		wrappedComponent.runCycle();
	}
	
	public String[] getVariables() {
		return new String[] { "QueueMaxLen", "QueueLen" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
			case "QueueMaxLen":
				return wrappedComponent.getQueueMaxLen();
			case "QueueLen":
				return wrappedComponent.getQueueLen();
		}
		throw new IllegalArgumentException("Not known variable: " + variable);
	}
	
	public String[] getComponents() {
		return new String[] { };
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
