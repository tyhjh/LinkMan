/*
package shape;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.tyhj.linkman.GetMessage;
import com.example.tyhj.linkman.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*/
/**
 * Created by Tyhj on 2016/3/21.
 *//*

public class MessageReceive extends BroadcastReceiver {
    String fullMessage="";
    SmsMessage[] messages;
    String address="";
    List<String> list=new ArrayList<String>();
    Context context;
    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        if(intent.getExtras()!=null) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            address = messages[0].getOriginatingAddress();
            for (SmsMessage message : messages) {
                fullMessage += message.getMessageBody();
            }
        }
            list.add(fullMessage);
            list.add(address);
            Get.setNumber(list);
            Intent in = new Intent(context, GetMessage.class);
            PendingIntent pi = PendingIntent.getActivities(context, 0, new Intent[]{in},PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("亲，收到一条短信")
                    .setContentText(fullMessage)
                    .setSmallIcon(R.drawable.cloud_ic)
                    .setContentIntent(pi)
                    .build();
            long[] vibrates = {0, 100, 100, 100};
            notification.vibrate = vibrates;
            notificationManager.notify(1, notification);
        }
    public void getSmsFromPhone() {
        ContentResolver cr =context.getContentResolver();
        String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type
        String where = " address = '1066321332' AND date >  "
                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));
            fullMessage=body;
            Toast.makeText(context,fullMessage,Toast.LENGTH_SHORT).show();
            address=name;
            //这里我是要获取自己短信服务号码中的验证码~~
            Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String res = matcher.group().substring(1, 11);
            }
        }
    }
    class SmsObserver extends ContentObserver {
        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone();
        }
    }
    public Handler smsHandler = new Handler() {
        //这里可以进行回调的操作
        //TODO

    };
}
*/
