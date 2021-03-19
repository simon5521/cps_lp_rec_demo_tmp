

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from RawImage.idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

#ifndef RawImagePlugin_1296340066_h
#define RawImagePlugin_1296340066_h

#include "RawImage.hpp"

struct RTICdrStream;

#ifndef pres_typePlugin_h
#include "pres/pres_typePlugin.h"
#endif

#if (defined(RTI_WIN32) || defined (RTI_WINCE) || defined(RTI_INTIME)) && defined(NDDS_USER_DLL_EXPORT)
/* If the code is building on Windows, start exporting symbols.
*/
#undef NDDSUSERDllExport
#define NDDSUSERDllExport __declspec(dllexport)
#endif

#define RAWRGBImagePlugin_get_sample PRESTypePluginDefaultEndpointData_getSample

#define RAWRGBImagePlugin_get_buffer PRESTypePluginDefaultEndpointData_getBuffer 
#define RAWRGBImagePlugin_return_buffer PRESTypePluginDefaultEndpointData_returnBuffer

#define RAWRGBImagePlugin_create_sample PRESTypePluginDefaultEndpointData_createSample 
#define RAWRGBImagePlugin_destroy_sample PRESTypePluginDefaultEndpointData_deleteSample 

/* --------------------------------------------------------------------------------------
Support functions:
* -------------------------------------------------------------------------------------- */

NDDSUSERDllExport extern RAWRGBImage*
RAWRGBImagePluginSupport_create_data_w_params(
    const struct DDS_TypeAllocationParams_t * alloc_params);

NDDSUSERDllExport extern RAWRGBImage*
RAWRGBImagePluginSupport_create_data_ex(RTIBool allocate_pointers);

NDDSUSERDllExport extern RAWRGBImage*
RAWRGBImagePluginSupport_create_data(void);

NDDSUSERDllExport extern RTIBool 
RAWRGBImagePluginSupport_copy_data(
    RAWRGBImage *out,
    const RAWRGBImage *in);

NDDSUSERDllExport extern void 
RAWRGBImagePluginSupport_destroy_data_w_params(
    RAWRGBImage *sample,
    const struct DDS_TypeDeallocationParams_t * dealloc_params);

NDDSUSERDllExport extern void 
RAWRGBImagePluginSupport_destroy_data_ex(
    RAWRGBImage *sample,RTIBool deallocate_pointers);

NDDSUSERDllExport extern void 
RAWRGBImagePluginSupport_destroy_data(
    RAWRGBImage *sample);

NDDSUSERDllExport extern void 
RAWRGBImagePluginSupport_print_data(
    const RAWRGBImage *sample,
    const char *desc,
    unsigned int indent);

/* ----------------------------------------------------------------------------
Callback functions:
* ---------------------------------------------------------------------------- */

NDDSUSERDllExport extern PRESTypePluginParticipantData 
RAWRGBImagePlugin_on_participant_attached(
    void *registration_data, 
    const struct PRESTypePluginParticipantInfo *participant_info,
    RTIBool top_level_registration, 
    void *container_plugin_context,
    RTICdrTypeCode *typeCode);

NDDSUSERDllExport extern void 
RAWRGBImagePlugin_on_participant_detached(
    PRESTypePluginParticipantData participant_data);

NDDSUSERDllExport extern PRESTypePluginEndpointData 
RAWRGBImagePlugin_on_endpoint_attached(
    PRESTypePluginParticipantData participant_data,
    const struct PRESTypePluginEndpointInfo *endpoint_info,
    RTIBool top_level_registration, 
    void *container_plugin_context);

NDDSUSERDllExport extern void 
RAWRGBImagePlugin_on_endpoint_detached(
    PRESTypePluginEndpointData endpoint_data);

NDDSUSERDllExport extern void    
RAWRGBImagePlugin_return_sample(
    PRESTypePluginEndpointData endpoint_data,
    RAWRGBImage *sample,
    void *handle);    

NDDSUSERDllExport extern RTIBool 
RAWRGBImagePlugin_copy_sample(
    PRESTypePluginEndpointData endpoint_data,
    RAWRGBImage *out,
    const RAWRGBImage *in);

/* ----------------------------------------------------------------------------
(De)Serialize functions:
* ------------------------------------------------------------------------- */

NDDSUSERDllExport extern RTIBool
RAWRGBImagePlugin_serialize_to_cdr_buffer(
    char * buffer,
    unsigned int * length,
    const RAWRGBImage *sample,
    ::dds::core::policy::DataRepresentationId representation
    = ::dds::core::policy::DataRepresentation::xcdr()); 

NDDSUSERDllExport extern RTIBool 
RAWRGBImagePlugin_deserialize(
    PRESTypePluginEndpointData endpoint_data,
    RAWRGBImage **sample, 
    RTIBool * drop_sample,
    struct RTICdrStream *stream,
    RTIBool deserialize_encapsulation,
    RTIBool deserialize_sample, 
    void *endpoint_plugin_qos);

NDDSUSERDllExport extern RTIBool
RAWRGBImagePlugin_deserialize_from_cdr_buffer(
    RAWRGBImage *sample,
    const char * buffer,
    unsigned int length);    

NDDSUSERDllExport extern unsigned int 
RAWRGBImagePlugin_get_serialized_sample_max_size(
    PRESTypePluginEndpointData endpoint_data,
    RTIBool include_encapsulation,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

/* --------------------------------------------------------------------------------------
Key Management functions:
* -------------------------------------------------------------------------------------- */
NDDSUSERDllExport extern PRESTypePluginKeyKind 
RAWRGBImagePlugin_get_key_kind(void);

NDDSUSERDllExport extern unsigned int 
RAWRGBImagePlugin_get_serialized_key_max_size(
    PRESTypePluginEndpointData endpoint_data,
    RTIBool include_encapsulation,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

NDDSUSERDllExport extern unsigned int 
RAWRGBImagePlugin_get_serialized_key_max_size_for_keyhash(
    PRESTypePluginEndpointData endpoint_data,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

NDDSUSERDllExport extern RTIBool 
RAWRGBImagePlugin_deserialize_key(
    PRESTypePluginEndpointData endpoint_data,
    RAWRGBImage ** sample,
    RTIBool * drop_sample,
    struct RTICdrStream *stream,
    RTIBool deserialize_encapsulation,
    RTIBool deserialize_key,
    void *endpoint_plugin_qos);

/* Plugin Functions */
NDDSUSERDllExport extern struct PRESTypePlugin*
RAWRGBImagePlugin_new(void);

NDDSUSERDllExport extern void
RAWRGBImagePlugin_delete(struct PRESTypePlugin *);

#if (defined(RTI_WIN32) || defined (RTI_WINCE) || defined(RTI_INTIME)) && defined(NDDS_USER_DLL_EXPORT)
/* If the code is building on Windows, stop exporting symbols.
*/
#undef NDDSUSERDllExport
#define NDDSUSERDllExport
#endif

#endif /* RawImagePlugin_1296340066_h */

