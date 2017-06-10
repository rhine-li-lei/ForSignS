package com.list.asus.forsigns.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.list.asus.forsigns.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ClockinFragment clockinFragment;
    private CurriculumFragment curriculumFragment;
    private MineFragment mineFragment;

    //主界面toolbar中左右边的button
    public Button buToolbarLeft;
    public Button buToolbarRight;

    //主界面底部item的button，即打卡,课表，我的
    public Button bottomClokin;
    public Button bottomCurriculum;
    public Button bottomMine;

    // Fragment管理器
    private android.support.v4.app.FragmentManager manager;
    android.support.v4.app.FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        Bmob.initialize(getApplicationContext(), "a16ec96b707da5d0f2a404b0ce9d755b");

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            initView();
            LatLng pt1 = new LatLng(39.915291, 116.403857);
            LatLng pt2 = new LatLng(39.915258, 116.403894);
            //计算p1、p2两点之间的直线距离，单位：米
            double x = DistanceUtil. getDistance(pt1, pt2);
            Log.i("TAG", "onCreate: "+x);
            initActivityView();
        }else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //默认加载第一个布局
    private void initActivityView() {

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        changeItemColor(bottomClokin);
        buToolbarLeft.setText("打卡");
        buToolbarRight.setText("帮助");
        clockinFragment = new ClockinFragment();
        transaction.replace(R.id.main_fragment, clockinFragment);
        transaction.commit();
        buToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        buToolbarLeft = (Button)findViewById(R.id.toolbal_left);
        buToolbarRight = (Button)findViewById(R.id.toolbal_right);
        bottomClokin = (Button)findViewById(R.id.bottom_clockIn);
        bottomCurriculum = (Button)findViewById(R.id.bottom_curriculum);
        bottomMine = (Button)findViewById(R.id.bottom_mine);

        bottomClokin.setOnClickListener(this);
        bottomCurriculum.setOnClickListener(this);
        bottomMine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        switch (v.getId()){

            //点击底部item的打卡时
            case R.id.bottom_clockIn:
                changeItemColor(bottomClokin);
                buToolbarLeft.setText("打卡");
                buToolbarRight.setText("帮助");
                clockinFragment = new ClockinFragment();
                transaction.replace(R.id.main_fragment, clockinFragment);
                transaction.commit();
                //在第一个fragment中点击toolbar右边按钮（“帮助”按钮）
                buToolbarRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.bottom_curriculum:
                changeItemColor(bottomCurriculum);
                buToolbarLeft.setText("课表详情");
                buToolbarRight.setText("");
                curriculumFragment = new CurriculumFragment();
                transaction.replace(R.id.main_fragment, curriculumFragment);
                transaction.commit();
                break;
            case R.id.bottom_mine:
                changeItemColor(bottomMine);
                buToolbarLeft.setText("我的");
                buToolbarRight.setText("修改");
                mineFragment = new MineFragment();
                buToolbarRight.setTextColor(this.getResources().getColor(R.color.colorBlue));
                transaction.replace(R.id.main_fragment, mineFragment);
                transaction.commit();
                //在第三个fragment中点击toolbar右边按钮（“修改”按钮）
                buToolbarRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }
    }

    public void changeItemColor(Button item){
        bottomClokin.setTextColor(this.getResources().getColor(R.color.colorBlack));
        bottomCurriculum.setTextColor(this.getResources().getColor(R.color.colorBlack));
        bottomMine.setTextColor(this.getResources().getColor(R.color.colorBlack));
        item.setTextColor(this.getResources().getColor(R.color.colorAccent));
    }
}