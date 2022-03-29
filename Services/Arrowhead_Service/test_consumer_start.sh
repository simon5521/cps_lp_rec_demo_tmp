#!/bin/bash
docker container stop lp-recognition-service
docker container rm lp-recognition-service
docker run --name lp-recognition-service lp_recognition_service:initial
