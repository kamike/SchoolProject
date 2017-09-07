package com.pursuege.schoolproject.utils;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.pursuege.schoolproject.bean.MncCidBean;
import com.pursuege.schoolproject.service.MyBackgroundService;

import java.util.List;

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
            if (cel instanceof GsmCellLocation) {
                GsmCellLocation gsm = (GsmCellLocation) cel;
                if (gsm != null) {
                    bean.cidId = gsm.getCid() + "";
                    return bean;
                }
            }
            if (cel instanceof CdmaCellLocation) {
                CdmaCellLocation cdma = (CdmaCellLocation) cel;
                if (cdma != null) {
                    bean.cidId = cdma.getBaseStationId() + "";
                    return bean;
                }
            }

        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return bean;
    }


    public static int getSimpleSimData(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();

        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46004") || imsi.startsWith("46007")) {
                //中国移动
                return 5;
            } else if (imsi.startsWith("46001") || imsi.startsWith("46006") || imsi.startsWith("46009")) {
                //中国联通
                return 1;
            } else if (imsi.startsWith("46003") || imsi.startsWith("46005") || imsi.startsWith("46011") || imsi.startsWith("460011")) {
                //中国电信
                return 3;
            }
        } else {

        }
        return 5;
    }

    public static int getSimpleSimData(String imsi) {

        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46004") || imsi.startsWith("46007")) {
                //中国移动
                return 5;
            } else if (imsi.startsWith("46001") || imsi.startsWith("46006") || imsi.startsWith("46009")) {
                //中国联通
                return 1;
            } else if (imsi.startsWith("46003") || imsi.startsWith("46005") || imsi.startsWith("46011") || imsi.startsWith("460011")) {
                //中国电信
                return 3;
            }
        } else {

        }
        return 5;
    }


    public static void setCidListener(Context context) {

        SubscriptionManager mSubscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<SubscriptionInfo> dataList = mSubscriptionManager.getActiveSubscriptionInfoList();
            if (dataList == null || dataList.isEmpty()) {
                return;
            }
            //都获取不到双卡数据
            int event = PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                    PhoneStateListener.LISTEN_CALL_STATE |
                    PhoneStateListener.LISTEN_CELL_LOCATION |
                    PhoneStateListener.LISTEN_DATA_ACTIVITY |
                    PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                    PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR |
                    PhoneStateListener.LISTEN_SERVICE_STATE |
                    PhoneStateListener.LISTEN_SIGNAL_STRENGTH | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS;
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

            tm.listen(new MyPhotoListener1(dataList.get(0).getSubscriptionId()), event);
            if (dataList.size() >= 2) {
                tm.listen(new MyPhotoListener2(dataList.get(1).getSubscriptionId()), event);
            }

        }
    }

    public static int mncToNetworkMnc(int mnc) {
        switch (mnc) {
            case 0:
            case 2:
            case 4:
            case 7:
                return 5;
            case 1:
            case 6:
            case 9:
                return 1;
            case 3:
            case 5:
            case 11:
                return 3;

        }
        return 5;
    }

    private static class MyPhotoListener2 extends PhoneStateListener {
        public MyPhotoListener2(int subscriptionId) {
            ReflectUtils.setFieldValue(this, "mSubId", subscriptionId);
        }

        /**
         * 返回手机当前所处的位置
         *
         * @param location
         */
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            LogUtils.i("onCellLocationChanged222");
            if (location == null) {
                return;
            }
            if (location instanceof GsmCellLocation) {
                GsmCellLocation gsm = (GsmCellLocation) location;
                if (gsm != null) {
                    MncCidBean cidBean = new MncCidBean();
                    cidBean.cidId = gsm.getCid() + "";
                    LogUtils.i("2卡的cid:" + gsm.getCid());
                    if (MyBackgroundService.allMncList[1] != null) {
                        MyBackgroundService.allMncList[1].cidId = gsm.getCid() + "";
                    }

                }
            }
            if (location instanceof CdmaCellLocation) {
                CdmaCellLocation cdma = (CdmaCellLocation) location;
                if (cdma != null) {
                    if (MyBackgroundService.allMncList[1] != null) {
                        MyBackgroundService.allMncList[1].cidId = cdma.getBaseStationId() + "";
                    }
                }
            }
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);

            //获取网络运营商
//            getSimpleSimData  46001   46000
            if (serviceState != null) {
                LogUtils.i("onServiceStateChanged2222:" + serviceState.getOperatorNumeric());
                if (MyBackgroundService.allMncList[1] != null) {
                    MyBackgroundService.allMncList[1].mnc = getSimpleSimData(serviceState.getOperatorNumeric());
                }
            }

        }
    }

    private static class MyPhotoListener1 extends PhoneStateListener {
        public MyPhotoListener1(int subscriptionId) {
            ReflectUtils.setFieldValue(this, "mSubId", subscriptionId);
        }

        /**
         * 返回手机当前所处的位置
         *
         * @param location
         */
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            LogUtils.i("onCellLocationChanged1111111");
            if (location instanceof GsmCellLocation) {
                GsmCellLocation gsm = (GsmCellLocation) location;
                if (gsm != null) {

                    if (MyBackgroundService.allMncList[0] != null) {
                        MyBackgroundService.allMncList[0].cidId = gsm.getCid() + "";
                    }
                }
            }
            if (location instanceof CdmaCellLocation) {
                CdmaCellLocation cdma = (CdmaCellLocation) location;
                if (cdma != null) {
                    if (MyBackgroundService.allMncList[0] != null) {
                        MyBackgroundService.allMncList[0].cidId = cdma.getBaseStationId() + "";
                    }
                }
            }
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);

            //获取网络运营商
//            getSimpleSimData  46001   46000
            if (serviceState != null) {
                LogUtils.i("onServiceStateChanged11111111:" + serviceState.getOperatorNumeric());
                if (MyBackgroundService.allMncList[0] != null) {
                    MyBackgroundService.allMncList[0].mnc = getSimpleSimData(serviceState.getOperatorNumeric());
                }
            }

        }

    }
}
