#!/bin/bash

# Stop bash when failer occur
set -e

# Constant
PORT=7700

# Script
START_TIME=$(date +%s)
echo "\n+++ Start +++"
git fetch && git pull origin master

echo "\n-- Installing..."
rm -rf build
gradle clean build -x test

echo "\n-- Building..."
fuser -k $PORT/tcp
nohup java -jar -Dspring.profiles.active=prod -Dserver.port=$PORT build/libs/tonthepduybao-api-1.0.jar >/dev/null 2>&1 &

nohup ./gradlew bootRun > "logs/app-$(date +%F).log" 2>&1 &
disown


END_TIME=$(date +%s)
EXEC_TIME=$(($END_TIME-$START_TIME))
echo "\n\n+++ Finish at $EXEC_TIME seconds +++\n"
