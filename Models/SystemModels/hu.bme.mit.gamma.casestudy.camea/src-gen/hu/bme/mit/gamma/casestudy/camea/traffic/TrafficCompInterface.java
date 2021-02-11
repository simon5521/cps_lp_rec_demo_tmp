package hu.bme.mit.gamma.casestudy.camea.traffic;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface;

public interface TrafficCompInterface {
	
	StreamInterface.Provided getCars();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
