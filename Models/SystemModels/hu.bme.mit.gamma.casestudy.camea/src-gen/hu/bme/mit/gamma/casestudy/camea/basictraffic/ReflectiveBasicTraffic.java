package hu.bme.mit.gamma.casestudy.camea.basictraffic;

import hu.bme.mit.gamma.casestudy.camea.*;

public class ReflectiveBasicTraffic implements ReflectiveComponentInterface {
	
	private BasicTraffic wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveBasicTraffic() {
		wrappedComponent = new BasicTraffic();
	}
	
	public ReflectiveBasicTraffic(BasicTraffic wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public BasicTraffic getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "InPort", "OutPort" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "InPort":
				return new String[] { "NewCar" };
			case "OutPort":
				return new String[] { "NewData" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "InPort.NewCar":
				wrappedComponent.getInPort().raiseNewCar();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "OutPort.NewData":
				if (wrappedComponent.getOutPort().isRaisedNewData()) {
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
				return new String[] { "StateA" };
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
			// If the class name is given, then it will return itself
			case "BasicTraffic":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
