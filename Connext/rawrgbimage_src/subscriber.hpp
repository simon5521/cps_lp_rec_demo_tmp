#pragma once
#include <algorithm>
#include <iostream>
#include <dds/sub/ddssub.hpp>
#include <dds/core/ddscore.hpp>
// Or simply include <dds/dds.hpp> 

#include "RawImage.hpp"

class Imagesubscriber : public dds::sub::NoOpDataReaderListener<RAWRGBImage> {

public:
	Imagesubscriber() : count_(0) {
		_pixels = new unsigned char[30000000];
		count_ = 0;
	}

	void on_data_available(dds::sub::DataReader<RAWRGBImage>& reader);

	unsigned char* _pixels;
	std::string _source;
	std::string _destination;
	bool _validdata;
	bool _debug;
	int _size;




	int count() const
	{
		return count_;
	}
private:
	int count_;
};

void subscriber_main(Imagesubscriber* imgsr, int domain_id = 0, int sample_count = 5);

extern "C" {
	__declspec(dllexport) Imagesubscriber* createimagesubscriber();
	__declspec(dllexport) void startListening(Imagesubscriber* imgsr);
	__declspec(dllexport) bool getValiddata(Imagesubscriber* imgsr);
	__declspec(dllexport) bool getDebug(Imagesubscriber* imgsr);
	__declspec(dllexport) const char* getDestination(Imagesubscriber* imgsr);
	__declspec(dllexport) const char* getSource(Imagesubscriber* imgsr);
	__declspec(dllexport) unsigned char* getPixels(Imagesubscriber* imgsr);
	__declspec(dllexport) int getSize(Imagesubscriber* imgsr);

	//for debug
	__declspec(dllexport) void getSomePixels(Imagesubscriber* imgsr);
}