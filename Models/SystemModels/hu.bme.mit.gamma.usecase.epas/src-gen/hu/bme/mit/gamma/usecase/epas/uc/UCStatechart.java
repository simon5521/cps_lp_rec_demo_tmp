package hu.bme.mit.gamma.usecase.epas.uc;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.usecase.epas.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.usecase.epas.uc.IUCStatemachine.*;
import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.uc.UCStatemachine;
import hu.bme.mit.gamma.usecase.epas.uc.UCStatemachine.State;

public class UCStatechart implements UCStatechartInterface {
	// The wrapped Yakindu statemachine
	private UCStatemachine uCStatemachine;
	// Port instances
	private HWFault hWFault;
	private Fault fault;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public UCStatechart() {
		uCStatemachine = new UCStatemachine();
		hWFault = new HWFault();
		fault = new Fault();
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
		uCStatemachine.init();
		uCStatemachine.enter();
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
					case "HWFault.Shutdown": 
						uCStatemachine.getSCIHWFault().raiseShutdown();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		uCStatemachine.runCycle();
	}
	
	// Inner classes representing Ports
	public class HWFault implements UCFaultInterface.Required {
		private List<UCFaultInterface.Listener.Required> registeredListeners = new LinkedList<UCFaultInterface.Listener.Required>();

		@Override
		public void raiseShutdown() {
			getInsertQueue().add(new Event("HWFault.Shutdown", null));
		}

		@Override
		public void registerListener(final UCFaultInterface.Listener.Required listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<UCFaultInterface.Listener.Required> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}

	}
	
	@Override
	public HWFault getHWFault() {
		return hWFault;
	}
	
	public class Fault implements UCFaultInterface.Provided {
		private List<UCFaultInterface.Listener.Provided> registeredListeners = new LinkedList<UCFaultInterface.Listener.Provided>();


		@Override
		public boolean isRaisedShutdown() {
			return uCStatemachine.getSCIFault().isRaisedShutdown();
		}
		@Override
		public void registerListener(final UCFaultInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			uCStatemachine.getSCIFault().getListeners().add(new SCIFaultListener() {
				@Override
				public void onShutdownRaised() {
					listener.raiseShutdown();
				}
			});
		}
		
		@Override
		public List<UCFaultInterface.Listener.Provided> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedShutdown()) {
				for (UCFaultInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseShutdown();
				}
			}
		}

	}
	
	@Override
	public Fault getFault() {
		return fault;
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getHWFault().notifyListeners();
		getFault().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return uCStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "UC":
				switch (state) {
					case "Off":
						return isStateActive(State.uC_Off);
					case "On":
						return isStateActive(State.uC_On);
				}
		}
		return false;
	}

	
	
	
}
