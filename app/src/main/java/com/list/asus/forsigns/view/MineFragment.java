package com.list.asus.forsigns.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.list.asus.forsigns.R;
import com.list.asus.forsigns.bean.Student;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/*
 * Created by ASUS on 2017/5/20.
 */

public class MineFragment extends Fragment {

    public TextView tvName;
    public ImageView ivSexuality;
    public TextView tvStudentId;
    public TextView tvPhoneNumber;
    public TextView tvEmail;
    public TextView tvSetting;
    public String mStuId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mineView = inflater.inflate(R.layout.main_fra_mine,container,false);
        initView(mineView);
        setBasicInfo();
        return mineView;
    }

    public void initView(View view){
        BmobUser bmobUser = BmobUser.getCurrentUser();
        mStuId = bmobUser.getUsername();
        tvName = (TextView) view.findViewById(R.id.mine_name);
        ivSexuality = (ImageView) view.findViewById(R.id.mine_sexuality);
        tvPhoneNumber = (TextView) view.findViewById(R.id.mine_phoneNumber);
        tvEmail = (TextView) view.findViewById(R.id.mine_email);
        tvSetting = (TextView) view.findViewById(R.id.setting);
        tvStudentId = (TextView) view.findViewById(R.id.mine_studentId);
    }

    public void setBasicInfo(){
        BmobQuery<Student> query  =  new BmobQuery<>();
        query.addWhereEqualTo("stuId", mStuId);
        query.findObjects(new FindListener<Student>() {
            @Override
            public void done(List<Student> object, BmobException e) {
                if(e==null){
                    tvName.setText(object.get(0).getStuName());
                    tvPhoneNumber.setText(object.get(0).getMobilePhoneNumber());
                    tvEmail.setText(object.get(0).getEmail());
                    tvStudentId.setText(object.get(0).getStuId());
                    if(!object.get(0).getSex()){
                        ivSexuality.setImageResource(R.drawable.woman);
                    }
                }else{
                    Log.d("TAG", "done: "+e.getErrorCode()+e.getMessage());
                }
            }
        });
    }

}
