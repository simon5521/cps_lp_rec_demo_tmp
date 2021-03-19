#pragma once
#include <algorithm>
#include <iostream>
#include <dds/sub/ddssub.hpp>
#include <dds/core/ddscore.hpp>
// Or simply include <dds/dds.hpp> 

#include "RGBImage.hpp"

class Imagesubscriber : public dds::sub::NoOpDataReaderListener<RGBImage> {

public:
	Imagesubscriber() : count_(0) {
		_pixels = new unsigned char[30000000];
		count_ = 0;
	}

	void on_data_available(dds::sub::DataReader<RGBImage>& reader);

	unsigned char* _pixels;
	std::string _source;
	std::string _destination;
	bool _validdata;
	bool _debug;
	int _size;

	int _cameraid;
	float _ymin;
	float _xmin;
	float _ymax;
	float _xmax;
	std::string _plaetnumber;




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
	__declspec(dllexport) int getCameraid(Imagesubscriber* imgsr);
	__declspec(dllexport) float getYmin(Imagesubscriber* imgsr);
	__declspec(dllexport) float getXmin(Imagesubscriber* imgsr);
	__declspec(dllexport) float getYmax(Imagesubscriber* imgsr);
	__declspec(dllexport) float getXmax(Imagesubscriber* imgsr);
	__declspec(dllexport) const char* getPlatenumber(Imagesubscriber* imgsr);

	//for debug
	//__declspec(dllexport) void getSomePixels(Imagesubscriber* imgsr);
}