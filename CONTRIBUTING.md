# Contributing to Exercise Guide App

Thank you for your interest in contributing to the Exercise Guide App! This project aims to help elderly users with knee rehabilitation, and we welcome contributions that improve accessibility, usability, and effectiveness.

## 🤝 Code of Conduct

By participating in this project, you agree to:
- Be respectful and inclusive
- Welcome newcomers and help them get started
- Focus on what's best for elderly users
- Show empathy towards other contributors
- Accept constructive criticism gracefully

## 🎯 Contribution Priorities

We especially welcome contributions that:
1. **Improve accessibility** for elderly users
2. **Add more exercises** with proper video demonstrations
3. **Enhance UI clarity** and ease of use
4. **Fix bugs** that affect user experience
5. **Improve documentation** and user guides
6. **Add translations** to other languages

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 17+
- Git
- Android device/emulator with API 26+

### Setup
1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/YOUR_USERNAME/ExerciseGuideApp.git
   cd ExerciseGuideApp
   ```
3. Create a new branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## 📝 Development Guidelines

### UI/UX Requirements

**IMPORTANT**: This app is designed for elderly users. All changes must maintain:

#### Minimum Sizes
- **Text**: 24sp for body, 32sp for important elements
- **Buttons**: 56dp minimum touch target, 80dp preferred
- **Icons**: 24dp minimum
- **Spacing**: 16dp minimum between interactive elements

#### Accessibility
- **Contrast**: Maintain high contrast (WCAG AA minimum)
- **Colors**: Use the established green/white scheme
- **Feedback**: Provide clear visual feedback for all interactions
- **Simplicity**: Avoid complex gestures or multi-step processes

### Code Standards

#### Kotlin Style
```kotlin
// Good: Clear, descriptive names
fun markExerciseAsCompleted(exercise: Exercise) {
    // Implementation
}

// Bad: Unclear abbreviations
fun markExComp(ex: Ex) {
    // Implementation
}
```

#### Testing Requirements
- Test on physical devices when possible
- Test with system font scaling at 200%
- Test with screen readers enabled
- Verify videos play correctly
- Check memory usage (2GB device limit)

### Commit Messages

Follow conventional commits format:
```
type(scope): description

[optional body]

[optional footer]
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Test additions/changes
- `chore`: Maintenance tasks

Examples:
```bash
feat(exercises): add shoulder rehabilitation exercises
fix(video): resolve playback issue on Android 8 devices
docs(readme): update installation instructions
```

## 🔄 Pull Request Process

### Before Submitting

1. **Test Thoroughly**
   - [ ] Run the app on a physical device
   - [ ] Test all affected features
   - [ ] Verify no regressions in existing functionality
   - [ ] Check for memory leaks
   - [ ] Test with Russian language enabled

2. **Code Quality**
   - [ ] Run linter: `./gradlew ktlintCheck`
   - [ ] Format code: `./gradlew ktlintFormat`
   - [ ] Remove debug logs and test code
   - [ ] Update relevant documentation

3. **Accessibility Check**
   - [ ] All text ≥ 24sp
   - [ ] All buttons ≥ 56dp
   - [ ] Proper content descriptions for screen readers
   - [ ] High contrast maintained

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] UI/UX improvement
- [ ] Documentation update

## Testing
- [ ] Tested on physical device
- [ ] Tested on emulator
- [ ] Tested with large fonts
- [ ] Tested in Russian language

## Screenshots (if UI changes)
[Add before/after screenshots]

## Checklist
- [ ] Follows elderly-friendly design guidelines
- [ ] Maintains minimum size requirements
- [ ] Code follows Kotlin conventions
- [ ] Documentation updated
- [ ] No sensitive data exposed
```

### Review Process

1. Submit PR with clear description
2. Respond to review comments
3. Make requested changes
4. Ensure CI checks pass
5. PR will be merged after approval

## 🎥 Adding New Exercises

If you want to add new rehabilitation exercises:

### Video Requirements
- **Format**: MP4 (H.264 codec)
- **Resolution**: 720p or 1080p
- **Duration**: 30-60 seconds
- **Content**: Clear demonstration with good lighting
- **Audio**: Optional, but no background music

### Steps to Add Exercise

1. **Prepare Video**
   - Record exercise demonstration
   - Edit to show complete movement cycle
   - Compress to under 10MB
   - Name file: `exercise_name.mp4`

2. **Add Video File**
   ```
   app/src/main/res/raw/exercise_name.mp4
   ```

3. **Create Thumbnail**
   - Extract frame from video
   - Resize to 400x300px
   - Save as: `thumb_exercise_name.jpg`
   ```
   app/src/main/res/drawable-nodpi/thumb_exercise_name.jpg
   ```

4. **Update Database**
   - Add entry to `ExerciseRepository.kt`
   - Include Russian translation

5. **Update Strings**
   ```xml
   <!-- values/strings.xml -->
   <string name="exercise_name">Exercise Name</string>

   <!-- values-ru/strings.xml -->
   <string name="exercise_name">Название упражнения</string>
   ```

## 🌍 Translations

To add a new language:

1. Create language folder:
   ```
   app/src/main/res/values-[language_code]/
   ```

2. Copy and translate `strings.xml`

3. Focus on clarity for elderly users:
   - Use simple, clear language
   - Avoid technical terms
   - Keep instructions brief

## 🐛 Reporting Issues

### Bug Reports Should Include:
- Device model and Android version
- Steps to reproduce
- Expected vs actual behavior
- Screenshots/videos if applicable
- Relevant log output (use `adb logcat`)

### Feature Requests Should Include:
- Use case description
- How it benefits elderly users
- Mockups/sketches if applicable
- Similar features in other apps

## 📚 Resources

### Android Development
- [Android Developers Guide](https://developer.android.com/guide)
- [Material Design for Accessibility](https://material.io/design/usability/accessibility.html)
- [Kotlin Style Guide](https://kotlinlang.org/docs/coding-conventions.html)

### Elderly-Friendly Design
- [Designing for Older Adults](https://www.nngroup.com/articles/usability-for-senior-citizens/)
- [WHO Guidelines on Ageing](https://www.who.int/ageing/publications/guidelines/en/)
- [WCAG Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)

## 💬 Communication

### Getting Help
- Open an issue for bugs/features
- Use discussions for questions
- Tag issues appropriately

### Stay Updated
- Watch the repository for updates
- Join discussions on improvements
- Follow project milestones

## 🏆 Recognition

Contributors will be:
- Added to CONTRIBUTORS.md
- Mentioned in release notes
- Thanked in the app's about section (future feature)

## ⚖️ Legal

By contributing, you agree that your contributions will be licensed under the MIT License.

---

**Thank you for helping elderly users stay active and healthy! 🙏**

Every contribution, no matter how small, makes a difference in someone's rehabilitation journey.