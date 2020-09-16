package hu.bme.mit.gamma.usecase.epas.epasenv;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.UCFaultInterface;

public interface UCenvInterface {
	
	UCFaultInterface.Provided getFaultout();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
