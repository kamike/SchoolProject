package com.pursuege.schoolproject.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.blankj.utilcode.util.ServiceUtils;
import com.pursuege.schoolproject.R;
import com.pursuege.schoolproject.bean.CidDataBean;
import com.pursuege.schoolproject.bean.MncCidBean;
import com.pursuege.schoolproject.utils.CidIdUtils;

import java.util.ArrayList;

import static com.pursuege.schoolproject.R.drawable.icon_mobile_enable;

public class MyBackgroundService extends Service {
    public static ArrayList<CidDataBean> listCidAll;
    private Vibrator vibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        vibrator  = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
                    boolean isSendNoify = false;
                    for (CidDataBean info : listCidAll) {
                        if (TextUtils.equals(info.mnc + "", mainMnc.mnc)) {
                            if (TextUtils.equals(info.cidId + "", mainMnc.cidId)) {
                                //在优惠区
                                handler.sendEmptyMessage(0);
                                isSendNoify = true;
                                break;
                            }
                        }

                    }
                    //不在优惠区
                    if (!isSendNoify) {
                        handler.sendEmptyMessage(1);
                    }
                    sleepTime(5000);
                }
            }


        }.start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SharedPreferences share = getApplication().getSharedPreferences("share", MODE_PRIVATE);
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
            nb.setSmallIcon(icon_mobile_enable).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_mobile_enable));
            nb.setContentInfo("移动...").setWhen(System.currentTimeMillis());
            nb.setOngoing(true);
            nb.setAutoCancel(true);


            if (share.getBoolean("isJar", true)) {

                long[] pattern = {50, 200, 50, 200};   // 停止 开启 停止 开启
                if (share.getBoolean("isMusic", true)) {
                    nb.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                    vibrator.vibrate(pattern, 0);
                } else {
                    nb.setDefaults(Notification.DEFAULT_VIBRATE);
                    vibrator.vibrate(pattern, -1);
                }
            } else {
                if (share.getBoolean("isMusic", true)) {
                    nb.setDefaults(Notification.DEFAULT_SOUND);
                }
            }

            NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(msg.what, nb.build());
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
        if(vibrator!=null){
            vibrator.cancel();
        }
    }
}
