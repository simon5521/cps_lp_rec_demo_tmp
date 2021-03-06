/** Generated by YAKINDU Statechart Tools code generator. */
package hu.bme.mit.gamma.casestudy.camea.basictraffic;


public class BasicTrafficStatemachine implements IBasicTrafficStatemachine {
	protected class SCIInPortImpl implements SCIInPort {
	
		private boolean newCar;
		
		
		public void raiseNewCar() {
			newCar = true;
		}
		
		protected void clearEvents() {
			newCar = false;
		}
	}
	
	
	protected class SCIOutPortImpl implements SCIOutPort {
	
		private boolean newData;
		
		
		public boolean isRaisedNewData() {
			return newData;
		}
		
		protected void raiseNewData() {
			newData = true;
		}
		
		protected void clearEvents() {
		}
		protected void clearOutEvents() {
		
		newData = false;
		}
		
	}
	
	
	protected SCIInPortImpl sCIInPort;
	
	protected SCIOutPortImpl sCIOutPort;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_StateA,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	public BasicTrafficStatemachine() {
		sCIInPort = new SCIInPortImpl();
		sCIOutPort = new SCIOutPortImpl();
	}
	
	public void init() {
		this.initialized = true;
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
				"The state machine needs to be initialized first by calling the init() function."
			);
		}
		enterSequence_main_region_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_StateA:
				main_region_StateA_react(true);
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
	public void exit() {
		exitSequence_main_region();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCIInPort.clearEvents();
		sCIOutPort.clearEvents();
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
		sCIOutPort.clearOutEvents();
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case main_region_StateA:
			return stateVector[0] == State.main_region_StateA;
		default:
			return false;
		}
	}
	
	public SCIInPort getSCIInPort() {
		return sCIInPort;
	}
	
	public SCIOutPort getSCIOutPort() {
		return sCIOutPort;
	}
	
	/* 'default' enter sequence for state StateA */
	private void enterSequence_main_region_StateA_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_StateA;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* Default exit sequence for state StateA */
	private void exitSequence_main_region_StateA() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_StateA:
			exitSequence_main_region_StateA();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_StateA_default();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean main_region_StateA_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (react()==false) {
				if (sCIInPort.newCar) {
					exitSequence_main_region_StateA();
					sCIOutPort.raiseNewData();
					
					enterSequence_main_region_StateA_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
}
