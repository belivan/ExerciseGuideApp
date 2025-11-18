#!/bin/bash

echo "Creating keystore for Grandfather's Exercise App..."
echo "=================================================="
echo ""
echo "You will be prompted for information."
echo "Use password: grandfather2025"
echo ""

keytool -genkey -v \
  -keystore grandfather-release-key.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias grandfather-app \
  -dname "CN=Exercise App, OU=Personal, O=Family, L=YourCity, ST=YourState, C=US" \
  -storepass grandfather2025 \
  -keypass grandfather2025

echo ""
echo "✅ Keystore created successfully!"
echo "File: grandfather-release-key.jks"
echo ""