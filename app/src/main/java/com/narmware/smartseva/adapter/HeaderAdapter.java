package com.narmware.smartseva.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.narmware.smartseva.R;
import com.narmware.smartseva.pojo.SubServices;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Lincoln on 31/03/16.
 */

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.MyViewHolder> {

     static List<SubServices> services;
    static Context mContext;

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
                    //Toast.makeText(mContext, mItem.getSer_name(), Toast.LENGTH_SHORT).show();

                    }
            });


        }
    }

    public HeaderAdapter(Context context, List<SubServices> services) {
        this.mContext = context;
        this.services = services;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubServices photo = services.get(position);


        holder.mRelativeItem.setTag(position);
        Picasso.with(mContext)
                .load(photo.getSer_img_url())
                .fit()
                .noFade()
                .placeholder(mContext.getResources().getDrawable(R.drawable.placeholder))
                .into(holder.mImgFrame);
        holder.mthumb_title.setText(photo.getSer_name());
        holder.mItem=photo;
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

}