package hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;

public class ReflectiveLPRecQueueManagerStatechart implements ReflectiveComponentInterface {
	
	private LPRecQueueManagerStatechart wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveLPRecQueueManagerStatechart() {
		wrappedComponent = new LPRecQueueManagerStatechart();
	}
	
	public ReflectiveLPRecQueueManagerStatechart(LPRecQueueManagerStatechart wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public LPRecQueueManagerStatechart getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Status", "ProcessedImg", "PreProcessedImgOut", "PreProcessedImg", "NetworkStream" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Status":
				return new String[] { "Full", "Free" };
			case "ProcessedImg":
				return new String[] { "NewData" };
			case "PreProcessedImgOut":
				return new String[] { "NewData" };
			case "PreProcessedImg":
				return new String[] { "NewData" };
			case "NetworkStream":
				return new String[] { "NewData" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "ProcessedImg.NewData":
				wrappedComponent.getProcessedImg().raiseNewData();
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
			case "PreProcessedImgOut.NewData":
				if (wrappedComponent.getPreProcessedImgOut().isRaisedNewData()) {
					return true;
				}
				break;
			case "NetworkStream.NewData":
				if (wrappedComponent.getNetworkStream().isRaisedNewData()) {
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
		return new String[] { "QueueLen", "QueueMaxLen" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
			case "QueueLen":
				return wrappedComponent.getQueueLen();
			case "QueueMaxLen":
				return wrappedComponent.getQueueMaxLen();
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
