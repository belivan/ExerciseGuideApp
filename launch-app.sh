#!/bin/bash

# Exercise Guide App Launcher Script
# For your grandfather's rehabilitation exercises

PROJECT_DIR="/Users/antonyanovich/Projects/TrainingNicolae/ExerciseGuideApp"
EMULATOR_NAME="Medium_Phone_API_36.1"
PACKAGE="com.rehabilitation.exerciseguide"

echo "🏃 Exercise Guide App Launcher"
echo "=============================="
echo ""

# Navigate to project
cd "$PROJECT_DIR" || exit 1

# Function to check if emulator is running
is_emulator_running() {
    ~/Library/Android/sdk/platform-tools/adb devices | grep -q "emulator"
}

# Function to wait for emulator
wait_for_emulator() {
    echo "⏳ Waiting for emulator to boot..."
    ~/Library/Android/sdk/platform-tools/adb wait-for-device

    # Wait for boot to complete
    while [ "$(~/Library/Android/sdk/platform-tools/adb shell getprop sys.boot_completed 2>/dev/null)" != "1" ]; do
        sleep 2
    done
    echo "✅ Emulator is ready!"
}

# Check if we need to build
if [ "$1" == "--build" ] || [ ! -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "🔨 Building app..."
    ./gradlew assembleDebug
    if [ $? -ne 0 ]; then
        echo "❌ Build failed!"
        exit 1
    fi
    echo "✅ Build successful!"
    echo ""
fi

# Check if emulator is running
if ! is_emulator_running; then
    echo "🚀 Starting emulator..."
    ~/Library/Android/sdk/emulator/emulator -avd "$EMULATOR_NAME" -no-boot-anim &
    wait_for_emulator
else
    echo "✅ Emulator already running"
fi

echo ""
echo "📱 Installing app..."
~/Library/Android/sdk/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk

echo ""
echo "🎯 Launching Exercise Guide..."
~/Library/Android/sdk/platform-tools/adb shell am start -n "$PACKAGE/.MainActivity"

echo ""
echo "✨ App launched successfully!"
echo ""
echo "💡 Tips:"
echo "  • To change to Russian: Settings → System → Languages → Add Russian → Drag to top"
echo "  • The app has large buttons for easy use"
echo "  • Videos will play when you tap an exercise (add .mp4 files to res/raw/)"
echo ""
echo "📊 To view logs: adb logcat | grep rehabilitation"
echo "🛑 To stop emulator: adb emu kill"