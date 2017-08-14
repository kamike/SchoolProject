package com.pursuege.schoolproject.bean;

/**
 * Created by wangtao on 2017/7/28.
 */

public class SchollInfoBean {
    public String cid;
    public int cidId;
    public int collId;
    public String college;
    public String dept;
    public int id;
    public String lac;
    public String loginName;
    public String loginNo;
    public String mnc;
    public String upTime;
    public SchoolAreaBean childArea;

    @Override
    public String toString() {
        return "SchollInfoBean{" +
                "cid='" + cid + '\'' +
                ", cidId=" + cidId +
                ", collId=" + collId +
                ", college='" + college + '\'' +
                ", dept='" + dept + '\'' +
                ", id=" + id +
                ", lac='" + lac + '\'' +
                ", loginName='" + loginName + '\'' +
                ", loginNo='" + loginNo + '\'' +
                ", mnc='" + mnc + '\'' +
                ", upTime='" + upTime + '\'' +
                '}';
    }
}
