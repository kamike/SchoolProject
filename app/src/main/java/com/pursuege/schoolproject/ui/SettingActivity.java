package com.pursuege.schoolproject.ui;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.pursuege.schoolproject.R;
import com.pursuege.schoolproject.bean.BaseServerBean;
import com.pursuege.schoolproject.bean.CidDataBean;
import com.pursuege.schoolproject.bean.MncCidBean;
import com.pursuege.schoolproject.service.MyBackgroundService;
import com.pursuege.schoolproject.utils.CidIdUtils;
import com.pursuege.schoolproject.utils.LogUtils;
import com.pursuege.schoolproject.utils.NetworkCore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingActivity extends BaseTitleActivity {

    private ImageView ivMsg, ivjar, ivMusic;
    private Button btnSubmit;

    @Override
    public String setTopTitle() {
        return getResources().getString(R.string.setting_noftify_type);
    }

    @Override
    public View getContentBaseView() {
        return setInflateView(R.layout.activity_setting);
    }

    @Override
    public void setupUiView() {
        super.setupUiView();
        ivMsg = (ImageView) findViewById(R.id.setting_msg_switch_iv);
        ivjar = (ImageView) findViewById(R.id.setting_jar_switch_iv);
        ivMusic = (ImageView) findViewById(R.id.setting_music_switch_iv);
        btnSubmit = (Button) findViewById(R.id.apply_notify_next_btn);

        EventBus.getDefault().register(this);
        share = getApplication().getSharedPreferences("share", MODE_PRIVATE);
        isMessage = share.getBoolean("isMessage", true);
        isJar = share.getBoolean("isJar", true);
        isMusic = share.getBoolean("isMusic", true);
        if (isMessage) {
            ivMsg.setImageResource(R.drawable.icon_switch_no);
        } else {
            ivMsg.setImageResource(R.drawable.icon_switch_off);
        }
        if (isJar) {
            ivjar.setImageResource(R.drawable.icon_switch_no);
        } else {
            ivjar.setImageResource(R.drawable.icon_switch_off);
        }
        if (isMusic) {
            ivMusic.setImageResource(R.drawable.icon_switch_no);
        } else {
            ivMusic.setImageResource(R.drawable.icon_switch_off);
        }

    }

    SharedPreferences share;
    private String cidId;
    private String mncSim1;
    private String mncSim2;

    @Override
    public void setupAllData() {
        if (share.getBoolean("isSaveData", false)) {
            btnSubmit.setVisibility(View.GONE);
            getCacheData();
            return;
        } else {
            btnSubmit.setVisibility(View.VISIBLE);
        }

        cidId = getIntent().getStringExtra("id_cid");
        mncSim1 = getIntent().getStringExtra("mncSIm1");
        mncSim2 = getIntent().getStringExtra("mncSIm2");
        LogUtils.i("最终请求参数：" + cidId + "," + mncSim1 + "," + mncSim2);

        if (TextUtils.isEmpty(cidId)) {
            doShowToast("cid未获取到请重新选择!");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }
        //test
//        cidId = "200100";
//        mncSim1 = "3";

        if (!TextUtils.isEmpty(mncSim1)) {
            StringBuilder sb = new StringBuilder(selectCidData);
            sb.append("?cidId=").append(cidId);
            sb.append("&mnc=").append(mncSim1);

            NetworkCore.doGetCid(sb.toString(), "1");
        }

        if (!TextUtils.isEmpty(mncSim2) && !TextUtils.equals(mncSim1, mncSim2)) {
            StringBuilder sb = new StringBuilder(selectCidData);
            sb.append("?cidId=").append(cidId);
            sb.append("&mnc=").append(mncSim2);
            NetworkCore.doGetCid(sb.toString(), "2");
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFail(String msg) {
        if (TextUtils.isEmpty(msg)) {
            doShowMesage(NetwException, null);
            return;
        }
        doShowMesage(msg, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(BaseServerBean base) {
        if (!TextUtils.equals(base.state, "success")) {
            doShowMesage(base.message, null);
            return;
        }
        if (TextUtils.isEmpty(base.data)) {
            doShowMesage("该校区没有任何数据!", null);
            return;
        }
        File f = null;
        if (TextUtils.equals(base.flag, "1")) {
            //根据文件名字来区分存的运营商
            File file = new File(Environment.getExternalStorageDirectory() + "/data/" + mncSim1);
            if (!file.exists()) {
                file.mkdirs();
            }
            f = new File(file.getAbsolutePath() + "/cid_all_data");
            if (f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            File file = new File(Environment.getExternalStorageDirectory() + "/data/" + mncSim2);
            if (!file.exists()) {
                file.mkdirs();
            }
            f = new File(file.getAbsolutePath() + "/cid_all_data");
            if (f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FileIOUtils.writeFileFromString(f, base.data);
        doShowToast("存储数据成功!");

    }

    private boolean isMessage = true;
    private boolean isJar = true;
    private boolean isMusic = true;

    public void onclickNotifySwitch(View v) {
        if (isMessage) {
            isMessage = false;
            ivMsg.setImageResource(R.drawable.icon_switch_off);
//            //通知关掉了
//            isJar = true;
//            onclickJarSwitch(null);
//            isMusic = true;
//            onclickMusicSwitch(null);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(998);
        } else {
            isMessage = true;
            ivMsg.setImageResource(R.drawable.icon_switch_no);
        }
        share.edit().putBoolean("isMessage", isMessage).commit();
    }

    public void onclickJarSwitch(View v) {
        if (isJar) {
            isJar = false;
            ivjar.setImageResource(R.drawable.icon_switch_off);
        } else {
            isJar = true;
            ivjar.setImageResource(R.drawable.icon_switch_no);
        }
        share.edit().putBoolean("isJar", isJar).commit();

    }

    public void onclickMusicSwitch(View v) {
        if (isMusic) {
            isMusic = false;
            ivMusic.setImageResource(R.drawable.icon_switch_off);
        } else {
            isMusic = true;
            ivMusic.setImageResource(R.drawable.icon_switch_no);
        }
        share.edit().putBoolean("isMusic", isMusic).commit();
    }

    public void onclickSubmit(View v) {
        File file = new File(Environment.getExternalStorageDirectory() + "/data/");
        File[] fileArray = file.listFiles();
        if (fileArray == null || fileArray.length <= 0) {
            doShowMesage("未存储到任何数据，请稍后再试！", null);
            return;
        }

        share.edit().putBoolean("isSaveData", true).commit();
        new AlertDialog.Builder(this).setTitle("开通成功").setMessage("请保持后台运行，以便实时提醒。如更 换运营商，请重新执行该步骤如推送数 据更新，请及时升级\n").setNegativeButton(R.string.comfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //btnSubmit.setVisibility(View.GONE);
                ActivityUtils.finishAllActivities();
//                getCacheData();
                Intent intent = new Intent(SettingActivity.this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }).setCancelable(false).show();

    }


    private void getCacheData() {
        MncCidBean mainMnc = CidIdUtils.getMainMncCid(this);
        if (mainMnc == null) {
            doShowToast("未获取到主卡数据");
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory() + "/data/" + mainMnc.mnc + "/cid_all_data");
        if (!file.exists()) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        String data = FileIOUtils.readFile2String(file);
        if (TextUtils.isEmpty(data)) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        ArrayList<CidDataBean> listCid = (ArrayList<CidDataBean>) JSON.parseArray(data, CidDataBean.class);
        if (listCid == null || listCid.isEmpty()) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        MyBackgroundService.listCacheCidAll = listCid;
        //拿到副卡的数据

        LogUtils.i("获取到本地多少条数据？" + listCid.size());
        if (!ServiceUtils.isServiceRunning(MyBackgroundService.class.getName())) {
            ServiceUtils.startService(MyBackgroundService.class);
        }


    }


    public static void startActivity(Context context, String id_cid, String mncSIm1, String mncSIm2) {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra("id_cid", id_cid);
        intent.putExtra("mncSIm1", mncSIm1);
        intent.putExtra("mncSIm2", mncSIm2);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
