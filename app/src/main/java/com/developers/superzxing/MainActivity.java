package com.developers.superzxing;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.developers.base.ZXingView;
import com.developers.base.core.SuperCodeView;
import com.supermax.base.common.permission.annotation.Permission;
import com.supermax.base.common.permission.annotation.PermissionCanceled;
import com.supermax.base.common.permission.annotation.PermissionDenied;
import com.supermax.base.common.permission.model.CancelModel;
import com.supermax.base.common.permission.model.DenyModel;
import com.supermax.base.common.viewbind.annotation.Bind;
import com.supermax.base.common.widget.toast.QsToast;
import com.supermax.base.mvp.QsActivity;

public class MainActivity extends QsActivity implements SuperCodeView.Delegate {

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
}