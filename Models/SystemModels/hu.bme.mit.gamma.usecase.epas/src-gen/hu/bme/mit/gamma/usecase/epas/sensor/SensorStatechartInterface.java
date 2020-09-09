package hu.bme.mit.gamma.usecase.epas.sensor;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.SensorFaultInterface;

public interface SensorStatechartInterface {
	
	SensorFaultInterface.Provided getSensorFault();
	SensorFaultInterface.Required getHWFault();
	
	void reset();
	
	void runCycle();
	
}
