#!/usr/bin/env bash

echo "Create the executable app"
sbt dist

echo "Create the docker image"
docker build -t student_course .
