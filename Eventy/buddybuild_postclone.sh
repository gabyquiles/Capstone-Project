#!/usr/bin/env bash
echo "Running post clone script"
# Get the google-services file from BuddyBuild Secure Files and copy it to correct location
cp ${BUDDYBUILD_SECURE_FILES}/google-services.json ${BUDDYBUILD_WORKSPACE}/app/google-services.json