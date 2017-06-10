package com.list.asus.forsigns.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.list.asus.forsigns.R;
import com.list.asus.forsigns.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;


/*
 * Created by ASUS on 2017/5/23.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etId;
    private EditText etPassword;

    private Button btLogin;
    private Button btToRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Toast.makeText(LoginActivity.this, "登录界面！", Toast.LENGTH_SHORT).show();

        initView();
    }

    private void initView() {
        etId = (EditText) findViewById(R.id.login_id);
        etPassword = (EditText) findViewById(R.id.login_password);
        btLogin = (Button)findViewById( R.id.login_button) ;
        btToRegister = (Button)findViewById( R.id.login_toRegister) ;

        btLogin.setOnClickListener(this);
        btToRegister.setOnClickListener(this);
    }

//    // 捕获返回键的方法2
//    @Override
//    public void onBackPressed() {
//        android.os.Process.killProcess(android.os.Process.myPid());
//        super.onBackPressed();
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            //用户登录
            case R.id.login_button:
                if (loginJudge() == 2){
                    BmobUser.loginByAccount( etId.getText().toString(), etPassword.getText().toString(), new LogInListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(user!=null){
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            //用户注册
            case R.id.login_toRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }


    /*  确保登陆输入的合法性
     *  返回值：0 -> 输入格式错误
     *         1 -> 输入正确且是老师
     *         2 -> 输入正确且是学生
     */
    public int loginJudge(){
        String id = etId.getText().toString();
        String password = etPassword.getText().toString();
        if (id.length() == 0){
            Toast.makeText(LoginActivity.this, "Your username is empty", Toast.LENGTH_LONG).show();
            return 0;
        }
        if(password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please input the password", Toast.LENGTH_LONG).show();
            return 0;
        }
        if(id.charAt(1) ==  't'){
            Toast.makeText(LoginActivity.this, "Please input student username", Toast.LENGTH_LONG).show();
            return 1;
        }else{
            return 2;
        }
    }




}

