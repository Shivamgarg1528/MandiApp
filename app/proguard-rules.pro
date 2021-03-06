# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android_Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
##---------------Begin: proguard configuration to ease retracing ----------
-keepattributes SourceFile,LineNumberTable

##---------------Begin: proguard configuration for GSON ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# pojo classes that will be serialized/deserialized over Gson
-keep class ig.com.digitalmandi.bean** { *; }

-dontwarn com.fasterxml.**
-dontwarn okio.**
-dontwarn retrofit2.**