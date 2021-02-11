package hu.bme.mit.gamma.casestudy.camea.queuemanager;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface;

public interface QueueManagerInterface {
	
	StreamInterface.Provided getOutStream();
	StreamInterface.Provided getDataLoss();
	StreamInterface.Provided getToHW();
	StreamInterface.Required getInStream();
	StreamInterface.Required getFromHW();
	
	void reset();
	
	void runCycle();
	
}
