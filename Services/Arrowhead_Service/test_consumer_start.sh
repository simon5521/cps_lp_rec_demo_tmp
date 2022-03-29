#!/bin/bash
docker container stop test-consumer-service
docker container rm test-consumer-service
docker run --network="host" --name test-consumer-service test_consumer:initial