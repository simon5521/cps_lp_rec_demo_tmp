package hu.bme.mit.gamma.usecase.epas.epasenv;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.*;
import hu.bme.mit.gamma.usecase.epas.uc.*;

public class UCenv implements UCenvInterface {
	// Component instances
	private UC ucsct;
	// Environmental Component instances
	// Port instances
	private Faultout faultout;
	
	
	public UCenv() {
		ucsct = new UC();
		faultout = new Faultout();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		ucsct.reset();
		// Setting only a single queue for cascade statecharts
		ucsct.changeInsertQueue();
		clearPorts();
		// Initializing chain of listeners and events 
		notifyAllListeners();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class Faultout implements UCFaultInterface.Provided {
		private List<UCFaultInterface.Listener.Provided> listeners = new LinkedList<UCFaultInterface.Listener.Provided>();
		boolean isRaisedShutdown;
		
		public Faultout() {
			// Registering the listener to the contained component
			ucsct.getFault().registerListener(new FaultoutUtil());
		}
		
		
		@Override
		public boolean isRaisedShutdown() {
			return isRaisedShutdown;
		}
		
		// Class for the setting of the boolean fields (events)
		private class FaultoutUtil implements UCFaultInterface.Listener.Provided {
			@Override
			public void raiseShutdown() {
				isRaisedShutdown = true;
			}
		}
		
		@Override
		public void registerListener(UCFaultInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<UCFaultInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
			isRaisedShutdown = false;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedShutdown) {
				for (UCFaultInterface.Listener.Provided listener : listeners) {
					listener.raiseShutdown();
				}
			}
		}
		
	}
	
	@Override
	public Faultout getFaultout() {
		return faultout;
	}
	
	/** Clears the the boolean flags of all out-events in each contained port. */
	private void clearPorts() {
		getFaultout().clear();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyAllListeners() {
		ucsct.notifyAllListeners();

		notifyListeners();
	}
	
	public void notifyListeners() {
		getFaultout().notifyListeners();
	}
	
	
	/** Returns whether all event queues of the contained component instances are empty. 
	Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return ucsct.isEventQueueEmpty();
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
		ucsct.runComponent();
		// Notifying registered listeners
		notifyListeners();
	}

	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public UC getUcsct() {
		return ucsct;
	}
	
	// Environmental Component instances
	
	
}
