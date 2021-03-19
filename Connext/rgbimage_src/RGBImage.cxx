

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from RGBImage.idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

#include <iosfwd>
#include <iomanip>

#include "rti/topic/cdr/Serialization.hpp"

#include "RGBImage.hpp"
#include "RGBImagePlugin.hpp"

#include <rti/util/ostream_operators.hpp>

// ---- Position: 

Position::Position() :
    m_ymin_ (0.0f) ,
    m_xmin_ (0.0f) ,
    m_ymax_ (0.0f) ,
    m_xmax_ (0.0f)  {
}   

Position::Position (
    float ymin,
    float xmin,
    float ymax,
    float xmax)
    :
        m_ymin_( ymin ),
        m_xmin_( xmin ),
        m_ymax_( ymax ),
        m_xmax_( xmax ) {
}

#ifdef RTI_CXX11_RVALUE_REFERENCES
#ifdef RTI_CXX11_NO_IMPLICIT_MOVE_OPERATIONS
Position::Position(Position&& other_) OMG_NOEXCEPT  :m_ymin_ (std::move(other_.m_ymin_))
,
m_xmin_ (std::move(other_.m_xmin_))
,
m_ymax_ (std::move(other_.m_ymax_))
,
m_xmax_ (std::move(other_.m_xmax_))
{
} 

Position& Position::operator=(Position&&  other_) OMG_NOEXCEPT {
    Position tmp(std::move(other_));
    swap(tmp); 
    return *this;
}
#endif
#endif   

void Position::swap(Position& other_)  OMG_NOEXCEPT 
{
    using std::swap;
    swap(m_ymin_, other_.m_ymin_);
    swap(m_xmin_, other_.m_xmin_);
    swap(m_ymax_, other_.m_ymax_);
    swap(m_xmax_, other_.m_xmax_);
}  

bool Position::operator == (const Position& other_) const {
    if (m_ymin_ != other_.m_ymin_) {
        return false;
    }
    if (m_xmin_ != other_.m_xmin_) {
        return false;
    }
    if (m_ymax_ != other_.m_ymax_) {
        return false;
    }
    if (m_xmax_ != other_.m_xmax_) {
        return false;
    }
    return true;
}
bool Position::operator != (const Position& other_) const {
    return !this->operator ==(other_);
}

std::ostream& operator << (std::ostream& o,const Position& sample)
{
    ::rti::util::StreamFlagSaver flag_saver (o);
    o <<"[";
    o << "ymin: " << std::setprecision(9) <<sample.ymin()<<", ";
    o << "xmin: " << std::setprecision(9) <<sample.xmin()<<", ";
    o << "ymax: " << std::setprecision(9) <<sample.ymax()<<", ";
    o << "xmax: " << std::setprecision(9) <<sample.xmax() ;
    o <<"]";
    return o;
}

// ---- RGBImage: 

RGBImage::RGBImage() :
    m_source_ ("") ,
    m_destination_ ("") ,
    m_debug_ (0) ,
    m_validdata_ (0) ,
    m_size_ (0) ,
    m_cameraid_ (0) ,
    m_platenumber_ ("")  {
        ::rti::core::fill_array< uint8_t >( m_pixels_, 0);
}   

RGBImage::RGBImage (
    const std::string& source,
    const std::string& destination,
    bool debug,
    bool validdata,
    const ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& pixels,
    int32_t size,
    int32_t cameraid,
    const Position& position,
    const std::string& platenumber)
    :
        m_source_( source ),
        m_destination_( destination ),
        m_debug_( debug ),
        m_validdata_( validdata ),
        m_pixels_( pixels ),
        m_size_( size ),
        m_cameraid_( cameraid ),
        m_position_( position ),
        m_platenumber_( platenumber ) {
}

#ifdef RTI_CXX11_RVALUE_REFERENCES
#ifdef RTI_CXX11_NO_IMPLICIT_MOVE_OPERATIONS
RGBImage::RGBImage(RGBImage&& other_) OMG_NOEXCEPT  :m_source_ (std::move(other_.m_source_))
,
m_destination_ (std::move(other_.m_destination_))
,
m_debug_ (std::move(other_.m_debug_))
,
m_validdata_ (std::move(other_.m_validdata_))
,
m_pixels_ (std::move(other_.m_pixels_))
,
m_size_ (std::move(other_.m_size_))
,
m_cameraid_ (std::move(other_.m_cameraid_))
,
m_position_ (std::move(other_.m_position_))
,
m_platenumber_ (std::move(other_.m_platenumber_))
{
} 

RGBImage& RGBImage::operator=(RGBImage&&  other_) OMG_NOEXCEPT {
    RGBImage tmp(std::move(other_));
    swap(tmp); 
    return *this;
}
#endif
#endif   

void RGBImage::swap(RGBImage& other_)  OMG_NOEXCEPT 
{
    using std::swap;
    swap(m_source_, other_.m_source_);
    swap(m_destination_, other_.m_destination_);
    swap(m_debug_, other_.m_debug_);
    swap(m_validdata_, other_.m_validdata_);
    swap(m_pixels_, other_.m_pixels_);
    swap(m_size_, other_.m_size_);
    swap(m_cameraid_, other_.m_cameraid_);
    swap(m_position_, other_.m_position_);
    swap(m_platenumber_, other_.m_platenumber_);
}  

bool RGBImage::operator == (const RGBImage& other_) const {
    if (m_source_ != other_.m_source_) {
        return false;
    }
    if (m_destination_ != other_.m_destination_) {
        return false;
    }
    if (m_debug_ != other_.m_debug_) {
        return false;
    }
    if (m_validdata_ != other_.m_validdata_) {
        return false;
    }
    if (m_pixels_ != other_.m_pixels_) {
        return false;
    }
    if (m_size_ != other_.m_size_) {
        return false;
    }
    if (m_cameraid_ != other_.m_cameraid_) {
        return false;
    }
    if (m_position_ != other_.m_position_) {
        return false;
    }
    if (m_platenumber_ != other_.m_platenumber_) {
        return false;
    }
    return true;
}
bool RGBImage::operator != (const RGBImage& other_) const {
    return !this->operator ==(other_);
}

std::ostream& operator << (std::ostream& o,const RGBImage& sample)
{
    ::rti::util::StreamFlagSaver flag_saver (o);
    o <<"[";
    o << "source: " << sample.source()<<", ";
    o << "destination: " << sample.destination()<<", ";
    o << "debug: " << sample.debug()<<", ";
    o << "validdata: " << sample.validdata()<<", ";
    o << "pixels: " << sample.pixels()<<", ";
    o << "size: " << sample.size()<<", ";
    o << "cameraid: " << sample.cameraid()<<", ";
    o << "position: " << sample.position()<<", ";
    o << "platenumber: " << sample.platenumber() ;
    o <<"]";
    return o;
}

// --- Type traits: -------------------------------------------------

namespace rti { 
    namespace topic {

        #ifndef NDDS_STANDALONE_TYPE

        template<>
        struct native_type_code< Position > {
            static DDS_TypeCode * get()
            {
                using namespace ::rti::topic::interpreter;

                static RTIBool is_initialized = RTI_FALSE;

                static DDS_TypeCode_Member Position_g_tc_members[4]=
                {

                    {
                        (char *)"ymin",/* Member name */
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
                        (char *)"xmin",/* Member name */
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
                        (char *)"ymax",/* Member name */
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
                        (char *)"xmax",/* Member name */
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
                    }
                };

                static DDS_TypeCode Position_g_tc =
                {{
                        DDS_TK_STRUCT, /* Kind */
                        DDS_BOOLEAN_FALSE, /* Ignored */
                        -1, /*Ignored*/
                        (char *)"Position", /* Name */
                        NULL, /* Ignored */      
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        4, /* Number of members */
                        Position_g_tc_members, /* Members */
                        DDS_VM_NONE, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER,
                        DDS_BOOLEAN_TRUE, /* _isCopyable */
                        NULL, /* _sampleAccessInfo: assigned later */
                        NULL /* _typePlugin: assigned later */
                    }}; /* Type code for Position*/

                if (is_initialized) {
                    return &Position_g_tc;
                }

                Position_g_tc._data._annotations._allowedDataRepresentationMask = 5;

                Position_g_tc_members[0]._representation._typeCode = (RTICdrTypeCode *)&DDS_g_tc_float;
                Position_g_tc_members[1]._representation._typeCode = (RTICdrTypeCode *)&DDS_g_tc_float;
                Position_g_tc_members[2]._representation._typeCode = (RTICdrTypeCode *)&DDS_g_tc_float;
                Position_g_tc_members[3]._representation._typeCode = (RTICdrTypeCode *)&DDS_g_tc_float;

                /* Initialize the values for member annotations. */
                Position_g_tc_members[0]._annotations._defaultValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[0]._annotations._defaultValue._u.float_value = 0.0f;
                Position_g_tc_members[0]._annotations._minValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[0]._annotations._minValue._u.float_value = RTIXCdrFloat_MIN;
                Position_g_tc_members[0]._annotations._maxValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[0]._annotations._maxValue._u.float_value = RTIXCdrFloat_MAX;

                Position_g_tc_members[1]._annotations._defaultValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[1]._annotations._defaultValue._u.float_value = 0.0f;
                Position_g_tc_members[1]._annotations._minValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[1]._annotations._minValue._u.float_value = RTIXCdrFloat_MIN;
                Position_g_tc_members[1]._annotations._maxValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[1]._annotations._maxValue._u.float_value = RTIXCdrFloat_MAX;

                Position_g_tc_members[2]._annotations._defaultValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[2]._annotations._defaultValue._u.float_value = 0.0f;
                Position_g_tc_members[2]._annotations._minValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[2]._annotations._minValue._u.float_value = RTIXCdrFloat_MIN;
                Position_g_tc_members[2]._annotations._maxValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[2]._annotations._maxValue._u.float_value = RTIXCdrFloat_MAX;

                Position_g_tc_members[3]._annotations._defaultValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[3]._annotations._defaultValue._u.float_value = 0.0f;
                Position_g_tc_members[3]._annotations._minValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[3]._annotations._minValue._u.float_value = RTIXCdrFloat_MIN;
                Position_g_tc_members[3]._annotations._maxValue._d = RTI_XCDR_TK_FLOAT;
                Position_g_tc_members[3]._annotations._maxValue._u.float_value = RTIXCdrFloat_MAX;

                Position_g_tc._data._sampleAccessInfo = sample_access_info();
                Position_g_tc._data._typePlugin = type_plugin_info();    

                is_initialized = RTI_TRUE;

                return &Position_g_tc;
            }

            static RTIXCdrSampleAccessInfo * sample_access_info()
            {
                static RTIBool is_initialized = RTI_FALSE;

                Position *sample;

                static RTIXCdrMemberAccessInfo Position_g_memberAccessInfos[4] =
                {RTIXCdrMemberAccessInfo_INITIALIZER};

                static RTIXCdrSampleAccessInfo Position_g_sampleAccessInfo = 
                RTIXCdrSampleAccessInfo_INITIALIZER;

                if (is_initialized) {
                    return (RTIXCdrSampleAccessInfo*) &Position_g_sampleAccessInfo;
                }

                RTIXCdrHeap_allocateStruct(
                    &sample, 
                    Position);
                if (sample == NULL) {
                    return NULL;
                }

                Position_g_memberAccessInfos[0].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->ymin() - (char *)sample);

                Position_g_memberAccessInfos[1].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->xmin() - (char *)sample);

                Position_g_memberAccessInfos[2].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->ymax() - (char *)sample);

                Position_g_memberAccessInfos[3].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->xmax() - (char *)sample);

                Position_g_sampleAccessInfo.memberAccessInfos = 
                Position_g_memberAccessInfos;

                {
                    size_t candidateTypeSize = sizeof(Position);

                    if (candidateTypeSize > RTIXCdrUnsignedLong_MAX) {
                        Position_g_sampleAccessInfo.typeSize[0] =
                        RTIXCdrUnsignedLong_MAX;
                    } else {
                        Position_g_sampleAccessInfo.typeSize[0] =
                        (RTIXCdrUnsignedLong) candidateTypeSize;
                    }
                }

                Position_g_sampleAccessInfo.useGetMemberValueOnlyWithRef =
                RTI_XCDR_TRUE;

                Position_g_sampleAccessInfo.getMemberValuePointerFcn = 
                interpreter::get_aggregation_value_pointer< Position >;

                Position_g_sampleAccessInfo.languageBinding = 
                RTI_XCDR_TYPE_BINDING_CPP_03_STL ;

                RTIXCdrHeap_freeStruct(sample);
                is_initialized = RTI_TRUE;
                return (RTIXCdrSampleAccessInfo*) &Position_g_sampleAccessInfo;
            }

            static RTIXCdrTypePlugin * type_plugin_info()
            {
                static RTIXCdrTypePlugin Position_g_typePlugin = 
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

                return &Position_g_typePlugin;
            }
        }; // native_type_code
        #endif

        const ::dds::core::xtypes::StructType& dynamic_type< Position >::get()
        {
            return static_cast<const ::dds::core::xtypes::StructType&>(
                ::rti::core::native_conversions::cast_from_native< ::dds::core::xtypes::DynamicType >(
                    *(native_type_code< Position >::get())));
        }

        #ifndef NDDS_STANDALONE_TYPE

        template<>
        struct native_type_code< RGBImage > {
            static DDS_TypeCode * get()
            {
                using namespace ::rti::topic::interpreter;

                static RTIBool is_initialized = RTI_FALSE;

                static DDS_TypeCode RGBImage_g_tc_source_string;
                static DDS_TypeCode RGBImage_g_tc_destination_string;
                static DDS_TypeCode RGBImage_g_tc_pixels_array =DDS_INITIALIZE_ARRAY_TYPECODE(1,(MAX_IMAGE_SIZE), NULL,NULL);
                static DDS_TypeCode RGBImage_g_tc_platenumber_string;

                static DDS_TypeCode_Member RGBImage_g_tc_members[9]=
                {

                    {
                        (char *)"source",/* Member name */
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
                        (char *)"destination",/* Member name */
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
                        (char *)"debug",/* Member name */
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
                        (char *)"validdata",/* Member name */
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
                        (char *)"pixels",/* Member name */
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
                        (char *)"size",/* Member name */
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
                    }, 
                    {
                        (char *)"cameraid",/* Member name */
                        {
                            6,/* Representation ID */
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
                        (char *)"position",/* Member name */
                        {
                            7,/* Representation ID */
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
                        (char *)"platenumber",/* Member name */
                        {
                            8,/* Representation ID */
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

                static DDS_TypeCode RGBImage_g_tc =
                {{
                        DDS_TK_STRUCT, /* Kind */
                        DDS_BOOLEAN_FALSE, /* Ignored */
                        -1, /*Ignored*/
                        (char *)"RGBImage", /* Name */
                        NULL, /* Ignored */      
                        0, /* Ignored */
                        0, /* Ignored */
                        NULL, /* Ignored */
                        9, /* Number of members */
                        RGBImage_g_tc_members, /* Members */
                        DDS_VM_NONE, /* Ignored */
                        RTICdrTypeCodeAnnotations_INITIALIZER,
                        DDS_BOOLEAN_TRUE, /* _isCopyable */
                        NULL, /* _sampleAccessInfo: assigned later */
                        NULL /* _typePlugin: assigned later */
                    }}; /* Type code for RGBImage*/

                if (is_initialized) {
                    return &RGBImage_g_tc;
                }

                RGBImage_g_tc_source_string = initialize_string_typecode((128));
                RGBImage_g_tc_destination_string = initialize_string_typecode((128));
                RGBImage_g_tc_platenumber_string = initialize_string_typecode((128));

                RGBImage_g_tc._data._annotations._allowedDataRepresentationMask = 5;

                RGBImage_g_tc_pixels_array._data._typeCode =(RTICdrTypeCode *)&DDS_g_tc_octet;
                RGBImage_g_tc_members[0]._representation._typeCode = (RTICdrTypeCode *)&RGBImage_g_tc_source_string;
                RGBImage_g_tc_members[1]._representation._typeCode = (RTICdrTypeCode *)&RGBImage_g_tc_destination_string;
                RGBImage_g_tc_members[2]._representation._typeCode = (RTICdrTypeCode *)&::rti::topic::interpreter::initialize_bool_typecode();
                RGBImage_g_tc_members[3]._representation._typeCode = (RTICdrTypeCode *)&::rti::topic::interpreter::initialize_bool_typecode();
                RGBImage_g_tc_members[4]._representation._typeCode = (RTICdrTypeCode *)& RGBImage_g_tc_pixels_array;
                RGBImage_g_tc_members[5]._representation._typeCode = (RTICdrTypeCode *)&DDS_g_tc_long;
                RGBImage_g_tc_members[6]._representation._typeCode = (RTICdrTypeCode *)&DDS_g_tc_long;
                RGBImage_g_tc_members[7]._representation._typeCode = (RTICdrTypeCode *)&::rti::topic::dynamic_type< Position>::get().native();
                RGBImage_g_tc_members[8]._representation._typeCode = (RTICdrTypeCode *)&RGBImage_g_tc_platenumber_string;

                /* Initialize the values for member annotations. */
                RGBImage_g_tc_members[0]._annotations._defaultValue._d = RTI_XCDR_TK_STRING;
                RGBImage_g_tc_members[0]._annotations._defaultValue._u.string_value = (DDS_Char *) "";

                RGBImage_g_tc_members[1]._annotations._defaultValue._d = RTI_XCDR_TK_STRING;
                RGBImage_g_tc_members[1]._annotations._defaultValue._u.string_value = (DDS_Char *) "";

                RGBImage_g_tc_members[2]._annotations._defaultValue._d = RTI_XCDR_TK_BOOLEAN;
                RGBImage_g_tc_members[2]._annotations._defaultValue._u.boolean_value = 0;

                RGBImage_g_tc_members[3]._annotations._defaultValue._d = RTI_XCDR_TK_BOOLEAN;
                RGBImage_g_tc_members[3]._annotations._defaultValue._u.boolean_value = 0;

                RGBImage_g_tc_members[5]._annotations._defaultValue._d = RTI_XCDR_TK_LONG;
                RGBImage_g_tc_members[5]._annotations._defaultValue._u.long_value = 0;
                RGBImage_g_tc_members[5]._annotations._minValue._d = RTI_XCDR_TK_LONG;
                RGBImage_g_tc_members[5]._annotations._minValue._u.long_value = RTIXCdrLong_MIN;
                RGBImage_g_tc_members[5]._annotations._maxValue._d = RTI_XCDR_TK_LONG;
                RGBImage_g_tc_members[5]._annotations._maxValue._u.long_value = RTIXCdrLong_MAX;

                RGBImage_g_tc_members[6]._annotations._defaultValue._d = RTI_XCDR_TK_LONG;
                RGBImage_g_tc_members[6]._annotations._defaultValue._u.long_value = 0;
                RGBImage_g_tc_members[6]._annotations._minValue._d = RTI_XCDR_TK_LONG;
                RGBImage_g_tc_members[6]._annotations._minValue._u.long_value = RTIXCdrLong_MIN;
                RGBImage_g_tc_members[6]._annotations._maxValue._d = RTI_XCDR_TK_LONG;
                RGBImage_g_tc_members[6]._annotations._maxValue._u.long_value = RTIXCdrLong_MAX;

                RGBImage_g_tc_members[8]._annotations._defaultValue._d = RTI_XCDR_TK_STRING;
                RGBImage_g_tc_members[8]._annotations._defaultValue._u.string_value = (DDS_Char *) "";

                RGBImage_g_tc._data._sampleAccessInfo = sample_access_info();
                RGBImage_g_tc._data._typePlugin = type_plugin_info();    

                is_initialized = RTI_TRUE;

                return &RGBImage_g_tc;
            }

            static RTIXCdrSampleAccessInfo * sample_access_info()
            {
                static RTIBool is_initialized = RTI_FALSE;

                RGBImage *sample;

                static RTIXCdrMemberAccessInfo RGBImage_g_memberAccessInfos[9] =
                {RTIXCdrMemberAccessInfo_INITIALIZER};

                static RTIXCdrSampleAccessInfo RGBImage_g_sampleAccessInfo = 
                RTIXCdrSampleAccessInfo_INITIALIZER;

                if (is_initialized) {
                    return (RTIXCdrSampleAccessInfo*) &RGBImage_g_sampleAccessInfo;
                }

                RTIXCdrHeap_allocateStruct(
                    &sample, 
                    RGBImage);
                if (sample == NULL) {
                    return NULL;
                }

                RGBImage_g_memberAccessInfos[0].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->source() - (char *)sample);

                RGBImage_g_memberAccessInfos[1].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->destination() - (char *)sample);

                RGBImage_g_memberAccessInfos[2].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->debug() - (char *)sample);

                RGBImage_g_memberAccessInfos[3].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->validdata() - (char *)sample);

                RGBImage_g_memberAccessInfos[4].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->pixels() - (char *)sample);

                RGBImage_g_memberAccessInfos[5].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->size() - (char *)sample);

                RGBImage_g_memberAccessInfos[6].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->cameraid() - (char *)sample);

                RGBImage_g_memberAccessInfos[7].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->position() - (char *)sample);

                RGBImage_g_memberAccessInfos[8].bindingMemberValueOffset[0] = 
                (RTIXCdrUnsignedLong) ((char *)&sample->platenumber() - (char *)sample);

                RGBImage_g_sampleAccessInfo.memberAccessInfos = 
                RGBImage_g_memberAccessInfos;

                {
                    size_t candidateTypeSize = sizeof(RGBImage);

                    if (candidateTypeSize > RTIXCdrUnsignedLong_MAX) {
                        RGBImage_g_sampleAccessInfo.typeSize[0] =
                        RTIXCdrUnsignedLong_MAX;
                    } else {
                        RGBImage_g_sampleAccessInfo.typeSize[0] =
                        (RTIXCdrUnsignedLong) candidateTypeSize;
                    }
                }

                RGBImage_g_sampleAccessInfo.useGetMemberValueOnlyWithRef =
                RTI_XCDR_TRUE;

                RGBImage_g_sampleAccessInfo.getMemberValuePointerFcn = 
                interpreter::get_aggregation_value_pointer< RGBImage >;

                RGBImage_g_sampleAccessInfo.languageBinding = 
                RTI_XCDR_TYPE_BINDING_CPP_03_STL ;

                RTIXCdrHeap_freeStruct(sample);
                is_initialized = RTI_TRUE;
                return (RTIXCdrSampleAccessInfo*) &RGBImage_g_sampleAccessInfo;
            }

            static RTIXCdrTypePlugin * type_plugin_info()
            {
                static RTIXCdrTypePlugin RGBImage_g_typePlugin = 
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

                return &RGBImage_g_typePlugin;
            }
        }; // native_type_code
        #endif

        const ::dds::core::xtypes::StructType& dynamic_type< RGBImage >::get()
        {
            return static_cast<const ::dds::core::xtypes::StructType&>(
                ::rti::core::native_conversions::cast_from_native< ::dds::core::xtypes::DynamicType >(
                    *(native_type_code< RGBImage >::get())));
        }

    }
}

namespace dds { 
    namespace topic {
        void topic_type_support< Position >:: register_type(
            ::dds::domain::DomainParticipant& participant,
            const std::string& type_name) 
        {

            ::rti::domain::register_type_plugin(
                participant,
                type_name,
                PositionPlugin_new,
                PositionPlugin_delete);
        }

        std::vector<char>& topic_type_support< Position >::to_cdr_buffer(
            std::vector<char>& buffer, 
            const Position& sample,
            ::dds::core::policy::DataRepresentationId representation)
        {
            // First get the length of the buffer
            unsigned int length = 0;
            RTIBool ok = PositionPlugin_serialize_to_cdr_buffer(
                NULL, 
                &length,
                &sample,
                representation);
            ::rti::core::check_return_code(
                ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
                "Failed to calculate cdr buffer size");

            // Create a vector with that size and copy the cdr buffer into it
            buffer.resize(length);
            ok = PositionPlugin_serialize_to_cdr_buffer(
                &buffer[0], 
                &length, 
                &sample,
                representation);
            ::rti::core::check_return_code(
                ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
                "Failed to copy cdr buffer");

            return buffer;
        }

        void topic_type_support< Position >::from_cdr_buffer(Position& sample, 
        const std::vector<char>& buffer)
        {

            RTIBool ok  = PositionPlugin_deserialize_from_cdr_buffer(
                &sample, 
                &buffer[0], 
                static_cast<unsigned int>(buffer.size()));
            ::rti::core::check_return_code(ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
            "Failed to create Position from cdr buffer");
        }

        void topic_type_support< Position >::reset_sample(Position& sample) 
        {
            sample.ymin(0.0f);
            sample.xmin(0.0f);
            sample.ymax(0.0f);
            sample.xmax(0.0f);
        }

        void topic_type_support< Position >::allocate_sample(Position& sample, int, int) 
        {
            RTIOsapiUtility_unusedParameter(sample); // [[maybe_unused]]

        }

        void topic_type_support< RGBImage >:: register_type(
            ::dds::domain::DomainParticipant& participant,
            const std::string& type_name) 
        {

            ::rti::domain::register_type_plugin(
                participant,
                type_name,
                RGBImagePlugin_new,
                RGBImagePlugin_delete);
        }

        std::vector<char>& topic_type_support< RGBImage >::to_cdr_buffer(
            std::vector<char>& buffer, 
            const RGBImage& sample,
            ::dds::core::policy::DataRepresentationId representation)
        {
            // First get the length of the buffer
            unsigned int length = 0;
            RTIBool ok = RGBImagePlugin_serialize_to_cdr_buffer(
                NULL, 
                &length,
                &sample,
                representation);
            ::rti::core::check_return_code(
                ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
                "Failed to calculate cdr buffer size");

            // Create a vector with that size and copy the cdr buffer into it
            buffer.resize(length);
            ok = RGBImagePlugin_serialize_to_cdr_buffer(
                &buffer[0], 
                &length, 
                &sample,
                representation);
            ::rti::core::check_return_code(
                ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
                "Failed to copy cdr buffer");

            return buffer;
        }

        void topic_type_support< RGBImage >::from_cdr_buffer(RGBImage& sample, 
        const std::vector<char>& buffer)
        {

            RTIBool ok  = RGBImagePlugin_deserialize_from_cdr_buffer(
                &sample, 
                &buffer[0], 
                static_cast<unsigned int>(buffer.size()));
            ::rti::core::check_return_code(ok ? DDS_RETCODE_OK : DDS_RETCODE_ERROR,
            "Failed to create RGBImage from cdr buffer");
        }

        void topic_type_support< RGBImage >::reset_sample(RGBImage& sample) 
        {
            sample.source("");
            sample.destination("");
            sample.debug(0);
            sample.validdata(0);
            ::rti::topic::reset_sample(sample.pixels());
            sample.size(0);
            sample.cameraid(0);
            ::rti::topic::reset_sample(sample.position());
            sample.platenumber("");
        }

        void topic_type_support< RGBImage >::allocate_sample(RGBImage& sample, int, int) 
        {
            RTIOsapiUtility_unusedParameter(sample); // [[maybe_unused]]

            ::rti::topic::allocate_sample(sample.source(),  -1, 128);
            ::rti::topic::allocate_sample(sample.destination(),  -1, 128);
            ::rti::topic::allocate_sample(sample.pixels(),  -1, -1);
            ::rti::topic::allocate_sample(sample.position(),  -1, -1);
            ::rti::topic::allocate_sample(sample.platenumber(),  -1, 128);
        }

    }
}  

