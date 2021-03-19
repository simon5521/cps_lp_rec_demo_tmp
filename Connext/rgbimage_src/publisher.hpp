#pragma once
#include <iostream>

#include <dds/pub/ddspub.hpp>
#include <rti/util/util.hpp> // for sleep()
#include <fstream>
#include "RGBImage.hpp"
//#include "opencv2/opencv.hpp"



class Imagepublisher {
public:
	std::vector<dds::pub::DataWriter<RGBImage>> writer;
	std::vector<dds::domain::DomainParticipant> participant;
	std::vector<dds::topic::Topic<RGBImage>> topic;
	RGBImage* sample;

	Imagepublisher();
	void Imagepublisher::sendsample(char* source = "src", char* destination = "dest",
									bool debug = false, bool validdata = true,
									unsigned char* data = NULL, size_t length = 0,
									int cameraid = 0, float ymin = 0, float xmin = 0,
									float ymax = 0, float xmax = 0, char* plaetnumber = "dc6-kro");

};


extern "C" {
	__declspec(dllexport) Imagepublisher* createimagepublisher();
	__declspec(dllexport) void sendimagesample(	Imagepublisher* imgpb, char* source = "src", char* destination = "dest",
												bool debug = false, bool validdata = true,
												unsigned char* data = NULL, size_t length = 0,
												int cameraid = 0, float ymin = 0, float xmin = 0,
												float ymax = 0, float xmax = 0, char* plaetnumber = "dc6-kro");


}