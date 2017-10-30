package com.pursuege.schoolproject.bean;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/23.
 */

public class CidDataBean {
    public String cid;//":"10892",
    public int cidId;//:200100,
    public int collId;//":0,
    public String college;//":"上海理工大学",
    public String dept;//":"黄埔校区",
    public int id;//":1900780,
    public String lac;//":"80981",
    public String loginName;//":"",
    public String loginNo;//":"",
    public int mnc;//":3,
    public Date upTime;//":""

    @Override
    public String toString() {
        return
                "cid=" + cid + ",mnc='" + mnc +
                        '}';
    }
}
