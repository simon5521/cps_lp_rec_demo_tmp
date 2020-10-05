package hu.bme.mit.gamma.casestudy.camea.system;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface;

public interface LPCameraSystemInterface {
	
	StreamInterface.Provided getDetNetLoss();
	StreamInterface.Provided getRecLoss();
	StreamInterface.Provided getLPNum();
	StreamInterface.Provided getDetLoss();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
