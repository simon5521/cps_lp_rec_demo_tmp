package hu.bme.mit.gamma.usecase.epas.epas1;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.SensorFaultInterface;
import hu.bme.mit.gamma.usecase.epas.interfaces.EvalInterface;
import hu.bme.mit.gamma.usecase.epas.interfaces.UCFaultInterface;

public interface EpasInterface {
	
	SensorFaultInterface.Required getS3AFault();
	EvalInterface.Provided getState();
	SensorFaultInterface.Required getS3BFault();
	UCFaultInterface.Required getUCAFault();
	SensorFaultInterface.Required getS2AFault();
	SensorFaultInterface.Required getS2BFault();
	SensorFaultInterface.Required getS1AFault();
	SensorFaultInterface.Required getS1BFault();
	UCFaultInterface.Required getUCBFault();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
