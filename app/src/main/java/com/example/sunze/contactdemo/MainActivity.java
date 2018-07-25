package com.example.sunze.contactdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRvContact;
    private List<ContactBean> contactBeanList;
    private String numberPhone = "";
    /**
     * 请求联系人权限的请求码
     */
    private int permissionContactRequestCode = 0;
    /**
     * 请求打电话权限的请求码
     */
    private int permissionCallPhoneRequestCode = 1;
    /**
     * 请求短信权限的请求码
     */
    private int permissionMessageRequestCode = 2;
    /**
     * 请求联系人权限的请求码
     */
    private int permissionCallRecordRequestCode = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    /**
     * 初始化view
     */
    private void initView() {
        mRvContact = (RecyclerView) findViewById(R.id.rv_contact);
        requestRuntimePermission(Manifest.permission.READ_CONTACTS, permissionContactRequestCode);
    }

    /**
     * 获取联系人数据
     */
    private void getContact() {
        contactBeanList = GetContacUtils.getContact(MainActivity.this);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2, LinearLayoutManager.VERTICAL, false);
        mRvContact.setLayoutManager(layoutManager);
        ContactAdapter adapter = new ContactAdapter(MainActivity.this, contactBeanList);
        mRvContact.setAdapter(adapter);

        adapter.onItemClicklistener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                numberPhone = contactBeanList.get(position).getNumber();
                if (numberPhone != null) {
                    createDialog(contactBeanList.get(position).getName(), contactBeanList.get(position).getNumber());
                } else {
                    showToast("此联系人没有号码");
                }
//                requestRuntimePermission(Manifest.permission.READ_CALL_LOG, permissionCallRecordRequestCode);
            }
        });
    }

    /**
     * 呼叫联系人
     *
     * @param number
     */
    private void callPhone(final String number) {
        if (number != null) {
            //用intent启动拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + number);
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        } else {
            showToast("此联系人没有号码");
        }
    }

    /**
     * 发送信息
     * @param number
     */
    public void sendMessage(String number) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        startActivity(intent);
    }

    /**
     * 获取联系人数据
     */
    public void getCallRecord(){
        List<CallRecordBean> callRecordBeanList = GetContacUtils.getCallRecord(MainActivity.this);
        Log.i("111111111", "onItemClick: 通话记录数据======"+callRecordBeanList.toString());
    }

    /**
     * 创建提示的dialog
     */
    private void createDialog(String name, final String number) {
        final MyDialog myDialog = new MyDialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.show();
        myDialog.setMessage(name, number.split(",")[0]);
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = myDialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        //设置高度和宽度
//        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = d.getWidth(); // 宽度设置为屏幕的0.65
        //设置位置
        p.gravity = Gravity.CENTER;
        //设置透明度
//        p.alpha = 0.5f;
        dialogWindow.setAttributes(p);
        myDialog.onCallClicklistener(new onDialogClickListener() {
            @Override
            public void onCall() {
                myDialog.dismiss();
                requestRuntimePermission(Manifest.permission.CALL_PHONE, permissionCallPhoneRequestCode);
            }

            @Override
            public void onMessage() {
                requestRuntimePermission(Manifest.permission.SEND_SMS, permissionMessageRequestCode);
            }
        });
    }

    //andrpoid 6.0 需要写运行时权限
    public void requestRuntimePermission(String permissions, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permissions) != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{permissions}, requestCode);
        } else {
            // 有权限了，去放肆吧。
            if (requestCode == permissionContactRequestCode) {
                getContact();
            } else if (requestCode == permissionCallPhoneRequestCode) {
                callPhone(numberPhone);
            } else if (requestCode == permissionMessageRequestCode) {
                sendMessage(numberPhone);
            }else if (requestCode == permissionCallRecordRequestCode){
                getCallRecord();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断请求的权限类型进行对应的处理
        switch (permissions[0]) {
            case Manifest.permission.READ_CONTACTS://请求的是获取联系人权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//获取权限成功
                    getContact();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {//获取权限失败
                    showToast("获取联系人权限失败，请主动打开权限");
                } else {
                    requestRuntimePermission(Manifest.permission.READ_CONTACTS, permissionContactRequestCode);
                }
                break;
            case Manifest.permission.CALL_PHONE://请求的是拨打电话权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//获取权限成功
                    callPhone(numberPhone);
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {//获取权限失败
                    showToast("获取呼叫权限失败，请主动打开权限");
                } else {
                    requestRuntimePermission(Manifest.permission.CALL_PHONE, permissionCallPhoneRequestCode);
                }
                break;
            case Manifest.permission.SEND_SMS://请求的发送短信的权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//获取权限成功
                    sendMessage(numberPhone);
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {//获取权限失败
                    showToast("获取短信权限失败，请主动打开权限");
                } else {
                    requestRuntimePermission(Manifest.permission.SEND_SMS, permissionCallPhoneRequestCode);
                }
                break;
            case Manifest.permission.READ_CALL_LOG://读取通话记录权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//获取权限成功
                    getCallRecord();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {//获取权限失败
                    showToast("获取通话记录权限失败，请主动打开权限");
                } else {
                    requestRuntimePermission(Manifest.permission.READ_CALL_LOG, permissionCallRecordRequestCode);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 弹出土司
     *
     * @param content
     */
    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }
}
