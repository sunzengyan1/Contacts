package com.example.sunze.contactdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.sunze.contactdemo.bean.CallRecordBean;
import com.example.sunze.contactdemo.bean.ContactBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sunze on 2017/8/7.
 */

public class GetContacUtils {

    private static List<ContactBean> contactBeanList = null;
    private static List<CallRecordBean> callRecordBeanList = null;

    /**
     * 获取联系人信息
     *
     * @param activity
     * @return
     */
    public static List<ContactBean> getContact(Activity activity) {
        contactBeanList = new ArrayList<>();
        Cursor cursor = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phoneNumber = "";
        while (cursor.moveToNext()) {
            ContactBean contactBean = new ContactBean();
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String PHOTO_THUMBNAIL_URI = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

            contactBean.setName(name);
            contactBean.setPhoto(PHOTO_THUMBNAIL_URI);

            int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (isHas > 0) {
                Cursor c = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                while (c.moveToNext()) {
                    String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneNumber += number + ",";
                }

                contactBean.setNumber(phoneNumber);
                phoneNumber = "";
                c.close();
            }

            contactBeanList.add(contactBean);
        }
        cursor.close();
        return contactBeanList;
    }

    /**
     * 获取通话记录信息
     * @param activity
     * @return
     */
    public static List<CallRecordBean> getCallRecord(Activity activity) {
        //  权限检查
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            /**
             * 动态申请权限
             * 这里没有在
             * onRequestPermissionsResult(int requestCode,
             * String[] permissions,int[] grantResults)方法里面处理
             *
             */
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 100);
            return null;
        }
        ContentResolver resolver = activity.getContentResolver();
        //获取cursor对象
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, new String[]{
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
        }, null, null, null);
        callRecordBeanList = new ArrayList<>();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    CallRecordBean callRecordBean = new CallRecordBean();
                    callRecordBean.setName(cursor.getString(0));
                    callRecordBean.setNumber(cursor.getString(1));
                    callRecordBean.setType(getCallType(cursor.getInt(2)));
                    callRecordBean.setDate(formatDate(cursor.getLong(3)));
                    Log.i("查询到的数据", "getCallRecord: " + callRecordBean.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();  //关闭cursor，避免内存泄露
            }
        }
        return callRecordBeanList;
    }

    /**
     * 转换时间格式
     *
     * @param time
     * @return
     */
    public static String formatDate(long time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        return format.format(new Date(time));
    }

    /**
     * 获取通话记录类型
     *
     * @param anInt
     * @return
     */
    private static String getCallType(int anInt) {
        switch (anInt) {
            case CallLog.Calls.INCOMING_TYPE:
                return "呼入";
            case CallLog.Calls.OUTGOING_TYPE:
                return "呼出";
            case CallLog.Calls.MISSED_TYPE:
                return "未接";
            case CallLog.Calls.REJECTED_TYPE:
                return "拒接";
            default:
                break;
        }
        return null;
    }
}
