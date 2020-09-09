package hu.bme.mit.gamma.casestudy.iotcamera1.interfaces;

import hu.bme.mit.gamma.casestudy.iotcamera1.*;
import java.util.List;

public interface TrafficInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedNewCar();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseNewCar();
		}
		
		interface Required  {
		}
		
	}
}
