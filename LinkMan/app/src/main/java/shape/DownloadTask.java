package shape;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.tyhj.linkman.MainActivity;
import com.example.tyhj.linkman.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyhj on 2016/3/12.
 */
public class DownloadTask extends AsyncTask<List<get_Download_Message>,Integer,Boolean> {
    Context mycontext=null;
    List<get_Download_Message> list=null;
    String username;
    List<AVFile> listbyte;
    public DownloadTask(Context context,List<get_Download_Message> list,String username,List<AVFile> listbyte) {
        // TODO Auto-generated constructor stub
        mycontext=context;
        this.list=list;
        this.username=username;
        this.listbyte=listbyte;
    }
    //保存到通讯录
    private void addMan_into_mobile(String name, String number,byte[] by) {
        ContentValues values=new ContentValues();
        Uri rawContactUri=mycontext.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId= ContentUris.parseId(rawContactUri);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        //添加名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        mycontext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        //设置号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,number);
        //设置添加电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        //添加号码
        mycontext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();
        if(by!=null){
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.Contacts.Photo.PHOTO, by);
            mycontext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        }
    }
    //
    @Override
    protected Boolean doInBackground(List<get_Download_Message>... params) {
        for(int i=0;i<list.size();i++){
            byte[] byt= new byte[0];
            try {
                if(listbyte.get(i)!=null) {
                    byt = listbyte.get(i).getData();
                }
            } catch (AVException e) {
                e.printStackTrace();
            }
            addMan_into_mobile(list.get(i).getName(), list.get(i).getNumber(), byt);
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean){
            Toast.makeText(mycontext,"同步完成",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(mycontext,MainActivity.class);
            FinishAll.removeAll();
            mycontext.startActivity(in);
        }else{
            Toast.makeText(mycontext,"同步失败",Toast.LENGTH_SHORT).show();
        }
    }
}
