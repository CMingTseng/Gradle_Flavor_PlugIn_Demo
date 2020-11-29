# Gradle ProductFlavors & buildTypes & PlugIn Develop Demo

## The project base on mix-fork from 
## https://github.com/zincPower/FlavorDemo 
& 
## https://github.com/ximik3/gradle-drive

>目录 
>一、项目宗旨 
>二、已有章节 
>三、后续章节 
>四、如何使用该项目 
>五、其他项目推荐 
>六、作者简介

## 一、项目宗旨

本项目用于服务 [GradleStudy](https://github.com/zincPower/GradleStudy) 讲解多渠道包的一些知识。
&
Show ProductFlavors & buildTypes export as standalone gradle
&
how to import extra gradle 
&
how to import extra gradle 

![](https://github.com/zincPower/GradleStudy/blob/master/img/logo.png)

### 1、參考資料讲解可移步至以下：

Google: [Configure your build](https://developer.android.com/studio/build)

CSDN: [Android Gradle](https://blog.csdn.net/weixin_37625173/category_9350766.html)

掘金：[猛猛的小盆友](https://juejin.im/user/5c3033ef51882524ec3a88ba/posts)

### 2、代码：

github: [GradleStudy](https://github.com/zincPower/GradleStudy)

## 二、Gradle技巧

can export gradle some section as standalone

here use [Version](https://github.com/CMingTseng/Gradle_Flavor_PlugIn_Demo/blob/c7b5ced90bb1ad5618576474e200e29be2fda8bd/app/build.gradle#L228) to demo 

```sh
class Version {
    def versionCode
    def versionName
}

Version  readVersionWithFlavor(String flavor) {.....

```

now export  as standalone gradle file  : [version.gradle](https://github.com/CMingTseng/Gradle_Flavor_PlugIn_Demo/blob/master/app/version.gradle)

now use  apply from to import standalone gradle file :

```sh
apply from: './version.gradle'
```

[apply from: './version.gradle'](https://github.com/CMingTseng/Gradle_Flavor_PlugIn_Demo/blob/ef567cb2907434b9b208e398445e8f72dcdea911/app/build.gradle#L9)

but something is different 

now we must get version from extra 

so must firset define version 


```sh
def version = this.ext.default_version()
```
[def version = this.ext.default_version()](https://github.com/CMingTseng/Gradle_Flavor_PlugIn_Demo/blob/ef567cb2907434b9b208e398445e8f72dcdea911/app/build.gradle#L10)

## 三、已有章节

### 1、buildTypes

- Google: [Configure build variants](https://developer.android.com/studio/build/build-variants)

- CSDN: [buildTypes——安卓gradle](https://blog.csdn.net/weixin_37625173/article/details/100824010)

- 掘金: [buildTypes——安卓gradle](https://juejin.im/post/5d7e01125188253a8305480a)

- github代码: [传送门](https://github.com/zincPower/GradleStudy/blob/master/app/zinc_buildTypes.gradle)

base on debug/release  buildTypes as real project have  debug/release/staging/prod/dev/... development environment
so  can create extra development environment staging/sit/prod/dev/..  and all of extends some buildtype by initWith

[prod_debug.initWith(buildTypes.debug)....](https://github.com/CMingTseng/Gradle_Flavor_PlugIn_Demo/blob/0d2cff181f9f00b80f14c7c3032b5b5dcfe142b2/app/build.gradle#L103)

```sh
buildTypes {
        debug {
           ....
        }

        release {
            ....
        }

        prod_debug.initWith(buildTypes.debug)
        sit_debug.initWith(buildTypes.debug)
        dev_debug.initWith(buildTypes.debug)
        staging_debug {
            initWith debug
        }

        prod_release.initWith(buildTypes.release)
        sit_release.initWith(buildTypes.release)
        dev_release.initWith(buildTypes.release)

        prod_debug {}
        prod_release {}

        sit_debug {}
        sit_release {}

        dev_debug {}
        dev_release {}
}
```


### 2、defaultConfig

- CSDN: [defaultConfig——安卓gradle](https://blog.csdn.net/weixin_37625173/article/details/100641538)

- 掘金: [defaultConfig——安卓gradle](https://juejin.im/post/5d7baa7d51882554841c50d5)

- github代码: [传送门](https://github.com/zincPower/GradleStudy/blob/master/app/zinc_defaultConfig.gradle)

### 3、ProductFlavors

- Google: [Configure build variants](https://developer.android.com/studio/build/build-variants)

- CSDN: [flavorDimensions和productFlavors——安卓gradle](https://blog.csdn.net/weixin_37625173/article/details/100867037)

- 掘金: [flavorDimensions和productFlavors——安卓gradle](https://juejin.im/post/5da7215ef265da5b576bebbd)

- github代码: [传送门](https://github.com/zincPower/GradleStudy/blob/master/app/zinc_flavor.gradle)

### 4、ProductFlavors风味包配置

- CSDN: [android多渠道包（风味包）——安卓gradle](https://blog.csdn.net/weixin_37625173/article/details/102510549)

- 掘金: [android多渠道包（风味包）——安卓gradle](https://juejin.im/post/5da722dbf265da5b8e0f1773)

- github代码: [传送门](https://github.com/zincPower/FlavorDemo)

### 5、sourceSets

- CSDN: [sourceSets——安卓gradle](https://blog.csdn.net/weixin_37625173/article/details/102616036)

- 掘金: [sourceSets——安卓gradle](https://juejin.im/post/5dd9eda7f265da7de667d2bc)

- github代码: [传送门](https://github.com/zincPower/GradleStudy/blob/master/app/zinc_sourceSets.gradle)

### 6、lintOptions

- CSDN: [lintOptions——安卓gradle](https://blog.csdn.net/weixin_37625173/article/details/103236227)

- 掘金: [lintOptions——安卓gradle](https://juejin.im/post/5ddca7a0518825730753a31e)

- github代码: [传送门](https://github.com/zincPower/GradleStudy/blob/master/app/zinc_lintOptions.gradle)

### 7、splits

- Google: [Build multiple APKs](https://developer.android.com/studio/build/configure-apk-splits)

- CSDN: [splits——安卓gradle](https://blog.csdn.net/weixin_37625173/article/details/103284575)

- 掘金: [splits——安卓gradle](https://juejin.im/post/5ddfe513e51d45027e2a7e96)

- github代码: [传送门](https://github.com/zincPower/GradleStudy/blob/master/app/zinc_splits.gradle)

## 四、后续章节

- dexOptions
- externalNativeBuild
- aaptOptions
- adbOptions
- packagingOptions
- compileOptions
- dataBinding
- signingConfigs
- testOptions

## 五、如何使用该项目

1. 打开根目录下的 settings.gradle 文件

2. 修改下面这段代码
```
// xxx 即我们想运行的 gradle 配置
// 例如：我们想运行 lintOptions 的配置，此处的 xxx 替换为 zinc_lintOptions
project(':app').buildFileName = 'xxx.gradle'
```

## 六、其他项目推荐

### 1、Android 高级UI

简介：分享Android中UI的一些知识和应用

项目地址：[UI2018](https://github.com/zincPower/UI2018)

### 2、JRecycleView

简介：简单的让RecycleView更有趣

项目地址：[JRecycleView](https://github.com/zincPower/JRecycleView)

### 3、JPermission

简介：Android（安卓）基于注解的6.0权限动态申请

项目地址：[JPermission](https://github.com/zincPower/JPermission)

## 六、作者简介
### 1、个人博客

掘金：https://juejin.im/user/5c3033ef51882524ec3a88ba/posts

csdn：https://blog.csdn.net/weixin_37625173

### 2、赞赏

如果觉得博客对您有所帮助或启发，赞赏下吧。小盆友会写更多的优质文章与大家分享

![](https://github.com/zincPower/GradleStudy/blob/master/img/zincPay.jpg)