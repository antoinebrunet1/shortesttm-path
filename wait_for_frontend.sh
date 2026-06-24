#!/bin/bash

if curl -sf --retry 60 --rety-delay 1 http://localhost:4200/; then
  echo "Frontend is up"
  exit 0
fi

echo "Frontend failed to start"
exit 1