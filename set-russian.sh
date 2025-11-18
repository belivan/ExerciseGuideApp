#!/bin/bash

# Script to set Android emulator to Russian language

echo "🇷🇺 Setting emulator language to Russian..."

# Set the locale to Russian
~/Library/Android/sdk/platform-tools/adb shell "setprop persist.sys.locale ru-RU; stop; start"

echo "⏳ Waiting for emulator to restart..."
sleep 10

# Wait for device to be ready
~/Library/Android/sdk/platform-tools/adb wait-for-device

# Wait for boot to complete
while [ "$(~/Library/Android/sdk/platform-tools/adb shell getprop sys.boot_completed 2>/dev/null)" != "1" ]; do
    sleep 2
done

echo "✅ Language changed to Russian!"
echo ""
echo "🚀 Launching Exercise Guide app..."
~/Library/Android/sdk/platform-tools/adb shell am start -n com.rehabilitation.exerciseguide/.MainActivity

echo "✨ Done! The app should now display in Russian."