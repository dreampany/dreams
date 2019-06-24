#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile

-keep class com.dreampany.demo.data.model.** { *; }
-keepclassmembers class com.dreampany.demo.data.model.** { *; }

-keep class com.dreampany.demo.ui.model.** { *; }
-keepclassmembers class com.dreampany.demo.ui.model.** { *; }