

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from RGBImage.idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

#ifndef RGBImagePlugin_5036790_h
#define RGBImagePlugin_5036790_h

#include "RGBImage.hpp"

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

#define PositionPlugin_get_sample PRESTypePluginDefaultEndpointData_getSample

#define PositionPlugin_get_buffer PRESTypePluginDefaultEndpointData_getBuffer 
#define PositionPlugin_return_buffer PRESTypePluginDefaultEndpointData_returnBuffer

#define PositionPlugin_create_sample PRESTypePluginDefaultEndpointData_createSample 
#define PositionPlugin_destroy_sample PRESTypePluginDefaultEndpointData_deleteSample 

/* --------------------------------------------------------------------------------------
Support functions:
* -------------------------------------------------------------------------------------- */

NDDSUSERDllExport extern Position*
PositionPluginSupport_create_data_w_params(
    const struct DDS_TypeAllocationParams_t * alloc_params);

NDDSUSERDllExport extern Position*
PositionPluginSupport_create_data_ex(RTIBool allocate_pointers);

NDDSUSERDllExport extern Position*
PositionPluginSupport_create_data(void);

NDDSUSERDllExport extern RTIBool 
PositionPluginSupport_copy_data(
    Position *out,
    const Position *in);

NDDSUSERDllExport extern void 
PositionPluginSupport_destroy_data_w_params(
    Position *sample,
    const struct DDS_TypeDeallocationParams_t * dealloc_params);

NDDSUSERDllExport extern void 
PositionPluginSupport_destroy_data_ex(
    Position *sample,RTIBool deallocate_pointers);

NDDSUSERDllExport extern void 
PositionPluginSupport_destroy_data(
    Position *sample);

NDDSUSERDllExport extern void 
PositionPluginSupport_print_data(
    const Position *sample,
    const char *desc,
    unsigned int indent);

/* ----------------------------------------------------------------------------
Callback functions:
* ---------------------------------------------------------------------------- */

NDDSUSERDllExport extern PRESTypePluginParticipantData 
PositionPlugin_on_participant_attached(
    void *registration_data, 
    const struct PRESTypePluginParticipantInfo *participant_info,
    RTIBool top_level_registration, 
    void *container_plugin_context,
    RTICdrTypeCode *typeCode);

NDDSUSERDllExport extern void 
PositionPlugin_on_participant_detached(
    PRESTypePluginParticipantData participant_data);

NDDSUSERDllExport extern PRESTypePluginEndpointData 
PositionPlugin_on_endpoint_attached(
    PRESTypePluginParticipantData participant_data,
    const struct PRESTypePluginEndpointInfo *endpoint_info,
    RTIBool top_level_registration, 
    void *container_plugin_context);

NDDSUSERDllExport extern void 
PositionPlugin_on_endpoint_detached(
    PRESTypePluginEndpointData endpoint_data);

NDDSUSERDllExport extern void    
PositionPlugin_return_sample(
    PRESTypePluginEndpointData endpoint_data,
    Position *sample,
    void *handle);    

NDDSUSERDllExport extern RTIBool 
PositionPlugin_copy_sample(
    PRESTypePluginEndpointData endpoint_data,
    Position *out,
    const Position *in);

/* ----------------------------------------------------------------------------
(De)Serialize functions:
* ------------------------------------------------------------------------- */

NDDSUSERDllExport extern RTIBool
PositionPlugin_serialize_to_cdr_buffer(
    char * buffer,
    unsigned int * length,
    const Position *sample,
    ::dds::core::policy::DataRepresentationId representation
    = ::dds::core::policy::DataRepresentation::xcdr()); 

NDDSUSERDllExport extern RTIBool 
PositionPlugin_deserialize(
    PRESTypePluginEndpointData endpoint_data,
    Position **sample, 
    RTIBool * drop_sample,
    struct RTICdrStream *stream,
    RTIBool deserialize_encapsulation,
    RTIBool deserialize_sample, 
    void *endpoint_plugin_qos);

NDDSUSERDllExport extern RTIBool
PositionPlugin_deserialize_from_cdr_buffer(
    Position *sample,
    const char * buffer,
    unsigned int length);    

NDDSUSERDllExport extern unsigned int 
PositionPlugin_get_serialized_sample_max_size(
    PRESTypePluginEndpointData endpoint_data,
    RTIBool include_encapsulation,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

/* --------------------------------------------------------------------------------------
Key Management functions:
* -------------------------------------------------------------------------------------- */
NDDSUSERDllExport extern PRESTypePluginKeyKind 
PositionPlugin_get_key_kind(void);

NDDSUSERDllExport extern unsigned int 
PositionPlugin_get_serialized_key_max_size(
    PRESTypePluginEndpointData endpoint_data,
    RTIBool include_encapsulation,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

NDDSUSERDllExport extern unsigned int 
PositionPlugin_get_serialized_key_max_size_for_keyhash(
    PRESTypePluginEndpointData endpoint_data,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

NDDSUSERDllExport extern RTIBool 
PositionPlugin_deserialize_key(
    PRESTypePluginEndpointData endpoint_data,
    Position ** sample,
    RTIBool * drop_sample,
    struct RTICdrStream *stream,
    RTIBool deserialize_encapsulation,
    RTIBool deserialize_key,
    void *endpoint_plugin_qos);

/* Plugin Functions */
NDDSUSERDllExport extern struct PRESTypePlugin*
PositionPlugin_new(void);

NDDSUSERDllExport extern void
PositionPlugin_delete(struct PRESTypePlugin *);

#define RGBImagePlugin_get_sample PRESTypePluginDefaultEndpointData_getSample

#define RGBImagePlugin_get_buffer PRESTypePluginDefaultEndpointData_getBuffer 
#define RGBImagePlugin_return_buffer PRESTypePluginDefaultEndpointData_returnBuffer

#define RGBImagePlugin_create_sample PRESTypePluginDefaultEndpointData_createSample 
#define RGBImagePlugin_destroy_sample PRESTypePluginDefaultEndpointData_deleteSample 

/* --------------------------------------------------------------------------------------
Support functions:
* -------------------------------------------------------------------------------------- */

NDDSUSERDllExport extern RGBImage*
RGBImagePluginSupport_create_data_w_params(
    const struct DDS_TypeAllocationParams_t * alloc_params);

NDDSUSERDllExport extern RGBImage*
RGBImagePluginSupport_create_data_ex(RTIBool allocate_pointers);

NDDSUSERDllExport extern RGBImage*
RGBImagePluginSupport_create_data(void);

NDDSUSERDllExport extern RTIBool 
RGBImagePluginSupport_copy_data(
    RGBImage *out,
    const RGBImage *in);

NDDSUSERDllExport extern void 
RGBImagePluginSupport_destroy_data_w_params(
    RGBImage *sample,
    const struct DDS_TypeDeallocationParams_t * dealloc_params);

NDDSUSERDllExport extern void 
RGBImagePluginSupport_destroy_data_ex(
    RGBImage *sample,RTIBool deallocate_pointers);

NDDSUSERDllExport extern void 
RGBImagePluginSupport_destroy_data(
    RGBImage *sample);

NDDSUSERDllExport extern void 
RGBImagePluginSupport_print_data(
    const RGBImage *sample,
    const char *desc,
    unsigned int indent);

/* ----------------------------------------------------------------------------
Callback functions:
* ---------------------------------------------------------------------------- */

NDDSUSERDllExport extern PRESTypePluginParticipantData 
RGBImagePlugin_on_participant_attached(
    void *registration_data, 
    const struct PRESTypePluginParticipantInfo *participant_info,
    RTIBool top_level_registration, 
    void *container_plugin_context,
    RTICdrTypeCode *typeCode);

NDDSUSERDllExport extern void 
RGBImagePlugin_on_participant_detached(
    PRESTypePluginParticipantData participant_data);

NDDSUSERDllExport extern PRESTypePluginEndpointData 
RGBImagePlugin_on_endpoint_attached(
    PRESTypePluginParticipantData participant_data,
    const struct PRESTypePluginEndpointInfo *endpoint_info,
    RTIBool top_level_registration, 
    void *container_plugin_context);

NDDSUSERDllExport extern void 
RGBImagePlugin_on_endpoint_detached(
    PRESTypePluginEndpointData endpoint_data);

NDDSUSERDllExport extern void    
RGBImagePlugin_return_sample(
    PRESTypePluginEndpointData endpoint_data,
    RGBImage *sample,
    void *handle);    

NDDSUSERDllExport extern RTIBool 
RGBImagePlugin_copy_sample(
    PRESTypePluginEndpointData endpoint_data,
    RGBImage *out,
    const RGBImage *in);

/* ----------------------------------------------------------------------------
(De)Serialize functions:
* ------------------------------------------------------------------------- */

NDDSUSERDllExport extern RTIBool
RGBImagePlugin_serialize_to_cdr_buffer(
    char * buffer,
    unsigned int * length,
    const RGBImage *sample,
    ::dds::core::policy::DataRepresentationId representation
    = ::dds::core::policy::DataRepresentation::xcdr()); 

NDDSUSERDllExport extern RTIBool 
RGBImagePlugin_deserialize(
    PRESTypePluginEndpointData endpoint_data,
    RGBImage **sample, 
    RTIBool * drop_sample,
    struct RTICdrStream *stream,
    RTIBool deserialize_encapsulation,
    RTIBool deserialize_sample, 
    void *endpoint_plugin_qos);

NDDSUSERDllExport extern RTIBool
RGBImagePlugin_deserialize_from_cdr_buffer(
    RGBImage *sample,
    const char * buffer,
    unsigned int length);    

NDDSUSERDllExport extern unsigned int 
RGBImagePlugin_get_serialized_sample_max_size(
    PRESTypePluginEndpointData endpoint_data,
    RTIBool include_encapsulation,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

/* --------------------------------------------------------------------------------------
Key Management functions:
* -------------------------------------------------------------------------------------- */
NDDSUSERDllExport extern PRESTypePluginKeyKind 
RGBImagePlugin_get_key_kind(void);

NDDSUSERDllExport extern unsigned int 
RGBImagePlugin_get_serialized_key_max_size(
    PRESTypePluginEndpointData endpoint_data,
    RTIBool include_encapsulation,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

NDDSUSERDllExport extern unsigned int 
RGBImagePlugin_get_serialized_key_max_size_for_keyhash(
    PRESTypePluginEndpointData endpoint_data,
    RTIEncapsulationId encapsulation_id,
    unsigned int current_alignment);

NDDSUSERDllExport extern RTIBool 
RGBImagePlugin_deserialize_key(
    PRESTypePluginEndpointData endpoint_data,
    RGBImage ** sample,
    RTIBool * drop_sample,
    struct RTICdrStream *stream,
    RTIBool deserialize_encapsulation,
    RTIBool deserialize_key,
    void *endpoint_plugin_qos);

/* Plugin Functions */
NDDSUSERDllExport extern struct PRESTypePlugin*
RGBImagePlugin_new(void);

NDDSUSERDllExport extern void
RGBImagePlugin_delete(struct PRESTypePlugin *);

#if (defined(RTI_WIN32) || defined (RTI_WINCE) || defined(RTI_INTIME)) && defined(NDDS_USER_DLL_EXPORT)
/* If the code is building on Windows, stop exporting symbols.
*/
#undef NDDSUSERDllExport
#define NDDSUSERDllExport
#endif

#endif /* RGBImagePlugin_5036790_h */

