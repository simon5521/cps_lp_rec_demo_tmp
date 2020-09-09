
#include "Epas.h"

//SynchronousCompositeComponentCode

Epas::Epas() :
	S1A(SensorStatechart()), 
	S2A(SensorStatechart()), 
	S3A(SensorStatechart()), 
	S1B(SensorStatechart()), 
	S2B(SensorStatechart()), 
	S3B(SensorStatechart()), 
	UCA(UCStatechart()), 
	UCB(UCStatechart()), 
	ACTRL(MainctrlStatechart()), 
	BCTRL(MainctrlStatechart()), 
	Ev(EvaluationStatechart())
	,
	s1AFault(*this), 
	s2AFault(*this), 
	s3AFault(*this), 
	s1BFault(*this), 
	s2BFault(*this), 
	s3BFault(*this), 
	uCAFault(*this), 
	uCBFault(*this), 
	state(*this)
	 {
	
	init();
}

/** Resets the contained statemachines recursively. Must be called to initialize the component. */

void Epas::reset() {
	S1A.reset();
	S2A.reset();
	S3A.reset();
	S1B.reset();
	S2B.reset();
	S3B.reset();
	UCA.reset();
	UCB.reset();
	ACTRL.reset();
	BCTRL.reset();
	Ev.reset();
	// Initializing chain of listeners and events 
	initListenerChain();
}

/** Creates the channel mappings and enters the wrapped statemachines. */
void Epas::init() {
	// Registration of simple channels
	S2B.getSensorFault().registerListener(BCTRL.getS2HW());
	BCTRL.getS2HW().registerListener(S2B.getSensorFault());
	S3A.getSensorFault().registerListener(ACTRL.getS3HW());
	ACTRL.getS3HW().registerListener(S3A.getSensorFault());
	S1A.getSensorFault().registerListener(ACTRL.getS1HW());
	ACTRL.getS1HW().registerListener(S1A.getSensorFault());
	BCTRL.getMonitor().registerListener(Ev.getBMonitor());
	Ev.getBMonitor().registerListener(BCTRL.getMonitor());
	S1B.getSensorFault().registerListener(BCTRL.getS1HW());
	BCTRL.getS1HW().registerListener(S1B.getSensorFault());
	S2A.getSensorFault().registerListener(ACTRL.getS2HW());
	ACTRL.getS2HW().registerListener(S2A.getSensorFault());
	UCB.getFault().registerListener(BCTRL.getUCHW());
	BCTRL.getUCHW().registerListener(UCB.getFault());
	UCA.getFault().registerListener(ACTRL.getUCHW());
	ACTRL.getUCHW().registerListener(UCA.getFault());
	ACTRL.getMonitor().registerListener(Ev.getAMonitor());
	Ev.getAMonitor().registerListener(ACTRL.getMonitor());
	S3B.getSensorFault().registerListener(BCTRL.getS3HW());
	BCTRL.getS3HW().registerListener(S3B.getSensorFault());
	// Registration of broadcast channels
	// Setting only a single queue for cascade statecharts
	S1A.changeInsertQueue();
	S2A.changeInsertQueue();
	S3A.changeInsertQueue();
	S1B.changeInsertQueue();
	S2B.changeInsertQueue();
	S3B.changeInsertQueue();
	UCA.changeInsertQueue();
	UCB.changeInsertQueue();
	ACTRL.changeInsertQueue();
	BCTRL.changeInsertQueue();
	Ev.changeInsertQueue();
}

/** Clears the the boolean flags of all out-events in each contained port. */
void Epas::clearPorts() {
	getS1AFault().clear();
	getS2AFault().clear();
	getS3AFault().clear();
	getS1BFault().clear();
	getS2BFault().clear();
	getS3BFault().clear();
	getUCAFault().clear();
	getUCBFault().clear();
	getState().clear();
}

/** Notifies all registered listeners in each contained port. */
void Epas::notifyListeners() {
	getS1AFault().notifyListeners();
	getS2AFault().notifyListeners();
	getS3AFault().notifyListeners();
	getS1BFault().notifyListeners();
	getS2BFault().notifyListeners();
	getS3BFault().notifyListeners();
	getUCAFault().notifyListeners();
	getUCBFault().notifyListeners();
	getState().notifyListeners();
}

/** Needed for the right event notification after initialization, as event notification from contained components
 * does not happen automatically (see the port implementations and runComponent method). */
void Epas::initListenerChain() {
	notifyListeners();
}


/** Returns whether all event queues of the contained component instances are empty. 
Should be used only be the container (composite system) class. */
bool Epas::isEventQueueEmpty() {
	return S1A.isEventQueueEmpty() && S2A.isEventQueueEmpty() && S3A.isEventQueueEmpty() && S1B.isEventQueueEmpty() && S2B.isEventQueueEmpty() && S3B.isEventQueueEmpty() && UCA.isEventQueueEmpty() && UCB.isEventQueueEmpty() && ACTRL.isEventQueueEmpty() && BCTRL.isEventQueueEmpty() && Ev.isEventQueueEmpty();
}

/** Initiates cycle runs until all event queues of component instances are empty. */

void Epas::runFullCycle() {
	do {
		runCycle();
	}
	while (!isEventQueueEmpty());
}

/** Changes event queues and initiates a cycle run.
	This should be the execution point from an asynchronous component. */

void Epas::runCycle() {
	// Composite type-dependent behavior
	runComponent();
}

/** Initiates a cycle run without changing the event queues.
 * Should be used only be the container (composite system) class. */
void Epas::runComponent() {
	// Starts with the clearing of the previous out-event flags
	clearPorts();
	// Running contained components
	S1A.runComponent();
	S2A.runComponent();
	S3A.runComponent();
	S1B.runComponent();
	S2B.runComponent();
	S3B.runComponent();
	UCA.runComponent();
	UCB.runComponent();
	ACTRL.runComponent();
	BCTRL.runComponent();
	Ev.runComponent();
	// Notifying registered listeners
	notifyListeners();
}


/**  Getter for component instances, e.g., enabling to check their states. */
SensorStatechart& Epas::getS1A() {
	return S1A;
}

SensorStatechart& Epas::getS2A() {
	return S2A;
}

SensorStatechart& Epas::getS3A() {
	return S3A;
}

SensorStatechart& Epas::getS1B() {
	return S1B;
}

SensorStatechart& Epas::getS2B() {
	return S2B;
}

SensorStatechart& Epas::getS3B() {
	return S3B;
}

UCStatechart& Epas::getUCA() {
	return UCA;
}

UCStatechart& Epas::getUCB() {
	return UCB;
}

MainctrlStatechart& Epas::getACTRL() {
	return ACTRL;
}

MainctrlStatechart& Epas::getBCTRL() {
	return BCTRL;
}

EvaluationStatechart& Epas::getEv() {
	return Ev;
}
