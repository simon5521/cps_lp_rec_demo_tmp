package hu.bme.mit.gamma.casestudy.iotcamera1.smartcontrol;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.casestudy.iotcamera1.smartcontrol.ISmartControlStatemachine.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.smartcontrol.SmartControlStatemachine;
import hu.bme.mit.gamma.casestudy.iotcamera1.smartcontrol.SmartControlStatemachine.State;

public class SmartControlStatechart implements SmartControlStatechartInterface {
	// The wrapped Yakindu statemachine
	private SmartControlStatemachine smartControlStatemachine;
	// Port instances
	private InPicStream inPicStream;
	private LocalStream localStream;
	private NetworkStream networkStream;
	private Status status;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public SmartControlStatechart() {
		smartControlStatemachine = new SmartControlStatemachine();
		inPicStream = new InPicStream();
		localStream = new LocalStream();
		networkStream = new NetworkStream();
		status = new Status();
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
		smartControlStatemachine.init();
		smartControlStatemachine.enter();
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
					case "InPicStream.NewData": 
						smartControlStatemachine.getSCIInPicStream().raiseNewData();
					break;
					case "Status.Full": 
						smartControlStatemachine.getSCIStatus().raiseFull();
					break;
					case "Status.Free": 
						smartControlStatemachine.getSCIStatus().raiseFree();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		smartControlStatemachine.runCycle();
	}
	
	// Inner classes representing Ports
	public class InPicStream implements StreamInterface.Required {
		private List<StreamInterface.Listener.Required> registeredListeners = new LinkedList<StreamInterface.Listener.Required>();

		@Override
		public void raiseNewData() {
			getInsertQueue().add(new Event("InPicStream.NewData", null));
		}

		@Override
		public void registerListener(final StreamInterface.Listener.Required listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<StreamInterface.Listener.Required> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}

	}
	
	@Override
	public InPicStream getInPicStream() {
		return inPicStream;
	}
	
	public class LocalStream implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return smartControlStatemachine.getSCILocalStream().isRaisedNewData();
		}
		@Override
		public void registerListener(final StreamInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			smartControlStatemachine.getSCILocalStream().getListeners().add(new SCILocalStreamListener() {
				@Override
				public void onNewDataRaised() {
					listener.raiseNewData();
				}
			});
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
	public LocalStream getLocalStream() {
		return localStream;
	}
	
	public class NetworkStream implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return smartControlStatemachine.getSCINetworkStream().isRaisedNewData();
		}
		@Override
		public void registerListener(final StreamInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			smartControlStatemachine.getSCINetworkStream().getListeners().add(new SCINetworkStreamListener() {
				@Override
				public void onNewDataRaised() {
					listener.raiseNewData();
				}
			});
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
	public NetworkStream getNetworkStream() {
		return networkStream;
	}
	
	public class Status implements StatusInterface.Required {
		private List<StatusInterface.Listener.Required> registeredListeners = new LinkedList<StatusInterface.Listener.Required>();

		@Override
		public void raiseFull() {
			getInsertQueue().add(new Event("Status.Full", null));
		}
		
		@Override
		public void raiseFree() {
			getInsertQueue().add(new Event("Status.Free", null));
		}

		@Override
		public void registerListener(final StatusInterface.Listener.Required listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<StatusInterface.Listener.Required> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}

	}
	
	@Override
	public Status getStatus() {
		return status;
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getInPicStream().notifyListeners();
		getLocalStream().notifyListeners();
		getNetworkStream().notifyListeners();
		getStatus().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return smartControlStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "mainregion":
				switch (state) {
					case "SendToCloud":
						return isStateActive(State.mainregion_SendToCloud);
					case "SendToLocal":
						return isStateActive(State.mainregion_SendToLocal);
				}
		}
		return false;
	}

	
	
	
}
