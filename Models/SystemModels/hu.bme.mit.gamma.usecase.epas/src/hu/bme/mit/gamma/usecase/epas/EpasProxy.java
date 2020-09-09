package hu.bme.mit.gamma.usecase.epas;


//import hu.bme.mit.gamma.usecase.epas.epas.Epas;
import hu.bme.mit.gamma.usecase.epas.evaluation.*;
import hu.bme.mit.gamma.usecase.epas.interfaces.EvalInterface;
import hu.bme.mit.gamma.usecase.epas.interfaces.MonitorInterface;

public class EpasProxy {
	/*
	private Epas epas; 
	private Integer simcount=1;
	private Evaluator evaluator;
	
	public EpasProxy(Epas epas) {
		this.epas=epas;
		evaluator=new Evaluator();
		epas.reset();
		epas.getState().registerListener(evaluator);
	}
	
	
	public void reset() {
		epas.reset();
		evaluator.reset();
		System.out.println("new simulation number: "+simcount.toString());
		simcount++;
	}
	
	public void raiseEvent(String eventID){
		//System.out.println("call");
		
		//epas.getACTRL().getS1HW().raiseDet();
		
		switch (eventID) {

		case "ahw_s1det":
			epas.getS1AFault().raiseDet();;
			break;
		case "ahw_s1latent":
			epas.getS1AFault().raiseLatent();
			break;
		case "ahw_s2det":
			epas.getS1AFault().raiseDet();;
			break;
		case "ahw_s2latent":
			epas.getS1AFault().raiseLatent();
			break;
		case "ahw_s3det":
			epas.getS1AFault().raiseDet();;
			break;
		case "ahw_s3latent":
			epas.getS1AFault().raiseLatent();
			break;
			
			
		case "bhw_s1det":
			epas.getS1AFault().raiseDet();;
			break;
		case "bhw_s1latent":
			epas.getS1AFault().raiseLatent();
			break;
		case "bhw_s2det":
			epas.getS1AFault().raiseDet();;
			break;
		case "bhw_s2latent":
			epas.getS1AFault().raiseLatent();
			break;
		case "bhw_s3det":
			epas.getS1AFault().raiseDet();;
			break;
		case "bhw_s3latent":
			epas.getS1AFault().raiseLatent();
			break;
			

		case "ahw_uCdet":
			epas.getUCAFault().raiseShutdown();;
			break;
		case "bhw_uCdet":
			epas.getUCBFault().raiseShutdown();;
			break;
	

		default:
			System.out.println("unrecognised fault: "+eventID);
			break;
		}
		epas.runFullCycle();
	}
	
	public String getState() {
		return evaluator.getState();
	}
	
	public class EpasState{
		
		private final String AState;
		private final String BState;
		
		public EpasState(String aState,String bState) {
			AState=aState;
			BState=bState;
		}

		public String getAState() {
			return AState;
		}

		public String getBState() {
			return BState;
		}
	}
	
	private class Evaluator implements EvalInterface.Listener.Provided{


		private String State="ok";
		
		public void reset() {
			State="ok";
		}
		
		@Override
		public void raiseSLoA() {
			System.out.println("Loss of assist!");
			State="loa";
		}


		public String getState() {
			return State;
		}

		@Override
		public void raiseSS() {
			System.out.println("Self Steering!");
			State="ss";
		}
		
	}
	
	
	private class MonitorListener implements MonitorInterface.Listener.Provided {
		
		private String State="normal";
		
		public void reset() {
			State="normal";
		}
		
		@Override
		public void raiseWarning() {
			//System.out.println("warning has raised");
			State="warning";
		}

		@Override
		public void raiseLoa() {
			//System.out.println("off has raised");
			State="off";
		}
		@Override
		public void raiseSelfsteering() {
			// TODO Auto-generated method stub
			
		}

		public String getState() {
			return State;
		}


		
	}
*/
}
