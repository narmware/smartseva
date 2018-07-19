package com.narmware.smartseva.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.narmware.smartseva.R;
import com.narmware.smartseva.fragment.SubSrvicesFragment;
import com.narmware.smartseva.pojo.ServiceMain;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Lincoln on 31/03/16.
 */

public class ViewMoreAdapter extends RecyclerView.Adapter<ViewMoreAdapter.MyViewHolder> {

     static List<ServiceMain> services;
    static Context mContext;
    ImageView mImgFrame;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String img;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mthumb_title;
        ServiceMain mItem;
        //RelativeLayout mRelativeItem;

        public MyViewHolder(View view) {
            super(view);
            mthumb_title= view.findViewById(R.id.txt_serv_name);
            mImgFrame=view.findViewById(R.id.img_serv);
           // mRelativeItem=view.findViewById(R.id.relative);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // int position= (int) mRelativeItem.getTag();
                    //Toast.makeText(mContext, mItem.getService_title()+"", Toast.LENGTH_SHORT).show();
                    setFragment(SubSrvicesFragment.newInstance(Integer.parseInt(mItem.getService_id()),img+mItem.getService_image(),mItem.getService_title(),mItem.getService_desc(),"subServiceList"));
                    }
            });


        }
    }

    public ViewMoreAdapter(Context context, List<ServiceMain> services, FragmentManager fragmentManager) {
        this.mContext = context;
        this.services = services;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_more, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ServiceMain serviceMain = services.get(position);

         img="http://aspiresoftwareweb.com/Asp_SmartSevaa/fetchGallery?type=UserPhoto&image=";

        //holder.mRelativeItem.setTag(position);
        Picasso.with(mContext)
                .load(img+serviceMain.getService_image())
                .fit()
                .noFade()
                .placeholder(mContext.getResources().getDrawable(R.drawable.placeholder))
                .into(mImgFrame);
        holder.mthumb_title.setText(serviceMain.getService_title());
        holder.mItem=serviceMain;
    }

    public void setFragment(Fragment fragment) {
        /*fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //fragment.setSharedElementReturnTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode));
            fragment.setExitTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode));

            // Create new fragment to add (Fragment B)
            //fragment.setSharedElementEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.slide_left));
            fragment.setEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode));

            // Add Fragment B
            fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack("transaction")
                    .addSharedElement(mImgFrame, "MyTransition");

            fragmentTransaction.commit();
        }
    }
    @Override
    public int getItemCount() {
        return services.size();
    }

}