package com.list.asus.forsigns.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.list.asus.forsigns.R;
import com.list.asus.forsigns.bean.Class_stuId;
import com.list.asus.forsigns.bean.Schedule;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/*
 * Created by ASUS on 2017/5/20.
 *
 */

public class CurriculumFragment extends Fragment {

    public List<String> classStuIdList = new ArrayList<>();   //该学生所有的教学班
    public List<Schedule> scheduleList = new ArrayList<>();
    public String mStuId;  //我的学号

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View curriculumView = inflater.inflate(R.layout.main_fra_curriculum,container,false);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        mStuId = bmobUser.getUsername();
        getClassStuID(curriculumView);
        return curriculumView;
    }

/*
* 得到该学生的课，是通过stuId去查询Class_stuId(教学班_学号)表
* classStuIdList简单存储了Class_stuId(教学班_学号)表中关于自己的信息
*/
    private void getClassStuID(final View view){
        BmobQuery<Class_stuId> query = new BmobQuery<>();
        query.addWhereEqualTo("stuId", mStuId);
        query.setLimit(30);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(new FindListener<Class_stuId>() {
            @Override
            public void done(List<Class_stuId> object, BmobException e) {
                if(e==null){
                    for(Class_stuId class_stuId : object) {
                        classStuIdList.add(class_stuId.getTeachingClass());   //所有的教学班
                    }
                    getSchedule(view);
                }else{
                    Toast.makeText(getContext(), "失败"+e.getErrorCode(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
    *    得到该学生的课程，是通过所有课的教学班号去查询Schedule表
    */
    private void getSchedule(final View view){
        for (String teachingClass : classStuIdList) {
            BmobQuery<Schedule> sQuery = new BmobQuery<>();
            sQuery.addWhereEqualTo("teachingClass", teachingClass);
            sQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            sQuery.findObjects(new FindListener<Schedule>() {
                @Override
                public void done(List<Schedule> object, BmobException e) {
                    if(e==null){
                        scheduleList.addAll(object);
                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_curriculum);
                        LinearLayoutManager lm = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(lm);
                        CurriculumRecAdapter adapter = new CurriculumRecAdapter(getContext(), scheduleList);
                        recyclerView.setAdapter(adapter);
                    }else{
                        Toast.makeText(getContext(), "失败"+e.getErrorCode(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
