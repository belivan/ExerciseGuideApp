# Exercise Guide - Knee Rehabilitation App

A specialized Android application designed for elderly users recovering from knee surgery. This app provides guided exercise videos with an intuitive, accessible interface optimized for users with limited technical experience.

## 🌟 Features

### Core Functionality
- **13 Pre-loaded Exercise Videos**: Complete set of knee rehabilitation exercises
- **Russian Language Interface**: Fully localized for Russian-speaking users
- **Offline Support**: No internet connection required after installation
- **Progress Tracking**: Track daily exercise completion
- **Daily Reminders**: Automated notifications at 8:00 AM

### Elderly-Friendly Design
- **Large UI Elements**: All buttons and text optimized for easy visibility
- **High Contrast Colors**: Clear visual hierarchy with green/white color scheme
- **Simple Navigation**: Swipe or tap large navigation buttons to move between exercises
- **Extra Large Fonts**: Minimum 24sp for body text, 32sp for navigation numbers
- **One-Tap Video Playback**: Simple play/pause controls positioned for easy access

### Video Features
- **In-Place Playback**: Videos play without navigating away
- **Auto-Stop on Navigation**: Videos automatically stop when switching exercises
- **Large Control Buttons**: Play/pause button in top-left, close button in top-right
- **Full-Screen Videos**: Maximized video area for better visibility

### Exercise Management
- **Customizable Parameters**: Edit repetitions, sets, and duration for each exercise
- **Visual Progress Indicators**: Color-coded completion status
- **Daily Reset Option**: "Start New Day" button to reset progress
- **Completion Tracking**: Check off exercises as completed

## 📱 Screenshots

The app features:
1. Main exercise carousel with video player
2. Progress statistics with completion percentage
3. Large navigation buttons with exercise numbers
4. Edit mode for customizing exercise parameters
5. Daily notification reminders

## 🛠️ Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Database for local storage
- **Video Player**: Media3 ExoPlayer
- **Minimum SDK**: API 26 (Android 8.0)
- **Target SDK**: API 35 (Android 15)

## 📋 Requirements

- Android 8.0 (API level 26) or higher
- Approximately 100MB storage space
- No internet connection required for regular use

## 🚀 Installation

### From Source
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ExerciseGuideApp.git
   ```

2. Open the project in Android Studio

3. Build and run the app:
   - Click "Run" button or use `Shift + F10`
   - Select your device or emulator

### APK Installation
1. Download the latest APK from the Releases section
2. Enable "Install from Unknown Sources" on your Android device
3. Install the APK file

## 🏗️ Building the Project

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

Note: Release builds require signing configuration. See the signing section in `app/build.gradle.kts`

## 📂 Project Structure

```
app/
├── src/main/
│   ├── java/com/rehabilitation/exerciseguide/
│   │   ├── MainActivity.kt
│   │   ├── data/               # Database and repositories
│   │   ├── domain/             # Business logic and models
│   │   ├── notifications/      # Daily reminder system
│   │   └── presentation/       # UI components
│   │       ├── screens/        # Composable screens
│   │       ├── theme/          # Material3 theming
│   │       └── viewmodels/     # MVVM ViewModels
│   └── res/
│       ├── raw/                # Exercise videos (MP4)
│       └── values/             # Strings, colors, themes
```

## 🔔 Notifications

The app sends daily reminders at 8:00 AM local time. Notifications include:
- Title: "Время для упражнений! 💪"
- Message: Reminder to complete daily rehabilitation exercises
- Tap to open the app directly

## ⚙️ Configuration

### Font Scaling
The app overrides system font scaling to maintain consistent UI layout. This ensures the interface remains usable even when system accessibility settings increase text size.

### Screen Orientation
The app is locked to portrait mode for optimal video viewing and consistent layout.

## 🎯 Target Audience

This app is specifically designed for:
- Elderly users (65+ years)
- Post-knee surgery patients
- Users with limited smartphone experience
- Russian-speaking rehabilitation patients

## 📄 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Support

For issues, questions, or suggestions, please open an issue on GitHub.

---

**Note**: This app was developed with care for helping elderly users maintain their rehabilitation routine. The large UI elements, simple navigation, and daily reminders are intentionally designed to make the experience as accessible as possible.