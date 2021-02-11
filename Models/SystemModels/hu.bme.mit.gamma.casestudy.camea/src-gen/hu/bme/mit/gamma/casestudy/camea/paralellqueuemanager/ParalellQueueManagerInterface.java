package hu.bme.mit.gamma.casestudy.camea.paralellqueuemanager;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface;

public interface ParalellQueueManagerInterface {
	
	StreamInterface.Provided getOutStream();
	StreamInterface.Provided getDataLoss();
	StreamInterface.Required getInStream();
	StreamInterface.Required getFromHW();
	StreamInterface.Provided getToHW();
	
	void reset();
	
	void runCycle();
	
}
