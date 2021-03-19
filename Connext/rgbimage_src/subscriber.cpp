#include "subscriber.hpp"
#include <chrono> 
using namespace std::chrono;

void Imagesubscriber::on_data_available(dds::sub::DataReader<RGBImage>& reader)
{
    auto start = high_resolution_clock::now();
    dds::sub::LoanedSamples<RGBImage> samples = reader.take();

    for (dds::sub::LoanedSamples<RGBImage>::iterator sample_it = samples.begin();
        sample_it != samples.end(); sample_it++) {

        if (sample_it->info().valid()) {
            _source                     = sample_it->data().source();
            _destination                = sample_it->data().destination();
            _debug                      = sample_it->data().debug();
            _validdata                  = sample_it->data().validdata();
            memcpy                      (_pixels, sample_it->data().pixels().data(), sample_it->data().pixels().size());
            _size                       = sample_it->data().size();
            _cameraid                   = sample_it->data().cameraid();
            _ymin                       = sample_it->data().position().ymin();
            _xmin                       = sample_it->data().position().xmin();
            _ymax                       = sample_it->data().position().ymax();
            _xmax                       = sample_it->data().position().xmax();
            _plaetnumber    = sample_it->data().platenumber();


            /*std::cout << "source: "         << sample_it->data().source()           << ", ";
            std::cout << "destination: "    << sample_it->data().destination()      << ", ";
            std::cout << "debug: "          << sample_it->data().debug()            << ", ";
            std::cout << "validdata: "      << sample_it->data().validdata()        << ", ";
            std::cout << "size: "           << sample_it->data().validdata()        << ", ";
            std::cout << "_cameraid: "      << sample_it->data().cameraid()         << ", ";
            std::cout << "ymin: "           << sample_it->data().position().ymin()  << ", ";
            std::cout << "xmin: "           << sample_it->data().position().xmin()  << ", ";
            std::cout << "ymax: "           << sample_it->data().position().ymax()  << ", ";
            std::cout << "xmax: "           << sample_it->data().position().xmax()  << ", ";
            std::cout << "platenumber: "    << sample_it->data().platenumber()      << std::endl;*/

            count_ = 5;

        }
        auto stop = high_resolution_clock::now();
        auto duration = duration_cast<microseconds>(stop - start);
        std::cout << "reader time processing: "<<duration.count() << std::endl;
    }
}

void subscriber_main(Imagesubscriber* listener, int domain_id, int sample_count)
{
    dds::domain::DomainParticipant participant(domain_id);
    dds::topic::Topic<RGBImage> topic(participant, "RGBImage");
    
    dds::sub::DataReader<RGBImage> reader(
        dds::sub::Subscriber(participant),
        topic,
        dds::core::QosProvider::Default().datareader_qos(),
        listener,
        dds::core::status::StatusMask::data_available());

    while (listener->count() < sample_count || sample_count == 0) {
        std::cout << "RGBImage subscriber sleeping for 0,5 sec..." << std::endl;

        rti::util::sleep(dds::core::Duration(1));
    }
   //reader.listener(NULL, dds::core::status::StatusMask::none());
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

int getCameraid(Imagesubscriber* imgsr)
{
	return imgsr->_cameraid;
}

float getYmin(Imagesubscriber* imgsr)
{
	return imgsr->_ymin;
}

float getXmin(Imagesubscriber* imgsr)
{
	return imgsr->_xmin;
}

float getYmax(Imagesubscriber* imgsr)
{
	return imgsr->_ymax;
}

float getXmax(Imagesubscriber* imgsr)
{
	return imgsr->_xmax;
}

const char* getPlatenumber(Imagesubscriber* imgsr)
{
	return imgsr->_plaetnumber.c_str();
}
