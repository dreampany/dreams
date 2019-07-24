#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile

-keep class com.dreampany.history.data.model.** { *; }
-keepclassmembers class com.dreampany.history.data.model.** { *; }

-keep class com.dreampany.history.ui.model.** { *; }
-keepclassmembers class com.dreampany.history.ui.model.** { *; }

-keep class com.dreampany.history.misc.** { *; }
-keepclassmembers class com.dreampany.history.misc.** { *; }

-keep class com.dreampany.history.api.** { *; }
-keepclassmembers class com.dreampany.history.api.** { *; }

-keep class com.dreampany.frame.data.model.** { *; }
-keepclassmembers class com.dreampany.frame.data.model.** { *; }

-keep class com.dreampany.frame.ui.model.** { *; }
-keepclassmembers class com.dreampany.frame.ui.model.** { *; }

-keep class com.dreampany.frame.misc.** { *; }
-keepclassmembers class com.dreampany.frame.misc.** { *; }

-keep class com.dreampany.translation.data.model.** { *; }
-keepclassmembers class com.dreampany.translation.data.model.** { *; }