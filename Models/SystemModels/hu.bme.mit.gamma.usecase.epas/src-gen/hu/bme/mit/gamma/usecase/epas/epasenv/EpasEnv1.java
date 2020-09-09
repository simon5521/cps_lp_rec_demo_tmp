	package hu.bme.mit.gamma.usecase.epas.epasenv;

	import java.util.List;
	import java.util.LinkedList;
	
	import hu.bme.mit.gamma.usecase.epas.interfaces.*;
	import hu.bme.mit.gamma.usecase.epas.epas1.*;
	
	public class EpasEnv1 implements EpasEnv1Interface {
		// Component instances
		private Epas epas;
		// Environmental Component instances
		private UCenv uC1env;
		private UCenv uC2env;
		// Port instances
		private Fout fout;
		
		
		public EpasEnv1() {
			epas = new Epas();
			fout = new Fout();
			// Environmental Component instances
				uC1env = new UCenv();
				uC2env = new UCenv();
			init();
		}
		
		/** Resets the contained statemachines recursively. Must be called to initialize the component. */
		@Override
		public void reset() {
			epas.reset();
			uC1env.reset();
			uC2env.reset();
			// Setting only a single queue for cascade statecharts
			// Initializing chain of listeners and events 
			notifyListeners();
		}
		
		/** Creates the channel mappings and enters the wrapped statemachines. */
		private void init() {
			// Registration of simple channels
			uC1env.getFout().registerListener(epas.getUCAFault());
			epas.getUCAFault().registerListener(uC1env.getFout());
			uC2env.getFout().registerListener(epas.getUCBFault());
			epas.getUCBFault().registerListener(uC2env.getFout());
			// Registration of broadcast channels
		}
		
		// Inner classes representing Ports
		public class Fout implements EvalInterface.Provided {
			private List<EvalInterface.Listener.Provided> listeners = new LinkedList<EvalInterface.Listener.Provided>();

			boolean isRaisedSS;
			boolean isRaisedSLoA;
			
			public Fout() {
				// Registering the listener to the contained component
				epas.getState().registerListener(new FoutUtil());
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
			private class FoutUtil implements EvalInterface.Listener.Provided {
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
		public Fout getFout() {
			return fout;
		}
		
		/** Clears the the boolean flags of all out-events in each contained port. */
		private void clearPorts() {
			getFout().clear();
		}
		
		/** Notifies all registered listeners in each contained port. */
		public void notifyListeners() {
			epas.notifyListeners();
			uC1env.notifyListeners();
			uC2env.notifyListeners();
			getFout().notifyListeners();
		}
		
		
		/** Returns whether all event queues of the contained component instances are empty. 
		Should be used only be the container (composite system) class. */
		public boolean isEventQueueEmpty() {
			return epas.isEventQueueEmpty();
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
			epas.runComponent();
			uC1env.runCycle();
			uC2env.runCycle();

			// Notifying registered listeners
			notifyListeners();
		}

		
		/**  Getter for component instances, e.g., enabling to check their states. */
		public Epas getEpas() {
			return epas;
		}
		
		// Environmental Component instances
			public UCenv getUC1env() {
				return uC1env;
			}
			public UCenv getUC2env() {
				return uC2env;
			}
		
	}
