package hu.bme.mit.gamma.casestudy.camea.systemwithtraffic;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface;

public interface LPCameraSystemInterface {
	
	StreamInterface.Provided getRecLoss();
	StreamInterface.Provided getLPNum();
	StreamInterface.Provided getDetLoss();
	StreamInterface.Provided getDetNetLoss();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
