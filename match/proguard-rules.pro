#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile

-keep class com.dreampany.match.data.model.** { *; }
-keepclassmembers class com.dreampany.match.data.model.** { *; }

-keep class com.dreampany.match.ui.model.** { *; }
-keepclassmembers class com.dreampany.match.ui.model.** { *; }

-keep class com.dreampany.match.misc.** { *; }
-keepclassmembers class com.dreampany.match.misc.** { *; }

-keep class com.dreampany.frame.misc.** { *; }
-keepclassmembers class com.dreampany.frame.misc.** { *; }