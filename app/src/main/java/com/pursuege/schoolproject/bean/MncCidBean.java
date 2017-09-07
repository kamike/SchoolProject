package com.pursuege.schoolproject.bean;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MncCidBean {
    public int mnc;
    public String cidId;
    public boolean isMainSim;

    @Override
    public String toString() {
        return "MncCidBean：" +
                "mnc='" + mnc + '\'' +
                ", cidId='" + cidId;
    }
}
