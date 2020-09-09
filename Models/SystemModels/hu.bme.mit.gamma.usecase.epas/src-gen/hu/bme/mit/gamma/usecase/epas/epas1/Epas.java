	package hu.bme.mit.gamma.usecase.epas.epas1;

	import java.util.List;
	import java.util.LinkedList;
	
	import hu.bme.mit.gamma.usecase.epas.interfaces.*;
	import hu.bme.mit.gamma.usecase.epas.sensor.*;
	import hu.bme.mit.gamma.usecase.epas.uc.*;
	import hu.bme.mit.gamma.usecase.epas.mainctrl.*;
	import hu.bme.mit.gamma.usecase.epas.evaluation.*;
	
	public class Epas implements EpasInterface {
		// Component instances
		private SensorStatechart S1A;
		private SensorStatechart S2A;
		private SensorStatechart S3A;
		private SensorStatechart S1B;
		private SensorStatechart S2B;
		private SensorStatechart S3B;
		private UCStatechart UCA;
		private UCStatechart UCB;
		private MainctrlStatechart ACTRL;
		private MainctrlStatechart BCTRL;
		private EvaluationStatechart Ev;
		// Port instances
		private S1AFault s1AFault;
		private S2AFault s2AFault;
		private S3AFault s3AFault;
		private S1BFault s1BFault;
		private S2BFault s2BFault;
		private S3BFault s3BFault;
		private UCAFault uCAFault;
		private UCBFault uCBFault;
		private State state;
		
		
		public Epas() {
			S1A = new SensorStatechart();
			S2A = new SensorStatechart();
			S3A = new SensorStatechart();
			S1B = new SensorStatechart();
			S2B = new SensorStatechart();
			S3B = new SensorStatechart();
			UCA = new UCStatechart();
			UCB = new UCStatechart();
			ACTRL = new MainctrlStatechart();
			BCTRL = new MainctrlStatechart();
			Ev = new EvaluationStatechart();
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
			// Initializing chain of listeners and events 
			notifyListeners();
		}
		
		/** Creates the channel mappings and enters the wrapped statemachines. */
		private void init() {
			// Registration of simple channels
			S3B.getSensorFault().registerListener(BCTRL.getS3HW());
			BCTRL.getS3HW().registerListener(S3B.getSensorFault());
			BCTRL.getMonitor().registerListener(Ev.getBMonitor());
			Ev.getBMonitor().registerListener(BCTRL.getMonitor());
			S2A.getSensorFault().registerListener(ACTRL.getS2HW());
			ACTRL.getS2HW().registerListener(S2A.getSensorFault());
			S2B.getSensorFault().registerListener(BCTRL.getS2HW());
			BCTRL.getS2HW().registerListener(S2B.getSensorFault());
			S3A.getSensorFault().registerListener(ACTRL.getS3HW());
			ACTRL.getS3HW().registerListener(S3A.getSensorFault());
			S1A.getSensorFault().registerListener(ACTRL.getS1HW());
			ACTRL.getS1HW().registerListener(S1A.getSensorFault());
			S1B.getSensorFault().registerListener(BCTRL.getS1HW());
			BCTRL.getS1HW().registerListener(S1B.getSensorFault());
			ACTRL.getMonitor().registerListener(Ev.getAMonitor());
			Ev.getAMonitor().registerListener(ACTRL.getMonitor());
			UCA.getFault().registerListener(ACTRL.getUCHW());
			ACTRL.getUCHW().registerListener(UCA.getFault());
			UCB.getFault().registerListener(BCTRL.getUCHW());
			BCTRL.getUCHW().registerListener(UCB.getFault());
			// Registration of broadcast channels
		}
		
		// Inner classes representing Ports
		public class S1AFault implements SensorFaultInterface.Required {
			private List<SensorFaultInterface.Listener.Required> listeners = new LinkedList<SensorFaultInterface.Listener.Required>();

			
			public S1AFault() {
				// Registering the listener to the contained component
				S1A.getHWFault().registerListener(new S1AFaultUtil());
			}
			
			@Override
			public void raiseLatent() {
				S1A.getHWFault().raiseLatent();
			}
			
			@Override
			public void raiseDet() {
				S1A.getHWFault().raiseDet();
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
			public void raiseLatent() {
				S2A.getHWFault().raiseLatent();
			}
			
			@Override
			public void raiseDet() {
				S2A.getHWFault().raiseDet();
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
			public void raiseLatent() {
				S3A.getHWFault().raiseLatent();
			}
			
			@Override
			public void raiseDet() {
				S3A.getHWFault().raiseDet();
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
			public void raiseLatent() {
				S1B.getHWFault().raiseLatent();
			}
			
			@Override
			public void raiseDet() {
				S1B.getHWFault().raiseDet();
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
			public void raiseLatent() {
				S2B.getHWFault().raiseLatent();
			}
			
			@Override
			public void raiseDet() {
				S2B.getHWFault().raiseDet();
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
			public void raiseLatent() {
				S3B.getHWFault().raiseLatent();
			}
			
			@Override
			public void raiseDet() {
				S3B.getHWFault().raiseDet();
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
		
		public class State implements EvalInterface.Provided {
			private List<EvalInterface.Listener.Provided> listeners = new LinkedList<EvalInterface.Listener.Provided>();

			boolean isRaisedSS;
			boolean isRaisedSLoA;
			
			public State() {
				// Registering the listener to the contained component
				Ev.getEval().registerListener(new StateUtil());
			}
			
			
			@Override
			public boolean isRaisedSS() {
				return isRaisedSS;
			}
			
			@Override
			public boolean isRaisedSLoA() {
				return isRaisedSLoA;
			}
			
			// Class for the setting of the boolean fields (events)
			private class StateUtil implements EvalInterface.Listener.Provided {
				@Override
				public void raiseSS() {
					isRaisedSS = true;
				}
				
				@Override
				public void raiseSLoA() {
					isRaisedSLoA = true;
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
				isRaisedSS = false;
				isRaisedSLoA = false;
			}
			
			/** Notifying the registered listeners. */
			public void notifyListeners() {
				if (isRaisedSS) {
					for (EvalInterface.Listener.Provided listener : listeners) {
						listener.raiseSS();
					}
				}
				if (isRaisedSLoA) {
					for (EvalInterface.Listener.Provided listener : listeners) {
						listener.raiseSLoA();
					}
				}
			}
			
		}
		
		@Override
		public State getState() {
			return state;
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
		public void notifyListeners() {
			S1A.notifyListeners();
			S2A.notifyListeners();
			S3A.notifyListeners();
			S1B.notifyListeners();
			S2B.notifyListeners();
			S3B.notifyListeners();
			UCA.notifyListeners();
			UCB.notifyListeners();
			ACTRL.notifyListeners();
			BCTRL.notifyListeners();
			Ev.notifyListeners();
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
			This should be the execution point from an asynchronous component. */
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
		public SensorStatechart getS1A() {
			return S1A;
		}
		
		public SensorStatechart getS2A() {
			return S2A;
		}
		
		public SensorStatechart getS3A() {
			return S3A;
		}
		
		public SensorStatechart getS1B() {
			return S1B;
		}
		
		public SensorStatechart getS2B() {
			return S2B;
		}
		
		public SensorStatechart getS3B() {
			return S3B;
		}
		
		public UCStatechart getUCA() {
			return UCA;
		}
		
		public UCStatechart getUCB() {
			return UCB;
		}
		
		public MainctrlStatechart getACTRL() {
			return ACTRL;
		}
		
		public MainctrlStatechart getBCTRL() {
			return BCTRL;
		}
		
		public EvaluationStatechart getEv() {
			return Ev;
		}
		
		
	}
