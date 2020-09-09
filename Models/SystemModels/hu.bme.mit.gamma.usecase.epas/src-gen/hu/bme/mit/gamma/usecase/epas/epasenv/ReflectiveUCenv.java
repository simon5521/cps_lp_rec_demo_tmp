package hu.bme.mit.gamma.usecase.epas.epasenv;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.uc.*;

public class ReflectiveUCenv implements ReflectiveComponentInterface {
	
	private UCenv wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface ucsct = null;
	
	
	public ReflectiveUCenv() {
		wrappedComponent = new UCenv();
	}
	
	public ReflectiveUCenv(UCenv wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public UCenv getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "fout" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "fout":
				return new String[] { "shutdown" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "fout.shutdown":
				if (wrappedComponent.getFout().isRaisedShutdown()) {
					return true;
				}
				break;
			default:
				throw new IllegalArgumentException("Not known port-out event combination: " + portEvent);
		}
		return false;
	}
	
	public boolean isStateActive(String region, String state) {
		return false;
	}
	
	public String[] getRegions() {
		return new String[] {  };
	}
	
	public String[] getStates(String region) {
		switch (region) {
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
		return new String[] { "ucsct"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "ucsct":
				if (ucsct == null) {
					ucsct = new ReflectiveUCStatechart(wrappedComponent.getUcsct());
				}
				return ucsct;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
