 # Add project specific ProGuard rules here.
 # By default, the flags in this file are appended to flags specified
 # in D:\Develop_Tools\sdk/tools/proguard/proguard-android.txt
 # You can edit the include path and order by changing the proguardFiles
 # directive in build.gradle.
 #
 # For more details, see
 #   http://developer.android.com/guide/developing/tools/proguard.html

 # Add any project specific keep options here:

 # If your project uses WebView with JS, uncomment the following
 # and specify the fully qualified class name to the JavaScript interface
 # class:
 # Add project specific ProGuard rules here.
 # You can control the set of applied configuration files using the
 # proguardFiles setting in build.gradle.
 #
 # For more details, see
 #   http://developer.android.com/guide/developing/tools/proguard.html

 # If your project uses WebView with JS, uncomment the following
 # and specify the fully qualified class name to the JavaScript interface
 # class:
 #-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 #   public *;
 #}

 # Uncomment this to preserve the line number information for
 # debugging stack traces.
 #-keepattributes SourceFile,LineNumberTable

 # If you keep the line number information, uncomment this to
 # hide the original source file name.
 #-renamesourcefileattribute SourceFile
 -optimizationpasses 5
 -keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
 }
 ## 不忽略非公共的库类
 -dontskipnonpubliclibraryclasses
  # 是否使用大小写混合
 -dontusemixedcaseclassnames
   # 混淆时是否做预校验
 -dontpreverify
 -verbose
 -dontoptimize
 #
 -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
 ## 保持注解
 -keepattributes *Annotation*
 ##保持哪些类不被混淆
 #
 ## 保持哪些类不被混淆
 -keep public class * extends android.support.** #如果有引用v4或者v7包，需添加
 -keep public class * extends android.app.Fragment
 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep public class com.android.vending.licensing.ILicensingService
 -keep public class com.isgala.xishuashua.view.*
 ##如果有引用v4包可以添加下面这行
 -keep public class * extends android.support.v4.app.Fragment
 -keep public class * extends android.support.v7.app.Fragment
 ###记录生成的日志数据,gradle build时在本项目根目录输出 start##
 ##apk 包内所有 class 的内部结构
 -dump proguard/class_files.txt
 ##未混淆的类和成员
 -printseeds proguard/seeds.txt
 ##列出从 apk 中删除的代码
 -printusage proguard/unused.txt
 ##混淆前后的映射
 -printmapping proguard/mapping.txt
 #########记录生成的日志数据，gradle build时 在本项目根目录输出-end######
    #混淆第三方jar包，其中xxx为jar包名
  # 保持 native 方法不被混淆
 -keepclasseswithmembernames class * {
     native <methods>;
 }
 # 保持自定义控件类不被混淆
 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }
   # 保持自定义控件类不被混淆
 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }



 #保持 Serializable 不被混淆
 -keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     !static !transient <fields>;
     !private <fields>;
     !private <methods>;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }

 #保持枚举 enum 类不被混淆
 -keepclassmembers enum * {
   public static **[] values();
   public static ** valueOf(java.lang.String);
 }


     -keep class com.zhuochi.hydream.bathhousekeeper.bean.** { *; }
       -keep class  com.zhuochi.hydream.bathhousekeeper.entity.** { *; }

 # ButterKnife
     -keep class butterknife.** { *; }
     -dontwarn butterknife.internal.**
     -keep class **$$ViewBinder { *; }

     -keepclasseswithmembernames class * {
         @butterknife.* <fields>;
     }

     -keepclasseswithmembernames class * {
         @butterknife.* <methods>;
     }

 #gson
 #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
 -keepattributes Signature
 # Gson specific classes
 -keep class sun.misc.Unsafe { *; }
 # Application classes that will be serialized/deserialized over Gson
 -keep class com.google.gson.** { *; }
 -keep class com.google.gson.stream.** { *; }


 # 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。
 -keep class com.matrix.app.entity.json.** { *; }
 -keep class com.matrix.appsdk.network.model.** { *; }


 #支付宝混淆代码

 #-libraryjars libs/alipaySdk-20161009.jar

 -keep class com.alipay.android.app.IAlixPay{*;}
 -keep class com.alipay.android.app.IAlixPay$Stub{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
 -keep class com.alipay.sdk.app.PayTask{ public *;}
 -keep class com.alipay.sdk.app.AuthTask{ public *;}

 #微信支付
 #-libraryjars libs/libammsdk.jar
 -keep class com.tencent.** { *;}
  -keep class  com.klcxkj.** { *;}



 # 其他的三方库
 -keep class com.nineoldandroids.** { *; }
 -keep interface com.nineoldandroids.** { *; }
 -dontwarn com.nineoldandroids.**


 -keep class com.qiniu.android.** { *; }
 -keep interface com.qiniu.android.** { *; }
 -dontwarn com.qiniu.android.**


 #不混淆指定文件
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }

   # 忽略警告
 -ignorewarning
