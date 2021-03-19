

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from RGBImage.idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

#ifndef RGBImage_5036790_hpp
#define RGBImage_5036790_hpp

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

class NDDSUSERDllExport Position {
  public:
    Position();

    Position(
        float ymin,
        float xmin,
        float ymax,
        float xmax);

    #ifdef RTI_CXX11_RVALUE_REFERENCES
    #ifndef RTI_CXX11_NO_IMPLICIT_MOVE_OPERATIONS
    Position (Position&&) = default;
    Position& operator=(Position&&) = default;
    Position& operator=(const Position&) = default;
    Position(const Position&) = default;
    #else
    Position(Position&& other_) OMG_NOEXCEPT;  
    Position& operator=(Position&&  other_) OMG_NOEXCEPT;
    #endif
    #endif 

    float& ymin() OMG_NOEXCEPT {
        return m_ymin_;
    }

    const float& ymin() const OMG_NOEXCEPT {
        return m_ymin_;
    }

    void ymin(float value) {
        m_ymin_ = value;
    }

    float& xmin() OMG_NOEXCEPT {
        return m_xmin_;
    }

    const float& xmin() const OMG_NOEXCEPT {
        return m_xmin_;
    }

    void xmin(float value) {
        m_xmin_ = value;
    }

    float& ymax() OMG_NOEXCEPT {
        return m_ymax_;
    }

    const float& ymax() const OMG_NOEXCEPT {
        return m_ymax_;
    }

    void ymax(float value) {
        m_ymax_ = value;
    }

    float& xmax() OMG_NOEXCEPT {
        return m_xmax_;
    }

    const float& xmax() const OMG_NOEXCEPT {
        return m_xmax_;
    }

    void xmax(float value) {
        m_xmax_ = value;
    }

    bool operator == (const Position& other_) const;
    bool operator != (const Position& other_) const;

    void swap(Position& other_) OMG_NOEXCEPT ;

  private:

    float m_ymin_;
    float m_xmin_;
    float m_ymax_;
    float m_xmax_;

};

inline void swap(Position& a, Position& b)  OMG_NOEXCEPT 
{
    a.swap(b);
}

NDDSUSERDllExport std::ostream& operator<<(std::ostream& o, const Position& sample);

static const int32_t MAX_IMAGE_SIZE = 1500000;

class NDDSUSERDllExport RGBImage {
  public:
    RGBImage();

    RGBImage(
        const std::string& source,
        const std::string& destination,
        bool debug,
        bool validdata,
        const ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& pixels,
        int32_t size,
        int32_t cameraid,
        const Position& position,
        const std::string& platenumber);

    #ifdef RTI_CXX11_RVALUE_REFERENCES
    #ifndef RTI_CXX11_NO_IMPLICIT_MOVE_OPERATIONS
    RGBImage (RGBImage&&) = default;
    RGBImage& operator=(RGBImage&&) = default;
    RGBImage& operator=(const RGBImage&) = default;
    RGBImage(const RGBImage&) = default;
    #else
    RGBImage(RGBImage&& other_) OMG_NOEXCEPT;  
    RGBImage& operator=(RGBImage&&  other_) OMG_NOEXCEPT;
    #endif
    #endif 

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

    ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& pixels() OMG_NOEXCEPT {
        return m_pixels_;
    }

    const ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& pixels() const OMG_NOEXCEPT {
        return m_pixels_;
    }

    void pixels(const ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)>& value) {
        m_pixels_ = value;
    }

    int32_t& size() OMG_NOEXCEPT {
        return m_size_;
    }

    const int32_t& size() const OMG_NOEXCEPT {
        return m_size_;
    }

    void size(int32_t value) {
        m_size_ = value;
    }

    int32_t& cameraid() OMG_NOEXCEPT {
        return m_cameraid_;
    }

    const int32_t& cameraid() const OMG_NOEXCEPT {
        return m_cameraid_;
    }

    void cameraid(int32_t value) {
        m_cameraid_ = value;
    }

    Position& position() OMG_NOEXCEPT {
        return m_position_;
    }

    const Position& position() const OMG_NOEXCEPT {
        return m_position_;
    }

    void position(const Position& value) {
        m_position_ = value;
    }

    std::string& platenumber() OMG_NOEXCEPT {
        return m_platenumber_;
    }

    const std::string& platenumber() const OMG_NOEXCEPT {
        return m_platenumber_;
    }

    void platenumber(const std::string& value) {
        m_platenumber_ = value;
    }

    bool operator == (const RGBImage& other_) const;
    bool operator != (const RGBImage& other_) const;

    void swap(RGBImage& other_) OMG_NOEXCEPT ;

  private:

    std::string m_source_;
    std::string m_destination_;
    bool m_debug_;
    bool m_validdata_;
    ::dds::core::array< uint8_t, (MAX_IMAGE_SIZE)> m_pixels_;
    int32_t m_size_;
    int32_t m_cameraid_;
    Position m_position_;
    std::string m_platenumber_;

};

inline void swap(RGBImage& a, RGBImage& b)  OMG_NOEXCEPT 
{
    a.swap(b);
}

NDDSUSERDllExport std::ostream& operator<<(std::ostream& o, const RGBImage& sample);

namespace rti {
    namespace flat {
        namespace topic {
        }
    }
}
namespace dds {
    namespace topic {

        template<>
        struct topic_type_name< Position > {
            NDDSUSERDllExport static std::string value() {
                return "Position";
            }
        };

        template<>
        struct is_topic_type< Position > : public ::dds::core::true_type {};

        template<>
        struct topic_type_support< Position > {
            NDDSUSERDllExport 
            static void register_type(
                ::dds::domain::DomainParticipant& participant,
                const std::string & type_name);

            NDDSUSERDllExport 
            static std::vector<char>& to_cdr_buffer(
                std::vector<char>& buffer, 
                const Position& sample,
                ::dds::core::policy::DataRepresentationId representation 
                = ::dds::core::policy::DataRepresentation::auto_id());

            NDDSUSERDllExport 
            static void from_cdr_buffer(Position& sample, const std::vector<char>& buffer);
            NDDSUSERDllExport 
            static void reset_sample(Position& sample);

            NDDSUSERDllExport 
            static void allocate_sample(Position& sample, int, int);

            static const ::rti::topic::TypePluginKind::type type_plugin_kind = 
            ::rti::topic::TypePluginKind::STL;
        };

        template<>
        struct topic_type_name< RGBImage > {
            NDDSUSERDllExport static std::string value() {
                return "RGBImage";
            }
        };

        template<>
        struct is_topic_type< RGBImage > : public ::dds::core::true_type {};

        template<>
        struct topic_type_support< RGBImage > {
            NDDSUSERDllExport 
            static void register_type(
                ::dds::domain::DomainParticipant& participant,
                const std::string & type_name);

            NDDSUSERDllExport 
            static std::vector<char>& to_cdr_buffer(
                std::vector<char>& buffer, 
                const RGBImage& sample,
                ::dds::core::policy::DataRepresentationId representation 
                = ::dds::core::policy::DataRepresentation::auto_id());

            NDDSUSERDllExport 
            static void from_cdr_buffer(RGBImage& sample, const std::vector<char>& buffer);
            NDDSUSERDllExport 
            static void reset_sample(RGBImage& sample);

            NDDSUSERDllExport 
            static void allocate_sample(RGBImage& sample, int, int);

            static const ::rti::topic::TypePluginKind::type type_plugin_kind = 
            ::rti::topic::TypePluginKind::STL;
        };

    }
}

namespace rti { 
    namespace topic {
        #ifndef NDDS_STANDALONE_TYPE
        template<>
        struct dynamic_type< Position > {
            typedef ::dds::core::xtypes::StructType type;
            NDDSUSERDllExport static const ::dds::core::xtypes::StructType& get();
        };
        #endif

        template <>
        struct extensibility< Position > {
            static const ::dds::core::xtypes::ExtensibilityKind::type kind =
            ::dds::core::xtypes::ExtensibilityKind::EXTENSIBLE;                
        };

        #ifndef NDDS_STANDALONE_TYPE
        template<>
        struct dynamic_type< RGBImage > {
            typedef ::dds::core::xtypes::StructType type;
            NDDSUSERDllExport static const ::dds::core::xtypes::StructType& get();
        };
        #endif

        template <>
        struct extensibility< RGBImage > {
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

#endif // RGBImage_5036790_hpp

