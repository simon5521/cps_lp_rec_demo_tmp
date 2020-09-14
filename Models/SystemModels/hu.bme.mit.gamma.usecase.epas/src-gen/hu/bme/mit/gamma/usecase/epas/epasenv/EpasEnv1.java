package hu.bme.mit.gamma.usecase.epas.epasenv;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.usecase.epas.*;
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
		// Setting only a single queue for cascade statecharts
		uC1env.reset();
		uC2env.reset();
		clearPorts();
		// Initializing chain of listeners and events 
		notifyAllListeners();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		uC2env.getFaultout().registerListener(epas.getUCBFault());
		epas.getUCBFault().registerListener(uC2env.getFaultout());
		uC1env.getFaultout().registerListener(epas.getUCAFault());
		epas.getUCAFault().registerListener(uC1env.getFaultout());
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class Fout implements EvalInterface.Provided {
		private List<EvalInterface.Listener.Provided> listeners = new LinkedList<EvalInterface.Listener.Provided>();
		boolean isRaisedSLoA;
		boolean isRaisedSS;
		
		public Fout() {
			// Registering the listener to the contained component
			epas.getState().registerListener(new FoutUtil());
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
		private class FoutUtil implements EvalInterface.Listener.Provided {
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
	public Fout getFout() {
		return fout;
	}
	
	/** Clears the the boolean flags of all out-events in each contained port. */
	private void clearPorts() {
		getFout().clear();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyAllListeners() {
		epas.notifyAllListeners();

		uC1env.notifyListeners();
		uC2env.notifyListeners();
		notifyListeners();
	}
	
	public void notifyListeners() {
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
