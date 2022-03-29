/** Generated by YAKINDU Statechart Tools code generator. */

#ifndef DEMUX1TO4_H_
#define DEMUX1TO4_H_

#include "../src/sc_types.h"

#ifdef __cplusplus
extern "C" { 
#endif 

/*! \file Header of the state machine 'DeMux1to4'.
*/

/*! Define number of states in the state enum */

#define DEMUX1TO4_STATE_COUNT 1

/*! Define dimension of the state configuration vector for orthogonal states. */
#define DEMUX1TO4_MAX_ORTHOGONAL_STATES 1


/*! Define indices of states in the StateConfVector */
#define SCVI_DEMUX1TO4_MAIN_REGION_STATEA 0

/*! Enumeration of all states */ 
typedef enum
{
	DeMux1to4_last_state,
	DeMux1to4_main_region_StateA
} DeMux1to4States;

/*! Type definition of the data structure for the DeMux1to4Internal interface scope. */
typedef struct
{
	sc_integer sel;
} DeMux1to4Internal;



/*! Type definition of the data structure for the DeMux1to4IfaceIn interface scope. */
typedef struct
{
	sc_boolean NewEvent_raised;
	sc_integer NewEvent_value;
} DeMux1to4IfaceIn;



/*! Type definition of the data structure for the DeMux1to4IfaceOut1 interface scope. */
typedef struct
{
	sc_boolean NewEvent_raised;
} DeMux1to4IfaceOut1;



/*! Type definition of the data structure for the DeMux1to4IfaceOut2 interface scope. */
typedef struct
{
	sc_boolean NewEvent_raised;
} DeMux1to4IfaceOut2;



/*! Type definition of the data structure for the DeMux1to4IfaceOut3 interface scope. */
typedef struct
{
	sc_boolean NewEvent_raised;
} DeMux1to4IfaceOut3;



/*! Type definition of the data structure for the DeMux1to4IfaceOut4 interface scope. */
typedef struct
{
	sc_boolean NewEvent_raised;
} DeMux1to4IfaceOut4;




/*! 
 * Type definition of the data structure for the DeMux1to4 state machine.
 * This data structure has to be allocated by the client code. 
 */
typedef struct
{
	DeMux1to4States stateConfVector[DEMUX1TO4_MAX_ORTHOGONAL_STATES];
	sc_ushort stateConfVectorPosition; 
	
	DeMux1to4Internal internal;
	DeMux1to4IfaceIn ifaceIn;
	DeMux1to4IfaceOut1 ifaceOut1;
	DeMux1to4IfaceOut2 ifaceOut2;
	DeMux1to4IfaceOut3 ifaceOut3;
	DeMux1to4IfaceOut4 ifaceOut4;
} DeMux1to4;



/*! Initializes the DeMux1to4 state machine data structures. Must be called before first usage.*/
extern void deMux1to4_init(DeMux1to4* handle);

/*! Activates the state machine */
extern void deMux1to4_enter(DeMux1to4* handle);

/*! Deactivates the state machine */
extern void deMux1to4_exit(DeMux1to4* handle);

/*! Performs a 'run to completion' step. */
extern void deMux1to4_runCycle(DeMux1to4* handle);


/*! Raises the in event 'NewEvent' that is defined in the interface scope 'In'. */ 
extern void deMux1to4IfaceIn_raise_newEvent(DeMux1to4* handle, sc_integer value);

/*! Checks if the out event 'NewEvent' that is defined in the interface scope 'Out1' has been raised. */ 
extern sc_boolean deMux1to4IfaceOut1_israised_newEvent(const DeMux1to4* handle);

/*! Checks if the out event 'NewEvent' that is defined in the interface scope 'Out2' has been raised. */ 
extern sc_boolean deMux1to4IfaceOut2_israised_newEvent(const DeMux1to4* handle);

/*! Checks if the out event 'NewEvent' that is defined in the interface scope 'Out3' has been raised. */ 
extern sc_boolean deMux1to4IfaceOut3_israised_newEvent(const DeMux1to4* handle);

/*! Checks if the out event 'NewEvent' that is defined in the interface scope 'Out4' has been raised. */ 
extern sc_boolean deMux1to4IfaceOut4_israised_newEvent(const DeMux1to4* handle);


/*!
 * Checks whether the state machine is active (until 2.4.1 this method was used for states).
 * A state machine is active if it was entered. It is inactive if it has not been entered at all or if it has been exited.
 */
extern sc_boolean deMux1to4_isActive(const DeMux1to4* handle);

/*!
 * Checks if all active states are final. 
 * If there are no active states then the state machine is considered being inactive. In this case this method returns false.
 */
extern sc_boolean deMux1to4_isFinal(const DeMux1to4* handle);

/*! Checks if the specified state is active (until 2.4.1 the used method for states was called isActive()). */
extern sc_boolean deMux1to4_isStateActive(const DeMux1to4* handle, DeMux1to4States state);


#ifdef __cplusplus
}
#endif 

#endif /* DEMUX1TO4_H_ */