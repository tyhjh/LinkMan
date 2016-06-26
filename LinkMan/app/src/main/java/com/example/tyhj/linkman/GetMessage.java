/*
package com.example.tyhj.linkman;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import java.util.List;

import shape.Get;

*/
/**
 * Created by Tyhj on 2016/3/21.
 *//*

public class GetMessage extends Activity {
    TextView tv_from,tv_message;
    String FROM="From      ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceptmessage);
        tv_from= (TextView) findViewById(R.id.tv_from);
        tv_message= (TextView) findViewById(R.id.tv_message);
        List<String> list=Get.getNumber();
        String number=list.get(1);
        String message=list.get(0);
        tv_message.setText(message);
        tv_from.setText(FROM+number);
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            GetMessage.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
*/
