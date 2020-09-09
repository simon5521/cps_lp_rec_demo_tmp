package hu.bme.mit.gamma.usecase.epas.evaluation;

import hu.bme.mit.gamma.usecase.epas.*;

public class ReflectiveEvaluationStatechart implements ReflectiveComponentInterface {
	
	private EvaluationStatechart wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveEvaluationStatechart() {
		wrappedComponent = new EvaluationStatechart();
	}
	
	public ReflectiveEvaluationStatechart(EvaluationStatechart wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public EvaluationStatechart getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Eval", "BMonitor", "AMonitor" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Eval":
				return new String[] { "SLoA", "SS" };
			case "BMonitor":
				return new String[] { "loa", "warning", "selfsteering" };
			case "AMonitor":
				return new String[] { "loa", "warning", "selfsteering" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "BMonitor.warning":
				wrappedComponent.getBMonitor().raiseWarning();
				break;
			case "BMonitor.loa":
				wrappedComponent.getBMonitor().raiseLoa();
				break;
			case "BMonitor.selfsteering":
				wrappedComponent.getBMonitor().raiseSelfsteering();
				break;
			case "AMonitor.warning":
				wrappedComponent.getAMonitor().raiseWarning();
				break;
			case "AMonitor.loa":
				wrappedComponent.getAMonitor().raiseLoa();
				break;
			case "AMonitor.selfsteering":
				wrappedComponent.getAMonitor().raiseSelfsteering();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Eval.SS":
				if (wrappedComponent.getEval().isRaisedSS()) {
					return true;
				}
				break;
			case "Eval.SLoA":
				if (wrappedComponent.getEval().isRaisedSLoA()) {
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
		return new String[] { "r1", "evaluation" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "r1":
				return new String[] { "Normal", "Warning" };
			case "evaluation":
				return new String[] { "Operation", "LoA", "Selfsteering" };
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
	public void schedule(String instance) {
		wrappedComponent.runCycle();
	}
	
	public String[] getVariables() {
		return new String[] { "sides" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
			case "sides":
				return wrappedComponent.getSides();
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
