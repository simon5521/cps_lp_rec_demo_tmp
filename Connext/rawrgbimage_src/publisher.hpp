#pragma once
#include <iostream>

#include <dds/pub/ddspub.hpp>
#include <rti/util/util.hpp> // for sleep()
#include <fstream>
#include "RawImage.hpp"
//#include "opencv2/opencv.hpp"



class Imagepublisher {
public:
	std::vector<dds::pub::DataWriter<RAWRGBImage>> writer;
	std::vector<dds::domain::DomainParticipant> participant;
	std::vector<dds::topic::Topic<RAWRGBImage>> topic;
	RAWRGBImage* sample;

	Imagepublisher();
	void Imagepublisher::sendsample(char* source = "src", char* destination = "dest", bool debug = false, bool validdata = true, unsigned char* data = NULL, size_t length = 0);
	
};


extern "C" {
	__declspec(dllexport) Imagepublisher* createimagepublisher();
	__declspec(dllexport) void sendimagesample(Imagepublisher* imgpb, char* source = "src", char* destination = "dest", bool debug = 0, bool validdata = 1, unsigned char* data = NULL, size_t length = 0);


}