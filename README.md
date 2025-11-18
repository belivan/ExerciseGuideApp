# Exercise Guide - Knee Rehabilitation App 🏥

A specialized Android application designed for elderly users recovering from knee surgery. This app provides guided exercise videos with an intuitive, accessible interface optimized for users with limited technical experience.

## 📋 Table of Contents
- [Features](#-features)
- [Prerequisites](#-prerequisites)
- [Quick Start Guide](#-quick-start-guide)
- [Detailed Setup Instructions](#-detailed-setup-instructions)
- [Building the App](#-building-the-app)
- [Deployment](#-deployment)
- [App Usage Guide](#-app-usage-guide)
- [Technical Details](#-technical-details)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

## 🌟 Features

### Core Functionality
- **13 Pre-loaded Exercise Videos**: Complete set of knee rehabilitation exercises
- **Russian Language Interface**: Fully localized for Russian-speaking users
- **Offline Support**: No internet connection required after installation
- **Progress Tracking**: Track daily exercise completion with visual indicators
- **Daily Reminders**: Automated notifications at 8:00 AM local time

### Elderly-Friendly Design
- **Large UI Elements**: All buttons minimum 80dp for easy tapping
- **High Contrast Colors**: Green/white color scheme for clear visibility
- **Simple Navigation**: Swipe gestures or large numbered navigation buttons
- **Extra Large Fonts**: 24sp minimum for body text, 32sp for navigation numbers
- **One-Tap Video Playback**: Simple play/pause controls in accessible positions

### Video Features
- **In-Place Playback**: Videos play without navigating to new screens
- **Auto-Stop on Navigation**: Videos automatically pause when switching exercises
- **Large Control Buttons**: 48dp play/pause button (top-left), close button (top-right)
- **Full-Screen Videos**: Maximized video area for better visibility

## 📱 Prerequisites

### For Development
- **Android Studio**: Arctic Fox or newer (2025.2.1 recommended)
- **Java Development Kit (JDK)**: Version 17 or higher
- **Android SDK**: API level 35 (Android 15)
- **Git**: For version control
- **Minimum 4GB RAM**: 8GB+ recommended for smooth development

### For Running the App
- **Android Device**: Android 8.0 (API 26) or higher
- **Storage**: ~100MB free space
- **RAM**: 2GB minimum

## 🚀 Quick Start Guide

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/ExerciseGuideApp.git
cd ExerciseGuideApp
```

### 2. Open in Android Studio
1. Launch Android Studio
2. Click "Open"
3. Navigate to the cloned `ExerciseGuideApp` folder
4. Click "OK" and wait for project sync

### 3. Run the App
1. Connect your Android device via USB OR create an emulator (see below)
2. Enable USB debugging on your device (Settings → Developer Options)
3. Click the green "Run" button (▶️) in Android Studio
4. Select your device/emulator
5. Wait for the app to install and launch

## 📝 Detailed Setup Instructions

### Setting Up Android Studio

1. **Download Android Studio**
   - Visit: https://developer.android.com/studio
   - Download for your operating system
   - Run the installer

2. **Install Required SDK Components**
   - Open Android Studio
   - Go to: Tools → SDK Manager
   - Install:
     - Android SDK Platform 35
     - Android SDK Build-Tools 35.0.0
     - Android Emulator
     - Android SDK Platform-Tools

### Creating an Emulator (Optional)

1. Open Android Studio
2. Go to: Tools → AVD Manager
3. Click "Create Virtual Device"
4. Select: Phone → Pixel 6 (or any phone)
5. Select system image: API 35 (Android 15)
6. Name it: "Elderly Test Device"
7. Click "Finish"

### Configuring for Physical Device

1. **Enable Developer Options on Android Device**:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings → Developer Options
   - Enable "USB Debugging"

2. **Connect Device**:
   - Connect via USB cable
   - Accept debugging prompt on device
   - Verify connection:
   ```bash
   adb devices
   ```

## 🏗️ Building the App

### Debug Build (for testing)
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build (for distribution)
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk`

### Signing the Release Build

1. **Create Keystore** (first time only):
   ```bash
   ./create-keystore.sh
   ```
   - Follow prompts to set passwords
   - Keep the `grandfather-release-key.jks` file safe!

2. **Configure Signing**:
   - Edit `keystore.properties`:
   ```properties
   storePassword=yourpassword
   keyPassword=yourpassword
   keyAlias=grandfather-app
   storeFile=grandfather-release-key.jks
   ```

3. **Build Signed APK**:
   ```bash
   ./gradlew assembleRelease
   ```

## 📲 Deployment

### Installing on Device via USB

1. **Debug Version**:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

2. **Release Version**:
   ```bash
   adb install app/build/outputs/apk/release/app-release.apk
   ```

### Installing APK Manually

1. Copy APK to device (via USB, email, or cloud storage)
2. On device: Settings → Security → Enable "Unknown Sources"
3. Open file manager and tap the APK file
4. Follow installation prompts

### Launching the App

Via ADB:
```bash
adb shell am start -n com.rehabilitation.exerciseguide/.MainActivity
```

Or use the provided script:
```bash
./launch-app.sh
```

## 📖 App Usage Guide

### For End Users (Grandfather)

1. **Starting Exercises**:
   - Open the app
   - Tap "НАЧАТЬ ТРЕНИРОВКУ" (Start Training)
   - First exercise appears automatically

2. **Watching Exercise Videos**:
   - Tap the exercise image to play video
   - Tap play/pause button (top-left) to control
   - Tap X button (top-right) to close video

3. **Navigating Between Exercises**:
   - Swipe left/right OR
   - Tap large numbered buttons at bottom
   - Numbers show which exercise you'll go to

4. **Marking Exercise Complete**:
   - Tap green "Отметить выполненным" button
   - Exercise gets checkmark
   - Progress updates automatically

5. **Starting New Day**:
   - Tap "НАЧАТЬ НОВЫЙ ДЕНЬ" to reset all progress
   - Use this each morning

6. **Editing Exercise Parameters**:
   - Tap "Изменить" (Edit) button
   - Tap exercise to open edit dialog
   - Adjust repetitions, sets, or duration
   - Tap "Сохранить" (Save)

### Daily Notifications

- Automatic reminder at 8:00 AM
- Tap notification to open app
- To test: Open app, notification appears in 5 seconds (test mode)

## 🛠️ Technical Details

### Project Structure
```
ExerciseGuideApp/
├── app/
│   ├── build.gradle.kts         # App-level build configuration
│   └── src/main/
│       ├── java/.../
│       │   ├── MainActivity.kt   # Main entry point
│       │   ├── data/            # Database layer
│       │   │   ├── local/       # Room database
│       │   │   └── repository/  # Data repositories
│       │   ├── domain/          # Business logic
│       │   ├── notifications/   # Daily reminders
│       │   └── presentation/    # UI layer
│       │       ├── screens/     # Composable screens
│       │       ├── theme/       # Material3 theming
│       │       └── viewmodels/  # MVVM ViewModels
│       ├── res/
│       │   ├── raw/            # Exercise videos (13 MP4 files)
│       │   ├── drawable/       # Images and icons
│       │   ├── values/         # English strings
│       │   └── values-ru/      # Russian strings
│       └── AndroidManifest.xml # App configuration
├── gradle/                     # Gradle wrapper files
├── build.gradle.kts           # Project-level build config
├── settings.gradle.kts        # Project settings
└── README.md                  # This file
```

### Key Technologies
- **Language**: Kotlin 1.9.0
- **UI**: Jetpack Compose BOM 2024.10.01
- **Architecture**: MVVM with Repository pattern
- **Database**: Room 2.6.1
- **Video Player**: Media3 ExoPlayer 1.5.0
- **Navigation**: Navigation Compose 2.8.4
- **Dependency Injection**: Manual (no DI framework)

### Database Schema

**Exercise Table**:
- `id`: INTEGER PRIMARY KEY
- `titleKey`: TEXT (exercise name)
- `videoFileName`: TEXT (video resource name)
- `thumbnailResId`: INTEGER (thumbnail image)
- `repetitions`: INTEGER (default: 10)
- `sets`: INTEGER (default: 3)
- `durationSeconds`: INTEGER (default: 30)
- `isCompleted`: INTEGER (0 or 1)
- `orderIndex`: INTEGER (display order)

## 🔧 Troubleshooting

### Common Issues and Solutions

1. **"App not installed" error**:
   - Uninstall previous version first
   - Check device has enough storage
   - Enable "Unknown Sources" in settings

2. **Videos not playing**:
   - Ensure all video files are in `app/src/main/res/raw/`
   - Check video format is MP4 H.264
   - Verify device supports hardware video decoding

3. **Text too small/large**:
   - App overrides system font scaling
   - This is intentional for consistent layout
   - Cannot be changed by user

4. **Notifications not appearing**:
   - Check notification permissions in Settings
   - Ensure "Do Not Disturb" is off
   - For Android 13+: Grant notification permission when prompted

5. **Build errors**:
   - Run: `./gradlew clean`
   - Sync project in Android Studio
   - Check JDK version is 17+

### ADB Commands for Debugging

```bash
# View logs
adb logcat -s ExerciseGuide

# Clear app data
adb shell pm clear com.rehabilitation.exerciseguide

# Check permissions
adb shell dumpsys package com.rehabilitation.exerciseguide | grep permission

# Take screenshot
adb shell screencap /sdcard/screenshot.png
adb pull /sdcard/screenshot.png
```

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes
4. Test thoroughly on physical device
5. Commit: `git commit -m "Add feature"`
6. Push: `git push origin feature-name`
7. Create Pull Request

### Code Style Guidelines
- Use Kotlin coding conventions
- Minimum font sizes: 24sp
- Minimum touch targets: 56dp
- Add Russian translations for all strings
- Test with system font scaling at 200%

## 📄 License

This project is open source and available under the MIT License.

## 📧 Support

For issues or questions:
- Open an issue on GitHub
- Email: support@example.com

## 🙏 Acknowledgments

This app was created with love for helping elderly users maintain their rehabilitation routine. Special thanks to all healthcare professionals who provided exercise guidance.

---

**Made with ❤️ for Grandfather's recovery**