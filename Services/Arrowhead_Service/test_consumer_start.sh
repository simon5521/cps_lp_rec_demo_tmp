#!/bin/bash
docker container stop lp-recognition-service
docker container rm lp-recognition-service
docker run --name test-consumer-service test_consumer:initial
