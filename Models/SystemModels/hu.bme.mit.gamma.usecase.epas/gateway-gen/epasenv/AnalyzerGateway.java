package epasenv;

import hu.bme.mit.gamma.usecase.epas.epasenv.EpasEnv1;
import hu.bme.mit.gamma.usecase.epas.interfaces.EvalInterface;
import py4j.GatewayServer;

public class AnalyzerGateway{
	
		private static DetModelEntryPoint entryPoint;
		
		public AnalyzerGateway() {
			entryPoint=new DetModelEntryPoint();
		}
		
		public DetModelEntryPoint getEntryPoint() {
			return entryPoint;
		}
		
		public static void main(/*String[] args*/) {
			GatewayServer gatewayServer=new GatewayServer(new AnalyzerGateway());
			gatewayServer.start();
			System.out.println("Gateway has started!");
		}
		
		
		
		public class DetModelEntryPoint  {
			private EpasEnv1 detModel=new EpasEnv1();
			private Monitor monitor=new Monitor();
			
			public DetModelEntryPoint(){
				detModel.getFout().registerListener(monitor);
			}
			
			public void reset(){
				monitor.reset();
				detModel.reset();
			}
			
			public String getState(){
				return monitor.state;
			}
			
			public int getFreq(){
				return monitor.freq;
			}
			
			public EpasEnv1 getDetModel(){
				return detModel;
			}
			
			
			private class Monitor implements EvalInterface.Listener.Provided {
				
				public String state="run";
				
				public int freq=0;

						
				public void reset(){
					state="stop";
					freq=0;
				}
										
				@Override
				public void raiseSLoA(){
				}
				
				@Override
				public void raiseSS(){
					state="stop";
					freq++;
				}
				
			}
			
		}
}



