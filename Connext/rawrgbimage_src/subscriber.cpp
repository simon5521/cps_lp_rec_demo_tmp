#include "subscriber.hpp"

void Imagesubscriber::on_data_available(dds::sub::DataReader<RAWRGBImage>& reader)
{
    dds::sub::LoanedSamples<RAWRGBImage> samples = reader.take();

    for (dds::sub::LoanedSamples<RAWRGBImage>::iterator sample_it = samples.begin();
        sample_it != samples.end(); sample_it++) {
       
        if (sample_it->info().valid()) {
            _source = sample_it->data().source();
            _destination = sample_it->data().destination();
            _debug = sample_it->data().debug();
            _validdata = sample_it->data().validdata();
            memcpy(_pixels, sample_it->data().pixels().data(), sample_it->data().pixels().size());
            _size = sample_it->data().size();
            
            std::cout << "source: " << sample_it->data().source()<<", ";
            std::cout << "destination: " << sample_it->data().destination() << ", ";
            std::cout << "debug: " << sample_it->data().debug() << ", ";
            std::cout << "validdata: " << sample_it->data().validdata() << ", ";
            std::cout << "validdata: " << sample_it->data().validdata() << ", ";
            std::cout << "size: " << sample_it->data().size()<<std::endl;
            count_ = 5;

        }
    }
}

void subscriber_main(Imagesubscriber* listener, int domain_id, int sample_count) {

    dds::domain::DomainParticipant participant(domain_id);
    dds::topic::Topic<RAWRGBImage> topic(participant, "RAWRGBImage");
    dds::sub::DataReader<RAWRGBImage> reader(
        dds::sub::Subscriber(participant),
        topic,
        dds::core::QosProvider::Default().datareader_qos(),
        listener,
        dds::core::status::StatusMask::data_available());

    while (listener->count() < sample_count || sample_count == 0) {
        std::cout << "RAWRGBImage subscriber sleeping for ,5 sec..." << std::endl;

        rti::util::sleep(dds::core::Duration(0, 500000000));
    }

    std::cout << "start closing listener";
    reader.listener(NULL, dds::core::status::StatusMask::none());

}

Imagesubscriber* createimagesubscriber()
{
    return new Imagesubscriber();
}

void startListening(Imagesubscriber* imgsr)
{
    subscriber_main(imgsr);
}

bool getValiddata(Imagesubscriber* imgsr)
{
    return imgsr->_validdata;
}

bool getDebug(Imagesubscriber* imgsr)
{
    return imgsr->_debug;
}

const char* getDestination(Imagesubscriber* imgsr)
{
    return imgsr->_destination.c_str();
}

const char* getSource(Imagesubscriber* imgsr)
{
    return imgsr->_source.c_str();
}

unsigned char* getPixels(Imagesubscriber* imgsr)
{
    return imgsr->_pixels;
}

int getSize(Imagesubscriber* imgsr)
{
    return imgsr->_size;
}



//only for debug reasons
void getSomePixels(Imagesubscriber* imgsr)
{
    for (int i = 0; i < 10; i++) {
        std::cout << imgsr->_pixels[i] << std::endl;
    }
}
