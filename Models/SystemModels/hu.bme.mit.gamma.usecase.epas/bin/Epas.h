    #ifndef EPAS
    #define EPAS
    
    #include <functional>
    #include <vector>
    #include "../OneThreadedTimer.h"
    
    #include "./EpasInterface.h"
#include "../sensor/SensorStatechart.h"
#include "../uc/UCStatechart.h"
#include "../mainctrl/MainctrlStatechart.h"
#include "../evaluation/EvaluationStatechart.h"

	//SynchronousCompositeComponentCode
	class Epas : public EpasInterface {
		// Component instances
		SensorStatechart S1A;
		SensorStatechart S2A;
		SensorStatechart S3A;
		SensorStatechart S1B;
		SensorStatechart S2B;
		SensorStatechart S3B;
		UCStatechart UCA;
		UCStatechart UCB;
		MainctrlStatechart ACTRL;
		MainctrlStatechart BCTRL;
		EvaluationStatechart Ev;

	  public:
		// Inner classes representing Ports
		class S1AFault : public SensorFaultInterface::Required {
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			
			// Class for the setting of the boolean fields (events)
			class S1AFaultUtil : public SensorFaultInterface::Listener::Required {
			  S1AFault& parent;
			  public:
			  	S1AFaultUtil(S1AFault& parent_) : parent(parent_) {  }
			};
		  public:
			S1AFault(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				S1AFaultUtil util(*this);
				parent.S1A.getHWFault().registerListener(util);
			}
			
			void raiseLatent() override {
				parent.S1A.getHWFault().raiseLatent();
			}
			
			void raiseDet() override {
				parent.S1A.getHWFault().raiseDet();
			}
			
			
			
			void registerListener(SensorFaultInterface::Listener::Required& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
			}
			
		};
		
		S1AFault& getS1AFault() override {
			return s1AFault;
		}
		
		class S2AFault : public SensorFaultInterface::Required {
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			
			// Class for the setting of the boolean fields (events)
			class S2AFaultUtil : public SensorFaultInterface::Listener::Required {
			  S2AFault& parent;
			  public:
			  	S2AFaultUtil(S2AFault& parent_) : parent(parent_) {  }
			};
		  public:
			S2AFault(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				S2AFaultUtil util(*this);
				parent.S2A.getHWFault().registerListener(util);
			}
			
			void raiseLatent() override {
				parent.S2A.getHWFault().raiseLatent();
			}
			
			void raiseDet() override {
				parent.S2A.getHWFault().raiseDet();
			}
			
			
			
			void registerListener(SensorFaultInterface::Listener::Required& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
			}
			
		};
		
		S2AFault& getS2AFault() override {
			return s2AFault;
		}
		
		class S3AFault : public SensorFaultInterface::Required {
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			
			// Class for the setting of the boolean fields (events)
			class S3AFaultUtil : public SensorFaultInterface::Listener::Required {
			  S3AFault& parent;
			  public:
			  	S3AFaultUtil(S3AFault& parent_) : parent(parent_) {  }
			};
		  public:
			S3AFault(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				S3AFaultUtil util(*this);
				parent.S3A.getHWFault().registerListener(util);
			}
			
			void raiseLatent() override {
				parent.S3A.getHWFault().raiseLatent();
			}
			
			void raiseDet() override {
				parent.S3A.getHWFault().raiseDet();
			}
			
			
			
			void registerListener(SensorFaultInterface::Listener::Required& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
			}
			
		};
		
		S3AFault& getS3AFault() override {
			return s3AFault;
		}
		
		class S1BFault : public SensorFaultInterface::Required {
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			
			// Class for the setting of the boolean fields (events)
			class S1BFaultUtil : public SensorFaultInterface::Listener::Required {
			  S1BFault& parent;
			  public:
			  	S1BFaultUtil(S1BFault& parent_) : parent(parent_) {  }
			};
		  public:
			S1BFault(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				S1BFaultUtil util(*this);
				parent.S1B.getHWFault().registerListener(util);
			}
			
			void raiseLatent() override {
				parent.S1B.getHWFault().raiseLatent();
			}
			
			void raiseDet() override {
				parent.S1B.getHWFault().raiseDet();
			}
			
			
			
			void registerListener(SensorFaultInterface::Listener::Required& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
			}
			
		};
		
		S1BFault& getS1BFault() override {
			return s1BFault;
		}
		
		class S2BFault : public SensorFaultInterface::Required {
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			
			// Class for the setting of the boolean fields (events)
			class S2BFaultUtil : public SensorFaultInterface::Listener::Required {
			  S2BFault& parent;
			  public:
			  	S2BFaultUtil(S2BFault& parent_) : parent(parent_) {  }
			};
		  public:
			S2BFault(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				S2BFaultUtil util(*this);
				parent.S2B.getHWFault().registerListener(util);
			}
			
			void raiseLatent() override {
				parent.S2B.getHWFault().raiseLatent();
			}
			
			void raiseDet() override {
				parent.S2B.getHWFault().raiseDet();
			}
			
			
			
			void registerListener(SensorFaultInterface::Listener::Required& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
			}
			
		};
		
		S2BFault& getS2BFault() override {
			return s2BFault;
		}
		
		class S3BFault : public SensorFaultInterface::Required {
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			
			// Class for the setting of the boolean fields (events)
			class S3BFaultUtil : public SensorFaultInterface::Listener::Required {
			  S3BFault& parent;
			  public:
			  	S3BFaultUtil(S3BFault& parent_) : parent(parent_) {  }
			};
		  public:
			S3BFault(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				S3BFaultUtil util(*this);
				parent.S3B.getHWFault().registerListener(util);
			}
			
			void raiseLatent() override {
				parent.S3B.getHWFault().raiseLatent();
			}
			
			void raiseDet() override {
				parent.S3B.getHWFault().raiseDet();
			}
			
			
			
			void registerListener(SensorFaultInterface::Listener::Required& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<SensorFaultInterface::Listener::Required>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
			}
			
		};
		
		S3BFault& getS3BFault() override {
			return s3BFault;
		}
		
		class UCAFault : public UCFaultInterface::Required {
			std::vector<std::reference_wrapper<UCFaultInterface::Listener::Required>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			
			// Class for the setting of the boolean fields (events)
			class UCAFaultUtil : public UCFaultInterface::Listener::Required {
			  UCAFault& parent;
			  public:
			  	UCAFaultUtil(UCAFault& parent_) : parent(parent_) {  }
			};
		  public:
			UCAFault(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				UCAFaultUtil util(*this);
				parent.UCA.getHWFault().registerListener(util);
			}
			
			void raiseShutdown() override {
				parent.UCA.getHWFault().raiseShutdown();
			}
			
			
			
			void registerListener(UCFaultInterface::Listener::Required& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<UCFaultInterface::Listener::Required>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
			}
			
		};
		
		UCAFault& getUCAFault() override {
			return uCAFault;
		}
		
		class UCBFault : public UCFaultInterface::Required {
			std::vector<std::reference_wrapper<UCFaultInterface::Listener::Required>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			
			// Class for the setting of the boolean fields (events)
			class UCBFaultUtil : public UCFaultInterface::Listener::Required {
			  UCBFault& parent;
			  public:
			  	UCBFaultUtil(UCBFault& parent_) : parent(parent_) {  }
			};
		  public:
			UCBFault(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				UCBFaultUtil util(*this);
				parent.UCB.getHWFault().registerListener(util);
			}
			
			void raiseShutdown() override {
				parent.UCB.getHWFault().raiseShutdown();
			}
			
			
			
			void registerListener(UCFaultInterface::Listener::Required& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<UCFaultInterface::Listener::Required>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
			}
			
		};
		
		UCBFault& getUCBFault() override {
			return uCBFault;
		}
		
		class State : public EvalInterface::Provided {
			std::vector<std::reference_wrapper<EvalInterface::Listener::Provided>> listeners;
			Epas& parent;
			
			//Cascade components need their raised events saved (multiple schedule of a component in a single turn)
			bool mIsRaisedSLoA = false;
			bool mIsRaisedSS = false;
			
			// Class for the setting of the boolean fields (events)
			class StateUtil : public EvalInterface::Listener::Provided {
			  State& parent;
			  public:
			  	StateUtil(State& parent_) : parent(parent_) {  }
				
				void raiseSLoA() override {
					parent.mIsRaisedSLoA = true;
				}
				
				
				void raiseSS() override {
					parent.mIsRaisedSS = true;
				}
			};
		  public:
			State(Epas& parent_) : parent(parent_) {
				// Registering the listener to the contained component
				StateUtil util(*this);
				parent.Ev.getEval().registerListener(util);
			}
			
			
			bool isRaisedSLoA() override {
				return mIsRaisedSLoA;
			}
			
			bool isRaisedSS() override {
				return mIsRaisedSS;
			}
			
			
			void registerListener(EvalInterface::Listener::Provided& listener) override {
				listeners.push_back(listener);
			}
			
			std::vector<std::reference_wrapper<EvalInterface::Listener::Provided>> getRegisteredListeners() override {
				return listeners;
			}
			
			/** Resetting the boolean event flags to false. */
			void clear() {
				mIsRaisedSLoA = false;
				mIsRaisedSS = false;
			}
			
			/** Notifying the registered listeners. */
			void notifyListeners() {
				if (mIsRaisedSLoA) {
					for (auto listener : listeners) {
						listener.get().raiseSLoA();
					}
				}
				if (mIsRaisedSS) {
					for (auto listener : listeners) {
						listener.get().raiseSS();
					}
				}
			}
			
		};
		
		State& getState() override {
			return state;
		}

  private:
	// Port instances
	S1AFault s1AFault;
	S2AFault s2AFault;
	S3AFault s3AFault;
	S1BFault s1BFault;
	S2BFault s2BFault;
	S3BFault s3BFault;
	UCAFault uCAFault;
	UCBFault uCBFault;
	State state;

	/** Creates the channel mappings and enters the wrapped statemachines. */
	void init();

	/** Clears the the boolean flags of all out-events in each contained port. */
	void clearPorts();
	
	/** Notifies all registered listeners in each contained port. */
	void notifyListeners();
  public:
	Epas();
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	
	void reset() override;
  
  	/** Needed for the right event notification after initialization, as event notification from contained components
	 * does not happen automatically (see the port implementations and runComponent method). */
	void initListenerChain();
	
	
	/** Returns whether all event queues of the contained component instances are empty. 
	Should be used only be the container (composite system) class. */
	bool isEventQueueEmpty();
	/** Initiates cycle runs until all event queues of component instances are empty. */
	
	void runFullCycle() override;
	
	/** Changes event queues and initiates a cycle run.
		This should be the execution point from an asynchronous component. */
	
	void runCycle() override;
	
	/** Initiates a cycle run without changing the event queues.
	 * Should be used only be the container (composite system) class. */
	void runComponent();

	
	/**  Getter for component instances, e.g., enabling to check their states. */
	SensorStatechart& getS1A();
	
	SensorStatechart& getS2A();
	
	SensorStatechart& getS3A();
	
	SensorStatechart& getS1B();
	
	SensorStatechart& getS2B();
	
	SensorStatechart& getS3B();
	
	UCStatechart& getUCA();
	
	UCStatechart& getUCB();
	
	MainctrlStatechart& getACTRL();
	
	MainctrlStatechart& getBCTRL();
	
	EvaluationStatechart& getEv();
	
};
#endif
