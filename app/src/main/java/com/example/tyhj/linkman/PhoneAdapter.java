package com.example.tyhj.linkman;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tyhj on 2016/3/9.
 */
public class PhoneAdapter extends ArrayAdapter<Phone> {
    int resourseId;
    viewH vh;
    public PhoneAdapter(Context context, int resource, List<Phone> objects) {
        super(context, resource, objects);
        resourseId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Phone phone=getItem(position);
        vh=new viewH();
        convertView = LayoutInflater.from(getContext()).inflate(resourseId, null);
        vh.ib= (ImageButton) convertView.findViewById(R.id.ib_send_message);
        vh.tv= (TextView) convertView.findViewById(R.id.tv_number);
        vh.tv.setText(phone.getNumber());
        Picasso.with(getContext()).load(phone.getImage()).resize(80, 80).centerCrop().into(vh.ib);
        onClikEvent(phone);
        return convertView;
    }

    private void onClikEvent(final Phone phone) {
        //电话
        vh.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(phone);
            }
        });
        //短信
        vh.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder di = new AlertDialog.Builder(getContext());
                di.setCancelable(false);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View layout = inflater.inflate(R.layout.send_message, null);
                di.setView(layout);
                di.create();
                TextView tvsendto= (TextView) layout.findViewById(R.id.tvsendto);
                final Dialog dialog =di.show();
                final EditText et_message= (EditText) layout.findViewById(R.id.et_message);
                Button bt_send= (Button) layout.findViewById(R.id.bt_send);
                Button bt_cancel= (Button) layout.findViewById(R.id.bt_cancel);
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
                        if(!et_message.getText().toString().equals("")) {
                            sendSMS(phone.getNumber(), et_message.getText().toString());
                            Toast.makeText(getContext(), "已发送", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getContext(),"内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //电话
    private void callPhone(Phone name) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + name.getNumber()));
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
}
class viewH{
        ImageButton ib;
        TextView tv;
        }