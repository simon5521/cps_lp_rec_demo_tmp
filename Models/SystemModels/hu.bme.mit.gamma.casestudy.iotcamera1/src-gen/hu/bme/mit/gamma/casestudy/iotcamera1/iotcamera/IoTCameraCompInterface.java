package hu.bme.mit.gamma.casestudy.iotcamera1.iotcamera;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import hu.bme.mit.gamma.casestudy.iotcamera1.interfaces.StreamInterface;

public interface IoTCameraCompInterface {
	
	StreamInterface.Provided getRecognisedLP();
	StreamInterface.Provided getLPCroppedImg();
	StreamInterface.Provided getLPNumbers();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
