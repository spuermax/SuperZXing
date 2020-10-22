package com.developers.superzxing;

import com.supermax.base.QsApplication;
import com.supermax.base.common.http.HttpBuilder;

/**
 * @Author yinzh
 * @Date 2020/10/22 14:50
 * @Description
 */
public class SuperApplication extends QsApplication {
    @Override
    public boolean isLogOpen() {
        return true;
    }

    @Override
    public void initHttpAdapter(HttpBuilder builder) throws Exception {

    }
}
