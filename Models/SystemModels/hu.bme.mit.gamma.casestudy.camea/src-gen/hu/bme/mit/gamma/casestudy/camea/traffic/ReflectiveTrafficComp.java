package hu.bme.mit.gamma.casestudy.camea.traffic;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.basictraffic.*;

public class ReflectiveTrafficComp implements ReflectiveComponentInterface {
	
	private TrafficComp wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface basic = null;
	
	
	public ReflectiveTrafficComp() {
		wrappedComponent = new TrafficComp();
	}
	
	public ReflectiveTrafficComp(TrafficComp wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public TrafficComp getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "cars" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "cars":
				return new String[] { "NewData" };
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
			case "cars.NewData":
				if (wrappedComponent.getCars().isRaisedNewData()) {
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
		return new String[] { "basic"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "basic":
				if (basic == null) {
					basic = new ReflectiveBasicTraffic(wrappedComponent.getBasic());
				}
				return basic;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
