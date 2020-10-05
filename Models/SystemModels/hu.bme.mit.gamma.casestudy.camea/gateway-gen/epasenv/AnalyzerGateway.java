package epasenv;

import hu.bme.mit.gamma.casestudy.camea.system.LPCameraSystem;
import hu.bme.mit.gamma.casestudy.camea.interfaces.StreamInterface;
import py4j.GatewayServer;

public class AnalyzerGateway{
	
		private static DetModelEntryPoint entryPoint;
		
		public AnalyzerGateway() {
			entryPoint=new DetModelEntryPoint();
		}
		
		public DetModelEntryPoint getEntryPoint() {
			return entryPoint;
		}
		
		public static void main(String[] args) {
			GatewayServer gatewayServer=new GatewayServer(new AnalyzerGateway());
			gatewayServer.start();
			System.out.println("Gateway has started!");
		}
		
		
		
		public class DetModelEntryPoint  {
			private LPCameraSystem detModel=new LPCameraSystem();
			private Monitor monitor=new Monitor();
			
			public DetModelEntryPoint(){
				detModel.getRecLoss().registerListener(monitor);
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
			
			public LPCameraSystem getDetModel(){
				return detModel;
			}
			
			
			private class Monitor implements StreamInterface.Listener.Provided {
				
				public String state="run";
				
				public int freq=0;

						
				public void reset(){
					state="stop";
					freq=0;
				}
										
				@Override
				public void raiseNewData(){
					state="stop";
					freq++;
				}
				
			}
			
		}
}



