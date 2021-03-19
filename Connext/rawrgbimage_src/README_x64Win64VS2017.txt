Welcome to your first Connext DDS example! 

This example was generated for architecture x64Win64VS2017, using the
data type RAWRGBImage from RawImage.idl.
This example builds two applications, named RawImage_publisher and
RawImage_subscriber.

To Build this Example:
======================
1. Launch Visual Studio in one of two ways:
        - By typing RawImage-x64Win64VS2017.sln in the
          Command Prompt window.
        - By opening Visual Studio on your machine and then opening the
          RawImage-x64Win64VS2017.sln file (in the
          Solution Explorer) via File > Open Project.

    Note: If you are using Visual Studio 2017 or later, you might be prompted
    to retarget the file. If this happens, in the Retarget Projects window that
    appears, select an installed version of Windows SDK and click OK.

2. Select Build Solution from the Build menu. (You can also right-click
   RawImage_publisher or RawImage_subscriber and
   choose Build; do for each.) 

    Note: If you get the Windows SDK error, right-click the solution and choose
    Retarget Solution.

To Modify the Data:
===================

To modify the data being sent: edit the RawImage_publisher.cxx
file where it says 
// Modify the data to be written here

Recompile after making the changes.

To Run this Example:
====================
 
To run from Visual Studio:
1. Right-click RawImage_publisher and choose Debug > Start new instance.
2. Right-click RawImage_subscriber and choose Debug > Start new instance.
If Visual Studio will not let you choose Debug > Start new instance, run the
subscribing application from the command prompt instead.
 
To run from the command prompt: 
1. Make sure you are in the directory where the USER_QOS_PROFILES.xml file was
   generated (the same directory this README file is in).
2. Run D:\RTIConnextDDS\rti_connext_dds-6.0.1\bin\..\resource\scripts\rtisetenv_x64Win64VS2017.bat
   to make sure the Connext libraries are in the path, especially if you opened a
   new command prompt window.
3. Run the publishing or subscribing application by typing:
> objs\x64Win64VS2017\RawImage_publisher.exe <domain_id> <sample_count>
> objs\x64Win64VS2017\RawImage_subscriber.exe <domain_id> <sample_count>