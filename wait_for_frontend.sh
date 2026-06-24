#!/bin/bash

for i in {1..59} ; do
    if [ "$(curl -s http://localhost:4200/)" ]; then
        echo "Frontend is up"
        exit 0
    fi
    echo "Waiting for frontend..."
    sleep 1
done
if [ "$(curl -s http://localhost:4200/)" ]; then
    echo "Frontend is up"
    exit 0
fi
echo "Frontend failed to start"
exit 1