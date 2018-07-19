package com.narmware.smartseva.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.narmware.smartseva.R;
import com.narmware.smartseva.dialogs.PropDialogFragment;
import com.narmware.smartseva.helper.Constants;
import com.narmware.smartseva.pojo.MyBookings;

import java.util.ArrayList;


/**
 * Created by Lincoln on 31/03/16.
 */

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyViewHolder> {

    ArrayList<MyBookings> bookings;
    Context mContext;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtBService_name,mTxtBDate,mTxtBDesc,mTxtPrice;
        RatingBar mRatingBar;
        ImageView mImgStaus;
        MyBookings mItem;
        //RelativeLayout mRelativeItem;

        public MyViewHolder(View view) {
            super(view);
            mTxtBService_name= view.findViewById(R.id.txt_name);
            mTxtBDate=view.findViewById(R.id.txt_date);
            mTxtBDesc=view.findViewById(R.id.txt_desc);
            mRatingBar=view.findViewById(R.id.simpleRatingBar);
            mImgStaus=view.findViewById(R.id.status_img);

           // mRelativeItem=view.findViewById(R.id.relative);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // int position= (int) mRelativeItem.getTag();
                    //Toast.makeText(mContext, mItem.getB_service_img()+"", Toast.LENGTH_SHORT).show();
                    if(mItem.getB_status().equals(Constants.STATUS_COMPLETE)) {
                        setFragment(mItem.getB_service_name(), mItem.getB_date(), mItem.getB_desc(), mItem.getB_service_img(), mItem.getB_ratings());
                    }
                    }
            });


        }
    }

    public BookingsAdapter(Context context, ArrayList<MyBookings> bookings, FragmentManager fragmentManager) {
        this.mContext = context;
        this.bookings = bookings;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_boooking, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyBookings booking = bookings.get(position);


        //holder.mRelativeItem.setTag(position);
        holder.mTxtBDate.setText(booking.getB_date());
        holder.mTxtBService_name.setText(booking.getB_service_name());
        holder.mTxtBDesc.setText(booking.getB_desc());

        if(booking.getB_status().equals(Constants.STATUS_PENDING))
        {
            holder.mRatingBar.setVisibility(View.INVISIBLE);
            holder.mImgStaus.setImageResource(R.drawable.order_inprogress_copy);
        }
        if(booking.getB_status().equals(Constants.STATUS_COMPLETE))
        {
            holder.mRatingBar.setVisibility(View.VISIBLE);
            holder.mImgStaus.setImageResource(R.drawable.order_complete_copy);
            float rate= Float.parseFloat(booking.getB_ratings());
            holder.mRatingBar.setRating(rate);
        }


        holder.mItem=booking;
    }

    public void setFragment(String name,String date,String desc,String image_url,String rating) {
        /*fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //fragment.setSharedElementReturnTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode));
//            fragment.setExitTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode));

            // Create new fragment to add (Fragment B)
            //fragment.setSharedElementEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.slide_left));
            //fragment.setEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode));

            // Add Fragment B
           /* fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack("transaction")
                    .addSharedElement(mImgFrame, "MyTransition");

            fragmentTransaction.commit();*/


            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment prev = fragmentManager.findFragmentByTag("dialog");
            if (prev != null) {
                fragmentTransaction.remove(prev);
            }
            fragmentTransaction.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = PropDialogFragment.newInstance();
            Bundle args = new Bundle();
            args.putString("name",name);
            args.putString("date",date);
            args.putString("desc",desc);
            args.putString("image",image_url);
            args.putString("rating",rating);

            newFragment.setArguments(args);
            //newFragment.setSharedElementEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.slide_left));
            newFragment.setEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.slide_right));
            newFragment.setCancelable(false);
            newFragment.show(fragmentTransaction, "dialog");
        }
    }
    @Override
    public int getItemCount() {
        return bookings.size();
    }

}