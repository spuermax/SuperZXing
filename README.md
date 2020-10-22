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

   #### step 3：xml布局里面 引入 ZXingView
   
   
```
    <com.developers.base.ZXingView
        android:id="@+id/zxing_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toBottomOf="@id/tv_title"
        app:qrcv_animTime="1000"
        app:qrcv_borderColor="@android:color/white"
        app:qrcv_borderSize="1dp"
        app:qrcv_cornerColor="@color/colorPrimaryDark"
        app:qrcv_cornerLength="20dp"
        app:qrcv_cornerSize="3dp"
        app:qrcv_maskColor="#33FFFFFF"
        app:qrcv_rectWidth="200dp"
        app:qrcv_scanLineColor="@color/colorPrimaryDark"
        app:qrcv_scanLineSize="1dp"
        app:qrcv_topOffset="90dp"
        />
        
        
```

   #### step 4：Activity 里面调用 ZXIngView
   
  ``` 
    @Bind(R.id.zxing_view)
    private ZXingView mZXingView;


    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Permission(value = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE},
            requestCode = 1
    )
    @Override
    public void initData(Bundle savedInstanceState) {
        mZXingView.setDelegate(this);

    }

    @PermissionDenied
    public void denyPermission(DenyModel denyModel) {

    }

    @PermissionCanceled
    public void cancelPermission(CancelModel model) {
    }

    @Override
    protected void onStart() {
        super.onStart();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别

    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result 摄像头扫码时只要回调了该方法 result 就一定有值，不会为 null。
     *               解析本地图片或 Bitmap 时 result 可能为 null
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        QsToast.show(result);
        vibrate();

//        mZXingView.startSpot(); // 开始识别

    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     * 摄像头环境亮度发生变化
     *
     * @param isDark 是否变暗
     */
    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            QsToast.show(ambientBrightnessTip);
        }

        Log.i("AAAAAAAAAA",isDark + "----");
    }

    /**
     * 处理打开相机出错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        QsToast.show("打开相机出错33");
    }
    
    ```


   





