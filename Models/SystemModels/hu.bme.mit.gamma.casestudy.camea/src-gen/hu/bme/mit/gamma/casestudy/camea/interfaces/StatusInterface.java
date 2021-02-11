package hu.bme.mit.gamma.casestudy.camea.interfaces;

import hu.bme.mit.gamma.casestudy.camea.*;
import java.util.List;

public interface StatusInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedFree();
		public boolean isRaisedFull();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseFree();
			void raiseFull();
		}
		
		interface Required  {
		}
		
	}
}
