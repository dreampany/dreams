#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile

-keep class com.dreampany.cast.data.model.** { *; }
-keepclassmembers class com.dreampany.cast.data.model.** { *; }

-keep class com.dreampany.cast.ui.model.** { *; }
-keepclassmembers class com.dreampany.cast.ui.model.** { *; }

-keep class com.dreampany.cast.misc.** { *; }
-keepclassmembers class com.dreampany.cast.misc.** { *; }

-keep class com.dreampany.frame.misc.** { *; }
-keepclassmembers class com.dreampany.frame.misc.** { *; }