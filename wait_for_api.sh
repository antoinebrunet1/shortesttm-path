#!/bin/bash

for i in {1..59} ; do
    if [ "$(curl -s http://localhost:8080/actuator/health)" ]; then
        echo "API is up"
        exit 0
    fi
    echo "Waiting for API..."
    sleep 1
done
if [ "$(curl -s http://localhost:8080/actuator/health)" ]; then
    echo "API is up"
    exit 0
fi
echo "API failed to start"
exit 1