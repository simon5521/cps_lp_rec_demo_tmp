package hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StatusInterface;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StreamInterface;

public interface LPRecQueueManagerStatechartInterface {
	
	StatusInterface.Provided getStatus();
	StreamInterface.Required getProcessedImg();
	StreamInterface.Required getPreProcessedImg();
	StreamInterface.Provided getPreProcessedImgOut();
	StreamInterface.Provided getNetworkStream();
	
	void reset();
	
	void runCycle();
	
}
