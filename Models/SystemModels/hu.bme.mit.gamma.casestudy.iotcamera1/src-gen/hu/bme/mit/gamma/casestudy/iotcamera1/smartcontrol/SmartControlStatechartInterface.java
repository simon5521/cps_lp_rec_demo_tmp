package hu.bme.mit.gamma.casestudy.iotcamera1.smartcontrol;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StreamInterface;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StatusInterface;

public interface SmartControlStatechartInterface {
	
	StreamInterface.Required getInPicStream();
	StreamInterface.Provided getLocalStream();
	StatusInterface.Required getStatus();
	StreamInterface.Provided getNetworkStream();
	
	void reset();
	
	void runCycle();
	
}
