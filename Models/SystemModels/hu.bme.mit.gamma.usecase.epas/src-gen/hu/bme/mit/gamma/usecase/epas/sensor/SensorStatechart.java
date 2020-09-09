package hu.bme.mit.gamma.usecase.epas.sensor;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.usecase.epas.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.usecase.epas.sensor.ISensorStatemachine.*;
import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.sensor.SensorStatemachine;
import hu.bme.mit.gamma.usecase.epas.sensor.SensorStatemachine.State;

public class SensorStatechart implements SensorStatechartInterface {
	// The wrapped Yakindu statemachine
	private SensorStatemachine sensorStatemachine;
	// Port instances
	private SensorFault sensorFault;
	private HWFault hWFault;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public SensorStatechart() {
		sensorStatemachine = new SensorStatemachine();
		sensorFault = new SensorFault();
		hWFault = new HWFault();
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
		sensorStatemachine.init();
		sensorStatemachine.enter();
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
					case "HWFault.Latent": 
						sensorStatemachine.getSCIHWFault().raiseLatent();
					break;
					case "HWFault.Det": 
						sensorStatemachine.getSCIHWFault().raiseDet();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		sensorStatemachine.runCycle();
	}
	
	// Inner classes representing Ports
	public class SensorFault implements SensorFaultInterface.Provided {
		private List<SensorFaultInterface.Listener.Provided> registeredListeners = new LinkedList<SensorFaultInterface.Listener.Provided>();


		@Override
		public boolean isRaisedLatent() {
			return sensorStatemachine.getSCISensorFault().isRaisedLatent();
		}
		@Override
		public boolean isRaisedDet() {
			return sensorStatemachine.getSCISensorFault().isRaisedDet();
		}
		@Override
		public void registerListener(final SensorFaultInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			sensorStatemachine.getSCISensorFault().getListeners().add(new SCISensorFaultListener() {
				@Override
				public void onLatentRaised() {
					listener.raiseLatent();
				}
				
				@Override
				public void onDetRaised() {
					listener.raiseDet();
				}
			});
		}
		
		@Override
		public List<SensorFaultInterface.Listener.Provided> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedLatent()) {
				for (SensorFaultInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseLatent();
				}
			}
			if (isRaisedDet()) {
				for (SensorFaultInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseDet();
				}
			}
		}

	}
	
	@Override
	public SensorFault getSensorFault() {
		return sensorFault;
	}
	
	public class HWFault implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> registeredListeners = new LinkedList<SensorFaultInterface.Listener.Required>();

		@Override
		public void raiseLatent() {
			getInsertQueue().add(new Event("HWFault.Latent", null));
		}
		
		@Override
		public void raiseDet() {
			getInsertQueue().add(new Event("HWFault.Det", null));
		}

		@Override
		public void registerListener(final SensorFaultInterface.Listener.Required listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<SensorFaultInterface.Listener.Required> getRegisteredListeners() {
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
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getSensorFault().notifyListeners();
		getHWFault().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return sensorStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "Sensor":
				switch (state) {
					case "LatentFailure":
						return isStateActive(State.sensor_LatentFailure);
					case "Off":
						return isStateActive(State.sensor_Off);
					case "Ok":
						return isStateActive(State.sensor_Ok);
				}
		}
		return false;
	}

	
	
	
}
