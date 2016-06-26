package com.example.tyhj.linkman;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Tyhj on 2016/3/9.
 */
public class Number extends Activity {
    String[] str=new String[5];
    TextView tv_name;
    ImageButton imageView;
    List<Phone> list;
    PhoneAdapter ad;
    ListView lv;
    int x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonenumber);
        init();
        lv.setAdapter(ad);
    }
    private void init() {
        Intent in=getIntent();
        str= in.getStringArrayExtra("phonenumber");
        imageView= (ImageButton) findViewById(R.id.ib_head_bg);
        tv_name= (TextView) findViewById(R.id.tv_bg_name);
        tv_name.setText(str[2]);
        lv= (ListView) findViewById(R.id.lv_number);
        list=new ArrayList<Phone>();
        Phone ph=new Phone(str[0],R.drawable.ib_message);
        list.add(ph);
        if(str[1]!=null){
            Phone ph1=new Phone(str[1],R.drawable.ib_message);
            list.add(ph1);
            if(str[4]!=null){
                Phone ph2=new Phone(str[4],R.drawable.ib_message);
                list.add(ph2);
            }
        }
        WindowManager wm = (WindowManager) Number.this
                .getSystemService(Context.WINDOW_SERVICE);
        final int width = wm.getDefaultDisplay().getWidth();
        final int height = wm.getDefaultDisplay().getHeight();
        ad=new PhoneAdapter(Number.this,R.layout.list_number,list);
        ContentResolver cr = Number.this.getContentResolver();
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(str[5]));
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
           /* Bitmap photo = BitmapFactory.decodeStream(input);*/
        if(input!=null){
            Picasso.with(Number.this).load(uri).resize(width, 2*height/5).centerCrop().into(imageView);
        }
    }
    //监听返回键
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            Number.this.finish();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }

}
