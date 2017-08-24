package com.pursuege.schoolproject.utils;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import com.pursuege.schoolproject.bean.MncCidBean;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Administrator on 2017/8/24.
 */

public class CidIdUtils {

    public static MncCidBean getMainMncCid(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        MncCidBean bean = new MncCidBean();
        bean.isMainSim = true;
        bean.mnc = getSimpleSimData(context);
        try {
            CellLocation cel = tm.getCellLocation();
            GsmCellLocation gsm = (GsmCellLocation) cel;
            if (gsm != null) {
                bean.cidId = gsm.getCid() + "";
            }
            return bean;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSimpleSimData(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                //中国移动
                return "5";
            } else if (imsi.startsWith("46001")) {
                //中国联通
                return "1";
            } else if (imsi.startsWith("46003")) {
                //中国电信
                return "3";
            }
        }
        return "5";
    }
}
