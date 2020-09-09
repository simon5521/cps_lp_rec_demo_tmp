package hu.bme.mit.gamma.usecase.epas.interfaces;

import hu.bme.mit.gamma.usecase.epas.*;
import java.util.List;

public interface SensorFaultInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedDet();
		public boolean isRaisedLatent();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseDet();
			void raiseLatent();
		}
		
		interface Required  {
		}
		
	}
}
