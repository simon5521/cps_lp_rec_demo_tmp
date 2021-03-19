

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from RawImage.idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

#ifndef RawImage_1296340066_hpp
#define RawImage_1296340066_hpp

#include <iosfwd>

#if (defined(RTI_WIN32) || defined (RTI_WINCE) || defined(RTI_INTIME)) && defined(NDDS_USER_DLL_EXPORT)
/* If the code is building on Windows, start exporting symbols.
*/
#undef RTIUSERDllExport
#define RTIUSERDllExport __declspec(dllexport)
#endif

#include "dds/domain/DomainParticipant.hpp"
#include "dds/topic/TopicTraits.hpp"
#include "dds/core/SafeEnumeration.hpp"
#include "dds/core/String.hpp"
#include "dds/core/array.hpp"
#include "dds/core/vector.hpp"
#include "dds/core/Optional.hpp"
#include "dds/core/xtypes/DynamicType.hpp"
#include "dds/core/xtypes/StructType.hpp"
#include "dds/core/xtypes/UnionType.hpp"
#include "dds/core/xtypes/EnumType.hpp"
#include "dds/core/xtypes/AliasType.hpp"
#include "rti/core/array.hpp"
#include "rti/core/BoundedSequence.hpp"
#include "rti/util/StreamFlagSaver.hpp"
#include "rti/domain/PluginSupport.hpp"
#include "rti/core/LongDouble.hpp"
#include "dds/core/External.hpp"
#include "rti/core/Pointer.hpp"
#include "rti/topic/TopicTraits.hpp"

#if (defined(RTI_WIN32) || defined (RTI_WINCE) || defined(RTI_INTIME)) && defined(NDDS_USER_DLL_EXPORT)
/* If the code is building on Windows, stop exporting symbols.
*/
#undef RTIUSERDllExport
#define RTIUSERDllExport
#endif

#if (defined(RTI_WIN32) || defined (RTI_WINCE) || defined(RTI_INTIME)) && defined(NDDS_USER_DLL_EXPORT)
/* If the code is building on Windows, start exporting symbols.
*/
#undef NDDSUSERDllExport
#define NDDSUSERDllExport __declspec(dllexport)
#endif

static const int32_t MAX_IMAGE_SIZE = 30000000;

class NDDSUSERDllExport RAWRGBImage {
  public:
    RAWRGBImage();

    RAWRGBImage(
        int32_t size,
        const std::string& source,
        const std::string& destination,
        const ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& pixels,
        bool debug,
        bool validdata);

    #ifdef RTI_CXX11_RVALUE_REFERENCES
    #ifndef RTI_CXX11_NO_IMPLICIT_MOVE_OPERATIONS
    RAWRGBImage (RAWRGBImage&&) = default;
    RAWRGBImage& operator=(RAWRGBImage&&) = default;
    RAWRGBImage& operator=(const RAWRGBImage&) = default;
    RAWRGBImage(const RAWRGBImage&) = default;
    #else
    RAWRGBImage(RAWRGBImage&& other_) OMG_NOEXCEPT;  
    RAWRGBImage& operator=(RAWRGBImage&&  other_) OMG_NOEXCEPT;
    #endif
    #endif 

    int32_t& size() OMG_NOEXCEPT {
        return m_size_;
    }

    const int32_t& size() const OMG_NOEXCEPT {
        return m_size_;
    }

    void size(int32_t value) {
        m_size_ = value;
    }

    std::string& source() OMG_NOEXCEPT {
        return m_source_;
    }

    const std::string& source() const OMG_NOEXCEPT {
        return m_source_;
    }

    void source(const std::string& value) {
        m_source_ = value;
    }

    std::string& destination() OMG_NOEXCEPT {
        return m_destination_;
    }

    const std::string& destination() const OMG_NOEXCEPT {
        return m_destination_;
    }

    void destination(const std::string& value) {
        m_destination_ = value;
    }

    ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& pixels() OMG_NOEXCEPT {
        return m_pixels_;
    }

    const ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& pixels() const OMG_NOEXCEPT {
        return m_pixels_;
    }

    void pixels(const ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& value) {
        m_pixels_ = value;
    }

    bool& debug() OMG_NOEXCEPT {
        return m_debug_;
    }

    const bool& debug() const OMG_NOEXCEPT {
        return m_debug_;
    }

    void debug(bool value) {
        m_debug_ = value;
    }

    bool& validdata() OMG_NOEXCEPT {
        return m_validdata_;
    }

    const bool& validdata() const OMG_NOEXCEPT {
        return m_validdata_;
    }

    void validdata(bool value) {
        m_validdata_ = value;
    }

    bool operator == (const RAWRGBImage& other_) const;
    bool operator != (const RAWRGBImage& other_) const;

    void swap(RAWRGBImage& other_) OMG_NOEXCEPT ;

  private:

    int32_t m_size_;
    std::string m_source_;
    std::string m_destination_;
    ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)> m_pixels_;
    bool m_debug_;
    bool m_validdata_;

};

inline void swap(RAWRGBImage& a, RAWRGBImage& b)  OMG_NOEXCEPT 
{
    a.swap(b);
}

NDDSUSERDllExport std::ostream& operator<<(std::ostream& o, const RAWRGBImage& sample);

namespace rti {
    namespace flat {
        namespace topic {
        }
    }
}
namespace dds {
    namespace topic {

        template<>
        struct topic_type_name< RAWRGBImage > {
            NDDSUSERDllExport static std::string value() {
                return "RAWRGBImage";
            }
        };

        template<>
        struct is_topic_type< RAWRGBImage > : public ::dds::core::true_type {};

        template<>
        struct topic_type_support< RAWRGBImage > {
            NDDSUSERDllExport 
            static void register_type(
                ::dds::domain::DomainParticipant& participant,
                const std::string & type_name);

            NDDSUSERDllExport 
            static std::vector<char>& to_cdr_buffer(
                std::vector<char>& buffer, 
                const RAWRGBImage& sample,
                ::dds::core::policy::DataRepresentationId representation 
                = ::dds::core::policy::DataRepresentation::auto_id());

            NDDSUSERDllExport 
            static void from_cdr_buffer(RAWRGBImage& sample, const std::vector<char>& buffer);
            NDDSUSERDllExport 
            static void reset_sample(RAWRGBImage& sample);

            NDDSUSERDllExport 
            static void allocate_sample(RAWRGBImage& sample, int, int);

            static const ::rti::topic::TypePluginKind::type type_plugin_kind = 
            ::rti::topic::TypePluginKind::STL;
        };

    }
}

namespace rti { 
    namespace topic {

        #ifndef NDDS_STANDALONE_TYPE
        template<>
        struct dynamic_type< RAWRGBImage > {
            typedef ::dds::core::xtypes::StructType type;
            NDDSUSERDllExport static const ::dds::core::xtypes::StructType& get();
        };
        #endif

        template <>
        struct extensibility< RAWRGBImage > {
            static const ::dds::core::xtypes::ExtensibilityKind::type kind =
            ::dds::core::xtypes::ExtensibilityKind::EXTENSIBLE;                
        };

    }
}

#if (defined(RTI_WIN32) || defined (RTI_WINCE) || defined(RTI_INTIME)) && defined(NDDS_USER_DLL_EXPORT)
/* If the code is building on Windows, stop exporting symbols.
*/
#undef NDDSUSERDllExport
#define NDDSUSERDllExport
#endif

#endif // RawImage_1296340066_hpp

