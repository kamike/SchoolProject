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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.pursuege.schoolproject.R;
import com.pursuege.schoolproject.TestDataActivity;
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
import java.util.Arrays;
import java.util.HashMap;

public class SettingActivity extends BaseTitleActivity {

    private ImageView ivMsg, ivjar, ivMusic;
    private Button btnSubmit;
    private TextView tvContent;
    private TextView tvContent2;

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
        EventBus.getDefault().register(this);
        ivMsg = (ImageView) findViewById(R.id.setting_msg_switch_iv);
        ivjar = (ImageView) findViewById(R.id.setting_jar_switch_iv);
        ivMusic = (ImageView) findViewById(R.id.setting_music_switch_iv);
        btnSubmit = (Button) findViewById(R.id.apply_notify_next_btn);
        tvContent = (TextView) findViewById(R.id.setting_content_tv);
        tvContent2 = (TextView) findViewById(R.id.setting_content_tv2);

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
        String mncSim1 = getIntent().getStringExtra("mncSIm1");
        String mncSim2 = getIntent().getStringExtra("mncSIm2");
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
//        mncSim1 = "1";
//        mncSim2 = "1";

        if (!TextUtils.isEmpty(mncSim1)) {
//            StringBuilder sb = new StringBuilder(selectCidData);
//            sb.append("?cidId=").append(cidId);
//            sb.append("&mnc=").append(mncSim1);
            HashMap<String, Object> params = new HashMap<>();
            params.put("cidId", cidId);
            params.put("mnc", mncSim1);
            NetworkCore.doPostParams(selectCidData, params, "1");
        }

        if (!TextUtils.isEmpty(mncSim2) && !TextUtils.equals(mncSim1, mncSim2)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("cidId", cidId);
            params.put("mnc", mncSim2);
            NetworkCore.doPostParams(selectCidData, params, "1");
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFail(String msg) {
        if (TextUtils.isEmpty(msg)) {
            doShowMesage(NetwException, null);
            return;
        }
        doShowMesage("服务器异常:" + msg, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(BaseServerBean base) {
        if (!base.success) {
            doShowMesage("服务器异常:" + base.message, null);
            return;
        }
        if (TextUtils.isEmpty(base.data)) {
            doShowMesage("该校区没有任何数据!", null);
            return;
        }
        File f = null;
        if (TextUtils.equals(base.flag, "1")) {
            //根据文件名字来区分存的运营商
            File file = new File(Environment.getExternalStorageDirectory() + "/data/sim1");
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
            File file = new File(Environment.getExternalStorageDirectory() + "/data/sim2");
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
        File file = new File(Environment.getExternalStorageDirectory() + "/data/sim1");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/data/sim2");
        File[] fileArray = file.listFiles();
        File[] fileArray2 = file2.listFiles();
        if ((fileArray == null || fileArray.length <= 0) && (fileArray2 == null || fileArray2.length <= 0)) {
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
        File file = new File(Environment.getExternalStorageDirectory() + "/data/sim1/cid_all_data");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/data/sim2/cid_all_data");
        if (!file.exists() && !file2.exists()) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        String data = FileIOUtils.readFile2String(file);
        String data2 = FileIOUtils.readFile2String(file2);
        if (TextUtils.isEmpty(data) && TextUtils.isEmpty(data2)) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        ArrayList<CidDataBean> listCid = (ArrayList<CidDataBean>) JSON.parseArray(data, CidDataBean.class);
        ArrayList<CidDataBean> listCid2 = (ArrayList<CidDataBean>) JSON.parseArray(data2, CidDataBean.class);
        if (listCid == null || listCid.isEmpty()) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        if (listCid == null) {
            listCid = new ArrayList<>();
        }
        if (listCid2 != null) {
            listCid.addAll(listCid2);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFail(MncCidBean mnc) {

        tvContent.setText("主卡数据：" + mnc);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFail(MncCidBean[] mnc) {
        if (mnc == null || mnc.length == 0) {
            tvContent2.setText("获取双卡方式没有数据");
            return;
        }
        tvContent2.setText("主卡副卡数据：" + Arrays.toString(mnc));

    }


    public void getList(View v) {
        ActivityUtils.startActivity(TestDataActivity.class);
    }
}
