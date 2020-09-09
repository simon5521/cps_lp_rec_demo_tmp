package hu.bme.mit.gamma.casestudy.iotcamera1.smartcontrol;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;

public class ReflectiveSmartControlStatechart implements ReflectiveComponentInterface {
	
	private SmartControlStatechart wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveSmartControlStatechart() {
		wrappedComponent = new SmartControlStatechart();
	}
	
	public ReflectiveSmartControlStatechart(SmartControlStatechart wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public SmartControlStatechart getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "InPicStream", "LocalStream", "NetworkStream", "Status" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "InPicStream":
				return new String[] { "NewData" };
			case "LocalStream":
				return new String[] { "NewData" };
			case "NetworkStream":
				return new String[] { "NewData" };
			case "Status":
				return new String[] { "Full", "Free" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "InPicStream.NewData":
				wrappedComponent.getInPicStream().raiseNewData();
				break;
			case "Status.Full":
				wrappedComponent.getStatus().raiseFull();
				break;
			case "Status.Free":
				wrappedComponent.getStatus().raiseFree();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "LocalStream.NewData":
				if (wrappedComponent.getLocalStream().isRaisedNewData()) {
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
		return new String[] { "mainregion" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "mainregion":
				return new String[] { "SendToCloud", "SendToLocal" };
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
	public void schedule(String instance) {
		wrappedComponent.runCycle();
	}
	
	public String[] getVariables() {
		return new String[] {  };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
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
