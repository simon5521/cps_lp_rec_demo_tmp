package hu.bme.mit.gamma.casestudy.camea.paralellqueuemanager;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.camea.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.casestudy.camea.paralellqueuemanager.IParalellQueueManagerStatemachine.*;
import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.paralellqueuemanager.ParalellQueueManagerStatemachine.State;

public class ParalellQueueManager implements ParalellQueueManagerInterface {
	// The wrapped Yakindu statemachine
	private ParalellQueueManagerStatemachine paralellQueueManagerStatemachine;
	// Port instances
	private DataLoss dataLoss;
	private InStream inStream;
	private FromHW fromHW;
	private ToHW toHW;
	private OutStream outStream;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public ParalellQueueManager() {
		paralellQueueManagerStatemachine = new ParalellQueueManagerStatemachine();
		dataLoss = new DataLoss();
		inStream = new InStream();
		fromHW = new FromHW();
		toHW = new ToHW();
		outStream = new OutStream();
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
		paralellQueueManagerStatemachine.init();
		paralellQueueManagerStatemachine.enter();
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
						paralellQueueManagerStatemachine.getSCIInStream().raiseNewData();
					break;
					case "FromHW.NewData": 
						paralellQueueManagerStatemachine.getSCIFromHW().raiseNewData();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		paralellQueueManagerStatemachine.runCycle();
		notifyListeners();
	}
	
	// Inner classes representing Ports
	public class DataLoss implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return paralellQueueManagerStatemachine.getSCIDataLoss().isRaisedNewData();
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
	
	public class ToHW implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return paralellQueueManagerStatemachine.getSCIToHW().isRaisedNewData();
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
	
	public class OutStream implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return paralellQueueManagerStatemachine.getSCIOutStream().isRaisedNewData();
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
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getDataLoss().notifyListeners();
		getInStream().notifyListeners();
		getFromHW().notifyListeners();
		getToHW().notifyListeners();
		getOutStream().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return paralellQueueManagerStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main_region":
				switch (state) {
					case "Full":
						return isStateActive(State.main_region_Full);
					case "AllProcWorking":
						return isStateActive(State.main_region_AllProcWorking);
					case "NotAllProcWorking":
						return isStateActive(State.main_region_NotAllProcWorking);
				}
		}
		return false;
	}

	
	public long getAct_proc_num() {
		return paralellQueueManagerStatemachine.getAct_proc_num();
	}
	
	public long getProc_num() {
		return paralellQueueManagerStatemachine.getProc_num();
	}
	
	public long getQ_size() {
		return paralellQueueManagerStatemachine.getQ_size();
	}
	
	public long getB_size() {
		return paralellQueueManagerStatemachine.getB_size();
	}
	
	
}
