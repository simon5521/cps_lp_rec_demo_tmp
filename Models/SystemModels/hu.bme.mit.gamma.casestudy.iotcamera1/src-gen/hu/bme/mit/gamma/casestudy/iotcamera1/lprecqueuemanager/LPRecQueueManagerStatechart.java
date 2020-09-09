package hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager.ILPRecQueueManagerStatemachine.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager.LPRecQueueManagerStatemachine;
import hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager.LPRecQueueManagerStatemachine.State;

public class LPRecQueueManagerStatechart implements LPRecQueueManagerStatechartInterface {
	// The wrapped Yakindu statemachine
	private LPRecQueueManagerStatemachine lPRecQueueManagerStatemachine;
	// Port instances
	private Status status;
	private ProcessedImg processedImg;
	private PreProcessedImgOut preProcessedImgOut;
	private PreProcessedImg preProcessedImg;
	private NetworkStream networkStream;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public LPRecQueueManagerStatechart() {
		lPRecQueueManagerStatemachine = new LPRecQueueManagerStatemachine();
		status = new Status();
		processedImg = new ProcessedImg();
		preProcessedImgOut = new PreProcessedImgOut();
		preProcessedImg = new PreProcessedImg();
		networkStream = new NetworkStream();
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
		lPRecQueueManagerStatemachine.init();
		lPRecQueueManagerStatemachine.enter();
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
					case "ProcessedImg.NewData": 
						lPRecQueueManagerStatemachine.getSCIProcessedImg().raiseNewData();
					break;
					case "PreProcessedImg.NewData": 
						lPRecQueueManagerStatemachine.getSCIPreProcessedImg().raiseNewData();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		lPRecQueueManagerStatemachine.runCycle();
	}
	
	// Inner classes representing Ports
	public class Status implements StatusInterface.Provided {
		private List<StatusInterface.Listener.Provided> registeredListeners = new LinkedList<StatusInterface.Listener.Provided>();


		@Override
		public boolean isRaisedFull() {
			return lPRecQueueManagerStatemachine.getSCIStatus().isRaisedFull();
		}
		@Override
		public boolean isRaisedFree() {
			return lPRecQueueManagerStatemachine.getSCIStatus().isRaisedFree();
		}
		@Override
		public void registerListener(final StatusInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			lPRecQueueManagerStatemachine.getSCIStatus().getListeners().add(new SCIStatusListener() {
				@Override
				public void onFullRaised() {
					listener.raiseFull();
				}
				
				@Override
				public void onFreeRaised() {
					listener.raiseFree();
				}
			});
		}
		
		@Override
		public List<StatusInterface.Listener.Provided> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedFull()) {
				for (StatusInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseFull();
				}
			}
			if (isRaisedFree()) {
				for (StatusInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseFree();
				}
			}
		}

	}
	
	@Override
	public Status getStatus() {
		return status;
	}
	
	public class ProcessedImg implements StreamInterface.Required {
		private List<StreamInterface.Listener.Required> registeredListeners = new LinkedList<StreamInterface.Listener.Required>();

		@Override
		public void raiseNewData() {
			getInsertQueue().add(new Event("ProcessedImg.NewData", null));
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
	public ProcessedImg getProcessedImg() {
		return processedImg;
	}
	
	public class PreProcessedImgOut implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return lPRecQueueManagerStatemachine.getSCIPreProcessedImgOut().isRaisedNewData();
		}
		@Override
		public void registerListener(final StreamInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			lPRecQueueManagerStatemachine.getSCIPreProcessedImgOut().getListeners().add(new SCIPreProcessedImgOutListener() {
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
	public PreProcessedImgOut getPreProcessedImgOut() {
		return preProcessedImgOut;
	}
	
	public class PreProcessedImg implements StreamInterface.Required {
		private List<StreamInterface.Listener.Required> registeredListeners = new LinkedList<StreamInterface.Listener.Required>();

		@Override
		public void raiseNewData() {
			getInsertQueue().add(new Event("PreProcessedImg.NewData", null));
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
	public PreProcessedImg getPreProcessedImg() {
		return preProcessedImg;
	}
	
	public class NetworkStream implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return lPRecQueueManagerStatemachine.getSCINetworkStream().isRaisedNewData();
		}
		@Override
		public void registerListener(final StreamInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			lPRecQueueManagerStatemachine.getSCINetworkStream().getListeners().add(new SCINetworkStreamListener() {
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
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getStatus().notifyListeners();
		getProcessedImg().notifyListeners();
		getPreProcessedImgOut().notifyListeners();
		getPreProcessedImg().notifyListeners();
		getNetworkStream().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return lPRecQueueManagerStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main_region":
				switch (state) {
					case "NotFull":
						return isStateActive(State.main_region_NotFull);
					case "Full":
						return isStateActive(State.main_region_Full);
				}
		}
		return false;
	}

	
	public long getQueueLen() {
		return lPRecQueueManagerStatemachine.getQueueLen();
	}
	
	public long getQueueMaxLen() {
		return lPRecQueueManagerStatemachine.getQueueMaxLen();
	}
	
	
}
