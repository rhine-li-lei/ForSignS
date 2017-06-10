package com.list.asus.forsigns.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.list.asus.forsigns.R;
import com.list.asus.forsigns.bean.CheckRecord;
import com.list.asus.forsigns.bean.CheckResult;
import com.list.asus.forsigns.bean.Class_stuId;
import com.list.asus.forsigns.bean.MonitorCheckRecord;
import com.list.asus.forsigns.bean.Schedule;
import com.list.asus.forsigns.bean.Student;
import com.list.asus.forsigns.util.time;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;

import static android.content.ContentValues.TAG;
import static cn.bmob.v3.Bmob.getApplicationContext;

/*
 * Created by ASUS on 2017/5/20.
 */

public class ClockinFragment extends Fragment implements View.OnClickListener, OnLocationAdapterListener{

    public Button mainClockin;
    public TextView mainName;
    public TextView mainStudentId;
    public TextView mainCourse;
    public ImageView imageRange;
    public TextView Range;
    public ImageView imageIsCheck;
    public TextView Check;

    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocation = true;

    public boolean isRightPlace = true;           //是否在打卡范围
    public boolean isTeacherStartClockIn = false;           //是否教师已经开始考勤
    public boolean isClockedIn = false;                             //是否已经打卡
    private List<Class_stuId> classStuIdList = new ArrayList<>();   //该学生所有的教学班
    public String teaId;
    public String mStuId;                       //我的学号
    public String mName;                        //姓名
    public String thisCheckId = "20170429s1";  //这节课的checkId
    final BmobRealTimeData rtd = new BmobRealTimeData();  //监听表

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        View clockinView = inflater.inflate(R.layout.main_fra_clockin, container, false);
        initView(clockinView);
        setStuBasicInfo();
        getClassStuID();
        mainClockin.setOnClickListener(this);
        return clockinView;
    }


    private void initView(View v) {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        mStuId = bmobUser.getUsername();
        mainClockin = (Button)v.findViewById(R.id.mainClockIn);
        mainName = (TextView) v.findViewById(R.id.name);
        mainStudentId = (TextView) v.findViewById(R.id.studentId);
        mainCourse = (TextView) v.findViewById(R.id.course);
        imageRange = (ImageView) v.findViewById(R.id.image_range);
        Range = (TextView) v.findViewById(R.id.range);
        imageIsCheck = (ImageView) v.findViewById(R.id.image_isCheck);
        Check= (TextView) v.findViewById(R.id.isCheck);
        mapView = (MapView) v.findViewById(R.id.bmapView);

        myListener.setOnLocationListener(this);

        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest
                .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest
                .permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest
                .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permission = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permission, 1);
        }else{
            requestLocation();
        }
    }


    /*
    *   得到学生的基本信息，是通过stuId去查询Student表（_User表也可以）
    *   改进：先判断本地是否已经缓存
    */
    private void setStuBasicInfo() {
        BmobQuery<Student> query = new BmobQuery<>();
        query.addWhereEqualTo("stuId", mStuId);
        query.findObjects(new FindListener<Student>() {
            @Override
            public void done(List<Student> object, BmobException e) {
                if(e==null){
                    for (Student student : object) {
                        mName = student.getStuName();
                        mainName.setText(student.getStuName());
                        mainStudentId.setText(student.getStuId());
                    }
                }else{
                    Toast.makeText(getContext(), "失败"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /*
   * 得到该学生的教学班，是通过stuId去查询Class_stuId(教学班_学号)表
   * classStuIdList简单存储了Class_stuId(教学班_学号)表中关于自己的信息
   */
    private void getClassStuID() {
        BmobQuery<Class_stuId> query = new BmobQuery<>();
        query.addWhereEqualTo("stuId", mStuId);
        query.setLimit(30);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(new FindListener<Class_stuId>() {
            @Override
            public void done(List<Class_stuId> object, BmobException e) {
                //应加缓存
                if(e==null){
                    Integer classOrder = time.getClassOrder();
                    classOrder = 2;
                    for(Class_stuId class_stuId:object ){
                        classStuIdList.add(class_stuId);
                        if (class_stuId.getClassOrder()==classOrder){
                            getSchedule(class_stuId.getClassOrder(), class_stuId.getTeachingClass());
                        }else{
                            mainCourse.setText("you are free");
                        }
                    }

                }else{
                    Toast.makeText(getContext(), "失败"+e.getErrorCode(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
    *    得到该学生的课程，是通过所有教学班的教学班号去查询Schedule表
    */
    private void getSchedule(Integer classOrder , String teachingClass) {
        BmobQuery<Schedule> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("teachingClass", teachingClass);
        BmobQuery<Schedule> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("classOrder", classOrder);
        List<BmobQuery<Schedule>> andQuerys = new ArrayList<BmobQuery<Schedule>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        BmobQuery<Schedule> query = new BmobQuery<>();
        query.and(andQuerys);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(new FindListener<Schedule>() {
            @Override
            public void done(List<Schedule> object, BmobException e) {
                if(e==null){
                    if (!object.isEmpty()){
                        teaId = object.get(0).getTeaId();
                        thisCheckId = time.getCheckId(teaId);
                        mainCourse.setText(object.get(0).getCourseName());
                        scanCheckRecord();
                        scanCheckResult();
                        monitorCheckRecord();
                    }
                }else{
                    Toast.makeText(getContext(), "失败"+e.getErrorCode(),Toast.LENGTH_LONG).show();
                }
            }
        });

//        Log.d("TAG get  22", "done: "+scheduleList.size());
    }

   //扫描  判断老师是否已经发起打卡
    public void scanCheckRecord(){
        BmobQuery<CheckRecord> query = new BmobQuery<>();
        query.addWhereEqualTo("checkId", thisCheckId);
        query.setLimit(30);
        query.findObjects(new FindListener<CheckRecord>() {
            @Override
            public void done(List<CheckRecord> object, BmobException e) {
                if(e==null){
                    if (!object.isEmpty()){
                        isTeacherStartClockIn = true;
                        imageIsCheck.setImageResource(R.drawable.yes);
                        Check.setText("老师已经发起打卡");
                    }
                }else{
                    Toast.makeText(getContext(), "失败"+e.getErrorCode(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //扫描（判断当前课程是否已经打卡）
    public void scanCheckResult(){
        BmobQuery<CheckResult> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("checkId", thisCheckId);
        BmobQuery<CheckResult> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("stuId", mStuId);
        //最后组装完整的and条件
        List<BmobQuery<CheckResult>> andQuerys = new ArrayList<BmobQuery<CheckResult>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        //查询符合整个and条件
        BmobQuery<CheckResult> query = new BmobQuery<>();
        query.and(andQuerys);
        query.findObjects(new FindListener<CheckResult>() {
            @Override
            public void done(List<CheckResult> object, BmobException e) {
                if(e==null){
                    Log.d("TAG", "done: "+object.size());
                    if (!object.isEmpty()){
                        Log.d("TAG", "done: "+object.get(0).getCheckId());
                        isClockedIn = true;
                        mainClockin.setText("已打卡");
                        mainClockin.setBackgroundResource(R.drawable.meet_conditions);
                    } else {
                        if (isTeacherStartClockIn){
                            mainClockin.setText("考勤打卡");
                            mainClockin.setBackgroundResource(R.drawable.meet_conditions);
                        } else {
                            mainClockin.setText("不符合打卡条件");
                            mainClockin.setBackgroundResource(R.drawable.dismeet_conditions);
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "失败"+e.getErrorCode(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //监听CheckRecord
    public void monitorCheckRecord(){
//        start方法中的ValueEventListener参数用于监听连接成功和数据变化的回调
        rtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                //第一步：json数据转换为实体类
                MonitorCheckRecord monitorCheckRecord = new MonitorCheckRecord();
                String jsonDataString = data.toString();
                monitorCheckRecord = new Gson().fromJson(jsonDataString, MonitorCheckRecord.class);
                //第二步：判断是否是当前学生对应的老师以及当前的课程
                if(time.getCheckId(teaId).equals(monitorCheckRecord.getData().getCheckId())){
                    isTeacherStartClockIn = true;
                    imageIsCheck.setImageResource(R.drawable.yes);
                    Check.setText("老师已经开始考勤");
                    if (!isClockedIn && isRightPlace){
                        mainClockin.setText("考勤打卡");
                        mainClockin.setBackgroundResource(R.drawable.meet_conditions);
                    }
                }
            }
            @Override
            public void onConnectCompleted(Exception ex) {
                // 监听表更新
                if(rtd.isConnected()){
                    rtd.subTableUpdate("CheckRecord");
                }
            }
        });
    }


//    //在view onDestroy的时候取消监听表更新
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        rtd.unsubTableUpdate("CheckResult");
//    }


    //中央button的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainClockIn:
                if(isTeacherStartClockIn && isRightPlace && !isClockedIn){
                    addCheckResult();
                    isClockedIn = true;
                    mainClockin.setText("已打卡");
                }
                break;
        }
    }

    //打卡  向checkResult添加一条数据
    public void addCheckResult(){
        CheckResult checkResult = new CheckResult();
        checkResult.setCheckId(thisCheckId);
        checkResult.setClockStatus(true);
        checkResult.setStuId(mStuId);
        checkResult.setStuName(mName);
        checkResult.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(getContext(),"考勤成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"考勤失败"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onLocationAdapterRespond(BDLocation bdLocation) {
        navigateTo(bdLocation);
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(getContext(), "必须同意所有权才能使用本程序", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(getContext(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
                break;

        }
    }

    private void navigateTo(BDLocation location){
        if (isFirstLocation){
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(18f);
            baiduMap.animateMapStatus(update);
            isFirstLocation = false;
        }
        MyLocationData.Builder locationbuilder = new MyLocationData.Builder();
        locationbuilder.latitude(location.getLatitude());
        locationbuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationbuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

}
