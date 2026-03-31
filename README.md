# 📚 Ankit Study Point — Android App

A complete **D.Pharmacy study platform** Android app for [ankitstudypoint.blogspot.com](https://ankitstudypoint.blogspot.com).

---

## 🚀 Features

| Feature | Status |
|---|---|
| Full WebView Website | ✅ |
| Bottom Navigation (Home, Notes, Quiz, Tools, YouTube) | ✅ |
| Pharma Tools Grid (9 tools) | ✅ |
| Study Timer (Floating Button) | ✅ |
| Bookmark System (Offline) | ✅ |
| Dark Mode Support | ✅ |
| Splash Screen | ✅ |
| Push Notifications (Daily Reminder) | ✅ |
| Offline Page | ✅ |
| PDF Download Support | ✅ |
| Share Button | ✅ |
| Back Button Handling | ✅ |
| Progress Tracker | ✅ |
| Settings Page | ✅ |

---

## 📁 Project Structure

```
AnkitStudyPoint/
├── app/
│   ├── src/main/
│   │   ├── java/com/ankitstudypoint/app/
│   │   │   ├── MainActivity.kt           ← Bottom nav + fragments
│   │   │   ├── SplashActivity.kt         ← Animated splash screen
│   │   │   ├── WebViewActivity.kt        ← Full-featured WebView
│   │   │   ├── StudyTimerActivity.kt     ← Study timer page
│   │   │   ├── BookmarksActivity.kt      ← Saved bookmarks
│   │   │   ├── SettingsActivity.kt       ← App settings
│   │   │   ├── OfflineActivity.kt        ← No internet page
│   │   │   ├── AppConstants.kt           ← All URLs & keys
│   │   │   ├── adapters/
│   │   │   │   ├── ToolsAdapter.kt       ← Tools grid RecyclerView
│   │   │   │   └── BookmarksAdapter.kt   ← Bookmarks list
│   │   │   ├── fragments/
│   │   │   │   ├── WebViewFragment.kt    ← Base WebView fragment
│   │   │   │   ├── NavFragments.kt       ← Home/Notes/Quiz/YouTube
│   │   │   │   └── ToolsFragment.kt      ← Tools grid UI
│   │   │   ├── models/
│   │   │   │   └── ToolItem.kt           ← Tool data class
│   │   │   ├── utils/
│   │   │   │   ├── BookmarkManager.kt    ← Local bookmark storage
│   │   │   │   ├── PreferenceManager.kt  ← SharedPreferences
│   │   │   │   └── NetworkUtils.kt       ← Internet check
│   │   │   ├── services/
│   │   │   │   └── NotificationService.kt← Daily study reminders
│   │   │   └── receivers/
│   │   │       └── BootReceiver.kt       ← Restart notifications on boot
│   │   ├── res/
│   │   │   ├── layout/      ← All XML layouts
│   │   │   ├── drawable/    ← All vector icons
│   │   │   ├── anim/        ← All animations
│   │   │   ├── menu/        ← All menus
│   │   │   ├── values/      ← colors, strings, themes, dimens
│   │   │   └── xml/         ← network config, file paths, backup
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

---

## 🛠️ Setup Instructions

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17** or higher
- **Android SDK** API 34
- **Gradle 8.4**

### Step 1 — Clone / Download

```bash
git clone https://github.com/YOUR_USERNAME/AnkitStudyPoint.git
# OR extract the ZIP file
```

### Step 2 — Open in Android Studio

1. Open Android Studio
2. Click **"Open an Existing Project"**
3. Select the `AnkitStudyPoint` folder
4. Wait for Gradle sync to complete

### Step 3 — Set SDK Path

Edit `local.properties`:
```
# Windows example:
sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk

# Mac/Linux example:
sdk.dir=/Users/YourName/Library/Android/sdk
```

### Step 4 — Build & Run

**Run on device/emulator:**
```
Run → Run 'app'   (Shift + F10)
```

**Build Debug APK:**
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

**Build Release APK:**
```
Build → Generate Signed Bundle/APK → APK → Release
```

---

## 📱 APK Build via Command Line

```bash
# Windows
gradlew.bat assembleDebug

# Mac/Linux
chmod +x gradlew
./gradlew assembleDebug
```

Output APK location:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🎨 Customization

### Change URLs
Edit `AppConstants.kt`:
```kotlin
const val HOME_URL = "https://ankitstudypoint.blogspot.com/"
const val YOUTUBE_URL = "https://m.youtube.com/@rxvibeak"
// ... etc
```

### Change App Name
Edit `strings.xml`:
```xml
<string name="app_name">Ankit Study Point</string>
```

### Change Colors
Edit `colors.xml`:
```xml
<color name="primary">#1565C0</color>
<color name="secondary">#00897B</color>
```

### Add New Tool
In `ToolsFragment.kt`, add to the tools list:
```kotlin
ToolItem(
    id = 10,
    name = "New Tool",
    description = "Tool description",
    emoji = "🔬",
    url = "https://your-url.com",
    colorRes = R.color.tool_teal
)
```

---

## 🔔 Notifications

Notifications are scheduled using **WorkManager** — they fire once daily as study reminders. To change the interval, edit `NotificationService.kt`:

```kotlin
PeriodicWorkRequestBuilder<NotificationService>(24, TimeUnit.HOURS)
```

---

## 📦 Dependencies

| Library | Version | Purpose |
|---|---|---|
| Material Components | 1.11.0 | UI Components |
| WorkManager | 2.9.0 | Background notifications |
| Gson | 2.10.1 | Bookmark JSON storage |
| SwipeRefreshLayout | 1.1.0 | Pull-to-refresh |
| SplashScreen | 1.0.1 | Android 12+ splash |
| Coroutines | 1.7.3 | Async operations |

---

## 🌐 Website

**Blog:** https://ankitstudypoint.blogspot.com  
**YouTube:** https://youtube.com/@rxvibeak  
**GitHub Tools:** https://ankitkushwahaji938-hub.github.io

---

## 📄 License

This project is created for educational purposes for **Ankit Study Point**.

---

*Built with ❤️ for D.Pharmacy students*
