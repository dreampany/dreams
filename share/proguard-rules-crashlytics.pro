-keepattributes *Annotation*                      // Keep Crashlytics annotations
-keepattributes SourceFile,LineNumberTable        // Keep file names/line numbers
-keep public class * extends java.lang.Exception

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**