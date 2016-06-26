package com.example.tyhj.linkman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import shape.CircularImage;

/**
 * Created by Tyhj on 2016/3/8.
 */
public class NameAdpter extends ArrayAdapter<Name>{
    int resourseId;
    AVUser user = AVUser.getCurrentUser();
    String username=user.getUsername();
    ViewHolder viewH;
    List<Name> obj;
    public NameAdpter(Context context, int resource, List<Name> objects) {
        super(context, resource, objects);
        obj=objects;
        resourseId=resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Name name=getItem(position);
        View view;
        if(convertView==null) {
            view = LayoutInflater.from(getContext()).inflate(resourseId, null);
            viewH = new ViewHolder();
            viewH.ib = (CircularImage) view.findViewById(R.id.ib_head);
            viewH.tv = (TextView) view.findViewById(R.id.tv_name);
            viewH.ll_press= (LinearLayout) view.findViewById(R.id.ll_press);
            view.setTag(viewH);
        }else{
            view=convertView;
            viewH= (ViewHolder) view.getTag();
        }
        viewH.tv.setText(name.getName());
        //设置头像
        if(name.getImageUrl().equals("0")){
            ContentResolver cr = getContext().getContentResolver();
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(name.getId()));
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
            if(input==null){
                Picasso.with(getContext()).load(R.drawable.head_image).resize(160, 160).centerCrop().into(viewH.ib);
            }else {
                Picasso.with(getContext()).load(uri).resize(150, 150).centerCrop().into(viewH.ib);
            }
        }else{
            Picasso.with(getContext()).load(name.getImageUrl()).resize(150, 150).centerCrop().into(viewH.ib);
        }
        viewH.ll_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] number = new String[6];
                number[0] = name.getNumber1();
                number[1] = name.getNumber2();
                number[4] = name.getNumber3();
                number[2] = name.getName();
                number[3] = name.getImageUrl();
                number[5] = name.getId();
                Intent in = new Intent(getContext(), Number.class);
                in.putExtra("phonenumber", number);
                getContext().startActivity(in);
            }
        });
        viewH.ll_press.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder di = new AlertDialog.Builder(getContext());
                di.setCancelable(true);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View layout = inflater.inflate(R.layout.menu_man, null);
                Button bt_send_massage, bt_detail, bt_delete;
                bt_send_massage = (Button) layout.findViewById(R.id.bt_send_message);
                bt_detail = (Button) layout.findViewById(R.id.bt_detail);
                bt_delete = (Button) layout.findViewById(R.id.bt_delete);
                di.setView(layout);
                di.create();
                final Dialog dialog = di.show();
                bt_send_massage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //装备发送短信
                        dialog.dismiss();
                        final AlertDialog.Builder di = new AlertDialog.Builder(getContext());
                        di.setCancelable(false);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View layout = inflater.inflate(R.layout.send_message, null);
                        di.setView(layout);
                        di.create();
                        final Dialog dialog = di.show();
                        final EditText et_message = (EditText) layout.findViewById(R.id.et_message);
                        Button bt_send = (Button) layout.findViewById(R.id.bt_send);
                        Button bt_cancel = (Button) layout.findViewById(R.id.bt_cancel);
                        bt_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        bt_send.setOnClickListener(new View.OnClickListener() {
                            //发送短信出去
                            @Override
                            public void onClick(View v) {
                                if (!et_message.getText().toString().equals("")) {
                                    sendSMS(name.getNumber1(), et_message.getText().toString());
                                    Toast.makeText(getContext(), "已发送", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                bt_detail.setOnClickListener(new View.OnClickListener() {
                    //详情页面
                    @Override
                    public void onClick(View v) {
                        String[] number = new String[6];
                        number[0] = name.getNumber1();
                        number[1] = name.getNumber2();
                        number[4] = name.getNumber3();
                        number[2] = name.getName();
                        number[3] = name.getImageUrl();
                        number[5] = name.getId();
                        Intent in = new Intent(getContext(), Number.class);
                        in.putExtra("phonenumber", number);
                        dialog.dismiss();
                        getContext().startActivity(in);
                    }
                });
                bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除联系人
                        String id = name.getId();
                        obj.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), "若系统自带云服务，则请从系统联系人删除", Toast.LENGTH_SHORT).show();
                        String[] whereparams = new String[]{id};
                        String where = ContactsContract.Data._ID + " =?";
                        getContext().getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, where, whereparams);
                        if (internet()) {
                            AVQuery<AVObject> query = new AVQuery<AVObject>("iCludeMan");
                            query.whereEqualTo("name", name.getName());
                            query.whereEqualTo("username", username);
                            query.setLimit(1); // 限制最多10个结果
                            query.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null && list.size() > 0) {
                                        list.get(0).deleteInBackground();
                                        //listView中删除item
                                        Toast.makeText(getContext(), "已从云端删除联系人", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });
        return view;
    }

    private void callPhone(Name name) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + name.getNumber1()));
        getContext().startActivity(intent);
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
    private boolean internet(){
        ConnectivityManager con=(ConnectivityManager)getContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet){
            return true;
        }else{
            return false;
        }
    }
}
class ViewHolder{
    LinearLayout ll_press;
    CircularImage ib;
    TextView tv;
}
