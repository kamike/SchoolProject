package com.pursuege.schoolproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.blankj.utilcode.util.ServiceUtils;
import com.pursuege.schoolproject.bean.CidDataBean;
import com.pursuege.schoolproject.bean.MncCidBean;
import com.pursuege.schoolproject.utils.CidIdUtils;

import java.util.ArrayList;

public class MyBackgroundService extends Service {
    public static ArrayList<CidDataBean> listCidAll;

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (listCidAll == null) {
                        sleepTime(5000);
                        return;
                    }

                    MncCidBean mainMnc = CidIdUtils.getMainMncCid(getApplication());
                    if (mainMnc == null) {
                        return;
                    }
                    for (CidDataBean info : listCidAll) {
                        if (TextUtils.equals(info.mnc + "", mainMnc.mnc)) {
                            if (TextUtils.equals(info.cidId + "", mainMnc.cidId)) {
                                //在优惠区
                            }
                        }

                    }
                    //不在优惠区


                    sleepTime(5000);
                }
            }
        }.start();

    }

    private void sleepTime(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, START_FLAG_RETRY, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceUtils.startService(MyBackgroundService.class);
    }
}
