#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile
-keepattributes javax.xml.bind.annotation.*

-keep class com.dreampany.frame.data.model.** { *; }
-keepclassmembers class com.dreampany.frame.data.model.** { *; }

-keep class com.dreampany.frame.ui.model.** { *; }
-keepclassmembers class com.dreampany.frame.ui.model.** { *; }