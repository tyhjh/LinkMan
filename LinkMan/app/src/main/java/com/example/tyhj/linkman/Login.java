package com.example.tyhj.linkman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Tyhj on 2016/3/9.
 */
public class Login extends Activity{
    Button bt_go,bt_register;
    EditText et_number,et_password;
    boolean ifDestroy=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        AVUser.logOut();
        init();
        ClikEvent();
    }
    private void init() {
        et_number= (EditText) findViewById(R.id.et_number);
        et_password= (EditText) findViewById(R.id.et_password);
        bt_go= (Button) findViewById(R.id.bt_go);
        bt_register= (Button) findViewById(R.id.bt_register);

    }

    private void ClikEvent() {
        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_number.getText().toString().equals("")&&!et_password.getText().toString().equals("")){
                    AVUser.logInInBackground(et_number.getText().toString(),
                        et_password.getText().toString(), new LogInCallback() {
                            public void done(AVUser user, AVException e) {
                                if (e == null) {
                                    Intent in = new Intent(Login.this, MainActivity.class);
                                    startActivity(in);
                                    Login.this.finish();
                                    ifDestroy=false;
                                    overridePendingTransition(R.anim.out, R.anim.enter);
                                } else {
                                    // 登录失败
                                    loginFail(e);
                                    ifDestroy=true;
                                }
                            }
                        });
                }else{
                    Toast.makeText(Login.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder di = new AlertDialog.Builder(Login.this);
                di.setCancelable(true);
                LayoutInflater inflater = LayoutInflater.from(Login.this);
                View layout = inflater.inflate(R.layout.register, null);
                ImageButton ib_save= (ImageButton) layout.findViewById(R.id.ib_save);
                final EditText et_add_name= (EditText) layout.findViewById(R.id.et_add_name);
                final EditText et_add_password= (EditText) layout.findViewById(R.id.et_add_number);
                final EditText et_add_email= (EditText) layout.findViewById(R.id.et_add_email);
                Picasso.with(getApplicationContext()).load(R.drawable.gou).resize(70, 70).centerCrop().into(ib_save);
                di.setView(layout);
                di.create();
                final Dialog dialog =di.show();
                ib_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(et_add_name.getText().toString().length()!=11||
                                et_add_email.getText().toString().equals("")||
                                et_add_password.getText().toString().equals("")||et_add_password.getText().toString().length()<6){
                            if(et_add_password.getText().toString().length()<6){
                                Toast.makeText(Login.this,"密码必须大于6位",Toast.LENGTH_SHORT).show();
                            }else
                            Toast.makeText(Login.this,"请正确输入必须信息",Toast.LENGTH_SHORT).show();
                        }else{
                            //注册
                            AVUser user = new AVUser();
                            user.setUsername(et_add_name.getText().toString());
                            user.setMobilePhoneNumber(et_add_name.getText().toString());
                            user.setPassword(et_add_password.getText().toString());
                            user.setEmail(et_add_email.getText().toString());
                            user.signUpInBackground(new SignUpCallback() {
                                public void done(AVException e) {
                                    if (e == null) {
                                        Toast.makeText(Login.this,"注册成功，请到邮箱进行验证",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        ifDestroy=true;
                                    } else {
                                        registerError(e, et_add_email, et_add_name);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void loginFail(AVException e) {
        if (e.getMessage()
                .equals("java.net.UnknownHostException")) {
            Toast.makeText(
                    getApplicationContext(),
                    "网络出错，请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (e.getMessage()
                .substring(8, 11).equals("211")) {
            Toast.makeText(
                    getApplicationContext(),
                    "未找到该账号", Toast.LENGTH_SHORT).show();
        } else if (e.getMessage()
                .substring(8, 11).equals("210")) {
            Toast.makeText(
                    getApplicationContext(),
                    "账号或密码错误", Toast.LENGTH_SHORT).show();
        } else if (e.getMessage()
                .substring(8, 11).equals("216")) {
            Toast.makeText(
                    getApplicationContext(),
                    "请先到邮箱完成邮箱验证", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //错误提醒
    private void registerError(AVException e, EditText et_add_email, EditText et_add_name) {
        if (e.getMessage().equals(
                "java.net.UnknownHostException")) {
            Toast.makeText(getApplicationContext(),
                    "网络出错，请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (e.getMessage().substring(8, 11)
                .equals("203")) {
            Toast.makeText(getApplicationContext(),
                    "邮箱已被注册，请重新输入",  Toast.LENGTH_SHORT).show();
            et_add_email.setText("");
        } else if (e.getMessage().substring(8, 11)
                .equals("125")) {
            Toast.makeText(getApplicationContext(),
                    "未找到该邮箱，请核对后输入",  Toast.LENGTH_SHORT).show();
            et_add_email.setText("");
        } else if (e.getMessage().substring(8, 11)
                .equals("127")) {
            Toast.makeText(getApplicationContext(),
                    "未找到该手机号，请核对后输入",  Toast.LENGTH_SHORT).show();
            et_add_name.setText("");
        } else if (e.getMessage().substring(8, 11)
                .equals("214")) {
            Toast.makeText(getApplicationContext(),
                    "手机号已被注册，请重新输入",  Toast.LENGTH_SHORT).show();
            et_add_name.setText("");
        } else {
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),  Toast.LENGTH_SHORT).show();
        }
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            AVUser.logOut();
            Login.this.finish();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ifDestroy){
            AVUser.logOut();
        }
    }
}
