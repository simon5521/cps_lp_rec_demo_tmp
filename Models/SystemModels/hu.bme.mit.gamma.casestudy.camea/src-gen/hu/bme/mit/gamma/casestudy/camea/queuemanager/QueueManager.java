package hu.bme.mit.gamma.casestudy.camea.queuemanager;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.camea.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.casestudy.camea.queuemanager.IQueueManagerStatemachine.*;
import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.queuemanager.QueueManagerStatemachine.State;

public class QueueManager implements QueueManagerInterface {
	// The wrapped Yakindu statemachine
	private QueueManagerStatemachine queueManagerStatemachine;
	// Port instances
	private InStream inStream;
	private ToHW toHW;
	private FromHW fromHW;
	private OutStream outStream;
	private DataLoss dataLoss;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public QueueManager() {
		queueManagerStatemachine = new QueueManagerStatemachine();
		inStream = new InStream();
		toHW = new ToHW();
		fromHW = new FromHW();
		outStream = new OutStream();
		dataLoss = new DataLoss();
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
		queueManagerStatemachine.init();
		queueManagerStatemachine.enter();
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
					case "InStream.NewData": 
						queueManagerStatemachine.getSCIInStream().raiseNewData();
					break;
					case "FromHW.NewData": 
						queueManagerStatemachine.getSCIFromHW().raiseNewData();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		queueManagerStatemachine.runCycle();
		notifyListeners();
	}
	
	// Inner classes representing Ports
	public class InStream implements StreamInterface.Required {
		private List<StreamInterface.Listener.Required> registeredListeners = new LinkedList<StreamInterface.Listener.Required>();

		@Override
		public void raiseNewData() {
			getInsertQueue().add(new Event("InStream.NewData"));
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
	public InStream getInStream() {
		return inStream;
	}
	
	public class ToHW implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return queueManagerStatemachine.getSCIToHW().isRaisedNewData();
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
	public ToHW getToHW() {
		return toHW;
	}
	
	public class FromHW implements StreamInterface.Required {
		private List<StreamInterface.Listener.Required> registeredListeners = new LinkedList<StreamInterface.Listener.Required>();

		@Override
		public void raiseNewData() {
			getInsertQueue().add(new Event("FromHW.NewData"));
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
	public FromHW getFromHW() {
		return fromHW;
	}
	
	public class OutStream implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return queueManagerStatemachine.getSCIOutStream().isRaisedNewData();
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
	public OutStream getOutStream() {
		return outStream;
	}
	
	public class DataLoss implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return queueManagerStatemachine.getSCIDataLoss().isRaisedNewData();
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
	public DataLoss getDataLoss() {
		return dataLoss;
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getInStream().notifyListeners();
		getToHW().notifyListeners();
		getFromHW().notifyListeners();
		getOutStream().notifyListeners();
		getDataLoss().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return queueManagerStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main_region":
				switch (state) {
					case "Full":
						return isStateActive(State.main_region_Full);
					case "Working":
						return isStateActive(State.main_region_Working);
					case "NoData":
						return isStateActive(State.main_region_NoData);
				}
		}
		return false;
	}

	
	public long getQ_size() {
		return queueManagerStatemachine.getQ_size();
	}
	
	public long getB_size() {
		return queueManagerStatemachine.getB_size();
	}
	
	
}
