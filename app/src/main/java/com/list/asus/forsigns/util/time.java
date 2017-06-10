package com.list.asus.forsigns.util;

/*
 * Created by ASUS on 2017/5/30.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class time {

    public static String getCheckId(String tea) {
//        SimpleDateFormat sDateFormat= new SimpleDateFormat("yyyyMMdd");
//        Date curDate=new Date(System.currentTimeMillis());
//        String date = sDateFormat.format(curDate);
//        return ""+date+tea;
        return "20170502s1";
    }

    public static Integer getClassOrder() {
        return 1;
    }


//    //得到当前系统时间  直接获取总时间
//    private String getCurrentTime() {
//        SimpleDateFormat sDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date curDate=new Date(System.currentTimeMillis());
//        String date = sDateFormat.format(curDate);
//        return date;
//    }

}
