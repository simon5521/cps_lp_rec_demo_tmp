#!/bin/bash
docker container stop test-consumer-service
docker container rm test-consumer-service
docker run -p 17000:17000 --name test-consumer-service test_consumer:initial