#!/bin/bash

if curl -s --retry 60 --retry-delay 1 http://localhost:8080/actuator/health; then
  echo "API is up"
  exit 0
fi

echo "API failed to start"
exit 1