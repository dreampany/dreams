#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile

-keep class com.dreampany.word.data.model.** { *; }
-keepclassmembers class com.dreampany.word.data.model.** { *; }

-keep class com.dreampany.word.ui.model.** { *; }
-keepclassmembers class com.dreampany.word.ui.model.** { *; }

-keep class com.dreampany.word.misc.** { *; }
-keepclassmembers class com.dreampany.word.misc.** { *; }

-keep class com.dreampany.word.api.** { *; }
-keepclassmembers class com.dreampany.word.api.** { *; }

-keep class com.dreampany.frame.data.model.** { *; }
-keepclassmembers class com.dreampany.frame.data.model.** { *; }

-keep class com.dreampany.frame.ui.model.** { *; }
-keepclassmembers class com.dreampany.frame.ui.model.** { *; }

-keep class com.dreampany.frame.misc.** { *; }
-keepclassmembers class com.dreampany.frame.misc.** { *; }

-keep class com.dreampany.translation.data.model.** { *; }
-keepclassmembers class com.dreampany.translation.data.model.** { *; }

