// py4j/examples/ListenerApplication.java
package hu.bme.mit.gamma.usecase.epas;

import py4j.GatewayServer;

import java.util.ArrayList;
import java.util.List;

import hu.bme.mit.gamma.usecase.epas.epasenv.EpasEnv1;
import hu.bme.mit.gamma.usecase.epas.interfaces.UCFaultInterface;

public class ListenerApplication {

	EpasEnv1 epas=new EpasEnv1();
	
    List<UCFaultInterface.Listener.Provided> listeners = new ArrayList<UCFaultInterface.Listener.Provided>();

    public void registerListener(UCFaultInterface.Listener.Provided listener) {
        listeners.add(listener);
        epas.getUC1env().getUcsct().getFault().registerListener(listener);
    }
    
    public EpasEnv1 getEpas() {
    	return epas;
    }

    public void notifyAllListeners() {
        //for (UCFaultInterface.Listener.Provided listener: listeners) {
        //    listener.raiseShutdown();
        //}
    	epas.getUC1env().getUcsct().getHWFault().raiseShutdown();
    	epas.runCycle();
        System.out.println("Ok");
    }

    @Override
    public String toString() {
        return "<ListenerApplication> instance";
    }

    public static void main(String[] args) {
        ListenerApplication application = new ListenerApplication();
        GatewayServer server = new GatewayServer(application);
        server.start(true);
    }
}