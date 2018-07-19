package com.narmware.smartseva.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.narmware.smartseva.R;
import com.narmware.smartseva.helper.ImageBlur;
import com.squareup.picasso.Picasso;

public class PropDialogFragment extends DialogFragment {

    String name,desc,date,image_url,rating;
    TextView mTxtName,mTxtDate,mTxtDesc;
    RatingBar mRatingBar;
    ImageView mImgBlurr,mImgService;
    ImageButton mImgBtnClose;
    Bitmap bitmap;

    @SuppressLint("ValidFragment")
    private PropDialogFragment() { /*empty*/ }

    /** creates a new instance of PropDialogFragment */
    public static PropDialogFragment newInstance() {
        return new PropDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //getting proper access to LayoutInflater is the trick. getLayoutInflater is a                   //Function
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.item_booking_details, null);
        mTxtName=view.findViewById(R.id.txt_name);
        mTxtDate=view.findViewById(R.id.txt_date);
        mTxtDesc=view.findViewById(R.id.txt_desc);

        mImgBlurr=view.findViewById(R.id.img_blurr);
        mImgService=view.findViewById(R.id.img_service);

        mImgBtnClose=view.findViewById(R.id.btn_close);
        mRatingBar=view.findViewById(R.id.simpleRatingBar);

        name = getArguments().getString("name");
        date = getArguments().getString("date");
        desc = getArguments().getString("desc");
        image_url = getArguments().getString("image");
        rating = getArguments().getString("rating");

        mTxtName.setText(name);
        mTxtDesc.setText(desc);
        mTxtDate.setText(date);
        mRatingBar.setRating(Float.parseFloat(rating));

        Picasso.with(getContext())
                .load(image_url)
                .fit()
                .into(mImgService);
        try {

            bitmap = new ImageBlur().getBitmapFromURL(image_url);
            mImgBlurr.setImageBitmap(new ImageBlur().fastblur(bitmap, 4));
        }catch (Exception e)
        {

        }

        mImgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        //builder.setTitle("Hello").setNeutralButton("OK", null);
        return builder.create();
    }
}