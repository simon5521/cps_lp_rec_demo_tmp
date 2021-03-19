#include "publisher.hpp"
#include <exception>

Imagepublisher::Imagepublisher()
{
    participant.push_back(dds::domain::DomainParticipant(0));
    topic.push_back(dds::topic::Topic<RGBImage>(participant.at(0), "RGBImage"));
    writer.push_back(dds::pub::DataWriter<RGBImage>(dds::pub::Publisher(participant.at(0)), topic.at(0)));
    writer.push_back(dds::pub::DataWriter<RGBImage>(dds::pub::Publisher(participant.at(0)), topic.at(0)));
    sample = new RGBImage();
}

void Imagepublisher::sendsample(char* source, char* destination, bool debug, bool validdata, 
                                unsigned char* data, size_t length, int cameraid, float ymin, 
                                float xmin, float ymax, float xmax, char* plaetnumber)
{
#include <chrono> 
    using namespace std::chrono;
    auto start = high_resolution_clock::now();
    
        int count = 0;
        
        
        sample->source(source); 
        sample->destination(destination); 
        sample->debug(debug); 
        sample->validdata(validdata); 
        memcpy(sample->pixels().data(), data, length); 
        sample->size(length); 
        sample->cameraid(cameraid); 
        sample->position().ymin(ymin); 
        sample->position().xmin(xmin); 
        sample->position().ymax(ymax); 
        sample->position().xmax(xmax); 
        sample->platenumber(plaetnumber); 
        
        try {
            writer.at(0)->write(*sample);
        }
        catch (std::exception& e) {
            std::cout <<e.what()<<std::endl;
            if (data == NULL) std::cout << "null" << std::endl;
        }
        std::cout << "Writing RAWRGBImage, " << count << std::endl;
       // rti::util::sleep(dds::core::Duration(1));
        auto stop = high_resolution_clock::now();
        auto duration = duration_cast<microseconds>(stop - start);

        std::cout << "time for sending the sample"<<duration.count() << std::endl;
}

Imagepublisher* createimagepublisher()
{
	return new Imagepublisher();
}

void sendimagesample(   Imagepublisher* imgpb, char* source, char* destination, bool debug, bool validdata, 
                        unsigned char* data, size_t length, int cameraid, float ymin, float xmin, float ymax,   
                        float xmax, char* plaetnumber)
{
    imgpb->sendsample(source, destination, debug, validdata, data, length, cameraid, ymin, xmin, ymax, xmax, plaetnumber);
}
