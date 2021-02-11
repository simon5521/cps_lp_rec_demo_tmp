package hu.bme.mit.gamma.casestudy.camea.system;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.interfaces.*;
import hu.bme.mit.gamma.casestudy.camea.queuemanager.*;
import hu.bme.mit.gamma.casestudy.camea.paralellqueuemanager.*;

public class LPCameraSystem implements LPCameraSystemInterface {
	// Component instances
	private QueueManager LPDetection;
	private QueueManager LPNetworking;
	private ParalellQueueManager LPRecognition;
	// Environmental Component instances
	// Port instances
	private DetLoss detLoss;
	private DetNetLoss detNetLoss;
	private RecLoss recLoss;
	private LPNum lPNum;
	
	
	public LPCameraSystem() {
		LPDetection = new QueueManager();
		LPNetworking = new QueueManager();
		LPRecognition = new ParalellQueueManager();
		detLoss = new DetLoss();
		detNetLoss = new DetNetLoss();
		recLoss = new RecLoss();
		lPNum = new LPNum();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		LPDetection.reset();
		LPNetworking.reset();
		LPRecognition.reset();
		// Setting only a single queue for cascade statecharts
		LPDetection.changeInsertQueue();
		LPNetworking.changeInsertQueue();
		LPRecognition.changeInsertQueue();
		clearPorts();
		// Initializing chain of listeners and events 
		notifyAllListeners();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		LPDetection.getOutStream().registerListener(LPNetworking.getInStream());
		LPNetworking.getInStream().registerListener(LPDetection.getOutStream());
		LPNetworking.getOutStream().registerListener(LPRecognition.getInStream());
		LPRecognition.getInStream().registerListener(LPNetworking.getOutStream());
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class DetLoss implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> listeners = new LinkedList<StreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		
		public DetLoss() {
			// Registering the listener to the contained component
			LPDetection.getDataLoss().registerListener(new DetLossUtil());
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		
		// Class for the setting of the boolean fields (events)
		private class DetLossUtil implements StreamInterface.Listener.Provided {
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
	public DetLoss getDetLoss() {
		return detLoss;
	}
	
	public class DetNetLoss implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> listeners = new LinkedList<StreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		
		public DetNetLoss() {
			// Registering the listener to the contained component
			LPNetworking.getDataLoss().registerListener(new DetNetLossUtil());
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		
		// Class for the setting of the boolean fields (events)
		private class DetNetLossUtil implements StreamInterface.Listener.Provided {
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
	public DetNetLoss getDetNetLoss() {
		return detNetLoss;
	}
	
	public class RecLoss implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> listeners = new LinkedList<StreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		
		public RecLoss() {
			// Registering the listener to the contained component
			LPRecognition.getDataLoss().registerListener(new RecLossUtil());
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		
		// Class for the setting of the boolean fields (events)
		private class RecLossUtil implements StreamInterface.Listener.Provided {
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
	public RecLoss getRecLoss() {
		return recLoss;
	}
	
	public class LPNum implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> listeners = new LinkedList<StreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		
		public LPNum() {
			// Registering the listener to the contained component
			LPRecognition.getOutStream().registerListener(new LPNumUtil());
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		
		// Class for the setting of the boolean fields (events)
		private class LPNumUtil implements StreamInterface.Listener.Provided {
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
	public LPNum getLPNum() {
		return lPNum;
	}
	
	/** Clears the the boolean flags of all out-events in each contained port. */
	private void clearPorts() {
		getDetLoss().clear();
		getDetNetLoss().clear();
		getRecLoss().clear();
		getLPNum().clear();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyAllListeners() {
		LPDetection.notifyAllListeners();
		LPNetworking.notifyAllListeners();
		LPRecognition.notifyAllListeners();

		notifyListeners();
	}
	
	public void notifyListeners() {
		getDetLoss().notifyListeners();
		getDetNetLoss().notifyListeners();
		getRecLoss().notifyListeners();
		getLPNum().notifyListeners();
	}
	
	
	/** Returns whether all event queues of the contained component instances are empty. 
	Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return LPDetection.isEventQueueEmpty() && LPNetworking.isEventQueueEmpty() && LPRecognition.isEventQueueEmpty();
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
		LPDetection.runComponent();
		LPNetworking.runComponent();
		LPRecognition.runComponent();
		// Notifying registered listeners
		notifyListeners();
	}

	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public QueueManager getLPDetection() {
		return LPDetection;
	}
	
	public QueueManager getLPNetworking() {
		return LPNetworking;
	}
	
	public ParalellQueueManager getLPRecognition() {
		return LPRecognition;
	}
	
	// Environmental Component instances
	
	
}
