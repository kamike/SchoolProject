package com.pursuege.schoolproject.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.blankj.utilcode.util.ServiceUtils;
import com.pursuege.schoolproject.bean.CidDataBean;
import com.pursuege.schoolproject.bean.MncCidBean;
import com.pursuege.schoolproject.utils.CidIdUtils;
import com.pursuege.schoolproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.pursuege.schoolproject.R.drawable.icon_mobile_enable;
import static com.pursuege.schoolproject.R.drawable.icon_mobile_unenable;
import static com.pursuege.schoolproject.R.drawable.icon_telcom_enable;
import static com.pursuege.schoolproject.R.drawable.icon_telcom_unenable;
import static com.pursuege.schoolproject.R.drawable.icon_unicom_enable;
import static com.pursuege.schoolproject.R.drawable.icon_unicom_unenable;

public class MyBackgroundService extends Service {
    /**
     * 从v本地获取到的缓存
     */
    public static ArrayList<CidDataBean> listCacheCidAll;
    private Vibrator vibrator;

    /**
     * 双卡数据
     */
    public static MncCidBean[] allMncList = new MncCidBean[2];
    public static final int timeDelay = 10 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        CidIdUtils.setCidListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    LogUtils.i("onCreate====while -===true");
                    if (listCacheCidAll == null) {
                        LogUtils.i("缓存的数据是空的");
                        sleepTime(timeDelay);
                        continue;
                    }

                    MncCidBean mainMncList = CidIdUtils.getMainMncCid(getApplication());
                    LogUtils.i("主卡数据：" + mainMncList);
                    boolean isSendNoify = false;
                    //判断主卡
                    if (mainMncList != null) {
                        for (CidDataBean cacheCid : listCacheCidAll) {
                            if (mainMncList.mnc == cacheCid.mnc) {
                                if (TextUtils.equals(mainMncList.cidId + "", cacheCid.cid)) {
                                    //在优惠区
                                    handler.sendEmptyMessage(cacheCid.mnc);
                                    isSendNoify = true;
                                    break;
                                }
                            }
                        }
                    }

                    //双卡数据
                    LogUtils.i("副卡数据:" + Arrays.toString(allMncList));
                    if (allMncList != null && !isSendNoify) {
                        OUT:
                        for (CidDataBean info : listCacheCidAll) {

                            for (MncCidBean mncCid : allMncList) {
                                if (mncCid == null) {
                                    continue;
                                }
                                if (TextUtils.equals(info.cid + "", mncCid.cidId)) {
                                    //在优惠区
                                    handler.sendEmptyMessage(info.mnc);
                                    isSendNoify = true;
                                    break OUT;
                                }
                            }

                        }
                    }

                    int mnc = 1;
                    if (listCacheCidAll.size() > 1) {
                        mnc = listCacheCidAll.get(0).mnc;
                    }
                    //不在优惠区
                    if (!isSendNoify) {
                        handler.sendEmptyMessage(mnc * -1);
                    }
                    sleepTime(timeDelay);
                }
            }


        }.start();

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SharedPreferences share = getApplication().getSharedPreferences("share", MODE_PRIVATE);

            if (share.getBoolean("isJar", true)) {
                long[] pattern = {10, 300,50,500};   // 停止 开启 停止 开启
                vibrator.vibrate(pattern, -1);


            }
            if (share.getBoolean("isMusic", true)) {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }

            //没有推送就什么都不做了
            if (!share.getBoolean("isMessage", true)) {
                return;
            }

            NotificationCompat.Builder nb = new NotificationCompat.Builder(MyBackgroundService.this);
            if (msg.what == 0) {
                nb.setContentTitle(null).setContentText("您已进入优惠基站范围，请断开一次数据连接后再使用");
            } else {
                nb.setContentTitle(null).setContentText("您已离开基站范围，请注意使用");
            }
            switch (msg.what) {
                case 1:
                    nb.setSmallIcon(icon_unicom_enable).setLargeIcon(BitmapFactory.decodeResource(getResources(), icon_unicom_enable));
                    break;
                case 3:
                    nb.setSmallIcon(icon_telcom_enable).setLargeIcon(BitmapFactory.decodeResource(getResources(), icon_telcom_enable));
                    break;
                case 5:
                    nb.setSmallIcon(icon_mobile_enable).setLargeIcon(BitmapFactory.decodeResource(getResources(), icon_mobile_enable));
                    break;

                case -1:
                    nb.setSmallIcon(icon_unicom_unenable).setLargeIcon(BitmapFactory.decodeResource(getResources(), icon_unicom_unenable));
                    break;
                case -3:
                    nb.setSmallIcon(icon_telcom_unenable).setLargeIcon(BitmapFactory.decodeResource(getResources(), icon_telcom_unenable));
                    break;
                case -5:
                    nb.setSmallIcon(icon_mobile_unenable).setLargeIcon(BitmapFactory.decodeResource(getResources(), icon_mobile_unenable));
                    break;
            }
            nb.setOngoing(true);
            nb.setAutoCancel(true);


            NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(998, nb.build());
        }


    };

    private void sleepTime(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceUtils.startService(MyBackgroundService.class);
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
