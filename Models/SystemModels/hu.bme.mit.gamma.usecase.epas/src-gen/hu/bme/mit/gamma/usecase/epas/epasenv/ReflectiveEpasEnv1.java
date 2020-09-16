package hu.bme.mit.gamma.usecase.epas.epasenv;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.epas1.*;

public class ReflectiveEpasEnv1 implements ReflectiveComponentInterface {
	
	private EpasEnv1 wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface epas = null;
	
	
	public ReflectiveEpasEnv1() {
		wrappedComponent = new EpasEnv1();
	}
	
	public ReflectiveEpasEnv1(EpasEnv1 wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public EpasEnv1 getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "fout" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "fout":
				return new String[] { "SLoA", "SS" };
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
			case "fout.SLoA":
				if (wrappedComponent.getFout().isRaisedSLoA()) {
					return true;
				}
				break;
			case "fout.SS":
				if (wrappedComponent.getFout().isRaisedSS()) {
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
		return new String[] { "epas"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "epas":
				if (epas == null) {
					epas = new ReflectiveEpas(wrappedComponent.getEpas());
				}
				return epas;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
