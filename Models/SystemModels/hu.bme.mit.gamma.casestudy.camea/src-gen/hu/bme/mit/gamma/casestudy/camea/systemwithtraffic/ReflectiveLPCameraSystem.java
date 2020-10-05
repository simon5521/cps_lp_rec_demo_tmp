package hu.bme.mit.gamma.casestudy.camea.systemwithtraffic;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.queuemanager.*;
import hu.bme.mit.gamma.casestudy.camea.paralellqueuemanager.*;

public class ReflectiveLPCameraSystem implements ReflectiveComponentInterface {
	
	private LPCameraSystem wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface lPDetection = null;
	private ReflectiveComponentInterface lPNetworking = null;
	private ReflectiveComponentInterface lPRecognition = null;
	
	
	public ReflectiveLPCameraSystem() {
		wrappedComponent = new LPCameraSystem();
	}
	
	public ReflectiveLPCameraSystem(LPCameraSystem wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public LPCameraSystem getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "DetLoss", "DetNetLoss", "RecLoss", "LPNum" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "DetLoss":
				return new String[] { "NewData" };
			case "DetNetLoss":
				return new String[] { "NewData" };
			case "RecLoss":
				return new String[] { "NewData" };
			case "LPNum":
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
			case "DetLoss.NewData":
				if (wrappedComponent.getDetLoss().isRaisedNewData()) {
					return true;
				}
				break;
			case "DetNetLoss.NewData":
				if (wrappedComponent.getDetNetLoss().isRaisedNewData()) {
					return true;
				}
				break;
			case "RecLoss.NewData":
				if (wrappedComponent.getRecLoss().isRaisedNewData()) {
					return true;
				}
				break;
			case "LPNum.NewData":
				if (wrappedComponent.getLPNum().isRaisedNewData()) {
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
		return new String[] { "LPDetection", "LPNetworking", "LPRecognition"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "LPDetection":
				if (lPDetection == null) {
					lPDetection = new ReflectiveQueueManager(wrappedComponent.getLPDetection());
				}
				return lPDetection;
			case "LPNetworking":
				if (lPNetworking == null) {
					lPNetworking = new ReflectiveQueueManager(wrappedComponent.getLPNetworking());
				}
				return lPNetworking;
			case "LPRecognition":
				if (lPRecognition == null) {
					lPRecognition = new ReflectiveParalellQueueManager(wrappedComponent.getLPRecognition());
				}
				return lPRecognition;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
