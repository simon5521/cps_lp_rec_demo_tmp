package hu.bme.mit.gamma.usecase.epas.sensor;

import hu.bme.mit.gamma.usecase.epas.*;

public class ReflectiveSensorStatechart implements ReflectiveComponentInterface {
	
	private SensorStatechart wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveSensorStatechart() {
		wrappedComponent = new SensorStatechart();
	}
	
	public ReflectiveSensorStatechart(SensorStatechart wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public SensorStatechart getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "SensorFault", "HWFault" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "SensorFault":
				return new String[] { "det", "latent" };
			case "HWFault":
				return new String[] { "det", "latent" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "HWFault.latent":
				wrappedComponent.getHWFault().raiseLatent();
				break;
			case "HWFault.det":
				wrappedComponent.getHWFault().raiseDet();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "SensorFault.latent":
				if (wrappedComponent.getSensorFault().isRaisedLatent()) {
					return true;
				}
				break;
			case "SensorFault.det":
				if (wrappedComponent.getSensorFault().isRaisedDet()) {
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
		return new String[] { "Sensor" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "Sensor":
				return new String[] { "LatentFailure", "Off", "Ok" };
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
