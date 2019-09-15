#debugging and stack trace
#-repackageclasses
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*, Signature, Exception
#-optimizations !method/removal/parameter
-ignorewarnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-renamesourcefileattribute SourceFile

#framework
-keep class com.dreampany.framework.data.model.** { *; }
-keepclassmembers class com.dreampany.framework.data.model.** { *; }

-keep class com.dreampany.framework.ui.model.** { *; }
-keepclassmembers class com.dreampany.framework.ui.model.** { *; }

-keep class com.dreampany.framework.misc.** { *; }
-keepclassmembers class com.dreampany.framework.misc.** { *; }

-keep class com.dreampany.translation.data.model.** { *; }
-keepclassmembers class com.dreampany.translation.data.model.** { *; }

-keep class com.dreampany.language.data.model.** { *; }
-keepclassmembers class com.dreampany.language.data.model.** { *; }

-keep class com.dreampany.firebase.data.model.** { *; }
-keepclassmembers class com.dreampany.firebase.data.model.** { *; }

#app
-keep class com.dreampany.tools.data.model.** { *; }
-keepclassmembers class com.dreampany.tools.data.model.** { *; }

-keep class com.dreampany.tools.ui.model.** { *; }
-keepclassmembers class com.dreampany.tools.ui.model.** { *; }

-keep class com.dreampany.tools.misc.** { *; }
-keepclassmembers class com.dreampany.tools.misc.** { *; }

-keep class com.dreampany.tools.api.** { *; }
-keepclassmembers class com.dreampany.tools.api.** { *; }