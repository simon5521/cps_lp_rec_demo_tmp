#!/usr/bin/env python3
from __future__ import print_function

import rticonnextdds_connector as rti
import numpy as np
import cv2

with rti.open_connector(
        config_name="MyParticipantLibrary::ImageSubParticipant",
        url="ImagesExample.xml") as connector:

    input = connector.get_input("MySubscriber::RawReader")

    print("Waiting for publications...")
    input.wait_for_publications() # wait for at least one matching publication

    print("Waiting for data...")
    for i in range(1, 100):
        input.wait() # wait for data on this input
        input.take()
        for sample in input.samples.valid_data_iter:
            # You can get all the fields in a get_dictionary()
            data = sample.get_dictionary()
            structured_pixels = np.array(data["pixels"], dtype="uint8").reshape((len(data["pixels"]), 1))
            decodedframe = cv2.imdecode(structured_pixels, flags = 1)
            #print(data["source"])

        # Display the resulting frame
        cv2.imshow('frame',decodedframe)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break