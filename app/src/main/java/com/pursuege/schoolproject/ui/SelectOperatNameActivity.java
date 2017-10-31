package com.pursuege.schoolproject.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.pursuege.schoolproject.R;
import com.pursuege.schoolproject.utils.LogUtils;
import com.pursuege.schoolproject.utils.MySpinner;
import com.pursuege.schoolproject.utils.PermissionUtil;

import java.util.List;

import static com.baidu.location.h.a.i;

public class SelectOperatNameActivity extends BaseTitleActivity {


    private LinearLayout linearSim1;
    private LinearLayout linearSim2;
    private LinearLayout linearImg;

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
        linearImg = (LinearLayout) findViewById(R.id.linear_operate_img);
        linearSim1 = (LinearLayout) findViewById(R.id.select_operate1_linear);
        linearSim2 = (LinearLayout) findViewById(R.id.select_operate2_linear);

        ViewGroup.LayoutParams params = linearImg.getLayoutParams();
        params.height = (int) ((ScreenUtils.getScreenWidth() / 2.0f - SizeUtils.dp2px(25)) * 0.919f);
        linearImg.setLayoutParams(params);

        LinearLayout.LayoutParams paramsSIm = (LinearLayout.LayoutParams) linearSim1.getLayoutParams();
        paramsSIm.topMargin = (int) (params.height * 0.228f);
        paramsSIm.leftMargin = SizeUtils.dp2px(-2);
        linearSim1.setLayoutParams(paramsSIm);

        LinearLayout.LayoutParams paramsSIm2 = (LinearLayout.LayoutParams) linearSim2.getLayoutParams();
        paramsSIm2.topMargin = (int) (params.height * 0.228f);
        paramsSIm2.leftMargin = SizeUtils.dp2px(-10);
        linearSim2.setLayoutParams(paramsSIm2);
        linearSim2.setLayoutParams(paramsSIm2);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void setupAllData() {
        super.setupAllData();

        if (PermissionUtil.checkPermission(this, Manifest.permission.READ_PHONE_STATE, 0)) {
            onStatePermission();
        }


    }

    private MySpinner mySpinner1;
    private MySpinner mySpinner2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length < 1) {
            doShowToast("权限申请失败");
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 0) {
                onStatePermission();
            }
            if (requestCode == 10) {
                onWriteExtPermion();
            }
        } else {
            Toast.makeText(this, R.string.permission_fail, Toast.LENGTH_SHORT).show();

        }

    }


    private void onStatePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            setSimpleSimData();
            return;
        }
        SubscriptionManager mSubscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<SubscriptionInfo> dataList = mSubscriptionManager.getActiveSubscriptionInfoList();
            if (dataList == null || dataList.isEmpty()) {
                setSimpleSimData();
                return;
            }


            if (dataList.size() == 1) {
                setSim1(dataList.get(0));

            } else {
                for (int index = 0; index < dataList.size(); index++) {
                    if (isRunSim(dataList.get(index).getCarrierName())) {
                        if (index == 0) {
                            setSim1(dataList.get(0));
                        }
                        if (index == 1) {
                            setSim2(dataList.get(1));
                        }
                    }
                }

            }
        }

    }

    private boolean isRunSim(CharSequence carrierName) {
        if (TextUtils.isEmpty(carrierName)) {
            return false;
        }

        if (carrierName.toString().contains("无")) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void setSim2(SubscriptionInfo info) {
        mySpinner2 = new MySpinner(this, arrayImgs, linearSim2, new MySpinner.OnViewSelectClickListener() {
            @Override
            public void onViewSelect(int position) {
                onclickSelectSim2(position);
            }
        });
        switch (info.getMnc()) {
            case 0:
            case 2://移动
            case 4://移动
            case 7://移动
                mncSIm2 = "5";
//                ivOperateSim2.setImageResource(R.drawable.icon_mobile);
                mySpinner2.setSelectIndex(0);
                break;

            case 1:
            case 6:
            case 9:
                mncSIm2 = "1";
//                ivOperateSim2.setImageResource(R.drawable.icon_unicom);
                mySpinner2.setSelectIndex(1);
                break;
            default:
                mncSIm2 = "3";
//                ivOperateSim2.setImageResource(R.drawable.icon_telecom);
                mySpinner2.setSelectIndex(2);
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void setSim1(SubscriptionInfo info) {
        mySpinner1 = new MySpinner(this, arrayImgs, linearSim1, new MySpinner.OnViewSelectClickListener() {
            @Override
            public void onViewSelect(int position) {
                onclickSelectSim1(position);
            }
        });
        switch (info.getMnc()) {
            case 0:
            case 2://移动
            case 4://移动
            case 7://移动
                mncSIm1 = "5";
//                ivOperateSim1.setImageResource(R.drawable.icon_mobile);
                mySpinner1.setSelectIndex(0);
                break;

            case 1:
            case 6:
            case 9:
                mncSIm1 = "1";
//                ivOperateSim1.setImageResource(R.drawable.icon_unicom);
                mySpinner1.setSelectIndex(1);
                break;
            default:
                mncSIm1 = "3";
//                ivOperateSim1.setImageResource(R.drawable.icon_telecom);
                mySpinner1.setSelectIndex(2);
                break;

        }
    }

    private void setSimpleSimData() {
        mySpinner1 = new MySpinner(this, arrayImgs, linearSim1, new MySpinner.OnViewSelectClickListener() {
            @Override
            public void onViewSelect(int position) {
                onclickSelectSim1(position);
            }
        });
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46004") || imsi.startsWith("46007")) {
                //中国移动
                mncSIm1 = "5";
//                ivOperateSim1.setImageResource(R.drawable.icon_mobile);
                mySpinner1.setSelectIndex(0);
            } else if (imsi.startsWith("46001") || imsi.startsWith("46006") || imsi.startsWith("46009")) {
                //中国联通
                mncSIm1 = "1";
//                ivOperateSim1.setImageResource(R.drawable.icon_unicom);
                mySpinner1.setSelectIndex(1);
            } else if (imsi.startsWith("46003") || imsi.startsWith("46005") || imsi.startsWith("46011") || imsi.startsWith("460011")) {
                //中国电信
                mncSIm1 = "3";
//                ivOperateSim1.setImageResource(R.drawable.icon_telecom);
                mySpinner1.setSelectIndex(2);
            }
        } else {

        }
    }

    private String[] arrayOperate = {"中国移动", "中国联通", "中国电信"};
    private int[] arrayImgs = {R.drawable.icon_mobile, R.drawable.icon_unicom, R.drawable.icon_telecom};
    private int selectSim1 = 0;
    private int selectSim2 = 0;

    public void onclickSelectSim1(int index) {
//        new AlertDialog.Builder(this).setTitle("更改SIM1卡的运营商").setSingleChoiceItems(arrayOperate, selectSim1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
        selectSim1 = index;
        switch (i) {
            case 0:
                mncSIm1 = "5";
//                        ivOperateSim1.setImageResource(R.drawable.icon_mobile);
                //ivOperateSim1.setSelection(0);
                break;
            case 1:
                mncSIm1 = "1";
//              /          ivOperateSim1.setImageResource(R.drawable.icon_unicom);
                //ivOperateSim1.setSelection(1);
                break;
            case 2:
                mncSIm1 = "3";
//                        ivOperateSim1.setImageResource(R.drawable.icon_telecom);
                //  ivOperateSim1.setSelection(2);
                break;
        }
//            }
//        }).show();

    }


    public void onclickSelectSim2(int index) {
//        new AlertDialog.Builder(this).setTitle("更改SIM2卡的运营商").setSingleChoiceItems(arrayOperate, selectSim2, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
        selectSim2 = index;
        switch (i) {
            case 0:
                mncSIm2 = "5";
                // ivOperateSim2.setImageResource(R.drawable.icon_mobile);
                // ivOperateSim2.setSelection(0);
                break;
            case 1:
                mncSIm2 = "1";
                // ivOperateSim2.setImageResource(R.drawable.icon_unicom);
                // ivOperateSim2.setSelection(1);
                break;
            case 2:
                mncSIm2 = "3";
                // ivOperateSim2.setSelection(2);
                //ivOperateSim2.setImageResource(R.drawable.icon_telecom);
                break;
        }
//            }
//        }).show();
    }

    private String mncSIm1;
    private String mncSIm2;

    public void onclickOperateNext(View v) {
        if (PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 10)) {
            onWriteExtPermion();
        }


    }

    private void onWriteExtPermion() {
        String str = getIntent().getStringExtra("id_cid");
        LogUtils.i("cidid--end:" + str);

        SettingActivity.startActivity(this, str, mncSIm1, mncSIm2);
    }


    public static void startOperateSelect(Context context, String idId) {
        Intent intent = new Intent(context, SelectOperatNameActivity.class);
        intent.putExtra("id_cid", idId);

        context.startActivity(intent);
    }
}
