package hu.bme.mit.gamma.casestudy.camea.traffic;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.interfaces.*;
import hu.bme.mit.gamma.casestudy.camea.basictraffic.*;

public class TrafficComp implements TrafficCompInterface {
	// Component instances
	private BasicTraffic basic;
	// Environmental Component instances
	// Port instances
	private Cars cars;
	
	
	public TrafficComp() {
		basic = new BasicTraffic();
		cars = new Cars();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		basic.reset();
		// Setting only a single queue for cascade statecharts
		basic.changeInsertQueue();
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
	public class Cars implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> listeners = new LinkedList<StreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		
		public Cars() {
			// Registering the listener to the contained component
			basic.getOutPort().registerListener(new CarsUtil());
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		
		// Class for the setting of the boolean fields (events)
		private class CarsUtil implements StreamInterface.Listener.Provided {
			@Override
			public void raiseNewData() {
				isRaisedNewData = true;
			}
		}
		
		@Override
		public void registerListener(StreamInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<StreamInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
			isRaisedNewData = false;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedNewData) {
				for (StreamInterface.Listener.Provided listener : listeners) {
					listener.raiseNewData();
				}
			}
		}
		
	}
	
	@Override
	public Cars getCars() {
		return cars;
	}
	
	/** Clears the the boolean flags of all out-events in each contained port. */
	private void clearPorts() {
		getCars().clear();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyAllListeners() {
		basic.notifyAllListeners();

		notifyListeners();
	}
	
	public void notifyListeners() {
		getCars().notifyListeners();
	}
	
	
	/** Returns whether all event queues of the contained component instances are empty. 
	Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return basic.isEventQueueEmpty();
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
		basic.runComponent();
		// Notifying registered listeners
		notifyListeners();
	}

	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public BasicTraffic getBasic() {
		return basic;
	}
	
	// Environmental Component instances
	
	
}
