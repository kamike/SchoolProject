package com.pursuege.schoolproject;

/**
 * Created by wangtao on 2017/8/11.
 */

public interface Constants {
    //http://58.242.83.135:8989/ssm/getTabCid?mnc=2&cidId=4551
    String BASEURL = "http://58.242.83.135:8989";


    public static boolean DEBUG_TOGGLE = true;

    long TIMEOUT = 120*1000;

    String NetwException = "Network Exception";

    String selectShool = "/school-app/school/list";
    String selectCidData = "/ssm/getTabCid";
}
