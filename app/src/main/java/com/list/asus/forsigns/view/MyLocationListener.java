package com.list.asus.forsigns.view;

/*
 * Created by ASUS on 2017/6/9.
 */

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class MyLocationListener implements BDLocationListener {

    private OnLocationAdapterListener onLocationAdapterListener;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        onLocationAdapterListener.onLocationAdapterRespond(bdLocation);
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    public void setOnLocationListener(OnLocationAdapterListener onLocationAdapterListener){
        this.onLocationAdapterListener = onLocationAdapterListener;
    }
}
