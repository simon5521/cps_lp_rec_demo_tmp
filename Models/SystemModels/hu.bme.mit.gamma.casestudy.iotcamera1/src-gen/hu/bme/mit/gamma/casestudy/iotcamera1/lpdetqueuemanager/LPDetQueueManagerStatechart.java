package hu.bme.mit.gamma.casestudy.iotcamera1.lpdetqueuemanager;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.casestudy.iotcamera1.lpdetqueuemanager.ILPDetQueueManagerStatemachine.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.lpdetqueuemanager.LPDetQueueManagerStatemachine;
import hu.bme.mit.gamma.casestudy.iotcamera1.lpdetqueuemanager.LPDetQueueManagerStatemachine.State;

public class LPDetQueueManagerStatechart implements LPDetQueueManagerStatechartInterface {
	// The wrapped Yakindu statemachine
	private LPDetQueueManagerStatemachine lPDetQueueManagerStatemachine;
	// Port instances
	private Traffic traffic;
	private Status status;
	private Img img;
	private PreProcessedImg preProcessedImg;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public LPDetQueueManagerStatechart() {
		lPDetQueueManagerStatemachine = new LPDetQueueManagerStatemachine();
		traffic = new Traffic();
		status = new Status();
		img = new Img();
		preProcessedImg = new PreProcessedImg();
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
		lPDetQueueManagerStatemachine.init();
		lPDetQueueManagerStatemachine.enter();
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
					case "Traffic.NewCar": 
						lPDetQueueManagerStatemachine.getSCITraffic().raiseNewCar();
					break;
					case "PreProcessedImg.NewData": 
						lPDetQueueManagerStatemachine.getSCIPreProcessedImg().raiseNewData();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		lPDetQueueManagerStatemachine.runCycle();
	}
	
	// Inner classes representing Ports
	public class Traffic implements TrafficInterface.Required {
		private List<TrafficInterface.Listener.Required> registeredListeners = new LinkedList<TrafficInterface.Listener.Required>();

		@Override
		public void raiseNewCar() {
			getInsertQueue().add(new Event("Traffic.NewCar", null));
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
	public Traffic getTraffic() {
		return traffic;
	}
	
	public class Status implements StatusInterface.Provided {
		private List<StatusInterface.Listener.Provided> registeredListeners = new LinkedList<StatusInterface.Listener.Provided>();


		@Override
		public boolean isRaisedFull() {
			return lPDetQueueManagerStatemachine.getSCIStatus().isRaisedFull();
		}
		@Override
		public boolean isRaisedFree() {
			return lPDetQueueManagerStatemachine.getSCIStatus().isRaisedFree();
		}
		@Override
		public void registerListener(final StatusInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			lPDetQueueManagerStatemachine.getSCIStatus().getListeners().add(new SCIStatusListener() {
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
	
	public class Img implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> registeredListeners = new LinkedList<StreamInterface.Listener.Provided>();


		@Override
		public boolean isRaisedNewData() {
			return lPDetQueueManagerStatemachine.getSCIImg().isRaisedNewData();
		}
		@Override
		public void registerListener(final StreamInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			lPDetQueueManagerStatemachine.getSCIImg().getListeners().add(new SCIImgListener() {
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
	public Img getImg() {
		return img;
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
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getTraffic().notifyListeners();
		getStatus().notifyListeners();
		getImg().notifyListeners();
		getPreProcessedImg().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return lPDetQueueManagerStatemachine.isStateActive(state);
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

	
	public long getQueueMaxLen() {
		return lPDetQueueManagerStatemachine.getQueueMaxLen();
	}
	
	public long getQueueLen() {
		return lPDetQueueManagerStatemachine.getQueueLen();
	}
	
	
}
