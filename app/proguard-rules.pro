# ProGuard rules for AnkitStudyPoint

# Keep app class names for WebView JS interface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep Kotlin metadata
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Keep AndroidX
-keep class androidx.** { *; }
-dontwarn androidx.**

# Keep Material Components
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# Keep Gson models (Bookmarks)
-keep class com.ankitstudypoint.app.utils.BookmarkManager$Bookmark { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep WorkManager
-keep class androidx.work.** { *; }
-dontwarn androidx.work.**

# Keep WebView
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}

# Keep app entry points
-keep class com.ankitstudypoint.app.MainActivity { *; }
-keep class com.ankitstudypoint.app.SplashActivity { *; }
-keep class com.ankitstudypoint.app.WebViewActivity { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
