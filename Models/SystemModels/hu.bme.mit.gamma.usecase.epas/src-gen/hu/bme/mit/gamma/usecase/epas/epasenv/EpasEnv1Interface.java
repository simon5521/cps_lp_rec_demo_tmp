package hu.bme.mit.gamma.usecase.epas.epasenv;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.EvalInterface;

public interface EpasEnv1Interface {
	
	EvalInterface.Provided getFout();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
