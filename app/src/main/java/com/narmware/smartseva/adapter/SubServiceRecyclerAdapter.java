package com.narmware.smartseva.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.narmware.smartseva.R;
import com.narmware.smartseva.activity.BookAppointmentActivity;
import com.narmware.smartseva.fragment.SubSrvicesFragment;
import com.narmware.smartseva.helper.ImageBlur;
import com.narmware.smartseva.pojo.SubServices;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Lincoln on 31/03/16.
 */

public class SubServiceRecyclerAdapter extends RecyclerView.Adapter<SubServiceRecyclerAdapter.MyViewHolder> {

     static List<SubServices> services;
    static Context mContext;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Dialog mPriceDialog;
    String img;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mthumb_title;
        ImageView mImgFrame;
        SubServices mItem;
        RelativeLayout mRelativeItem;

        public MyViewHolder(View view) {
            super(view);
            mthumb_title= view.findViewById(R.id.txt_serv_name);
            mImgFrame=view.findViewById(R.id.img_serv);
            mRelativeItem=view.findViewById(R.id.relative);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position= (int) mRelativeItem.getTag();

                    setFragment(SubSrvicesFragment.newInstance(Integer.parseInt(mItem.getSub_service_id()),img+mItem.getSer_img_url(),mItem.getSer_name(),mItem.getSub_service_desc(),"subSubServiceList"));
                    }
            });

        }
    }

    public SubServiceRecyclerAdapter(Context context, List<SubServices> services, FragmentManager fragmentManager) {
        this.mContext = context;
        this.services = services;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubServices photo = services.get(position);

        img="http://aspiresoftwareweb.com/Asp_SmartSevaa/fetchGallery?type=UserPhoto&image=";

        holder.mRelativeItem.setTag(position);
        Picasso.with(mContext)
                .load(img+photo.getSer_img_url())
                .fit()
                .noFade()
                .placeholder(mContext.getResources().getDrawable(R.drawable.placeholder))
                .into(holder.mImgFrame);
        holder.mthumb_title.setText(photo.getSer_name());
        holder.mItem=photo;
    }

    public void setFragment(Fragment fragment)
    {
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }
    @Override
    public int getItemCount() {
        return services.size();
    }

    private void showPriceDialog(final String s_name, final String image_url) {
        mPriceDialog = new Dialog(mContext);
        mPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mPriceDialog.setContentView(R.layout.item_price_dialog);
        mPriceDialog.setCancelable(true);
        mPriceDialog.show();

        Bitmap bitmap;
        ImageButton mImgBtnClose=mPriceDialog.findViewById(R.id.btn_close);
        TextView mTxtName=mPriceDialog.findViewById(R.id.txt_name);
        ImageView mImgService=mPriceDialog.findViewById(R.id.img_service);
        ImageView mImgBlurr=mPriceDialog.findViewById(R.id.img_blurr);
        Button mBtnCancel=mPriceDialog.findViewById(R.id.btn_cancel);
        Button mBtnProceed=mPriceDialog.findViewById(R.id.btn_proceed);

        mTxtName.setText(s_name);
        Picasso.with(mContext)
                .load(image_url)
                .fit()
                .into(mImgService);
        try {

            bitmap = new ImageBlur().getBitmapFromURL(image_url);
            mImgBlurr.setImageBitmap(new ImageBlur().fastblur(bitmap, 10));
        }catch (Exception e)
        {

        }
        mImgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPriceDialog.dismiss();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPriceDialog.dismiss();
            }
        });

        mBtnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPriceDialog.dismiss();
                Intent intent=new Intent(mContext, BookAppointmentActivity.class);
                intent.putExtra("image",image_url);
                intent.putExtra("name",s_name);
                mContext.startActivity(intent);
            }
        });
    }
}