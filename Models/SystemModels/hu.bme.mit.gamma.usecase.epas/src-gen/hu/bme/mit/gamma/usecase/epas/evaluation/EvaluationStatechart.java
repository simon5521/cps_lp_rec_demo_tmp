package hu.bme.mit.gamma.usecase.epas.evaluation;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.usecase.epas.interfaces.*;
// Yakindu listeners
import hu.bme.mit.gamma.usecase.epas.evaluation.IEvaluationStatemachine.*;
import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.evaluation.EvaluationStatemachine;
import hu.bme.mit.gamma.usecase.epas.evaluation.EvaluationStatemachine.State;

public class EvaluationStatechart implements EvaluationStatechartInterface {
	// The wrapped Yakindu statemachine
	private EvaluationStatemachine evaluationStatemachine;
	// Port instances
	private Eval eval;
	private BMonitor bMonitor;
	private AMonitor aMonitor;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public EvaluationStatechart() {
		evaluationStatemachine = new EvaluationStatemachine();
		eval = new Eval();
		bMonitor = new BMonitor();
		aMonitor = new AMonitor();
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
		evaluationStatemachine.init();
		evaluationStatemachine.enter();
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
					case "BMonitor.Warning": 
						evaluationStatemachine.getSCIBMonitor().raiseWarning();
					break;
					case "BMonitor.Loa": 
						evaluationStatemachine.getSCIBMonitor().raiseLoa();
					break;
					case "BMonitor.Selfsteering": 
						evaluationStatemachine.getSCIBMonitor().raiseSelfsteering();
					break;
					case "AMonitor.Warning": 
						evaluationStatemachine.getSCIAMonitor().raiseWarning();
					break;
					case "AMonitor.Loa": 
						evaluationStatemachine.getSCIAMonitor().raiseLoa();
					break;
					case "AMonitor.Selfsteering": 
						evaluationStatemachine.getSCIAMonitor().raiseSelfsteering();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		evaluationStatemachine.runCycle();
	}
	
	// Inner classes representing Ports
	public class Eval implements EvalInterface.Provided {
		private List<EvalInterface.Listener.Provided> registeredListeners = new LinkedList<EvalInterface.Listener.Provided>();


		@Override
		public boolean isRaisedSS() {
			return evaluationStatemachine.getSCIEval().isRaisedSS();
		}
		@Override
		public boolean isRaisedSLoA() {
			return evaluationStatemachine.getSCIEval().isRaisedSLoA();
		}
		@Override
		public void registerListener(final EvalInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			evaluationStatemachine.getSCIEval().getListeners().add(new SCIEvalListener() {
				@Override
				public void onSSRaised() {
					listener.raiseSS();
				}
				
				@Override
				public void onSLoARaised() {
					listener.raiseSLoA();
				}
			});
		}
		
		@Override
		public List<EvalInterface.Listener.Provided> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedSS()) {
				for (EvalInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseSS();
				}
			}
			if (isRaisedSLoA()) {
				for (EvalInterface.Listener.Provided listener : registeredListeners) {
					listener.raiseSLoA();
				}
			}
		}

	}
	
	@Override
	public Eval getEval() {
		return eval;
	}
	
	public class BMonitor implements MonitorInterface.Required {
		private List<MonitorInterface.Listener.Required> registeredListeners = new LinkedList<MonitorInterface.Listener.Required>();

		@Override
		public void raiseWarning() {
			getInsertQueue().add(new Event("BMonitor.Warning", null));
		}
		
		@Override
		public void raiseLoa() {
			getInsertQueue().add(new Event("BMonitor.Loa", null));
		}
		
		@Override
		public void raiseSelfsteering() {
			getInsertQueue().add(new Event("BMonitor.Selfsteering", null));
		}

		@Override
		public void registerListener(final MonitorInterface.Listener.Required listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<MonitorInterface.Listener.Required> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}

	}
	
	@Override
	public BMonitor getBMonitor() {
		return bMonitor;
	}
	
	public class AMonitor implements MonitorInterface.Required {
		private List<MonitorInterface.Listener.Required> registeredListeners = new LinkedList<MonitorInterface.Listener.Required>();

		@Override
		public void raiseWarning() {
			getInsertQueue().add(new Event("AMonitor.Warning", null));
		}
		
		@Override
		public void raiseLoa() {
			getInsertQueue().add(new Event("AMonitor.Loa", null));
		}
		
		@Override
		public void raiseSelfsteering() {
			getInsertQueue().add(new Event("AMonitor.Selfsteering", null));
		}

		@Override
		public void registerListener(final MonitorInterface.Listener.Required listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<MonitorInterface.Listener.Required> getRegisteredListeners() {
			return registeredListeners;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}

	}
	
	@Override
	public AMonitor getAMonitor() {
		return aMonitor;
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyListeners() {
		getEval().notifyListeners();
		getBMonitor().notifyListeners();
		getAMonitor().notifyListeners();
	}
	
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return evaluationStatemachine.isStateActive(state);
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "r1":
				switch (state) {
					case "Normal":
						return isStateActive(State.evaluation_Operation_r1_Normal);
					case "Warning":
						return isStateActive(State.evaluation_Operation_r1_Warning);
				}
			case "evaluation":
				switch (state) {
					case "Operation":
						return isStateActive(State.evaluation_Operation);
					case "LoA":
						return isStateActive(State.evaluation_LoA);
					case "Selfsteering":
						return isStateActive(State.evaluation_Selfsteering);
				}
		}
		return false;
	}

	
	public long getSides() {
		return evaluationStatemachine.getSides();
	}
	
	
}
