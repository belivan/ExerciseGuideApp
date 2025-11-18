# Setting Up Exercise Videos

## Video File Requirements

Your exercise videos should be:
- **Format**: MP4 (H.264 codec)
- **Resolution**: 720p (1280x720) recommended
- **Frame rate**: 30fps
- **File size**: 10-20MB per minute
- **Duration**: 30-60 seconds per exercise

## Where to Place Video Files

Place your video files in:
```
app/src/main/res/raw/
```

Create the `raw` directory if it doesn't exist:
```bash
mkdir -p app/src/main/res/raw
```

## Video File Naming

Name your video files to match the database entries:
- `knee_bend.mp4`
- `leg_raise.mp4`
- `heel_slide.mp4`
- `quad_sets.mp4`
- `ankle_pumps.mp4`

## Creating Sample Videos (For Testing)

If you don't have exercise videos yet, you can:

### Option 1: Use Placeholder Videos
Create simple placeholder videos using FFmpeg:

```bash
# Install FFmpeg if not already installed
brew install ffmpeg

# Create a 30-second placeholder video with exercise name
for exercise in "knee_bend" "leg_raise" "heel_slide" "quad_sets" "ankle_pumps"; do
  ffmpeg -f lavfi -i color=c=blue:s=1280x720:d=30 \
    -vf "drawtext=fontfile=/System/Library/Fonts/Helvetica.ttc:text='$exercise':fontcolor=white:fontsize=72:x=(w-text_w)/2:y=(h-text_h)/2" \
    -c:v libx264 -pix_fmt yuv420p \
    "app/src/main/res/raw/${exercise}.mp4"
done
```

### Option 2: Download Free Exercise Videos
You can find free rehabilitation exercise videos from:
- YouTube (with permission)
- Physical therapy educational resources
- Creative Commons licensed content

### Option 3: Record Your Own
1. Use your phone to record exercise demonstrations
2. Keep videos short (30-60 seconds)
3. Ensure good lighting and clear visibility
4. Convert to MP4 if needed

## Converting Videos to Proper Format

If your videos are in a different format or too large:

```bash
# Convert and compress video
ffmpeg -i input_video.mov -c:v libx264 -preset slow -crf 22 \
  -s 1280x720 -r 30 -c:a aac -b:a 128k \
  output_video.mp4
```

## Adding Videos to the Project

1. Copy your video files to `app/src/main/res/raw/`
2. Ensure file names match the database entries (without .mp4 extension in code)
3. Build the project - Android Studio will include them in the APK

## Testing Videos

After adding videos:
1. Clean and rebuild the project
2. Run on emulator or device
3. Videos should play when you tap on an exercise

## Troubleshooting

If videos don't play:
- Check file names match exactly (case-sensitive)
- Verify MP4 format with H.264 codec
- Ensure files are in `res/raw/` directory
- Check Android Studio recognizes the files (should appear in project view)
- Try cleaning and rebuilding: `./gradlew clean build`

## Sample Video Sources

For actual rehabilitation exercises, consider:
- Consulting with a physical therapist for proper form
- Using educational resources from medical institutions
- Recording demonstrations with professional guidance

Remember: Since this app is for your grandfather's rehabilitation, ensure all exercises are approved by his physical therapist or doctor.