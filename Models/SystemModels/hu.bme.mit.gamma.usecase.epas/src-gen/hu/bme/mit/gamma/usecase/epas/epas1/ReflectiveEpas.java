package hu.bme.mit.gamma.usecase.epas.epas1;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.sensor.*;
import hu.bme.mit.gamma.usecase.epas.uc.*;
import hu.bme.mit.gamma.usecase.epas.mainctrl.*;
import hu.bme.mit.gamma.usecase.epas.evaluation.*;

public class ReflectiveEpas implements ReflectiveComponentInterface {
	
	private Epas wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface s1A = null;
	private ReflectiveComponentInterface s2A = null;
	private ReflectiveComponentInterface s3A = null;
	private ReflectiveComponentInterface s1B = null;
	private ReflectiveComponentInterface s2B = null;
	private ReflectiveComponentInterface s3B = null;
	private ReflectiveComponentInterface uCA = null;
	private ReflectiveComponentInterface uCB = null;
	private ReflectiveComponentInterface aCTRL = null;
	private ReflectiveComponentInterface bCTRL = null;
	private ReflectiveComponentInterface ev = null;
	
	
	public ReflectiveEpas() {
		wrappedComponent = new Epas();
	}
	
	public ReflectiveEpas(Epas wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public Epas getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "State", "S1AFault", "S2AFault", "S3AFault", "S1BFault", "S2BFault", "S3BFault", "UCAFault", "UCBFault" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "State":
				return new String[] { "SLoA", "SS" };
			case "S1AFault":
				return new String[] { "det", "latent" };
			case "S2AFault":
				return new String[] { "det", "latent" };
			case "S3AFault":
				return new String[] { "det", "latent" };
			case "S1BFault":
				return new String[] { "det", "latent" };
			case "S2BFault":
				return new String[] { "det", "latent" };
			case "S3BFault":
				return new String[] { "det", "latent" };
			case "UCAFault":
				return new String[] { "shutdown" };
			case "UCBFault":
				return new String[] { "shutdown" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "S1AFault.det":
				wrappedComponent.getS1AFault().raiseDet();
				break;
			case "S1AFault.latent":
				wrappedComponent.getS1AFault().raiseLatent();
				break;
			case "S2AFault.det":
				wrappedComponent.getS2AFault().raiseDet();
				break;
			case "S2AFault.latent":
				wrappedComponent.getS2AFault().raiseLatent();
				break;
			case "S3AFault.det":
				wrappedComponent.getS3AFault().raiseDet();
				break;
			case "S3AFault.latent":
				wrappedComponent.getS3AFault().raiseLatent();
				break;
			case "S1BFault.det":
				wrappedComponent.getS1BFault().raiseDet();
				break;
			case "S1BFault.latent":
				wrappedComponent.getS1BFault().raiseLatent();
				break;
			case "S2BFault.det":
				wrappedComponent.getS2BFault().raiseDet();
				break;
			case "S2BFault.latent":
				wrappedComponent.getS2BFault().raiseLatent();
				break;
			case "S3BFault.det":
				wrappedComponent.getS3BFault().raiseDet();
				break;
			case "S3BFault.latent":
				wrappedComponent.getS3BFault().raiseLatent();
				break;
			case "UCAFault.shutdown":
				wrappedComponent.getUCAFault().raiseShutdown();
				break;
			case "UCBFault.shutdown":
				wrappedComponent.getUCBFault().raiseShutdown();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "State.SLoA":
				if (wrappedComponent.getState().isRaisedSLoA()) {
					return true;
				}
				break;
			case "State.SS":
				if (wrappedComponent.getState().isRaisedSS()) {
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
		return new String[] { "S1A", "S2A", "S3A", "S1B", "S2B", "S3B", "UCA", "UCB", "ACTRL", "BCTRL", "Ev"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "S1A":
				if (s1A == null) {
					s1A = new ReflectiveSensor(wrappedComponent.getS1A());
				}
				return s1A;
			case "S2A":
				if (s2A == null) {
					s2A = new ReflectiveSensor(wrappedComponent.getS2A());
				}
				return s2A;
			case "S3A":
				if (s3A == null) {
					s3A = new ReflectiveSensor(wrappedComponent.getS3A());
				}
				return s3A;
			case "S1B":
				if (s1B == null) {
					s1B = new ReflectiveSensor(wrappedComponent.getS1B());
				}
				return s1B;
			case "S2B":
				if (s2B == null) {
					s2B = new ReflectiveSensor(wrappedComponent.getS2B());
				}
				return s2B;
			case "S3B":
				if (s3B == null) {
					s3B = new ReflectiveSensor(wrappedComponent.getS3B());
				}
				return s3B;
			case "UCA":
				if (uCA == null) {
					uCA = new ReflectiveUC(wrappedComponent.getUCA());
				}
				return uCA;
			case "UCB":
				if (uCB == null) {
					uCB = new ReflectiveUC(wrappedComponent.getUCB());
				}
				return uCB;
			case "ACTRL":
				if (aCTRL == null) {
					aCTRL = new ReflectiveMainctrl(wrappedComponent.getACTRL());
				}
				return aCTRL;
			case "BCTRL":
				if (bCTRL == null) {
					bCTRL = new ReflectiveMainctrl(wrappedComponent.getBCTRL());
				}
				return bCTRL;
			case "Ev":
				if (ev == null) {
					ev = new ReflectiveEvaluation(wrappedComponent.getEv());
				}
				return ev;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
