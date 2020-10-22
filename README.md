# SuperZXing
ZXing 二维码扫面使用demo 。

## 如何使用
#### step 1：项目的build.gradle添加依赖

```
buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url 'https://jitpack.io'
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.3.2'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://www.jitpack.io' }

    }
}

```

  #### step 2：app的build.gradle添加依赖

```
 dependencies {
            ...
       implementation 'com.github.spuermax:SuperZXing:v1.0'
        }   

```




