package com.list.asus.forsigns.bean;

/*
 * Created by ASUS on 2017/6/3.
 */

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class Location extends BmobObject{

    private BmobGeoPoint gpsAdd;

    public BmobGeoPoint getGpsAdd() {
        return gpsAdd;
    }

    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }
}
