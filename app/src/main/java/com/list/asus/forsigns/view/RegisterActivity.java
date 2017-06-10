package com.list.asus.forsigns.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.list.asus.forsigns.R;
import com.list.asus.forsigns.bean.Student;
import com.list.asus.forsigns.bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/*
 * Created by ASUS on 2017/5/23.
 */

public class RegisterActivity extends AppCompatActivity {

    public EditText etId;
    public EditText etPassword;
    public EditText etRePassword;
    public Button btRegister1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Toast.makeText(RegisterActivity.this, "注册界面", Toast.LENGTH_SHORT).show();

        initView();

    }

    private void initView() {
        etId = (EditText) findViewById(R.id.register_id);
        etPassword = (EditText) findViewById(R.id.register_password);
        etRePassword = (EditText) findViewById(R.id.register_RePassword);
        btRegister1 = (Button)findViewById(R.id.register_button) ;

        btRegister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegisterJudge() == 2){
                    BmobUser bu = new BmobUser();
                    bu.setUsername( etId.getText().toString());
                    bu.setPassword(etPassword.getText().toString());
                    //注意：不能用save方法进行注册
                    bu.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User s, BmobException e) {
                            if(e==null){
                                addToStudent();
                                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }



    /*  确保注册输入的合法性
        *  返回值：0 -> 输入格式错误
        *         1 -> 输入正确且是老师
        *         2 -> 输入正确且是学生
        *  未解决问题：当username为10位时，如何判断是否是学号或者是教师编号
        */
    public int RegisterJudge(){
        String id = etId.getText().toString();
        String password = etPassword.getText().toString();
        String RePassword = etRePassword.getText().toString();
        if (id.length() == 0){
            Toast.makeText(RegisterActivity.this, "Your username is empty", Toast.LENGTH_LONG).show();
            return 0;
        }
        if(RePassword.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please input the password twice", Toast.LENGTH_LONG).show();
            return 0;
        }
        if(!password.equals(RePassword)){
            Toast.makeText(RegisterActivity.this, "Please input the same password", Toast.LENGTH_LONG).show();
            return 0;
        }
        if(id.charAt(1) ==  't'){
            return 1;
        }else{
            return 2;
        }
    }


    /*
    *  注册的时候向Student表添加一行数据
     */
    public void addToStudent(){
        Student student = new Student();
        student.setStuId(etId.getText().toString());
        student.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
            }
        });
    }

}

