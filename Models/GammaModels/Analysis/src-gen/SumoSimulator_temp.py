#these are template classes for external simulation
#behavior is not specified only the interfaces
#TODO: add simulation behavior
		#TODO: move the class into another file, this file might regenerate regularly on build
#TODO: remove 'ExternSimulationTemplateClass' from class name
class Sumo_Town4_scenario_1ExternSimulationTemplateClass():
	#interface 
	class InPortEventStream():
		def __init__(self,portname,portcall):
			portcall.registerListener(self)
			self.portcall=portcall
			self.name=portname
			self.events=dict()
	
	def getEvents(self,globalevents):
		globalevents.update(self.events)
		self.events.clear()
		
	#definition of the interface functions
	def raiseNewEvent(self,):
		self.events[portname+"->NewEvent",()]
	
	class Java:
		implements = ["traffic.interfaces.EventStreamInterface$Listener$Provided"]
		
	#functions calling output event
	#Do not modify these functions!
	def callCamera1NewData():
		callEvent=lambda:self.detmodel.getCamera1().raiseNewData()
		self.events.append(Event(self,actualTime,callEvent))
	def callCamera2NewData():
		callEvent=lambda:self.detmodel.getCamera2().raiseNewData()
		self.events.append(Event(self,actualTime,callEvent))
	def callCamera3NewData():
		callEvent=lambda:self.detmodel.getCamera3().raiseNewData()
		self.events.append(Event(self,actualTime,callEvent))
	def callCamera4NewData():
		callEvent=lambda:self.detmodel.getCamera4().raiseNewData()
		self.events.append(Event(self,actualTime,callEvent))
	def callCamera5NewData():
		callEvent=lambda:self.detmodel.getCamera5().raiseNewData()
		self.events.append(Event(self,actualTime,callEvent))
	def callCamera6NewData():
		callEvent=lambda:self.detmodel.getCamera6().raiseNewData()
		self.events.append(Event(self,actualTime,callEvent))
		
	def generateEvents(self):
		self.events=[]
	
	
	def getEvents(self):
		eventcopy=self.events.copy()
		self.events.clear()
		return eventcopy
	
	def __init__(self,detmodel):
		self.detmodel=detmodel
		#init input ports
		self.inports=[
		]
		self.inevents=dict()
		self.outevents=list()
		#TODO: init simulation
		#...
		pass
					
	def collectInEvents(self):
		self.inevents.clear()
		for port in inports:
			port.getEvents(self.inevents)
		
	def updateInputs(self):
		#collect the incoming events
		self.collectInEvents()
		#input check template
		#check Cars
		if "Cars_NewEvent" in self.inevents:
			parameters=self.inevents["Cars_NewEvent"]
			#TODO: handle the event
			#...
			pass
		
					
	def updateSimulation(self):
		#TODO: update the simulation
		#...
		pass
		
	def release(self):
		#TODO: release all allocated resources
		#...
		pass
		
	
