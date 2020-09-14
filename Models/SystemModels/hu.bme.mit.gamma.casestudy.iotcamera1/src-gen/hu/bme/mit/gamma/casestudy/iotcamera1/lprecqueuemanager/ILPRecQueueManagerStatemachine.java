/** Generated by YAKINDU Statechart Tools code generator. */
package hu.bme.mit.gamma.casestudy.iotcamera1.lprecqueuemanager;

import hu.bme.mit.gamma.casestudy.iotcamera1.IStatemachine;
import java.util.List;

public interface ILPRecQueueManagerStatemachine extends IStatemachine {
	public interface SCIPreProcessedImg {
	
		public void raiseNewData();
		
	}
	
	public SCIPreProcessedImg getSCIPreProcessedImg();
	
	public interface SCIPreProcessedImgOut {
	
		public boolean isRaisedNewData();
		
	public List<SCIPreProcessedImgOutListener> getListeners();
	}
	
	public interface SCIPreProcessedImgOutListener {
	
		public void onNewDataRaised();
		}
	
	public SCIPreProcessedImgOut getSCIPreProcessedImgOut();
	
	public interface SCIProcessedImg {
	
		public void raiseNewData();
		
	}
	
	public SCIProcessedImg getSCIProcessedImg();
	
	public interface SCIStatus {
	
		public boolean isRaisedFull();
		
		public boolean isRaisedFree();
		
	public List<SCIStatusListener> getListeners();
	}
	
	public interface SCIStatusListener {
	
		public void onFullRaised();
		public void onFreeRaised();
		}
	
	public SCIStatus getSCIStatus();
	
	public interface SCINetworkStream {
	
		public boolean isRaisedNewData();
		
	public List<SCINetworkStreamListener> getListeners();
	}
	
	public interface SCINetworkStreamListener {
	
		public void onNewDataRaised();
		}
	
	public SCINetworkStream getSCINetworkStream();
	
}