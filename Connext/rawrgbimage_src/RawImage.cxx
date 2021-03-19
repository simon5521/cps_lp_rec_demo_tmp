

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from RawImage.idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

#include <iosfwd>
#include <iomanip>

#include "rti/topic/cdr/Serialization.hpp"

#include "RawImage.hpp"
#include "RawImagePlugin.hpp"

#include <rti/util/ostream_operators.hpp>

// ---- RAWRGBImage: 

RAWRGBImage::RAWRGBImage() :
    m_size_ (0) ,
    m_source_ ("") ,
    m_destination_ ("") ,
    m_debug_ (0) ,
    m_validdata_ (0)  {
        ::rti::core::fill_array< uint8_t >( m_pixels_, 0);
}   

RAWRGBImage::RAWRGBImage (
    int32_t size,
    const std::string& source,
    const std::string& destination,
    const ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& pixels,
    bool debug,
    bool validdata)
    :
        m_size_( size ),
        m_source_( source ),
        m_destination_( destination ),
        m_pixels_( pixels ),
        m_debug_( debug ),
        m_validdata_( validdata ) {
}

#ifdef RTI_CXX11_RVALUE_REFERENCES
#ifdef RTI_CXX11_NO_IMPLICIT_MOVE_OPERATIONS
RAWRGBImage::RAWRGBImage(RAWRGBImage&& other_) OMG_NOEXCEPT  :m_size_ (std::move(other_.m_size_))
,
m_source_ (std::move(other_.m_source_))
,
m_destination_ (std::move(other_.m_destination_))
,
m_pixels_ (std::move(other_.m_pixels_))
,
m_debug_ (std::move(other_.m_debug_))
,
m_validdata_ (std::move(other_.m_validdata_))
{
} 

RAWRGBImage& RAWRGBImage::operator=(RAWRGBImage&&  other_) OMG_NOEXCEPT {
    RAWRGBImage tmp(std::move(other_));
    swap(tmp); 
    return *this;
}
#endif
#endif   

void RAWRGBImage::swap(RAWRGBImage& other_)  OMG_NOEXCEPT 
{
    using std::swap;
    swap(m_size_, other_.m_size_);
    swap(m_source_, other_.m_source_);
    swap(m_destination_, other_.m_destination_);
    swap(m_pixels_, other_.m_pixels_);
    swap(m_debug_, other_.m_debug_);
    swap(m_validdata_, other_.m_validdata_);
}  

bool RAWRGBImage::operator == (const RAWRGBImage& other_) const {
    if (m_size_ != other_.m_size_) {
        return false;
    }
    if (m_source_ != other_.m_source_) {
        return false;
    }
    if (m_destination_ != other_.m_destination_) {
        return false;
    }
    if (m_pixels_ != other_.m_pixels_) {
        return false;
    }
    if (m_debug_ != other_.m_debug_) {
        return false;
    }
    if (m_validdata_ != other_.m_validdata_) {
        return false;
    }
    return true;
}
bool RAWRGBImage::operator != (const RAWRGBImage& other_) const {
    return !this->operator ==(other_);
}

std::ostream& operator << (std::ostream& o,const RAWRGBImage& sample)
{
    ::rti::util::StreamFlagSaver flag_saver (o);
    o <<"[";
    o << "size: " << sample.size()<<", ";
    o << "source: " << sample.source()<<", ";
    o << "destination: " << sample.destination()<<", ";
    o << "pixels: " << sample.pixels()<<", ";
    o << "debug: " << sample.debug()<<", ";
    o << "validdata: " << sample.validdata() ;
    o <<"]";
    return o;
}

// --- Type traits: -------------------------------------------------

namespace rti { 
    namespace topic {

        #ifndef NDDS_STANDALONE_TYPE

        template<>
        struct native_type_code< RAWRGBImage > {
            static DDS_TypeCode * get()
            {
                using namespace ::rti::topic::interpreter;

                static RTIBool is_initialized = RTI_FALSE;

                static DDS_TypeCode RAWRGBImage_g_tc_source_string;
                static DDS_TypeCode RAWRGBImage_g_tc_destination_string;
                static DDS_TypeCode RAWRGBImage_g_tc_pixels_array =DDS_INITIALIZE_ARRAY_TYPECODE(1,(MAX_IMAGE_SIZE), NULL,NULL);

                static DDS_TypeCode_Member RAWRGBImage_g_tc_members[6]=
                {

                    {
                        (char *)"size",/* Member name */
                        {
                            0,/* Representation ID */
                            DDS_BOOLEAN_FALSE,/* Is a pointer? */
                            -1, /* Bitfield bits */
                            NULL/* Member type code is assigned later */
                        },
                        0, /* Ignored */
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        RTI_CDR_REQUIRED_MEMBER, /* Is a key? */
                        DDS_PUBLIC_MEMBER,/* Member visibility */
                        1,
                        NULL, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER
                    }, 
                    {
                        (char *)"source",/* Member name */
                        {
                            1,/* Representation ID */
                            DDS_BOOLEAN_FALSE,/* Is a pointer? */
                            -1, /* Bitfield bits */
                            NULL/* Member type code is assigned later */
                        },
                        0, /* Ignored */
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        RTI_CDR_REQUIRED_MEMBER, /* Is a key? */
                        DDS_PUBLIC_MEMBER,/* Member visibility */
                        1,
                        NULL, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER
                    }, 
                    {
                        (char *)"destination",/* Member name */
                        {
                            2,/* Representation ID */
                            DDS_BOOLEAN_FALSE,/* Is a pointer? */
                            -1, /* Bitfield bits */
                            NULL/* Member type code is assigned later */
                        },
                        0, /* Ignored */
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        RTI_CDR_REQUIRED_MEMBER, /* Is a key? */
                        DDS_PUBLIC_MEMBER,/* Member visibility */
                        1,
                        NULL, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER
                    }, 
                    {
                        (char *)"pixels",/* Member name */
                        {
                            3,/* Representation ID */
                            DDS_BOOLEAN_FALSE,/* Is a pointer? */
                            -1, /* Bitfield bits */
                            NULL/* Member type code is assigned later */
                        },
                        0, /* Ignored */
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        RTI_CDR_REQUIRED_MEMBER, /* Is a key? */
                        DDS_PUBLIC_MEMBER,/* Member visibility */
                        1,
                        NULL, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER
                    }, 
                    {
                        (char *)"debug",/* Member name */
                        {
                            4,/* Representation ID */
                            DDS_BOOLEAN_FALSE,/* Is a pointer? */
                            -1, /* Bitfield bits */
                            NULL/* Member type code is assigned later */
                        },
                        0, /* Ignored */
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        RTI_CDR_REQUIRED_MEMBER, /* Is a key? */
                        DDS_PUBLIC_MEMBER,/* Member visibility */
                        1,
                        NULL, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER
                    }, 
                    {
                        (char *)"validdata",/* Member name */
                        {
                            5,/* Representation ID */
                            DDS_BOOLEAN_FALSE,/* Is a pointer? */
                            -1, /* Bitfield bits */
                            NULL/* Member type code is assigned later */
                        },
                        0, /* Ignored */
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        RTI_CDR_REQUIRED_MEMBER, /* Is a key? */
                        DDS_PUBLIC_MEMBER,/* Member visibility */
                        1,
                        NULL, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER
                    }
                };

                static DDS_TypeCode RAWRGBImage_g_tc =
                {{
                        DDS_TK_STRUCT, /* Kind */
                        DDS_BOOLEAN_FALSE, /* Ignored */
                        -1, /*Ignored*/
                        (char *)"RAWRGBImage", /* Name */
                        NULL, /* Ignored */      
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        6, /* Number of members */
                        RAWRGBImage_g_tc_members, /* Members */
                        DDS_VM_NONE, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER,
                        DDS_BOOLEAN_TRUE, /* _isCopyable */
                        NULL, /* _sampleAccessInfo: assigned later */
                        NULL /* _typePlugin: assigned later */
                    }}; /* Type code for RAWRGBImage*/

                if (is_initialized) {
                    return &RAWRGBImage_g_tc;
                }

                RAWRGBImage_g_tc_source_string = initialize_string_typecode((128));
                RAWRGBImage_g_tc_destination_string = initialize_string_typecode((128));

                RAWRGBImage_g_tc._data._annotations._allowedDataRepresentationMask = 5;

                RAWRGBImage_g_tc_pixels_array._data._typeCode =(RTICdrTypeCode *)&DDS_g_tc_octet;
                RAWRGBImage_g_tc_members[0]._representation._typeCode = (RTICdrTypeCode *)&DDS_g_tc_long;
                RAWRGBImage_g_tc_members[1]._representation._typeCode = (RTICdrTypeCode *)&RAWRGBImage_g_tc_source_string;
                RAWRGBImage_g_tc_members[2]._representation._typeCode = (RTICdrTypeCode *)&RAWRGBImage_g_tc_destination_string;
                RAWRGBImage_g_tc_members[3]._representation._typeCode = (RTICdrTypeCode *)& RAWRGBImage_g_tc_pixels_array;
                RAWRGBImage_g_tc_members[4]._representation._typeCode = (RTICdrTypeCode *)&::rti::topic::interpreter::initialize_bool_typecode();
                RAWRGBImage_g_tc_members[5]._representation._typeCode = (RTICdrTypeCode *)&::rti::topic::interpreter::initialize_bool_typecode();

                /* Initialize the values for member annotations. */
                RAWRGBImage_g_tc_members[0]._annotations._defaultValue._d = RTI_XCDR_TK_LONG;
                RAWRGBImage_g_tc_members[0]._annotations._defaultValue._u.long_value = 0;
                RAWRGBImage_g_tc_members[0]._annotations._minValue._d = RTI_XCDR_TK_LONG;
                RAWRGBImage_g_tc_members[0]._annotations._minValue._u.long_value = RTIXCdrLong_MIN;
                RAWRGBImage_g_tc_members[0]._annotations._maxValue._d = RTI_XCDR_TK_LONG;
                RAWRGBImage_g_tc_members[0]._annotations._maxValue._u.long_value = RTIXCdrLong_MAX;

                RAWRGBImage_g_tc_members[1]._annotations._defaultValue._d = RTI_XCDR_TK_STRING;
                RAWRGBImage_g_tc_members[1]._annotations._defaultValue._u.string_value = (DDS_Char *) "";

                RAWRGBImage_g_tc_members[2]._annotations._defaultValue._d = RTI_XCDR_TK_STRING;
                RAWRGBImage_g_tc_members[2]._annotations._defaultValue._u.string_value = (DDS_Char *) "";

                RAWRGBImage_g_tc_members[4]._annotations._defaultValue._d = RTI_XCDR_TK_BOOLEAN;
                RAWRGBImage_g_tc_members[4]._annotations._defaultValue._u.boolean_value = 0;

                RAWRGBImage_g_tc_members[5]._annotations._defaultValue._d = RTI_XCDR_TK_BOOLEAN;
                RAWRGBImage_g_tc_members[5]._annotations._defaultValue._u.boolean_value = 0;

                RAWRGBImage_g_tc._data._sampleAccessInfo = sample_access_info();
                RAWRGBImage_g_tc._data._typePlugin = type_plugin_info();    

                is_initialized = RTI_TRUE;

                return &RAWRGBImage_g_tc;
            }

            static RTIXCdrSampleAccessInfo * sample_access_info()
            {
                static RTIBool is_initialized = RTI_FALSE;

                RAWRGBImage *sample;

                static RTIXCdrMemberAccessInfo RAWRGBImage_g_memberAccessInfos[6] =
                {RTIXCdrMemberAccessInfo_INITIALIZER};

                static RTIXCdrSampleAccessInfo RAWRGBImage_g_sampleAccessInfo = 
                RTIXCdrSampleAccessInfo_INITIALIZER;

                if (is_initialized) {
                    return (RTIXCdrSampleAccessInfo*) &RAWRGBImage_g_sampleAccessInfo;
                }

                RTIXCdrHeap_allocateStruct(
                    &sample, 
                    RAWRGBImage);
                if (sample == NULL) {
                    return NULL;
                }

                RAWRGBImage_g_memberAccessInfos[0].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->size() - (char *)sample);

                RAWRGBImage_g_memberAccessInfos[1].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->source() - (char *)sample);

                RAWRGBImage_g_memberAccessInfos[2].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->destination() - (char *)sample);

                RAWRGBImage_g_memberAccessInfos[3].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->pixels() - (char *)sample);

                RAWRGBImage_g_memberAccessInfos[4].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->debug() - (char *)sample);

                RAWRGBImage_g_memberAccessInfos[5].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->validdata() - (char *)sample);

                RAWRGBImage_g_sampleAccessInfo.memberAccessInfos = 
                RAWRGBImage_g_memberAccessInfos;

                {
                    size_t candidateTypeSize = sizeof(RAWRGBImage);

                    if (candidateTypeSize > RTIXCdrUnsignedLong_MAX) {
                        RAWRGBImage_g_sampleAccessInfo.typeSize[0] =
                        RTIXCdrUnsignedLong_MAX;
                    } else {
                        RAWRGBImage_g_sampleAccessInfo.typeSize[0] =
                        (RTIXCdrUnsignedLong) candidateTypeSize;
                    }
                }

                RAWRGBImage_g_sampleAccessInfo.useGetMemberValueOnlyWithRef =
                RTI_XCDR_TRUE;

                RAWRGBImage_g_sampleAccessInfo.getMemberValuePointerFcn = 
                interpreter::get_aggregation_value_pointer< RAWRGBImage >;

                RAWRGBImage_g_sampleAccessInfo.languageBinding = 
                RTI_XCDR_TYPE_BINDING_CPP_03_STL ;

                RTIXCdrHeap_freeStruct(sample);
                is_initialized = RTI_TRUE;
                return (RTIXCdrSampleAccessInfo*) &RAWRGBImage_g_sampleAccessInfo;
            }

            static RTIXCdrTypePlugin * type_plugin_info()
            {
                static RTIXCdrTypePlugin RAWRGBImage_g_typePlugin = 
                {
                    NULL, /* serialize */
                    NULL, /* serialize_key */
                    NULL, /* deserialize_sample */
                    NULL, /* deserialize_key_sample */
                    NULL, /* skip */
                    NULL, /* get_serialized_sample_size */
                    NULL, /* get_serialized_sample_max_size_ex */
                    NULL, /* get_serialized_key_max_size_ex */
                    NULL, /* get_serialized_sample_min_size */
                    NULL, /* serialized_sample_to_key */
                    NULL,
                    NULL,
                    NULL,
                    NULL
                };

                return &RAWRGBImage_g_typePlugin;
            }
        }; // native_type_code
        #endif

        const ::dds::core::xtypes::StructType& dynamic_type< RAWRGBImage >::get()
        {
            return static_cast<const ::dds::core::xtypes::StructType&>(
                ::rti::core::native_conversions::cast_from_native< ::dds::core::xtypes::DynamicType >(
                    *(native_type_code< RAWRGBImage >::get())));
        }

    }
}

namespace dds { 
    namespace topic {
        void topic_type_support< RAWRGBImage >:: register_type(
            ::dds::domain::DomainParticipant& participant,
            const std::string& type_name) 
        {

            ::rti::domain::register_type_plugin(
                participant,
                type_name,
                RAWRGBImagePlugin_new,
                RAWRGBImagePlugin_delete);
        }

        std::vector<char>& topic_type_support< RAWRGBImage >::to_cdr_buffer(
            std::vector<char>& buffer, 
            const RAWRGBImage& sample,
            ::dds::core::policy::DataRepresentationId representation)
        {
            // First get the length of the buffer
            unsigned int length = 0;
            RTIBool ok = RAWRGBImagePlugin_serialize_to_cdr_buffer(
                NULL, 
                &length,
                &sample,
                representation);
            ::rti::core::check_return_code(
                ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
                "Failed to calculate cdr buffer size");

            // Create a vector with that size and copy the cdr buffer into it
            buffer.resize(length);
            ok = RAWRGBImagePlugin_serialize_to_cdr_buffer(
                &buffer[0], 
                &length, 
                &sample,
                representation);
            ::rti::core::check_return_code(
                ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
                "Failed to copy cdr buffer");

            return buffer;
        }

        void topic_type_support< RAWRGBImage >::from_cdr_buffer(RAWRGBImage& sample, 
        const std::vector<char>& buffer)
        {

            RTIBool ok  = RAWRGBImagePlugin_deserialize_from_cdr_buffer(
                &sample, 
                &buffer[0], 
                static_cast<unsigned int>(buffer.size()));
            ::rti::core::check_return_code(ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
            "Failed to create RAWRGBImage from cdr buffer");
        }

        void topic_type_support< RAWRGBImage >::reset_sample(RAWRGBImage& sample) 
        {
            sample.size(0);
            sample.source("");
            sample.destination("");
            ::rti::topic::reset_sample(sample.pixels());
            sample.debug(0);
            sample.validdata(0);
        }

        void topic_type_support< RAWRGBImage >::allocate_sample(RAWRGBImage& sample, int, int) 
        {
            RTIOsapiUtility_unusedParameter(sample); // [[maybe_unused]]

            ::rti::topic::allocate_sample(sample.source(),  -1, 128);
            ::rti::topic::allocate_sample(sample.destination(),  -1, 128);
            ::rti::topic::allocate_sample(sample.pixels(),  -1, -1);
        }

    }
}  

