package com.example.sunze.contactdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by sunze on 2017/8/7.
 */

public class MyDialog extends Dialog implements View.OnClickListener {

    private TextView mTvName;
    private TextView mTvNumber;
    private Button mBtnCall;
    private Button mBtnMessage;
    private com.example.sunze.contactdemo.onDialogClickListener onDialogClickListener;

    protected MyDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_my);

        initView();
    }

    private void initView() {
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvNumber = (TextView) findViewById(R.id.tv_number);
        mBtnCall = (Button) findViewById(R.id.btn_call);
        mBtnMessage = (Button) findViewById(R.id.btn_message);

        mBtnCall.setOnClickListener(this);
        mBtnMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call:
                onDialogClickListener.onCall();
                break;
            case R.id.btn_message:
                onDialogClickListener.onMessage();
                break;
        }
    }

    /**
     * 设置提示信息
     * @param name
     * @param number
     */
    public void setMessage(String name, String number){
        mTvName.setText(name);
        mTvNumber.setText(number);
    }

    /**
     * 设置确定按钮的点击回调
     */
    public void onCallClicklistener(onDialogClickListener onDialogClickListener){
        this.onDialogClickListener = onDialogClickListener;
    }
}
