package com.example.tyhj.linkman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tyhj on 2016/3/10.
 */
public class Welcome extends Activity {
    TextView tv_date,tv_week,tv_time,tv_word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AVOSCloud.initialize(this, "Pi6osFNUGmUL2tDLqznHLrIB-gzGzoHsz", "qnji1CIpMyjL58RkvSD2urGn");
        final AVUser currentUser = AVUser.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        tv_date= (TextView) findViewById(R.id.tv_date);
        tv_time= (TextView) findViewById(R.id.tv_time);
        tv_week= (TextView) findViewById(R.id.tv_week);
        tv_word= (TextView) findViewById(R.id.tv_word);
        Typeface typeface = Typeface.createFromAsset(Welcome.this.getAssets(), "fonts/yahei_light.ttc");
        Typeface typeface1 = Typeface.createFromAsset(Welcome.this.getAssets(), "fonts/fangtin_caoxi.TTF");
        tv_time.setTypeface(typeface1);
        tv_date.setTypeface(typeface);
        tv_week.setTypeface(typeface);
        tv_word.setTypeface(typeface);
        TextView tv_tyhj=(TextView) findViewById(R.id.tv_tyhj);
        tv_tyhj.setTypeface(typeface1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    handler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        WindowManager wm = (WindowManager) Welcome.this
                .getSystemService(Context.WINDOW_SERVICE);
        final int width = wm.getDefaultDisplay().getWidth();
        final int height = wm.getDefaultDisplay().getHeight();
        final ImageView rl_welcome= (ImageView) findViewById(R.id.iv_welcome_bg);
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet){
            getBackgroud(width, height, rl_welcome);
            getsignture();
            }else{
            tv_word.setText("衣带渐宽终不悔，为伊消得人憔悴");
            Picasso.with(getApplicationContext()).load(R.drawable.welcome).resize(width, height).centerCrop().into(rl_welcome);
        }
        rl_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    if(currentUser.getBoolean("emailVerified")){
                        Intent in = new Intent(Welcome.this, MainActivity.class);
                        startActivity(in);
                        Welcome.this.finish();
                        overridePendingTransition(R.anim.out, R.anim.enter);
                    }
                }else {
                    Intent in = new Intent(Welcome.this, Login.class);
                    startActivity(in);
                    Welcome.this.finish();
                }
            }
        });
    }

    private void getsignture() {
        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        userQuery.whereEqualTo("username", "13678141943");
        userQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if(e==null&&list.size()>0){
                    tv_word.setText(list.get(0).getString("signature"));
                }
            }
        });
    }

    private void setWelcomeTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// 设置日期格式
        String data = df.format(new Date()).substring(0, 19);
        String week=null;
        //String date = data.substring(0, 4) + "/" + data.substring(5, 7) + "/" + data.substring(8, 10) + "  " + data.substring(11, 13) + ":" + data.substring(14, 16);
        String month=data.substring(5, 7);
        String days=data.substring(8, 10);
        String minute= data.substring(11, 13) + ":" + data.substring(14, 16);
        Date date = null;
        try {
            date = df.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM",Locale.ENGLISH);
            week = sdf.format(date);
            month=sdf1.format(date);
            tv_date.setText(month+" "+days);
            tv_week.setText(week);
            tv_time.setText(minute);
        }
    }

    private void getBackgroud(final int width, final int height, final ImageView rl_welcome) {
        //获取welcomeImage
        AVQuery<AVObject> query = new AVQuery<AVObject>("Welcome");
        query.whereNotEqualTo("number", "0");
        query.orderByDescending("createdAt");
        query.setLimit(1); // 限制最多1个结果
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(final List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    Picasso.with(getApplicationContext()).load(avObjects.get(0).getAVFile("welcomeImage").getUrl()).resize(width, height).centerCrop().into(rl_welcome);
                } else {
                    Picasso.with(getApplicationContext()).load(R.drawable.welcome).resize(width, height).centerCrop().into(rl_welcome);
                }
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    setWelcomeTime();
                    break;
            }
        }
    };
}
