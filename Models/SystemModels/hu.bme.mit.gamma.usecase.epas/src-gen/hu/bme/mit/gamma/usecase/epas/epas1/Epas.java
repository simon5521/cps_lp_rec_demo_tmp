package hu.bme.mit.gamma.usecase.epas.epas1;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.*;
import hu.bme.mit.gamma.usecase.epas.sensor.*;
import hu.bme.mit.gamma.usecase.epas.uc.*;
import hu.bme.mit.gamma.usecase.epas.mainctrl.*;
import hu.bme.mit.gamma.usecase.epas.evaluation.*;

public class Epas implements EpasInterface {
	// Component instances
	private Sensor S1A;
	private Sensor S2A;
	private Sensor S3A;
	private Sensor S1B;
	private Sensor S2B;
	private Sensor S3B;
	private UC UCA;
	private UC UCB;
	private Mainctrl ACTRL;
	private Mainctrl BCTRL;
	private Evaluation Ev;
	// Port instances
	private State state;
	private S1AFault s1AFault;
	private S2AFault s2AFault;
	private S3AFault s3AFault;
	private S1BFault s1BFault;
	private S2BFault s2BFault;
	private S3BFault s3BFault;
	private UCAFault uCAFault;
	private UCBFault uCBFault;
	
	
	public Epas() {
		S1A = new Sensor();
		S2A = new Sensor();
		S3A = new Sensor();
		S1B = new Sensor();
		S2B = new Sensor();
		S3B = new Sensor();
		UCA = new UC();
		UCB = new UC();
		ACTRL = new Mainctrl();
		BCTRL = new Mainctrl();
		Ev = new Evaluation();
		s1AFault = new S1AFault();
		s2AFault = new S2AFault();
		s3AFault = new S3AFault();
		s1BFault = new S1BFault();
		s2BFault = new S2BFault();
		s3BFault = new S3BFault();
		uCAFault = new UCAFault();
		uCBFault = new UCBFault();
		state = new State();
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		S1A.reset();
		S2A.reset();
		S3A.reset();
		S1B.reset();
		S2B.reset();
		S3B.reset();
		UCA.reset();
		UCB.reset();
		ACTRL.reset();
		BCTRL.reset();
		Ev.reset();
		// Setting only a single queue for cascade statecharts
		S1A.changeInsertQueue();
		S2A.changeInsertQueue();
		S3A.changeInsertQueue();
		S1B.changeInsertQueue();
		S2B.changeInsertQueue();
		S3B.changeInsertQueue();
		UCA.changeInsertQueue();
		UCB.changeInsertQueue();
		ACTRL.changeInsertQueue();
		BCTRL.changeInsertQueue();
		Ev.changeInsertQueue();
		clearPorts();
		// Initializing chain of listeners and events 
		notifyAllListeners();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		UCA.getFault().registerListener(ACTRL.getUCHW());
		ACTRL.getUCHW().registerListener(UCA.getFault());
		ACTRL.getMonitor().registerListener(Ev.getAMonitor());
		Ev.getAMonitor().registerListener(ACTRL.getMonitor());
		S3A.getSensorFault().registerListener(ACTRL.getS3HW());
		ACTRL.getS3HW().registerListener(S3A.getSensorFault());
		S1B.getSensorFault().registerListener(BCTRL.getS1HW());
		BCTRL.getS1HW().registerListener(S1B.getSensorFault());
		S3B.getSensorFault().registerListener(BCTRL.getS3HW());
		BCTRL.getS3HW().registerListener(S3B.getSensorFault());
		S1A.getSensorFault().registerListener(ACTRL.getS1HW());
		ACTRL.getS1HW().registerListener(S1A.getSensorFault());
		S2B.getSensorFault().registerListener(BCTRL.getS2HW());
		BCTRL.getS2HW().registerListener(S2B.getSensorFault());
		BCTRL.getMonitor().registerListener(Ev.getBMonitor());
		Ev.getBMonitor().registerListener(BCTRL.getMonitor());
		UCB.getFault().registerListener(BCTRL.getUCHW());
		BCTRL.getUCHW().registerListener(UCB.getFault());
		S2A.getSensorFault().registerListener(ACTRL.getS2HW());
		ACTRL.getS2HW().registerListener(S2A.getSensorFault());
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class State implements EvalInterface.Provided {
		private List<EvalInterface.Listener.Provided> listeners = new LinkedList<EvalInterface.Listener.Provided>();
		boolean isRaisedSLoA;
		boolean isRaisedSS;
		
		public State() {
			// Registering the listener to the contained component
			Ev.getEval().registerListener(new StateUtil());
		}
		
		
		@Override
		public boolean isRaisedSLoA() {
			return isRaisedSLoA;
		}
		
		@Override
		public boolean isRaisedSS() {
			return isRaisedSS;
		}
		
		// Class for the setting of the boolean fields (events)
		private class StateUtil implements EvalInterface.Listener.Provided {
			@Override
			public void raiseSLoA() {
				isRaisedSLoA = true;
			}
			
			@Override
			public void raiseSS() {
				isRaisedSS = true;
			}
		}
		
		@Override
		public void registerListener(EvalInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<EvalInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
			isRaisedSLoA = false;
			isRaisedSS = false;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedSLoA) {
				for (EvalInterface.Listener.Provided listener : listeners) {
					listener.raiseSLoA();
				}
			}
			if (isRaisedSS) {
				for (EvalInterface.Listener.Provided listener : listeners) {
					listener.raiseSS();
				}
			}
		}
		
	}
	
	@Override
	public State getState() {
		return state;
	}
	
	public class S1AFault implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> listeners = new LinkedList<SensorFaultInterface.Listener.Required>();
		
		public S1AFault() {
			// Registering the listener to the contained component
			S1A.getHWFault().registerListener(new S1AFaultUtil());
		}
		
		@Override
		public void raiseDet() {
			S1A.getHWFault().raiseDet();
		}
		
		@Override
		public void raiseLatent() {
			S1A.getHWFault().raiseLatent();
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class S1AFaultUtil implements SensorFaultInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(SensorFaultInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<SensorFaultInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public S1AFault getS1AFault() {
		return s1AFault;
	}
	
	public class S2AFault implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> listeners = new LinkedList<SensorFaultInterface.Listener.Required>();
		
		public S2AFault() {
			// Registering the listener to the contained component
			S2A.getHWFault().registerListener(new S2AFaultUtil());
		}
		
		@Override
		public void raiseDet() {
			S2A.getHWFault().raiseDet();
		}
		
		@Override
		public void raiseLatent() {
			S2A.getHWFault().raiseLatent();
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class S2AFaultUtil implements SensorFaultInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(SensorFaultInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<SensorFaultInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public S2AFault getS2AFault() {
		return s2AFault;
	}
	
	public class S3AFault implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> listeners = new LinkedList<SensorFaultInterface.Listener.Required>();
		
		public S3AFault() {
			// Registering the listener to the contained component
			S3A.getHWFault().registerListener(new S3AFaultUtil());
		}
		
		@Override
		public void raiseDet() {
			S3A.getHWFault().raiseDet();
		}
		
		@Override
		public void raiseLatent() {
			S3A.getHWFault().raiseLatent();
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class S3AFaultUtil implements SensorFaultInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(SensorFaultInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<SensorFaultInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public S3AFault getS3AFault() {
		return s3AFault;
	}
	
	public class S1BFault implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> listeners = new LinkedList<SensorFaultInterface.Listener.Required>();
		
		public S1BFault() {
			// Registering the listener to the contained component
			S1B.getHWFault().registerListener(new S1BFaultUtil());
		}
		
		@Override
		public void raiseDet() {
			S1B.getHWFault().raiseDet();
		}
		
		@Override
		public void raiseLatent() {
			S1B.getHWFault().raiseLatent();
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class S1BFaultUtil implements SensorFaultInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(SensorFaultInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<SensorFaultInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public S1BFault getS1BFault() {
		return s1BFault;
	}
	
	public class S2BFault implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> listeners = new LinkedList<SensorFaultInterface.Listener.Required>();
		
		public S2BFault() {
			// Registering the listener to the contained component
			S2B.getHWFault().registerListener(new S2BFaultUtil());
		}
		
		@Override
		public void raiseDet() {
			S2B.getHWFault().raiseDet();
		}
		
		@Override
		public void raiseLatent() {
			S2B.getHWFault().raiseLatent();
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class S2BFaultUtil implements SensorFaultInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(SensorFaultInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<SensorFaultInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public S2BFault getS2BFault() {
		return s2BFault;
	}
	
	public class S3BFault implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> listeners = new LinkedList<SensorFaultInterface.Listener.Required>();
		
		public S3BFault() {
			// Registering the listener to the contained component
			S3B.getHWFault().registerListener(new S3BFaultUtil());
		}
		
		@Override
		public void raiseDet() {
			S3B.getHWFault().raiseDet();
		}
		
		@Override
		public void raiseLatent() {
			S3B.getHWFault().raiseLatent();
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class S3BFaultUtil implements SensorFaultInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(SensorFaultInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<SensorFaultInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public S3BFault getS3BFault() {
		return s3BFault;
	}
	
	public class UCAFault implements UCFaultInterface.Required {
		private List<UCFaultInterface.Listener.Required> listeners = new LinkedList<UCFaultInterface.Listener.Required>();
		
		public UCAFault() {
			// Registering the listener to the contained component
			UCA.getHWFault().registerListener(new UCAFaultUtil());
		}
		
		@Override
		public void raiseShutdown() {
			UCA.getHWFault().raiseShutdown();
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class UCAFaultUtil implements UCFaultInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(UCFaultInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<UCFaultInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public UCAFault getUCAFault() {
		return uCAFault;
	}
	
	public class UCBFault implements UCFaultInterface.Required {
		private List<UCFaultInterface.Listener.Required> listeners = new LinkedList<UCFaultInterface.Listener.Required>();
		
		public UCBFault() {
			// Registering the listener to the contained component
			UCB.getHWFault().registerListener(new UCBFaultUtil());
		}
		
		@Override
		public void raiseShutdown() {
			UCB.getHWFault().raiseShutdown();
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class UCBFaultUtil implements UCFaultInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(UCFaultInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<UCFaultInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public UCBFault getUCBFault() {
		return uCBFault;
	}
	
	/** Clears the the boolean flags of all out-events in each contained port. */
	private void clearPorts() {
		getS1AFault().clear();
		getS2AFault().clear();
		getS3AFault().clear();
		getS1BFault().clear();
		getS2BFault().clear();
		getS3BFault().clear();
		getUCAFault().clear();
		getUCBFault().clear();
		getState().clear();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyAllListeners() {
		S1A.notifyAllListeners();
		S2A.notifyAllListeners();
		S3A.notifyAllListeners();
		S1B.notifyAllListeners();
		S2B.notifyAllListeners();
		S3B.notifyAllListeners();
		UCA.notifyAllListeners();
		UCB.notifyAllListeners();
		ACTRL.notifyAllListeners();
		BCTRL.notifyAllListeners();
		Ev.notifyAllListeners();

		notifyListeners();
	}
	
	public void notifyListeners() {
		getS1AFault().notifyListeners();
		getS2AFault().notifyListeners();
		getS3AFault().notifyListeners();
		getS1BFault().notifyListeners();
		getS2BFault().notifyListeners();
		getS3BFault().notifyListeners();
		getUCAFault().notifyListeners();
		getUCBFault().notifyListeners();
		getState().notifyListeners();
	}
	
	
	/** Returns whether all event queues of the contained component instances are empty. 
	Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return S1A.isEventQueueEmpty() && S2A.isEventQueueEmpty() && S3A.isEventQueueEmpty() && S1B.isEventQueueEmpty() && S2B.isEventQueueEmpty() && S3B.isEventQueueEmpty() && UCA.isEventQueueEmpty() && UCB.isEventQueueEmpty() && ACTRL.isEventQueueEmpty() && BCTRL.isEventQueueEmpty() && Ev.isEventQueueEmpty();
	}
	
	/** Initiates cycle runs until all event queues of component instances are empty. */
	@Override
	public void runFullCycle() {
		do {
			runCycle();
		}
		while (!isEventQueueEmpty());
	}
	
	/** Changes event queues and initiates a cycle run.
	 * This should be the execution point from an asynchronous component. */
	@Override
	public void runCycle() {
		// Composite type-dependent behavior
		runComponent();
	}
	
	/** Initiates a cycle run without changing the event queues.
	 * Should be used only be the container (composite system) class. */
	public void runComponent() {
		// Starts with the clearing of the previous out-event flags
		clearPorts();
		// Running contained components
		S1A.runComponent();
		S2A.runComponent();
		S3A.runComponent();
		S1B.runComponent();
		S2B.runComponent();
		S3B.runComponent();
		UCA.runComponent();
		UCB.runComponent();
		ACTRL.runComponent();
		BCTRL.runComponent();
		Ev.runComponent();
		// Notifying registered listeners
		notifyListeners();
	}

	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public Sensor getS1A() {
		return S1A;
	}
	
	public Sensor getS2A() {
		return S2A;
	}
	
	public Sensor getS3A() {
		return S3A;
	}
	
	public Sensor getS1B() {
		return S1B;
	}
	
	public Sensor getS2B() {
		return S2B;
	}
	
	public Sensor getS3B() {
		return S3B;
	}
	
	public UC getUCA() {
		return UCA;
	}
	
	public UC getUCB() {
		return UCB;
	}
	
	public Mainctrl getACTRL() {
		return ACTRL;
	}
	
	public Mainctrl getBCTRL() {
		return BCTRL;
	}
	
	public Evaluation getEv() {
		return Ev;
	}
	
	
	
}
