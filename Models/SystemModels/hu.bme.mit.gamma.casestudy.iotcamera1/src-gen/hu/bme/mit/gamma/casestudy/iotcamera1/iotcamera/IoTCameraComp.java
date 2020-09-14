package hu.bme.mit.gamma.casestudy.iotcamera1.iotcamera;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.lpdetqueuemanager.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.smartcontrol.*;

public class IoTCameraComp implements IoTCameraCompInterface {
	// Component instances
	private LPRecQueueManager LPRecQueueManager;
	private LPDetQueueManager LPDetQueueManager;
	private SmartControl SmartControl;
	// Environmental Component instances
	// Port instances
	private LPNumbers lPNumbers;
	private LPCroppedImg lPCroppedImg;
	private RecognisedLP recognisedLP;
	
	
	public IoTCameraComp() {
		LPRecQueueManager = new LPRecQueueManager();
		LPDetQueueManager = new LPDetQueueManager();
		SmartControl = new SmartControl();
		lPCroppedImg = new LPCroppedImg();
		lPNumbers = new LPNumbers();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		LPRecQueueManager.reset();
		LPDetQueueManager.reset();
		SmartControl.reset();
		// Setting only a single queue for cascade statecharts
		LPRecQueueManager.changeInsertQueue();
		LPDetQueueManager.changeInsertQueue();
		SmartControl.changeInsertQueue();
		clearPorts();
		// Initializing chain of listeners and events 
		notifyAllListeners();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		SmartControl.getLocalStream().registerListener(LPRecQueueManager.getPreProcessedImg());
		LPRecQueueManager.getPreProcessedImg().registerListener(SmartControl.getLocalStream());
		LPRecQueueManager.getStatus().registerListener(SmartControl.getStatus());
		SmartControl.getStatus().registerListener(LPRecQueueManager.getStatus());
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class LPNumbers implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> listeners = new LinkedList<StreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		
		public LPNumbers() {
			// Registering the listener to the contained component
			LPRecQueueManager.getNetworkStream().registerListener(new LPNumbersUtil());
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		
		// Class for the setting of the boolean fields (events)
		private class LPNumbersUtil implements StreamInterface.Listener.Provided {
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
	public LPNumbers getLPNumbers() {
		return lPNumbers;
	}
	
	public class LPCroppedImg implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> listeners = new LinkedList<StreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		
		public LPCroppedImg() {
			// Registering the listener to the contained component
			SmartControl.getNetworkStream().registerListener(new LPCroppedImgUtil());
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		
		// Class for the setting of the boolean fields (events)
		private class LPCroppedImgUtil implements StreamInterface.Listener.Provided {
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
	public LPCroppedImg getLPCroppedImg() {
		return lPCroppedImg;
	}
	
	public class RecognisedLP implements StreamInterface.Provided {
		private List<StreamInterface.Listener.Provided> listeners = new LinkedList<StreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		
		public RecognisedLP() {
			// Registering the listener to the contained component
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		
		// Class for the setting of the boolean fields (events)
		private class RecognisedLPUtil implements StreamInterface.Listener.Provided {
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
	public RecognisedLP getRecognisedLP() {
		return recognisedLP;
	}
	
	/** Clears the the boolean flags of all out-events in each contained port. */
	private void clearPorts() {
		getLPCroppedImg().clear();
		getLPNumbers().clear();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyAllListeners() {
		LPRecQueueManager.notifyAllListeners();
		LPDetQueueManager.notifyAllListeners();
		SmartControl.notifyAllListeners();

		notifyListeners();
	}
	
	public void notifyListeners() {
		getLPCroppedImg().notifyListeners();
		getLPNumbers().notifyListeners();
	}
	
	
	/** Returns whether all event queues of the contained component instances are empty. 
	Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return LPRecQueueManager.isEventQueueEmpty() && LPDetQueueManager.isEventQueueEmpty() && SmartControl.isEventQueueEmpty();
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
		LPRecQueueManager.runComponent();
		LPDetQueueManager.runComponent();
		SmartControl.runComponent();
		// Notifying registered listeners
		notifyListeners();
	}

	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public LPRecQueueManager getLPRecQueueManager() {
		return LPRecQueueManager;
	}
	
	public LPDetQueueManager getLPDetQueueManager() {
		return LPDetQueueManager;
	}
	
	public SmartControl getSmartControl() {
		return SmartControl;
	}
	
	// Environmental Component instances
	
	
}
