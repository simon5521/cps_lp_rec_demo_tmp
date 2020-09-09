package hu.bme.mit.gamma.usecase.epas.mainctrl;

import hu.bme.mit.gamma.usecase.epas.*;

public class ReflectiveMainctrlStatechart implements ReflectiveComponentInterface {
	
	private MainctrlStatechart wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveMainctrlStatechart() {
		wrappedComponent = new MainctrlStatechart();
	}
	
	public ReflectiveMainctrlStatechart(MainctrlStatechart wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public MainctrlStatechart getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "S1HW", "Monitor", "S3HW", "S2HW", "UCHW" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "S1HW":
				return new String[] { "det", "latent" };
			case "Monitor":
				return new String[] { "loa", "warning", "selfsteering" };
			case "S3HW":
				return new String[] { "det", "latent" };
			case "S2HW":
				return new String[] { "det", "latent" };
			case "UCHW":
				return new String[] { "shutdown" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "S1HW.latent":
				wrappedComponent.getS1HW().raiseLatent();
				break;
			case "S1HW.det":
				wrappedComponent.getS1HW().raiseDet();
				break;
			case "S3HW.latent":
				wrappedComponent.getS3HW().raiseLatent();
				break;
			case "S3HW.det":
				wrappedComponent.getS3HW().raiseDet();
				break;
			case "S2HW.latent":
				wrappedComponent.getS2HW().raiseLatent();
				break;
			case "S2HW.det":
				wrappedComponent.getS2HW().raiseDet();
				break;
			case "UCHW.shutdown":
				wrappedComponent.getUCHW().raiseShutdown();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Monitor.warning":
				if (wrappedComponent.getMonitor().isRaisedWarning()) {
					return true;
				}
				break;
			case "Monitor.loa":
				if (wrappedComponent.getMonitor().isRaisedLoa()) {
					return true;
				}
				break;
			case "Monitor.selfsteering":
				if (wrappedComponent.getMonitor().isRaisedSelfsteering()) {
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
		return new String[] { "evaluation", "operation", "r1", "main_region" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "evaluation":
				return new String[] { "working", "ShutdownState", "SelfSteeringState" };
			case "operation":
				return new String[] { "On", "Off" };
			case "r1":
				return new String[] { "NormalOperation", "Warning" };
			case "main_region":
				return new String[] { "main" };
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
	public void schedule(String instance) {
		wrappedComponent.runCycle();
	}
	
	public String[] getVariables() {
		return new String[] { "latentsensors", "onsensors", "oksensors", "offsensors" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
			case "latentsensors":
				return wrappedComponent.getLatentsensors();
			case "onsensors":
				return wrappedComponent.getOnsensors();
			case "oksensors":
				return wrappedComponent.getOksensors();
			case "offsensors":
				return wrappedComponent.getOffsensors();
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
