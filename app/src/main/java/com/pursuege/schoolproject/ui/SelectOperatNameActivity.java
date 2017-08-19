package com.pursuege.schoolproject.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pursuege.schoolproject.R;

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
                    ivOperateSim1.setImageResource(R.drawable.icon_mobile);
                    break;

                case 1:
                    ivOperateSim1.setImageResource(R.drawable.icon_unicom);
                    break;
                default:
                    ivOperateSim1.setImageResource(R.drawable.icon_telecom);
                    break;

            }

        } else {
            SubscriptionInfo info = dataList.get(1);
            switch (info.getMnc()) {
                case 0:
                case 2://移动
                    ivOperateSim2.setImageResource(R.drawable.icon_mobile);
                    break;

                case 1:
                    ivOperateSim2.setImageResource(R.drawable.icon_unicom);
                    break;
                default:
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
                ivOperateSim1.setImageResource(R.drawable.icon_mobile);
            } else if (imsi.startsWith("46001")) {
                //中国联通
                ivOperateSim1.setImageResource(R.drawable.icon_unicom);
            } else if (imsi.startsWith("46003")) {
                //中国电信
                ivOperateSim1.setImageResource(R.drawable.icon_telecom);
            }
        }
    }

    private String[] arrayOperate = {"中国移动", "中国联通", "中国电信"};


    public void onclickSelectSim1(View v) {
        new AlertDialog.Builder(this).setSingleChoiceItems(arrayOperate, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        ivOperateSim1.setImageResource(R.drawable.icon_mobile);
                        break;
                    case 1:
                        ivOperateSim1.setImageResource(R.drawable.icon_unicom);
                        break;
                    case 2:
                        ivOperateSim1.setImageResource(R.drawable.icon_telecom);
                        break;
                }
            }
        });

    }


    public void onclickSelectSim2(View v) {

    }
}
