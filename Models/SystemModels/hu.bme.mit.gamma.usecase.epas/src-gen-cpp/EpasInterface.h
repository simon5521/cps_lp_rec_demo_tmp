//package hu.bme.mit.gamma.usecase.epas.epas1;

#ifndef EPASINTERFACE
#define EPASINTERFACE

#include "../interfaces/UCFaultInterface.h"
#include "../interfaces/EvalInterface.h"
#include "../interfaces/SensorFaultInterface.h"

//ComponentInterfaceGenerator
class EpasInterface {
  public:
	virtual UCFaultInterface::Required& getUCAFault() = 0;
	virtual EvalInterface::Provided& getState() = 0;
	virtual SensorFaultInterface::Required& getS2BFault() = 0;
	virtual SensorFaultInterface::Required& getS3AFault() = 0;
	virtual UCFaultInterface::Required& getUCBFault() = 0;
	virtual SensorFaultInterface::Required& getS1AFault() = 0;
	virtual SensorFaultInterface::Required& getS2AFault() = 0;
	virtual SensorFaultInterface::Required& getS3BFault() = 0;
	virtual SensorFaultInterface::Required& getS1BFault() = 0;
	
	virtual void reset() = 0;
	
	virtual void runCycle() = 0;
	virtual void runFullCycle() = 0;

};

#endif
