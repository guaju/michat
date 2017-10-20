package com.guaju.michat;

import android.app.Application;
import android.content.Context;

import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

/**
 * Created by guaju on 2017/10/20.
 */

public class MyApplication extends Application {
    Context appContext;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
        initTXIM();
    }
     //初始化腾讯im
    private void initTXIM() {
        //初始化IMSDK
        /**
         * 参数2 是内容部debug中的索引，不是指特定的哪个debug
         */
        InitBusiness.start(getApplicationContext(),4);
        //初始化TLS
        TlsBusiness.init(getApplicationContext());
    }
}
