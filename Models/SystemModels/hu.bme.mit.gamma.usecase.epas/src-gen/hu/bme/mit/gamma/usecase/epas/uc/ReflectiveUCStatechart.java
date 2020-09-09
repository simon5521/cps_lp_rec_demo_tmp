package hu.bme.mit.gamma.usecase.epas.uc;

import hu.bme.mit.gamma.usecase.epas.*;

public class ReflectiveUCStatechart implements ReflectiveComponentInterface {
	
	private UCStatechart wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveUCStatechart() {
		wrappedComponent = new UCStatechart();
	}
	
	public ReflectiveUCStatechart(UCStatechart wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public UCStatechart getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "HWFault", "Fault" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "HWFault":
				return new String[] { "shutdown" };
			case "Fault":
				return new String[] { "shutdown" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "HWFault.shutdown":
				wrappedComponent.getHWFault().raiseShutdown();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Fault.shutdown":
				if (wrappedComponent.getFault().isRaisedShutdown()) {
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
		return new String[] { "UC" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "UC":
				return new String[] { "Off", "On" };
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
