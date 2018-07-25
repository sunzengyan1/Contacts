package com.example.sunze.contactdemo;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sunze on 2017/8/7.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Activity activity;
    private List<ContactBean> contactBeanList;
    private OnItemClickListener onItemClickListener;

    public ContactAdapter(Activity activity , List<ContactBean> contactBeanList){
        this.activity = activity;
        this.contactBeanList = contactBeanList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity,R.layout.rv_item_contact,null);
        ContactViewHolder holder = new ContactViewHolder(view,onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactBean contactBean = contactBeanList.get(position);
        if (contactBean.getName() != null){
            holder.mTvName.setText(contactBean.getName());
        }
        if (contactBean.getNumber() != null){
            holder.mTvNumber.setText(contactBean.getNumber().split(",")[0]);
        }
        if (contactBean.getPhoto() != null){
            holder.mIvPhoto.setImageURI(Uri.parse(contactBean.getPhoto()));
        }else {
            holder.mIvPhoto.setImageResource(R.mipmap.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return contactBeanList.size();
    }

    public void onItemClicklistener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        private ImageView mIvPhoto;
        private TextView mTvName;
        private TextView mTvNumber;
        private final OnItemClickListener onItemClickListener;

        public ContactViewHolder(View itemView , final OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvNumber = (TextView) itemView.findViewById(R.id.tv_number);

            mTvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }
}
