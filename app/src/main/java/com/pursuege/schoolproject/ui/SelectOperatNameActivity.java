package com.pursuege.schoolproject.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pursuege.schoolproject.R;
import com.pursuege.schoolproject.utils.PermissionUtil;

import java.util.List;

public class SelectOperatNameActivity extends BaseTitleActivity {

    private ImageView ivOperateSim1;
    private ImageView ivOperateSim2;

    private LinearLayout linearSim1;
    private LinearLayout linearSim2;

    @Override
    public String setTopTitle() {
        return getResources().getString(R.string.select_operate_name);
    }

    @Override
    public View getContentBaseView() {
        return setInflateView(R.layout.activity_select_operat_name);
    }

    @Override
    public void setupUiView() {
        super.setupUiView();
        ivOperateSim1 = (ImageView) findViewById(R.id.operate_name_sim1_iv);
        ivOperateSim2 = (ImageView) findViewById(R.id.operate_name_sim2_iv);
        linearSim1 = (LinearLayout) findViewById(R.id.select_operate1_linear);
        linearSim2 = (LinearLayout) findViewById(R.id.select_operate2_linear);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void setupAllData() {
        super.setupAllData();

        if (PermissionUtil.checkPermission(this, Manifest.permission.READ_PHONE_STATE, 0)) {
            onStatePermission();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length < 1) {
            doShowToast("权限申请失败");
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    onStatePermission();
                }
            }
        } else {
            Toast.makeText(this, R.string.permission_fail, Toast.LENGTH_SHORT).show();

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void onStatePermission() {
        SubscriptionManager mSubscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            setSimpleSimData();
            return;
        }
        List<SubscriptionInfo> dataList = mSubscriptionManager.getActiveSubscriptionInfoList();
        if (dataList == null || dataList.isEmpty()) {
            setSimpleSimData();
            return;
        }
        if (dataList.size() == 1) {
            SubscriptionInfo info = dataList.get(0);
            switch (info.getMnc()) {
                case 0:
                case 2://移动
                    mncSIm1="5";
                    ivOperateSim1.setImageResource(R.drawable.icon_mobile);
                    break;

                case 1:
                    mncSIm1="1";
                    ivOperateSim1.setImageResource(R.drawable.icon_unicom);
                    break;
                default:
                    mncSIm1="3";
                    ivOperateSim1.setImageResource(R.drawable.icon_telecom);
                    break;

            }

        } else {
            SubscriptionInfo info = dataList.get(1);
            switch (info.getMnc()) {
                case 0:
                case 2://移动
                    mncSIm2="5";
                    ivOperateSim2.setImageResource(R.drawable.icon_mobile);
                    break;

                case 1:
                    mncSIm2="1";
                    ivOperateSim2.setImageResource(R.drawable.icon_unicom);
                    break;
                default:
                    mncSIm2="3";
                    ivOperateSim2.setImageResource(R.drawable.icon_telecom);
                    break;

            }
        }
    }

    private void setSimpleSimData() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                //中国移动
                mncSIm1="5";
                ivOperateSim1.setImageResource(R.drawable.icon_mobile);
            } else if (imsi.startsWith("46001")) {
                //中国联通
                mncSIm1="1";
                ivOperateSim1.setImageResource(R.drawable.icon_unicom);
            } else if (imsi.startsWith("46003")) {
                //中国电信
                mncSIm1="3";
                ivOperateSim1.setImageResource(R.drawable.icon_telecom);
            }
        }
    }

    private String[] arrayOperate = {"中国移动", "中国联通", "中国电信"};
    private int selectSim1 = 0;
    private int selectSim2 = 0;

    public void onclickSelectSim1(View v) {
        new AlertDialog.Builder(this).setTitle("更改SIM1卡的运营商").setSingleChoiceItems(arrayOperate, selectSim1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectSim1 = i;
                switch (i) {
                    case 0:
                        mncSIm1="5";
                        ivOperateSim1.setImageResource(R.drawable.icon_mobile);
                        break;
                    case 1:
                        mncSIm1="1";
                        ivOperateSim1.setImageResource(R.drawable.icon_unicom);
                        break;
                    case 2:
                        mncSIm1="3";
                        ivOperateSim1.setImageResource(R.drawable.icon_telecom);
                        break;
                }
            }
        }).show();

    }


    public void onclickSelectSim2(View v) {
        new AlertDialog.Builder(this).setTitle("更改SIM2卡的运营商").setSingleChoiceItems(arrayOperate, selectSim2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectSim2 = i;
                switch (i) {
                    case 0:
                        mncSIm2="5";
                        ivOperateSim2.setImageResource(R.drawable.icon_mobile);
                        break;
                    case 1:
                        mncSIm2="1";
                        ivOperateSim2.setImageResource(R.drawable.icon_unicom);
                        break;
                    case 2:
                        mncSIm2="3";
                        ivOperateSim2.setImageResource(R.drawable.icon_telecom);
                        break;
                }
            }
        }).show();
    }

    private String mncSIm1;
    private String mncSIm2;
    public void onclickOperateNext(View v) {

        SettingActivity.startActivity(this,getIntent().getStringExtra("id_cid"),mncSIm1,mncSIm2);
        finish();
    }


    public static void startOperateSelect(Context context, String idId) {
        Intent intent = new Intent(context, SelectOperatNameActivity.class);
        intent.putExtra("id_cid", idId);
        context.startActivity(intent);
    }
}
