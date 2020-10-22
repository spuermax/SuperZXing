package com.developers.base.core;

import android.graphics.PointF;

/**
 * @Author yinzh
 * @Date 2020/10/22 15:40
 * @Description
 */
public class ScanResult {
    String result;
    PointF[] resultPoints;

    public ScanResult(String result) {
        this.result = result;
    }

    public ScanResult(String result, PointF[] resultPoints) {
        this.result = result;
        this.resultPoints = resultPoints;
    }
}
