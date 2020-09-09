package hu.bme.mit.gamma.casestudy.iotcamera1.lpdetqueuemanager;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StreamInterface;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.TrafficInterface;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StatusInterface;

public interface LPDetQueueManagerStatechartInterface {
	
	StreamInterface.Provided getImg();
	StreamInterface.Required getPreProcessedImg();
	TrafficInterface.Required getTraffic();
	StatusInterface.Provided getStatus();
	
	void reset();
	
	void runCycle();
	
}
