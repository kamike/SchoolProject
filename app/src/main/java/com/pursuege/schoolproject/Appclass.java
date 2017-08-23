package com.pursuege.schoolproject;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Administrator on 2017/8/23.
 */

public class Appclass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
