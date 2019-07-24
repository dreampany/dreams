-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keep class * extends android.webkit.WebChromeClient { *; }
-dontwarn im.delight.android.webview.**



