package hu.bme.mit.gamma.usecase.epas.evaluation;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.EvalInterface;
import hu.bme.mit.gamma.usecase.epas.interfaces.MonitorInterface;

public interface EvaluationStatechartInterface {
	
	EvalInterface.Provided getEval();
	MonitorInterface.Required getBMonitor();
	MonitorInterface.Required getAMonitor();
	
	void reset();
	
	void runCycle();
	
}
