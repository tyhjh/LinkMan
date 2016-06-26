package shape;

import android.app.Activity;

import com.example.tyhj.linkman.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyhj on 2016/3/12.
 */
public class FinishAll {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
                activity.overridePendingTransition(R.anim.out, R.anim.enter);
            }
        }
    }
}
//添加联系人操作，
  /*  public long write(Card card, Context context) throws Exception {
        if (null == card || tool.isBlank(card.getName())) {
            return 0;
        }
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        // 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri uri = resolver.insert(RawContacts.CONTENT_URI, values);
        long id = ContentUris.parseId(uri);
        values.clear();// 往data表入姓名数据
        values.put(Data.RAW_CONTACT_ID, id);
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        values.put(StructuredName.GIVEN_NAME, card.getName());
        resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.clear();// 添加用户名 职位 部门
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);
        values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
        values.put(Organization.LABEL, card.getLabel());
        values.put(Organization.TITLE, card.getPosition());
        values.put(Organization.COMPANY, card.getCompany());
        values.put(Organization.TYPE, Organization.TYPE_WORK);
        resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.clear();// 添加邮箱
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);
        values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
        values.put(Email.DATA, card.getEmail());
        values.put(Email.TYPE, Email.TYPE_WORK);
        resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.clear();// 添加手机
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, card.getMobile());
        values.put(Phone.TYPE, Phone.TYPE_WORK_MOBILE);
        resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.clear();// 添加固定电话
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, card.getPhone());
        values.put(Phone.TYPE, Phone.TYPE_WORK);
        resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.clear();// 添加传真
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, card.getFax());
        values.put(Phone.TYPE, Phone.TYPE_FAX_WORK);
        resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.clear();// 添加地址 邮编
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);
        values.put(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
        values.put(StructuredPostal.FORMATTED_ADDRESS, card.getAddress());
        values.put(StructuredPostal.POSTCODE, card.getPost());
        values.put(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK);
        resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.clear();// 个人网站
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);
        values.put(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE);
        values.put(Website.DATA, card.getUrl());
        values.put(Website.TYPE, Website.TYPE_WORK);
        resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        if (null != card.getBitmap()) {
            values.clear();// 添加头像
            values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);
            values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            card.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, array);
            values.put(Photo.PHOTO, array.toByteArray());
            resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
        }
        return id;
    }
*/