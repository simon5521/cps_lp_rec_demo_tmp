package hu.bme.mit.gamma.usecase.epas.uc;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.UCFaultInterface;

public interface UCStatechartInterface {
	
	UCFaultInterface.Required getHWFault();
	UCFaultInterface.Provided getFault();
	
	void reset();
	
	void runCycle();
	
}
