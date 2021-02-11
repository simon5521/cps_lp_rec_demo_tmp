package hu.bme.mit.gamma.casestudy.camea.basictraffic;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.camea.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.casestudy.camea.basictraffic.IBasicTrafficStatemachine.*;
import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.basictraffic.BasicTrafficStatemachine.State;

public class BasicTraffic implements BasicTrafficInterface {
	// The wrapped Yakindu statemachine
	private BasicTrafficStatemachine basicTrafficStatemachine;
	// Port instances
	private InPort inPort;
	private OutPort outPort;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public BasicTraffic() {
		basicTrafficStatemachine = new BasicTrafficStatemachine();
		inPort = new InPort();
		outPort = new OutPort();
	}
	
	/** Resets the statemachine. Must be called to initialize the component. */
	@Override
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		basicTrafficStatemachine.init();
		basicTrafficStatemachine.enter();
		notifyListeners();
	}
	
	/** Changes the event queues of the component instance. Should be used only be the container (composite system) class. */
	public void changeEventQueues() {
		insertQueue = !insertQueue;
		processQueue = !processQueue;
	}
	
	/** Changes the event queues to which the events are put. Should be used only be a cascade container (composite system) class. */
	public void changeInsertQueue() {
		insertQueue = !insertQueue;
	}
	
	/** Returns whether the eventQueue containing incoming messages is empty. Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return getInsertQueue().isEmpty();
	}
	
	/** Returns the event queue into which events should be put in the particular cycle. */
	private Queue<Event> getInsertQueue() {
		if (insertQueue) {
			return eventQueue1;
		}
		return eventQueue2;
	}
	
	/** Returns the event queue from which events should be inspected in the particular cycle. */
	private Queue<Event> getProcessQueue() {
		if (processQueue) {
			return eventQueue1;
		}
		return eventQueue2;
	}
	
	/** Changes event queues and initiating a cycle run. */
	@Override
	public void runCycle() {
		changeEventQueues();
		runComponent();
	}
	
	/** Changes the insert queue and initiates a run. */
	public void runAndRechangeInsertQueue() {
		// First the insert queue is changed back, so self-event sending can work
		changeInsertQueue();
		runComponent();
	}
	
	/** Initiates a cycle run without changing the event queues. It is needed if this component is contained (wrapped) by another component.
	Should be used only be the container (composite system) class. */
	public void runComponent() {
		Queue<Event> eventQueue = getProcessQueue();
		while (!eventQueue.isEmpty()) {
				Event event = eventQueue.remove();
				switch (event.getEvent()) {
					case "InPort.NewCar": 
						basicTrafficStatemachine.getSCIInPort().raiseNewCar();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		basicTrafficStatemachine.runCycle();
		notifyListeners();
	}
	
	// Inner classes representing Ports
	public class InPort implements TrafficInterface.Required {
		private List<TrafficInterface.Listener.Required> registeredListeners = new LinkedList<TrafficInterface.Listener.Required>();

		@Override
		public void raiseNewCar() {
			getInsertQueue().add(new Event("InPort.NewCar"));
		}

		@Override
		public void registerListener(final TrafficInterface.Listener.Required listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<TrafficInterface.Listener.Required> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}

	}
	
	@Override
	public InPort getInPort() {
		return inPort;
	}
	
	public class OutPort implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return basicTrafficStatemachine.getSCIOutPort().isRaisedNewData();
		}
		@Override
		public void registerListener(final StreamInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<StreamInterface.Listener.Provided> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedNewData()) {
				for (StreamInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseNewData();
				}
			}
		}

	}
	
	@Override
	public OutPort getOutPort() {
		return outPort;
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getInPort().notifyListeners();
		getOutPort().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return basicTrafficStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main_region":
				switch (state) {
					case "StateA":
						return isStateActive(State.main_region_StateA);
				}
		}
		return false;
	}

	
	
	
}
