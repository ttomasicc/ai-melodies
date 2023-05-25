#!/bin/bash

if ! command -v pack &> /dev/null
then
    echo "pack-cli could not be found"
    exit
fi

pack build ttomasic/ai-melodies-app \
    -B paketobuildpacks/builder:base \
    -b paketo-buildpacks/web-servers \
    -e BP_NODE_RUN_SCRIPTS=build \
    -e BP_WEB_SERVER=nginx \
    -e BP_WEB_SERVER_ROOT=dist/aimelodies \
    -e BP_WEB_SERVER_ENABLE_PUSH_STATE=true
