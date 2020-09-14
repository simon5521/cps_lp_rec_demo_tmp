package hu.bme.mit.gamma.casestudy.iotcamera1.iotcamera;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.lpdetqueuemanager.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.smartcontrol.*;

public class ReflectiveIoTCameraComp implements ReflectiveComponentInterface {
	
	private IoTCameraComp wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface lPRecQueueManager = null;
	private ReflectiveComponentInterface lPDetQueueManager = null;
	private ReflectiveComponentInterface smartControl = null;
	
	
	public ReflectiveIoTCameraComp() {
		wrappedComponent = new IoTCameraComp();
	}
	
	public ReflectiveIoTCameraComp(IoTCameraComp wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public IoTCameraComp getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "LPNumbers", "LPCroppedImg", "RecognisedLP" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "LPNumbers":
				return new String[] { "NewData" };
			case "LPCroppedImg":
				return new String[] { "NewData" };
			case "RecognisedLP":
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
			case "LPNumbers.NewData":
				if (wrappedComponent.getLPNumbers().isRaisedNewData()) {
					return true;
				}
				break;
			case "LPCroppedImg.NewData":
				if (wrappedComponent.getLPCroppedImg().isRaisedNewData()) {
					return true;
				}
				break;
			case "RecognisedLP.NewData":
				if (wrappedComponent.getRecognisedLP().isRaisedNewData()) {
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
		return new String[] { "LPRecQueueManager", "LPDetQueueManager", "SmartControl"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "LPRecQueueManager":
				if (lPRecQueueManager == null) {
					lPRecQueueManager = new ReflectiveLPRecQueueManager(wrappedComponent.getLPRecQueueManager());
				}
				return lPRecQueueManager;
			case "LPDetQueueManager":
				if (lPDetQueueManager == null) {
					lPDetQueueManager = new ReflectiveLPDetQueueManager(wrappedComponent.getLPDetQueueManager());
				}
				return lPDetQueueManager;
			case "SmartControl":
				if (smartControl == null) {
					smartControl = new ReflectiveSmartControl(wrappedComponent.getSmartControl());
				}
				return smartControl;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
