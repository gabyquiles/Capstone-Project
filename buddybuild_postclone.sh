#!/usr/bin/env bash
# Get the google-services file from BuddyBuild Secure Files and copy it to correct location
cp ${BUDDYBUILD_SECURE_FILES}/base-google-services.json ${BUDDYBUILD_WORKSPACE}/Eventy/app/google-services.json

cp ${BUDDYBUILD_SECURE_FILES}/free-google-services.json ${BUDDYBUILD_WORKSPACE}/Eventy/app/src/free/google-services.json
