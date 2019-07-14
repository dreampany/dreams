#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile

-keep class com.dreampany.lca.data.model.** { *; }
-keepclassmembers class com.dreampany.lca.data.model.** { *; }

-keep class com.dreampany.lca.ui.model.** { *; }
-keepclassmembers class com.dreampany.lca.ui.model.** { *; }

-keep class com.dreampany.lca.misc.** { *; }
-keepclassmembers class com.dreampany.lca.misc.** { *; }

-keep class com.dreampany.lca.api.** { *; }
-keepclassmembers class com.dreampany.lca.api.** { *; }

-keep class com.dreampany.frame.misc.** { *; }
-keepclassmembers class com.dreampany.frame.misc.** { *; }