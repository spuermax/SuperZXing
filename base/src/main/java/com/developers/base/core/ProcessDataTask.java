package com.developers.base.core;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

/**
 * @Author yinzh
 * @Date 2020/10/22 15:38
 * @Description
 */
public class ProcessDataTask extends AsyncTask<Void, Void, ScanResult> {
    private Camera mCamera;
    private byte[] mData;
    private boolean mIsPortrait;
    private String mPicturePath;
    private Bitmap mBitmap;
    private WeakReference<SuperCodeView> mQRCodeViewRef;
    private static long sLastStartTime = 0;

    ProcessDataTask(Camera camera, byte[] data, SuperCodeView qrCodeView, boolean isPortrait) {
        mCamera = camera;
        mData = data;
        mQRCodeViewRef = new WeakReference<>(qrCodeView);
        mIsPortrait = isPortrait;
    }

    ProcessDataTask(String picturePath, SuperCodeView qrCodeView) {
        mPicturePath = picturePath;
        mQRCodeViewRef = new WeakReference<>(qrCodeView);
    }

    ProcessDataTask(Bitmap bitmap, SuperCodeView qrCodeView) {
        mBitmap = bitmap;
        mQRCodeViewRef = new WeakReference<>(qrCodeView);
    }

    ProcessDataTask perform() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return this;
    }

    void cancelTask() {
        if (getStatus() != Status.FINISHED) {
            cancel(true);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mQRCodeViewRef.clear();
        mBitmap = null;
        mData = null;
    }

    private ScanResult processData(SuperCodeView qrCodeView) {
        if (mData == null) {
            return null;
        }

        int width = 0;
        int height = 0;
        byte[] data = mData;
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            width = size.width;
            height = size.height;

            if (mIsPortrait) {
                data = new byte[mData.length];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        data[x * height + height - y - 1] = mData[x + y * width];
                    }
                }
                int tmp = width;
                width = height;
                height = tmp;
            }

            return qrCodeView.processData(data, width, height, false);
        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                if (width != 0 && height != 0) {
                    SuperCodeUtil.d("识别失败重试");
                    return qrCodeView.processData(data, width, height, true);
                } else {
                    return null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected ScanResult doInBackground(Void... params) {
        SuperCodeView qrCodeView = mQRCodeViewRef.get();
        if (qrCodeView == null) {
            return null;
        }

        if (mPicturePath != null) {
            return qrCodeView.processBitmapData(SuperCodeUtil.getDecodeAbleBitmap(mPicturePath));
        } else if (mBitmap != null) {
            ScanResult result = qrCodeView.processBitmapData(mBitmap);
            mBitmap = null;
            return result;
        } else {
            if (SuperCodeUtil.isDebug()) {
                SuperCodeUtil.d("两次任务执行的时间间隔：" + (System.currentTimeMillis() - sLastStartTime));
                sLastStartTime = System.currentTimeMillis();
            }
            long startTime = System.currentTimeMillis();

            ScanResult scanResult = processData(qrCodeView);

            if (SuperCodeUtil.isDebug()) {
                long time = System.currentTimeMillis() - startTime;
                if (scanResult != null && !TextUtils.isEmpty(scanResult.result)) {
                    SuperCodeUtil.d("识别成功时间为：" + time);
                } else {
                    SuperCodeUtil.e("识别失败时间为：" + time);
                }
            }

            return scanResult;
        }
    }

    @Override
    protected void onPostExecute(ScanResult result) {
        SuperCodeView qrCodeView = mQRCodeViewRef.get();
        if (qrCodeView == null) {
            return;
        }

        if (mPicturePath != null || mBitmap != null) {
            mBitmap = null;
            qrCodeView.onPostParseBitmapOrPicture(result);
        } else {
            qrCodeView.onPostParseData(result);
        }
    }
}

