#include "publisher.hpp"

Imagepublisher::Imagepublisher()
{
    participant.push_back(dds::domain::DomainParticipant(0));
    topic.push_back(dds::topic::Topic<RAWRGBImage>(participant.at(0), "RAWRGBImage"));
    writer.push_back(dds::pub::DataWriter<RAWRGBImage>(dds::pub::Publisher(participant.at(0)), topic.at(0)));

    sample = new RAWRGBImage();
}

void Imagepublisher::sendsample(char* source, char* destination, bool debug, bool validdata, unsigned char* data, size_t length)
{
    for (int count = 0; count < 0 || 0 == 0; count++) {
        //filling up the sample with the data
        sample->source(source);
        sample->destination(destination);
        sample->debug(debug);
        sample->validdata(validdata);
        memcpy(sample->pixels().data(), data, length);
        sample->size(length);
        writer.at(0)->write(*sample);
        std::cout << "Writing RAWRGBImage, " << count << std::endl;
        rti::util::sleep(dds::core::Duration(1));
    }
}

Imagepublisher* createimagepublisher()
{
    //returning a Imagepublisher pointer for useage for python
    return new Imagepublisher;
}

void sendimagesample(Imagepublisher* imgpb, char* source, char* destination, bool debug, bool validdata, unsigned char* data, size_t length)
{
    //Snding the sample,with python interface
    imgpb->sendsample(source, destination, debug, validdata, data, length);
}