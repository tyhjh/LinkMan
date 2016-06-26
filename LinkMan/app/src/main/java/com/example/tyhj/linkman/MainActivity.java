package com.example.tyhj.linkman;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shape.ChineseRetuen;
import shape.CircularImage;
import shape.DownloadTask;
import shape.FinishAll;
import shape.get_Download_Message;

public class MainActivity extends Activity {
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    ll_download.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    ll_uploading.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    ll_add.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    findMan();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    String date;
    boolean focus1,focus2;
    LinearLayout ll_show;
    boolean rotatepiont=true;
    boolean wifi;
    AVFile file = null;
    Animation show1,show2;
    Animation rotate2,rotate1;
    RelativeLayout rl_black,rl_find_list;
    ListView lv_man,lv_find;
    FloatingActionButton fab;
    Button bt_find_canle;
    EditText et_find_list;
    ImageButton ib_addman,ib_find,ib_uploading,ib_download,ib_gone;
    ImageView iv_myheadImage;
    TextView tx_addman,tx_uploading,tx_download,tx_gone,tx_mysignature,tx_myname;
    View headerView;
    NameAdpter nameAdpter2,nameAdpter1;
    List<Name> list;
    List<Name> list1;
    List<Name> findListx;
    Name name;
    String username;
    boolean first;
    public Button camoral, images;
    boolean success;
    boolean ifchange=false;
    boolean same2,same3;
    boolean same1=false;
    String[] number=new String[3];
    AVUser user;
    ChineseRetuen crn=new ChineseRetuen();
    LinearLayout ll_add,ll_download,ll_uploading,ll_gone;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    int WHERE_PHOTO = 0;
    String bkground = "headImage.jpg";
    AlertDialog.Builder di;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FinishAll.addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        clikeListener();
        select_Message();
        if(wifi) {
            upload();
            upload_headImage();
        }
        lv_man.addHeaderView(headerView);
        nameAdpter2=new NameAdpter(MainActivity.this,R.layout.name,list);
        lv_man.setAdapter(nameAdpter2);
        nameAdpter1=new NameAdpter(MainActivity.this,R.layout.name,findListx);
        lv_find.setAdapter(nameAdpter1);
    }

    //上传联系人
    private void upload() {
         first=true;
        for (int i=0;i<list.size();i++) {
            final String[] number=new String[3];
            number[0]=list.get(i).getNumber1();
            number[1]=list.get(i).getNumber2();
            number[2]=list.get(i).getNumber3();
            final AVObject post = new AVObject("iCludeMan");
            post.put("username",username);
            post.put("name", list.get(i).getName());
            post.put("number1", list.get(i).getNumber1());
            if( number[1]!=null){
                post.put("number2", list.get(i).getNumber2());
            }
            if( number[2]!=null){
                post.put("number3", list.get(i).getNumber3());
            }
            cover(i, number, post);
        }
            upload_headImage();
    }
    //是否重复
    private void cover(final int i, final String[] number, final AVObject post) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("iCludeMan");
        query.whereEqualTo("name", list.get(i).getName() );
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null && avObjects.size() > 0) {
                    //我去，两个都是null
                    //1
                    if (!avObjects.get(0).getString("number1").equals(number[0])) {
                        avObjects.get(0).put("number1", number[0]);
                        ifchange = true;
                    }
                    //2
                    if (avObjects.get(0).getString("number2") != null && number[1] != null) {
                        same2 = !avObjects.get(0).getString("number2").equals(number[1]);
                        if (same2) {
                            avObjects.get(0).put("number2", number[1]);
                            ifchange = true;
                        }
                    } else {
                        if (avObjects.get(0).getString("number2") != null && number[1] == null) {
                            avObjects.get(0).remove("number2");
                            ifchange = true;
                        } else if (avObjects.get(0).getString("number2") == null && number[1] != null) {
                            avObjects.get(0).put("number2", number[1]);
                            ifchange = true;
                        }
                    }

                    //3
                    if (avObjects.get(0).getString("number3") != null && number[2] != null) {
                        same3 = !avObjects.get(0).getString("number3").equals(number[2]);
                        if (same3) {
                            avObjects.get(0).put("number3", number[2]);
                            ifchange = true;
                        }
                    } else {
                        if (avObjects.get(0).getString("number3") != null && number[2] == null) {
                            avObjects.get(0).remove("number3");
                            ifchange = true;
                        } else if (avObjects.get(0).getString("number3") == null && number[2] != null) {
                            avObjects.get(0).put("number3", number[2]);
                            ifchange = true;
                        }
                    }
                    //保存
                    avObjects.get(0).saveInBackground();
                } else if (e == null) {

                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                success = true;
                            } else {
                                Toast.makeText(MainActivity.this, "网络出错，请检查网络", Toast.LENGTH_SHORT).show();
                                first = false;
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "网络出错，请检查网络", Toast.LENGTH_SHORT).show();
                    first = false;
                }
            }
        });
    }
    //获得联系人信息
    private void select_Message() {
        int times=list1.size();
        Cursor cursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext()) {
            times--;
            if(times<0){
            byte[] bs=null;
            String[] str = new String[10];
            int count=0;
            String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            ContentResolver cr = MainActivity.this.getContentResolver();
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
            if(input!=null){
                try {
                    bs =readBytesFromIS(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String manname=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+id,null,null);
            while(phones.moveToNext()){
                str[count]=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                count++;
                if(count>3) break;
            }
            phones.close();
            name=new Name(manname,"0",str[0],str[1],str[2], id,bs);
                 list1.add(name);
            }
        }
        List<Name>[] myList=new List[26];
        for(int i=0;i<26;i++){
            myList[i]=new ArrayList<Name>();
        }
        for(int k=0;k<list1.size();k++){
            String str=crn.getSelling(list1.get(k).getName());
            myList[getInt(str)].add(list1.get(k));
        }
        list.clear();
       int y=0;
        for(int i=0;i<26;i++){
            for (int j = 0; j < myList[i].size(); j++) {
                try {
                    list.add(myList[i].get(j));
                    nameAdpter2.notifyDataSetChanged();
                }catch (Exception e){

                }
            }
        }
    }
    private void init() {
        user = AVUser.getCurrentUser();
        et_find_list= (EditText) findViewById(R.id.et_find);
        bt_find_canle= (Button) findViewById(R.id.bt_find_canle);
        ll_show= (LinearLayout) findViewById(R.id.ll_show);
        ll_add= (LinearLayout) findViewById(R.id.ll_add);
        ll_download= (LinearLayout) findViewById(R.id.ll_download);
        ll_gone= (LinearLayout) findViewById(R.id.ll_gone);
        ll_uploading= (LinearLayout) findViewById(R.id.ll_uploading);
        ib_gone= (ImageButton) findViewById(R.id.ib_gone);
        ib_addman= (ImageButton) findViewById(R.id.ib_addman);
        ib_uploading= (ImageButton) findViewById(R.id.ib_uploading);
        ib_download= (ImageButton) findViewById(R.id.ib_download);
        tx_addman= (TextView) findViewById(R.id.tx_add);
        tx_uploading= (TextView) findViewById(R.id.tx_uploading);
        tx_download= (TextView) findViewById(R.id.tx_download);
        tx_gone= (TextView) findViewById(R.id.tx_gone);
        lv_man= (ListView) findViewById(R.id.lv_man);
        lv_find= (ListView) findViewById(R.id.find_list);
        Picasso.with(getApplicationContext()).load(R.drawable.ib_gone).resize(100, 100).centerCrop().into(ib_gone);
        Picasso.with(getApplicationContext()).load(R.drawable.download2).resize(100, 100).centerCrop().into(ib_download);
        Picasso.with(getApplicationContext()).load(R.drawable.linkman_add).resize(100, 100).centerCrop().into(ib_addman);
        Picasso.with(getApplicationContext()).load(R.drawable.uploading1).resize(100, 100).centerCrop().into(ib_uploading);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        rl_black= (RelativeLayout) findViewById(R.id.rl_black);
        rl_black.setVisibility(View.INVISIBLE);
        rl_find_list= (RelativeLayout) findViewById(R.id.rl_find_list);
        ib_find= (ImageButton) findViewById(R.id.im_find);
        Picasso.with(getApplicationContext()).load(R.drawable.im_find).resize(70, 70).centerCrop().into(ib_find);
        rotate1= AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        rotate2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotatepositin);
        rotate1.setFillAfter(true);
        rotate2.setFillAfter(true);
        show1=AnimationUtils.loadAnimation(MainActivity.this, R.anim.show);
        show2=AnimationUtils.loadAnimation(MainActivity.this, R.anim.noshow);
        username=user.getUsername();
        headerView = getLayoutInflater().inflate(R.layout.userhead, null);
        tx_myname= (TextView) headerView.findViewById(R.id.tv_username);
        tx_mysignature= (TextView) headerView.findViewById(R.id.tv_usersignature);
        iv_myheadImage= (ImageView) headerView.findViewById(R.id.iv_myhead);
        Typeface typeface = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/yahei_light.ttc");
        tx_myname.setTypeface(typeface);
        //刷新
        tx_myname.setText(user.getString("myName"));
        tx_mysignature.setText(user.getString("signature"));
        list=new ArrayList<Name>();
        list1=new ArrayList<Name>();
        findListx=new ArrayList<Name>();
        if(internet()){
            if(user.getAVFile("headImage")!=null){
                Picasso.with(MainActivity.this).load(user.getAVFile("headImage").getUrl()).resize(200, 200).centerCrop().into(iv_myheadImage);
            }else{
                Picasso.with(MainActivity.this).load(R.drawable.default_headimage).resize(250, 250).centerCrop().into(iv_myheadImage);
            }
        }else{
            Picasso.with(MainActivity.this).load(R.drawable.default_headimage).resize(250, 250).centerCrop().into(iv_myheadImage);
        }
        getContentResolver().registerContentObserver(Uri.parse("content://contacts/people"),true,new LinkMenObserver(new Handler()));
    }
    private boolean internet(){
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
         wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet){
            return true;
        }else{
            return false;
        }
    }
    //查找联系人
    private void findMan(){
        findListx.clear();
        String str=et_find_list.getText().toString();
        int len=str.length();
        for(int i=0;i<list.size();i++){
            boolean b1=false;boolean b2=false;boolean b3=false;boolean b4=false;boolean b5=false;boolean b6=false;boolean b7=false;
            if(len<=11) {
                b1=list.get(i).getNumber2()!=null;
                b2=list.get(i).getNumber3()!=null;
                b5=list.get(i).getName().length()>=len;
                b7=list.get(i).getNumber1().length()>=len&&list.get(i).getNumber1().substring(0, len).equals(str);
                if(b5) b6=list.get(i).getName().substring(0, len).equals(str);
                if (b1&&list.get(i).getNumber2().length()>=len) b3 = list.get(i).getNumber2().substring(0, len).equals(str);
                if (b2&&list.get(i).getNumber3().length()>=len) b4 = list.get(i).getNumber3().substring(0, len).equals(str);
                if (b6|| b7 || b3 || b4) {
                    findListx.add(list.get(i));
                    nameAdpter1.notifyDataSetChanged();
                }
            }
        }
        nameAdpter1.notifyDataSetChanged();
    }
    private void clikeListener() {
        //名字
        tx_myname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPersonalMessage();
            }
        });
        //签名
        tx_mysignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPersonalMessage();
            }
        });
        //头像
        iv_myheadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        //查找联系人
        ib_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                same1=true;
                //new Thread(th1).start();
                rl_find_list.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
            }
        });
        bt_find_canle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                same1=false;
                rl_find_list.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
        rl_find_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_find_list.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                same1=false;
            }
        });
        //菜单关闭
        rl_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rotatepiont) {
                    fab.startAnimation(rotate2);
                    ll_gone.setVisibility(View.INVISIBLE);
                    ll_download.setVisibility(View.INVISIBLE);
                    ll_uploading.setVisibility(View.INVISIBLE);
                    ll_add.setVisibility(View.INVISIBLE);
                    rotatepiont = true;
                    rl_black.setVisibility(View.INVISIBLE);
                    rl_black.startAnimation(show2);
                }
            }
        });
        //菜单
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rotatepiont) {
                    rl_black.setVisibility(View.VISIBLE);
                    ll_gone.setVisibility(View.VISIBLE);
                    rl_black.startAnimation(show1);
                    rotatepiont = false;
                    fab.startAnimation(rotate1);
                    showMenu();
                } else {
                    rotatepiont = true;
                    fab.startAnimation(rotate2);
                    ll_gone.setVisibility(View.INVISIBLE);
                    ll_download.setVisibility(View.INVISIBLE);
                    ll_uploading.setVisibility(View.INVISIBLE);
                    ll_add.setVisibility(View.INVISIBLE);
                    rl_black.setVisibility(View.INVISIBLE);
                    rl_black.startAnimation(show2);
                }
            }
        });
        et_find_list.addTextChangedListener(textWatcher);
        //添加联系人
        ib_addman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotatepiont=true;
                fab.startAnimation(rotate2);
                rl_black.setVisibility(View.INVISIBLE);
                addMan();
            }
        });
        //添加联系人
        tx_addman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotatepiont=true;
                fab.startAnimation(rotate2);
                rl_black.setVisibility(View.INVISIBLE);
                addMan();
            }
        });
        //同步联系人
        ib_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internet()){
                    download();
                }else {
                    Toast.makeText(getApplicationContext(), "同步失败，请检查网络", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //同步联系人
        tx_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internet()){
                    download();
                }else {
                    Toast.makeText(getApplicationContext(), "同步失败，请检查网络", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //上传联系人
        ib_uploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internet()){
                    Toast.makeText(getApplicationContext(), "正在上传联系人到云端", Toast.LENGTH_SHORT).show();
                    upload();
                    Toast.makeText(getApplicationContext(), "上传完成", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "上传失败，请检查网络", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //上传联系人
        tx_uploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet()) {
                    Toast.makeText(getApplicationContext(), "正在上传联系人到云端", Toast.LENGTH_SHORT).show();
                    upload();
                } else {
                    Toast.makeText(getApplicationContext(), "上传失败，请检查网络", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //退出登录
        ib_gone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Login.class);
                startActivity(in);
                AVUser.logOut();
                MainActivity.this.finish();
            }
        });
        //退出登录
        tx_gone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Login.class);
                startActivity(in);
                AVUser.logOut();
                MainActivity.this.finish();
            }
        });
    }
//设置个人信息
    private void setPersonalMessage() {
        di = new AlertDialog.Builder(MainActivity.this);
        di.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View layout = inflater.inflate(R.layout.jump, null);
        di.setView(layout);
        di.create();
        final Dialog dialog =di.show();
        ImageButton save= (ImageButton) layout.findViewById(R.id.ib_save);
        Picasso.with(getApplicationContext()).load(R.drawable.gou).resize(70, 70).centerCrop().into(save);
        Button bt= (Button) layout.findViewById(R.id.bt_title);
        final EditText et_name= (EditText) layout.findViewById(R.id.et_add_name);
        final EditText et_signature= (EditText) layout.findViewById(R.id.et_add_number);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                focus1=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_signature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                focus2=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_signature.setInputType(InputType.TYPE_CLASS_TEXT);
        bt.setText("个人信息");
        et_name.setHint("昵称");
        et_signature.setHint("个性签名");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(focus1){
                    user.put("myName",et_name.getText().toString());
                    tx_myname.setText(et_name.getText().toString());
                }
                if(focus2){
                    user.put("signature",et_signature.getText().toString());
                    tx_mysignature.setText(et_signature.getText().toString());
                }
                focus1=false;
                focus2=false;
                user.saveInBackground();
                dialog.dismiss();
            }
        });
    }

    //同步联系人
    private void download() {
        final List<AVFile> listbyte = new ArrayList<AVFile>();
        final List<get_Download_Message> list_download = new ArrayList<get_Download_Message>();
        Toast.makeText(getApplicationContext(), "联系人同步中", Toast.LENGTH_SHORT).show();
        AVQuery<AVObject> query = new AVQuery<AVObject>("iCludeMan");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list2, AVException e) {
                if (e == null && list2.size() > 0) {
                    for (int i = 0; i < list2.size(); i++) {
                        boolean finish = false;
                        boolean one = false;
                        String number1 = list2.get(i).getString("number1");
                        String number2 = list2.get(i).getString("number2");
                        String number3 = list2.get(i).getString("number3");
                        String name = list2.get(i).getString("name");
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).getName().equals(name)) {
                                if (list.get(j).getNumber1() == null || !list.get(j).getNumber1().equals(number1)) {
                                    get_Download_Message gm = new get_Download_Message(name, number1);
                                    list_download.add(gm);
                                    listbyte.add(null);
                                }
                                if (number2 != null) {
                                    if (list.get(j).getNumber2() == null || !list.get(j).getNumber2().equals(number2)) {
                                        get_Download_Message gm = new get_Download_Message(name, number2);
                                        list_download.add(gm);
                                        listbyte.add(null);
                                    }
                                }
                                if (number3 != null) {
                                    if (list.get(j).getNumber3() == null || !list.get(j).getNumber3().equals(number3)) {
                                        get_Download_Message gm = new get_Download_Message(name, number3);
                                        list_download.add(gm);
                                        listbyte.add(null);
                                    }
                                }
                                finish = true;
                            }
                        }
                        if (!finish) {
                            get_Download_Message gm = new get_Download_Message(name, number1);
                            list_download.add(gm);
                            listbyte.add(list2.get(i).getAVFile("headImage"));
                            if (number2 != null) {
                                get_Download_Message gm1 = new get_Download_Message(name, number2);
                                list_download.add(gm1);
                                listbyte.add(list2.get(i).getAVFile("headImage"));
                            }
                            if (number3 != null) {
                                get_Download_Message gm2 = new get_Download_Message(name, number3);
                                list_download.add(gm2);
                                listbyte.add(list2.get(i).getAVFile("headImage"));
                            }
                        }
                    }
                    new DownloadTask(MainActivity.this, list_download, username, listbyte).execute();
                    Toast.makeText(MainActivity.this, "在完成前请不要关闭iCloud", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //show me
    private void showMenu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(40);
                    Message message=new Message();
                    message.what=1;
                    mHandler.sendMessage(message);
                    Thread.sleep(40);
                    Message message1=new Message();
                    message1.what=2;
                    mHandler.sendMessage(message1);
                    Thread.sleep(40);
                    Message message2=new Message();
                    message2.what=3;
                    mHandler.sendMessage(message2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //添加联系人
    private void addMan() {
        final AlertDialog.Builder di = new AlertDialog.Builder(MainActivity.this);
        di.setCancelable(true);
        //布局转view
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View layout = inflater.inflate(R.layout.jump, null);
        ImageButton ib_save= (ImageButton) layout.findViewById(R.id.ib_save);
        final EditText et_add_name= (EditText) layout.findViewById(R.id.et_add_name);
        final EditText et_add_number= (EditText) layout.findViewById(R.id.et_add_number);
        Picasso.with(getApplicationContext()).load(R.drawable.gou).resize(70, 70).centerCrop().into(ib_save);
        di.setView(layout);
        di.create();
        final Dialog dialog =di.show();
        ib_save.setOnClickListener(new View.OnClickListener() {
            //向通讯录添加联系人
            @Override
            public void onClick(View v) {
                //保存数据
                final String name = et_add_name.getText().toString();
                final String number = et_add_number.getText().toString();
                addMan_into_mobile(name, number,null);
                final AVObject post = new AVObject("iCludeMan");
                //是否重复
                AVQuery<AVObject> query = new AVQuery<AVObject>("iCludeMan");
                query.whereEqualTo("name", name);
                query.whereEqualTo("username", username);
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null && list.size() > 0) {
                            String str1 = list.get(0).getString("number1");
                            String str2 = list.get(0).getString("number2");
                            String str3 = list.get(0).getString("number3");
                            if (!str1.equals(number)) {
                                if (str2 == null) {
                                    post.put("username", username);
                                    post.put("name", name);
                                    post.put("number2", number);
                                    post.saveInBackground();
                                } else if (str3 == null) {
                                    post.put("username", username);
                                    post.put("name", name);
                                    post.put("number3", number);
                                    post.saveInBackground();
                                }
                            }
                        } else if (e == null) {
                            post.put("username", username);
                            post.put("name", name);
                            post.put("number1", number);
                            post.saveInBackground();
                        }
                    }
                });
                //完
                select_Message();
                Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    //添加联系人方法
    private void addMan_into_mobile(String name, String number, byte[] by) {
        ContentValues values=new ContentValues();
        Uri rawContactUri=getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId= ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        //添加名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        //设置号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,number);
        //设置添加电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();
        //头像
        if(by!=null) {
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.Contacts.Photo.PHOTO, by);
            getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        }
    }

    //监听返回键
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(same1){
                same1=false;
                rl_find_list.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }else if(!rotatepiont){
                rotatepiont = true;
                fab.startAnimation(rotate2);
                ll_gone.setVisibility(View.INVISIBLE);
                ll_download.setVisibility(View.INVISIBLE);
                ll_uploading.setVisibility(View.INVISIBLE);
                ll_add.setVisibility(View.INVISIBLE);
                rl_black.setVisibility(View.INVISIBLE);
                rl_black.startAnimation(show2);
            }else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    MainActivity.this.finish();
                }
            }
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
    //input 转二进制
    public static byte[] readBytesFromIS(InputStream is) throws IOException {
        int total = is.available();
        byte[] bs = new byte[total];
        is.read(bs);
        return bs;
    }
    //上传联系人头像
   private void upload_headImage(){
       for(int i=0;i<list.size();i++){
           if(list.get(i).getBs()!=null){
               final byte[] bs=list.get(i).getBs();
               final String strname=list.get(i).getName();
               AVQuery<AVObject> query = new AVQuery<AVObject>("iCludeMan");
               query.whereEqualTo("username", username);
               query.whereEqualTo("name", strname );
               query.setLimit(1);
               query.findInBackground(new FindCallback<AVObject>() {
                   @Override
                   public void done(final List<AVObject> list1, AVException e) {
                       if (e == null && list1.size() > 0) {
                           if (list1.get(0).getAVFile("headImage") == null) {
                               final AVFile avFile = new AVFile(username + strname, bs);
                               avFile.saveInBackground(new SaveCallback() {
                                   @Override
                                   public void done(AVException e) {
                                       if (e != null) {
                                           Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                       } else {
                                           list1.get(0).put("headImage", avFile);
                                           list1.get(0).saveInBackground();
                                       }
                                   }
                               });
                           }
                       }
                   }
               });
           }
       }
    }
    //发送短信
    public void sendSMS(String phoneNumber, String message) {
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }
    public int getInt(String str){
        int x;
        switch (str){
            case "a":x=0;break;case "b":x=1;break;case "c":x=2;break;case "d":x=3;break;
            case "e":x=4;break;case "f":x=5;break;case "g":x=6;break;case "h":x=7;break;
            case "i":x=8;break;case "j":x=9;break;case "k":x=10;break;case "l":x=11;break;
            case "m":x=12;break;case "n":x=13;break;case "o":x=14;break;case "p":x=15;break;
            case "q":x=16;break;case "r":x=17;break;case "s":x=18;break;case "t":x=19;break;
            case "u":x=20;break;case "v":x=21;break;case "w":x=22;break;case "x":x=23;break;
            case "y":x=24;break;case "z":x=25;break;default:return 0;
        }
        return x;
    }
    // 通过URI找path
    public static String getFilePathFromUri(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = context.getContentResolver().query(uri, projection,
                selection, selectionArgs, sortOrder);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        cursor.close();
        cursor = null;
        return path;
    }
    // 通过URI找path
    public static String getFilePathFromContentUri(Uri selectedVideoUri, ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    // 上传用户头像
    private void dialog() {
        di = new AlertDialog.Builder(MainActivity.this);
        di.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View layout = inflater.inflate(R.layout.headchoose, null);
        di.setView(layout);
        di.create();
        di.show();
        camoral = (Button) layout.findViewById(R.id.camoral);
        images = (Button) layout.findViewById(R.id.images);
        // 相机
        camoral.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File f1 = new File(Environment.getExternalStorageDirectory()+"/LinkManPhone");
                if(!f1.exists()){
                    f1.mkdirs();
                }
                File outputImage = new File(Environment
                        .getExternalStorageDirectory()+"/LinkManPhone", "head.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                WHERE_PHOTO = 1;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        // 相册
        images.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File f1 = new File(Environment.getExternalStorageDirectory()+"/LinkManPhone");
                if(!f1.exists()){
                    f1.mkdirs();
                }
                File outputImage = new File(Environment
                        .getExternalStorageDirectory()+"/LinkManPhone", "head.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);

                WHERE_PHOTO = 2;
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
            }
        });
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        imageUri = data.getData();
                    }
                    SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");// 设置日期格式
                    date =df.format(new Date()).substring(0, 19)+".JPEG";
                    Bitmap bitmap= null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    saveBitmap(date,bitmap);
                    File file=new File(Environment.getExternalStorageDirectory()+"/LinkManPhone",date);
                    //换图片
                    imageUri = Uri.fromFile(file);
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {

                        Picasso.with(MainActivity.this).load(imageUri).resize(250, 250).centerCrop().into(iv_myheadImage);
                            file = AVFile.withAbsoluteLocalPath("userHeadImage"+username,
                                    Environment.getExternalStorageDirectory()+"/LinkManPhone"+"/"+date);
                        WHERE_PHOTO = 0;
                        user.put("headImage", file);
                        user.saveInBackground();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
    //图片保存，压缩
    public void saveBitmap(String str,Bitmap bm) {
        File f1 = new File(Environment.getExternalStorageDirectory()+"/LinkManPhone");
        if(!f1.exists()){
            f1.mkdirs();
        }
        File f = new File(Environment.getExternalStorageDirectory()+"/LinkManPhone",str);
        int options = 80;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 2000) {
            baos.reset();
            options -= 10;
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private final class LinkMenObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public LinkMenObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            select_Message();
            nameAdpter2.notifyDataSetChanged();
        }
    }
    //EditText内容变化
    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            findMan();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}