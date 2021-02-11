package hu.bme.mit.gamma.casestudy.camea.basictraffic;

import hu.bme.mit.gamma.casestudy.camea.*;
import hu.bme.mit.gamma.casestudy.camea.interfaces.TrafficInterface;
import hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface;

public interface BasicTrafficInterface {
	
	TrafficInterface.Required getInPort();
	StreamInterface.Provided getOutPort();
	
	void reset();
	
	void runCycle();
	
}
