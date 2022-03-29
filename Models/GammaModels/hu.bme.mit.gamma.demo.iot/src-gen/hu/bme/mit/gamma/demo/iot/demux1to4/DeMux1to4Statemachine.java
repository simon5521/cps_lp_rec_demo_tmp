/** Generated by YAKINDU Statechart Tools code generator. */
package hu.bme.mit.gamma.demo.iot.demux1to4;


public class DeMux1to4Statemachine implements IDeMux1to4Statemachine {
	protected class SCIInImpl implements SCIIn {
	
		private boolean newData;
		
		private long newDataValue;
		
		
		public void raiseNewData(long value) {
			newDataValue = value;
			newData = true;
		}
		protected long getNewDataValue() {
			if (! newData ) 
				throw new IllegalStateException("Illegal event value access. Event NewData is not raised!");
			return newDataValue;
		}
		
		protected void clearEvents() {
			newData = false;
		}
	}
	
	
	protected class SCIOut1Impl implements SCIOut1 {
	
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
	
	
	protected class SCIOut2Impl implements SCIOut2 {
	
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
	
	
	protected class SCIOut3Impl implements SCIOut3 {
	
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
	
	
	protected class SCIOut4Impl implements SCIOut4 {
	
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
	
	
	protected SCIInImpl sCIIn;
	
	protected SCIOut1Impl sCIOut1;
	
	protected SCIOut2Impl sCIOut2;
	
	protected SCIOut3Impl sCIOut3;
	
	protected SCIOut4Impl sCIOut4;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_StateA,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	private long sel;
	
	protected long getSel() {
		return sel;
	}
	
	protected void setSel(long value) {
		this.sel = value;
	}
	
	
	public DeMux1to4Statemachine() {
		sCIIn = new SCIInImpl();
		sCIOut1 = new SCIOut1Impl();
		sCIOut2 = new SCIOut2Impl();
		sCIOut3 = new SCIOut3Impl();
		sCIOut4 = new SCIOut4Impl();
	}
	
	public void init() {
		this.initialized = true;
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		setSel(0);
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
		sCIIn.clearEvents();
		sCIOut1.clearEvents();
		sCIOut2.clearEvents();
		sCIOut3.clearEvents();
		sCIOut4.clearEvents();
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
		sCIOut1.clearOutEvents();
		sCIOut2.clearOutEvents();
		sCIOut3.clearOutEvents();
		sCIOut4.clearOutEvents();
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
	
	public SCIIn getSCIIn() {
		return sCIIn;
	}
	
	public SCIOut1 getSCIOut1() {
		return sCIOut1;
	}
	
	public SCIOut2 getSCIOut2() {
		return sCIOut2;
	}
	
	public SCIOut3 getSCIOut3() {
		return sCIOut3;
	}
	
	public SCIOut4 getSCIOut4() {
		return sCIOut4;
	}
	
	private boolean check_main_region__choice_0_tr0_tr0() {
		return getSel()==1;
	}
	
	private boolean check_main_region__choice_0_tr1_tr1() {
		return getSel()==2;
	}
	
	private boolean check_main_region__choice_0_tr2_tr2() {
		return getSel()==3;
	}
	
	private boolean check_main_region__choice_0_tr3_tr3() {
		return getSel()==4;
	}
	
	private void effect_main_region__choice_0_tr0() {
		sCIOut1.raiseNewData();
		
		enterSequence_main_region_StateA_default();
	}
	
	private void effect_main_region__choice_0_tr1() {
		sCIOut2.raiseNewData();
		
		enterSequence_main_region_StateA_default();
	}
	
	private void effect_main_region__choice_0_tr2() {
		sCIOut3.raiseNewData();
		
		enterSequence_main_region_StateA_default();
	}
	
	private void effect_main_region__choice_0_tr3() {
		sCIOut4.raiseNewData();
		
		enterSequence_main_region_StateA_default();
	}
	
	private void effect_main_region__choice_0_tr4() {
		enterSequence_main_region_StateA_default();
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
	
	/* The reactions of state null. */
	private void react_main_region__choice_0() {
		if (check_main_region__choice_0_tr0_tr0()) {
			effect_main_region__choice_0_tr0();
		} else {
			if (check_main_region__choice_0_tr1_tr1()) {
				effect_main_region__choice_0_tr1();
			} else {
				if (check_main_region__choice_0_tr2_tr2()) {
					effect_main_region__choice_0_tr2();
				} else {
					if (check_main_region__choice_0_tr3_tr3()) {
						effect_main_region__choice_0_tr3();
					} else {
						effect_main_region__choice_0_tr4();
					}
				}
			}
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
				if (sCIIn.newData) {
					exitSequence_main_region_StateA();
					setSel(sCIIn.getNewDataValue());
					
					react_main_region__choice_0();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
}