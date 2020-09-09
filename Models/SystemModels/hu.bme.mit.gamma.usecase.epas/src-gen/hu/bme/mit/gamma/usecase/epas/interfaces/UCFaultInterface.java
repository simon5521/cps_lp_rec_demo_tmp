package hu.bme.mit.gamma.usecase.epas.interfaces;

import hu.bme.mit.gamma.usecase.epas.*;
import java.util.List;

public interface UCFaultInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedShutdown();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseShutdown();
		}
		
		interface Required  {
		}
		
	}
}
