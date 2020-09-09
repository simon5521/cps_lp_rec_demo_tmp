package hu.bme.mit.gamma.usecase.epas.mainctrl;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.usecase.epas.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.usecase.epas.mainctrl.IMainctrlStatemachine.*;
import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.mainctrl.MainctrlStatemachine;
import hu.bme.mit.gamma.usecase.epas.mainctrl.MainctrlStatemachine.State;

public class MainctrlStatechart implements MainctrlStatechartInterface {
	// The wrapped Yakindu statemachine
	private MainctrlStatemachine mainctrlStatemachine;
	// Port instances
	private S1HW s1HW;
	private Monitor monitor;
	private S3HW s3HW;
	private S2HW s2HW;
	private UCHW uCHW;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public MainctrlStatechart() {
		mainctrlStatemachine = new MainctrlStatemachine();
		s1HW = new S1HW();
		monitor = new Monitor();
		s3HW = new S3HW();
		s2HW = new S2HW();
		uCHW = new UCHW();
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
		mainctrlStatemachine.init();
		mainctrlStatemachine.enter();
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
					case "S1HW.Latent": 
						mainctrlStatemachine.getSCIS1HW().raiseLatent();
					break;
					case "S1HW.Det": 
						mainctrlStatemachine.getSCIS1HW().raiseDet();
					break;
					case "S3HW.Latent": 
						mainctrlStatemachine.getSCIS3HW().raiseLatent();
					break;
					case "S3HW.Det": 
						mainctrlStatemachine.getSCIS3HW().raiseDet();
					break;
					case "S2HW.Latent": 
						mainctrlStatemachine.getSCIS2HW().raiseLatent();
					break;
					case "S2HW.Det": 
						mainctrlStatemachine.getSCIS2HW().raiseDet();
					break;
					case "UCHW.Shutdown": 
						mainctrlStatemachine.getSCIUCHW().raiseShutdown();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		mainctrlStatemachine.runCycle();
	}
	
	// Inner classes representing Ports
	public class S1HW implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> registeredListeners = new LinkedList<SensorFaultInterface.Listener.Required>();

		@Override
		public void raiseLatent() {
			getInsertQueue().add(new Event("S1HW.Latent", null));
		}
		
		@Override
		public void raiseDet() {
			getInsertQueue().add(new Event("S1HW.Det", null));
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
	public S1HW getS1HW() {
		return s1HW;
	}
	
	public class Monitor implements MonitorInterface.Provided {
		private List<MonitorInterface.Listener.Provided> registeredListeners = new LinkedList<MonitorInterface.Listener.Provided>();


		@Override
		public boolean isRaisedWarning() {
			return mainctrlStatemachine.getSCIMonitor().isRaisedWarning();
		}
		@Override
		public boolean isRaisedLoa() {
			return mainctrlStatemachine.getSCIMonitor().isRaisedLoa();
		}
		@Override
		public boolean isRaisedSelfsteering() {
			return mainctrlStatemachine.getSCIMonitor().isRaisedSelfsteering();
		}
		@Override
		public void registerListener(final MonitorInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			mainctrlStatemachine.getSCIMonitor().getListeners().add(new SCIMonitorListener() {
				@Override
				public void onWarningRaised() {
					listener.raiseWarning();
				}
				
				@Override
				public void onLoaRaised() {
					listener.raiseLoa();
				}
				
				@Override
				public void onSelfsteeringRaised() {
					listener.raiseSelfsteering();
				}
			});
		}
		
		@Override
		public List<MonitorInterface.Listener.Provided> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedWarning()) {
				for (MonitorInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseWarning();
				}
			}
			if (isRaisedLoa()) {
				for (MonitorInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseLoa();
				}
			}
			if (isRaisedSelfsteering()) {
				for (MonitorInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseSelfsteering();
				}
			}
		}

	}
	
	@Override
	public Monitor getMonitor() {
		return monitor;
	}
	
	public class S3HW implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> registeredListeners = new LinkedList<SensorFaultInterface.Listener.Required>();

		@Override
		public void raiseLatent() {
			getInsertQueue().add(new Event("S3HW.Latent", null));
		}
		
		@Override
		public void raiseDet() {
			getInsertQueue().add(new Event("S3HW.Det", null));
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
	public S3HW getS3HW() {
		return s3HW;
	}
	
	public class S2HW implements SensorFaultInterface.Required {
		private List<SensorFaultInterface.Listener.Required> registeredListeners = new LinkedList<SensorFaultInterface.Listener.Required>();

		@Override
		public void raiseLatent() {
			getInsertQueue().add(new Event("S2HW.Latent", null));
		}
		
		@Override
		public void raiseDet() {
			getInsertQueue().add(new Event("S2HW.Det", null));
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
	public S2HW getS2HW() {
		return s2HW;
	}
	
	public class UCHW implements UCFaultInterface.Required {
		private List<UCFaultInterface.Listener.Required> registeredListeners = new LinkedList<UCFaultInterface.Listener.Required>();

		@Override
		public void raiseShutdown() {
			getInsertQueue().add(new Event("UCHW.Shutdown", null));
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
	public UCHW getUCHW() {
		return uCHW;
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getS1HW().notifyListeners();
		getMonitor().notifyListeners();
		getS3HW().notifyListeners();
		getS2HW().notifyListeners();
		getUCHW().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return mainctrlStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "evaluation":
				switch (state) {
					case "working":
						return isStateActive(State.main_region_main_evaluation_working);
					case "ShutdownState":
						return isStateActive(State.main_region_main_evaluation_ShutdownState);
					case "SelfSteeringState":
						return isStateActive(State.main_region_main_evaluation_SelfSteeringState);
				}
			case "operation":
				switch (state) {
					case "On":
						return isStateActive(State.main_region_main_operation_On);
					case "Off":
						return isStateActive(State.main_region_main_operation_Off);
				}
			case "r1":
				switch (state) {
					case "NormalOperation":
						return isStateActive(State.main_region_main_operation_On_r1_NormalOperation);
					case "Warning":
						return isStateActive(State.main_region_main_operation_On_r1_Warning);
				}
			case "main_region":
				switch (state) {
					case "main":
						return isStateActive(State.main_region_main);
				}
		}
		return false;
	}

	
	public long getLatentsensors() {
		return mainctrlStatemachine.getLatentsensors();
	}
	
	public long getOnsensors() {
		return mainctrlStatemachine.getOnsensors();
	}
	
	public long getOksensors() {
		return mainctrlStatemachine.getOksensors();
	}
	
	public long getOffsensors() {
		return mainctrlStatemachine.getOffsensors();
	}
	
	
}
