/** Generated by YAKINDU Statechart Tools code generator. */

#include "LB_Provider.h"
#include "../src/sc_types.h"

/*! \file Implementation of the state machine 'LB_Provider'
*/

#define SC_UNUSED(P) (void)(P)
/* prototypes of all internal functions */
static sc_boolean check_main_region__choice_0_tr0_tr0(const LB_Provider* handle);
static sc_boolean check_main_region__choice_1_tr0_tr0(const LB_Provider* handle);
static sc_boolean check_main_region__choice_2_tr0_tr0(const LB_Provider* handle);
static sc_boolean check_main_region__choice_3_tr0_tr0(const LB_Provider* handle);
static sc_boolean check_main_region__choice_3_tr1_tr1(const LB_Provider* handle);
static void effect_main_region__choice_0_tr0(LB_Provider* handle);
static void effect_main_region__choice_0_tr1(LB_Provider* handle);
static void effect_main_region__choice_1_tr0(LB_Provider* handle);
static void effect_main_region__choice_1_tr1(LB_Provider* handle);
static void effect_main_region__choice_2_tr0(LB_Provider* handle);
static void effect_main_region__choice_2_tr1(LB_Provider* handle);
static void effect_main_region__choice_3_tr0(LB_Provider* handle);
static void effect_main_region__choice_3_tr1(LB_Provider* handle);
static void effect_main_region__choice_3_tr2(LB_Provider* handle);
static void enseq_main_region_Loading_default(LB_Provider* handle);
static void enseq_main_region_Sending_default(LB_Provider* handle);
static void enseq_main_region_RequestSubsccriber_default(LB_Provider* handle);
static void enseq_main_region_default(LB_Provider* handle);
static void exseq_main_region_Loading(LB_Provider* handle);
static void exseq_main_region_Sending(LB_Provider* handle);
static void exseq_main_region_RequestSubsccriber(LB_Provider* handle);
static void exseq_main_region(LB_Provider* handle);
static void react_main_region__choice_0(LB_Provider* handle);
static void react_main_region__choice_1(LB_Provider* handle);
static void react_main_region__choice_2(LB_Provider* handle);
static void react_main_region__choice_3(LB_Provider* handle);
static void react_main_region__entry_Default(LB_Provider* handle);
static sc_boolean react(LB_Provider* handle);
static sc_boolean main_region_Loading_react(LB_Provider* handle, const sc_boolean try_transition);
static sc_boolean main_region_Sending_react(LB_Provider* handle, const sc_boolean try_transition);
static sc_boolean main_region_RequestSubsccriber_react(LB_Provider* handle, const sc_boolean try_transition);
static void clearInEvents(LB_Provider* handle);
static void clearOutEvents(LB_Provider* handle);


void lB_Provider_init(LB_Provider* handle)
{
	sc_integer i;
	
	for (i = 0; i < LB_PROVIDER_MAX_ORTHOGONAL_STATES; ++i)
	{
		handle->stateConfVector[i] = LB_Provider_last_state;
	}
	
	
	handle->stateConfVectorPosition = 0;
	
	clearInEvents(handle);
	clearOutEvents(handle);
	
	/* Default init sequence for statechart LB_Provider */
	handle->internal.queue_length = 30;
	handle->internal.package_number = 0;
	handle->internal.id = "img_provider_01";
	handle->internal.subscriber_id = "";
	handle->internal.min_load_num = 10;
	handle->internal.send_cntr = 0;
	handle->internal.send_cntr_max = 10;
}

void lB_Provider_enter(LB_Provider* handle)
{
	/* Default enter sequence for statechart LB_Provider */
	enseq_main_region_default(handle);
}

void lB_Provider_runCycle(LB_Provider* handle)
{
	clearOutEvents(handle);
	for (handle->stateConfVectorPosition = 0;
		handle->stateConfVectorPosition < LB_PROVIDER_MAX_ORTHOGONAL_STATES;
		handle->stateConfVectorPosition++)
		{
			
		switch (handle->stateConfVector[handle->stateConfVectorPosition])
		{
		case LB_Provider_main_region_Loading:
		{
			main_region_Loading_react(handle, bool_true);
			break;
		}
		case LB_Provider_main_region_Sending:
		{
			main_region_Sending_react(handle, bool_true);
			break;
		}
		case LB_Provider_main_region_RequestSubsccriber:
		{
			main_region_RequestSubsccriber_react(handle, bool_true);
			break;
		}
		default:
			break;
		}
	}
	
	clearInEvents(handle);
}

void lB_Provider_exit(LB_Provider* handle)
{
	/* Default exit sequence for statechart LB_Provider */
	exseq_main_region(handle);
}

sc_boolean lB_Provider_isActive(const LB_Provider* handle)
{
	sc_boolean result = bool_false;
	sc_integer i;
	
	for(i = 0; i < LB_PROVIDER_MAX_ORTHOGONAL_STATES; i++)
	{
		result = result || handle->stateConfVector[i] != LB_Provider_last_state;
	}
	
	return result;
}

/* 
 * Always returns 'false' since this state machine can never become final.
 */
sc_boolean lB_Provider_isFinal(const LB_Provider* handle)
{
	SC_UNUSED(handle);
	return bool_false;
}

sc_boolean lB_Provider_isStateActive(const LB_Provider* handle, LB_ProviderStates state)
{
	sc_boolean result = bool_false;
	switch (state)
	{
		case LB_Provider_main_region_Loading :
			result = (sc_boolean) (handle->stateConfVector[SCVI_LB_PROVIDER_MAIN_REGION_LOADING] == LB_Provider_main_region_Loading
			);
			break;
		case LB_Provider_main_region_Sending :
			result = (sc_boolean) (handle->stateConfVector[SCVI_LB_PROVIDER_MAIN_REGION_SENDING] == LB_Provider_main_region_Sending
			);
			break;
		case LB_Provider_main_region_RequestSubsccriber :
			result = (sc_boolean) (handle->stateConfVector[SCVI_LB_PROVIDER_MAIN_REGION_REQUESTSUBSCCRIBER] == LB_Provider_main_region_RequestSubsccriber
			);
			break;
		default:
			result = bool_false;
			break;
	}
	return result;
}

static void clearInEvents(LB_Provider* handle)
{
	handle->ifaceInStream.NewData_raised = bool_false;
	handle->ifaceOutStreamAck.NewData_raised = bool_false;
	handle->ifaceInComm.NewWorker_raised = bool_false;
}

static void clearOutEvents(LB_Provider* handle)
{
	handle->ifaceOutStream.NewData_raised = bool_false;
	handle->ifaceLogger.DataLoss_raised = bool_false;
	handle->ifaceOutComm.NewWorker_raised = bool_false;
}


sc_boolean lB_ProviderIfaceOutStream_israised_newData(const LB_Provider* handle)
{
	return handle->ifaceOutStream.NewData_raised;
}
sc_string lB_ProviderIfaceOutStream_get_newData_value(const LB_Provider* handle)
{
	return handle->ifaceOutStream.NewData_value;
}

void lB_ProviderIfaceInStream_raise_newData(LB_Provider* handle)
{
	handle->ifaceInStream.NewData_raised = bool_true;
}


void lB_ProviderIfaceOutStreamAck_raise_newData(LB_Provider* handle)
{
	handle->ifaceOutStreamAck.NewData_raised = bool_true;
}



sc_boolean lB_ProviderIfaceLogger_israised_dataLoss(const LB_Provider* handle)
{
	return handle->ifaceLogger.DataLoss_raised;
}


sc_boolean lB_ProviderIfaceOutComm_israised_newWorker(const LB_Provider* handle)
{
	return handle->ifaceOutComm.NewWorker_raised;
}
sc_string lB_ProviderIfaceOutComm_get_newWorker_value(const LB_Provider* handle)
{
	return handle->ifaceOutComm.NewWorker_value;
}

void lB_ProviderIfaceInComm_raise_newWorker(LB_Provider* handle, sc_string value)
{
	handle->ifaceInComm.NewWorker_value = value;
	handle->ifaceInComm.NewWorker_raised = bool_true;
}



/* implementations of all internal functions */

static sc_boolean check_main_region__choice_0_tr0_tr0(const LB_Provider* handle)
{
	return (handle->internal.package_number) == (handle->internal.min_load_num);
}

static sc_boolean check_main_region__choice_1_tr0_tr0(const LB_Provider* handle)
{
	return (handle->internal.queue_length) > (handle->internal.package_number);
}

static sc_boolean check_main_region__choice_2_tr0_tr0(const LB_Provider* handle)
{
	return (handle->internal.queue_length) > (handle->internal.package_number);
}

static sc_boolean check_main_region__choice_3_tr0_tr0(const LB_Provider* handle)
{
	return ((handle->internal.queue_length) > (0)) && ((handle->internal.send_cntr) == (handle->internal.send_cntr_max));
}

static sc_boolean check_main_region__choice_3_tr1_tr1(const LB_Provider* handle)
{
	return ((handle->internal.queue_length) > (0)) && ((handle->internal.send_cntr) < (handle->internal.send_cntr_max));
}

static void effect_main_region__choice_0_tr0(LB_Provider* handle)
{
	handle->ifaceOutComm.NewWorker_value = handle->internal.id;
	handle->ifaceOutComm.NewWorker_raised = bool_true;
	enseq_main_region_RequestSubsccriber_default(handle);
}

static void effect_main_region__choice_0_tr1(LB_Provider* handle)
{
	enseq_main_region_Loading_default(handle);
}

static void effect_main_region__choice_1_tr0(LB_Provider* handle)
{
	handle->internal.package_number = (handle->internal.package_number + 1);
	enseq_main_region_RequestSubsccriber_default(handle);
}

static void effect_main_region__choice_1_tr1(LB_Provider* handle)
{
	handle->ifaceLogger.DataLoss_raised = bool_true;
	enseq_main_region_RequestSubsccriber_default(handle);
}

static void effect_main_region__choice_2_tr0(LB_Provider* handle)
{
	handle->internal.package_number = (handle->internal.package_number + 1);
	enseq_main_region_Sending_default(handle);
}

static void effect_main_region__choice_2_tr1(LB_Provider* handle)
{
	handle->ifaceLogger.DataLoss_raised = bool_true;
	enseq_main_region_Sending_default(handle);
}

static void effect_main_region__choice_3_tr0(LB_Provider* handle)
{
	handle->internal.send_cntr = 0;
	handle->ifaceOutComm.NewWorker_value = handle->internal.id;
	handle->ifaceOutComm.NewWorker_raised = bool_true;
	enseq_main_region_RequestSubsccriber_default(handle);
}

static void effect_main_region__choice_3_tr1(LB_Provider* handle)
{
	handle->ifaceOutStream.NewData_value = handle->internal.id;
	handle->ifaceOutStream.NewData_raised = bool_true;
	handle->internal.send_cntr = (handle->internal.send_cntr + 1);
	enseq_main_region_Sending_default(handle);
}

static void effect_main_region__choice_3_tr2(LB_Provider* handle)
{
	enseq_main_region_Loading_default(handle);
}

/* 'default' enter sequence for state Loading */
static void enseq_main_region_Loading_default(LB_Provider* handle)
{
	/* 'default' enter sequence for state Loading */
	handle->stateConfVector[0] = LB_Provider_main_region_Loading;
	handle->stateConfVectorPosition = 0;
	SC_UNUSED(handle);
}

/* 'default' enter sequence for state Sending */
static void enseq_main_region_Sending_default(LB_Provider* handle)
{
	/* 'default' enter sequence for state Sending */
	handle->stateConfVector[0] = LB_Provider_main_region_Sending;
	handle->stateConfVectorPosition = 0;
	SC_UNUSED(handle);
}

/* 'default' enter sequence for state RequestSubsccriber */
static void enseq_main_region_RequestSubsccriber_default(LB_Provider* handle)
{
	/* 'default' enter sequence for state RequestSubsccriber */
	handle->stateConfVector[0] = LB_Provider_main_region_RequestSubsccriber;
	handle->stateConfVectorPosition = 0;
	SC_UNUSED(handle);
}

/* 'default' enter sequence for region main region */
static void enseq_main_region_default(LB_Provider* handle)
{
	/* 'default' enter sequence for region main region */
	react_main_region__entry_Default(handle);
}

/* Default exit sequence for state Loading */
static void exseq_main_region_Loading(LB_Provider* handle)
{
	/* Default exit sequence for state Loading */
	handle->stateConfVector[0] = LB_Provider_last_state;
	handle->stateConfVectorPosition = 0;
	SC_UNUSED(handle);
}

/* Default exit sequence for state Sending */
static void exseq_main_region_Sending(LB_Provider* handle)
{
	/* Default exit sequence for state Sending */
	handle->stateConfVector[0] = LB_Provider_last_state;
	handle->stateConfVectorPosition = 0;
	SC_UNUSED(handle);
}

/* Default exit sequence for state RequestSubsccriber */
static void exseq_main_region_RequestSubsccriber(LB_Provider* handle)
{
	/* Default exit sequence for state RequestSubsccriber */
	handle->stateConfVector[0] = LB_Provider_last_state;
	handle->stateConfVectorPosition = 0;
	SC_UNUSED(handle);
}

/* Default exit sequence for region main region */
static void exseq_main_region(LB_Provider* handle)
{
	/* Default exit sequence for region main region */
	/* Handle exit of all possible states (of LB_Provider.main_region) at position 0... */
	switch(handle->stateConfVector[ 0 ])
	{
		case LB_Provider_main_region_Loading :
		{
			exseq_main_region_Loading(handle);
			break;
		}
		case LB_Provider_main_region_Sending :
		{
			exseq_main_region_Sending(handle);
			break;
		}
		case LB_Provider_main_region_RequestSubsccriber :
		{
			exseq_main_region_RequestSubsccriber(handle);
			break;
		}
		default: break;
	}
}

/* The reactions of state null. */
static void react_main_region__choice_0(LB_Provider* handle)
{
	/* The reactions of state null. */
	if (check_main_region__choice_0_tr0_tr0(handle) == bool_true)
	{ 
		effect_main_region__choice_0_tr0(handle);
	}  else
	{
		effect_main_region__choice_0_tr1(handle);
	}
}

/* The reactions of state null. */
static void react_main_region__choice_1(LB_Provider* handle)
{
	/* The reactions of state null. */
	if (check_main_region__choice_1_tr0_tr0(handle) == bool_true)
	{ 
		effect_main_region__choice_1_tr0(handle);
	}  else
	{
		effect_main_region__choice_1_tr1(handle);
	}
}

/* The reactions of state null. */
static void react_main_region__choice_2(LB_Provider* handle)
{
	/* The reactions of state null. */
	if (check_main_region__choice_2_tr0_tr0(handle) == bool_true)
	{ 
		effect_main_region__choice_2_tr0(handle);
	}  else
	{
		effect_main_region__choice_2_tr1(handle);
	}
}

/* The reactions of state null. */
static void react_main_region__choice_3(LB_Provider* handle)
{
	/* The reactions of state null. */
	if (check_main_region__choice_3_tr0_tr0(handle) == bool_true)
	{ 
		effect_main_region__choice_3_tr0(handle);
	}  else
	{
		if (check_main_region__choice_3_tr1_tr1(handle) == bool_true)
		{ 
			effect_main_region__choice_3_tr1(handle);
		}  else
		{
			effect_main_region__choice_3_tr2(handle);
		}
	}
}

/* Default react sequence for initial entry  */
static void react_main_region__entry_Default(LB_Provider* handle)
{
	/* Default react sequence for initial entry  */
	enseq_main_region_Loading_default(handle);
}

static sc_boolean react(LB_Provider* handle) {
	SC_UNUSED(handle);
	/* State machine reactions. */
	return bool_false;
}

static sc_boolean main_region_Loading_react(LB_Provider* handle, const sc_boolean try_transition) {
	/* The reactions of state Loading. */
	sc_boolean did_transition = try_transition;
	if (try_transition == bool_true)
	{ 
		if ((react(handle)) == (bool_false))
		{ 
			if (handle->ifaceInStream.NewData_raised == bool_true)
			{ 
				exseq_main_region_Loading(handle);
				handle->internal.package_number = (handle->internal.package_number + 1);
				react_main_region__choice_0(handle);
			}  else
			{
				did_transition = bool_false;
			}
		} 
	} 
	return did_transition;
}

static sc_boolean main_region_Sending_react(LB_Provider* handle, const sc_boolean try_transition) {
	/* The reactions of state Sending. */
	sc_boolean did_transition = try_transition;
	if (try_transition == bool_true)
	{ 
		if ((react(handle)) == (bool_false))
		{ 
			if (handle->ifaceInStream.NewData_raised == bool_true)
			{ 
				exseq_main_region_Sending(handle);
				react_main_region__choice_2(handle);
			}  else
			{
				if (handle->ifaceOutStreamAck.NewData_raised == bool_true)
				{ 
					exseq_main_region_Sending(handle);
					handle->internal.package_number = (handle->internal.package_number - 1);
					react_main_region__choice_3(handle);
				}  else
				{
					did_transition = bool_false;
				}
			}
		} 
	} 
	return did_transition;
}

static sc_boolean main_region_RequestSubsccriber_react(LB_Provider* handle, const sc_boolean try_transition) {
	/* The reactions of state RequestSubsccriber. */
	sc_boolean did_transition = try_transition;
	if (try_transition == bool_true)
	{ 
		if ((react(handle)) == (bool_false))
		{ 
			if (handle->ifaceInComm.NewWorker_raised == bool_true)
			{ 
				exseq_main_region_RequestSubsccriber(handle);
				handle->internal.subscriber_id = handle->ifaceInComm.NewWorker_value;
				handle->ifaceOutStream.NewData_value = (handle->internal.subscriber_id);
				handle->ifaceOutStream.NewData_raised = bool_true;
				handle->internal.send_cntr = (handle->internal.send_cntr + 1);
				enseq_main_region_Sending_default(handle);
			}  else
			{
				if (handle->ifaceInStream.NewData_raised == bool_true)
				{ 
					exseq_main_region_RequestSubsccriber(handle);
					react_main_region__choice_1(handle);
				}  else
				{
					did_transition = bool_false;
				}
			}
		} 
	} 
	return did_transition;
}


