#!/bin/bash

if curl -sf --retry 60 --rety-delay 1 http://localhost:8080/actuator/health; then
  echo "API is up"
  exit 0
fi

echo "API failed to start"
exit 1