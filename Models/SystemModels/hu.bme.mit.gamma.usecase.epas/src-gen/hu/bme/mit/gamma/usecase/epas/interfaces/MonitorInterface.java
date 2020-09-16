package hu.bme.mit.gamma.usecase.epas.interfaces;

import hu.bme.mit.gamma.usecase.epas.*;
import java.util.List;

public interface MonitorInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedWarning();
		public boolean isRaisedLoa();
		public boolean isRaisedSelfsteering();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseWarning();
			void raiseLoa();
			void raiseSelfsteering();
		}
		
		interface Required  {
		}
		
	}
}
