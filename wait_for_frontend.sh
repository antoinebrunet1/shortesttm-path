#!/bin/bash

if curl -s --retry 60 --retry-delay 1 http://localhost:4200/; then
  echo "Frontend is up"
  exit 0
fi

echo "Frontend failed to start"
exit 1