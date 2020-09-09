package hu.bme.mit.gamma.usecase.epas.mainctrl;

import hu.bme.mit.gamma.usecase.epas.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.SensorFaultInterface;
import hu.bme.mit.gamma.usecase.epas.interfaces.MonitorInterface;
import hu.bme.mit.gamma.usecase.epas.interfaces.UCFaultInterface;

public interface MainctrlStatechartInterface {
	
	SensorFaultInterface.Required getS1HW();
	SensorFaultInterface.Required getS2HW();
	MonitorInterface.Provided getMonitor();
	SensorFaultInterface.Required getS3HW();
	UCFaultInterface.Required getUCHW();
	
	void reset();
	
	void runCycle();
	
}
