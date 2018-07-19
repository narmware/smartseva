package com.narmware.smartseva.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.smartseva.MyApplication;
import com.narmware.smartseva.R;
import com.narmware.smartseva.fragment.AddressFragment;
import com.narmware.smartseva.fragment.ConfirmOrderFragment;
import com.narmware.smartseva.fragment.DateTimeFragment;
import com.narmware.smartseva.fragment.HomeFragment;
import com.narmware.smartseva.fragment.PersonalDetailsFragment;
import com.narmware.smartseva.helper.Constants;
import com.narmware.smartseva.helper.SharedPreferencesHelper;
import com.narmware.smartseva.helper.SupportFunctions;
import com.narmware.smartseva.pojo.BookSchedule;
import com.narmware.smartseva.pojo.BookScheduleResponse;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookAppointmentActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,DateTimeFragment.OnFragmentInteractionListener,AddressFragment.OnFragmentInteractionListener
,PersonalDetailsFragment.OnFragmentInteractionListener,ConfirmOrderFragment.OnFragmentInteractionListener
{
    @BindView(R.id.btn_next) Button mBtnNext;
    @BindView(R.id.btn_continue) Button mBtnContinue;
    @BindView(R.id.progress) ProgressBar mProgressBar;
    @BindView(R.id.linear_back) LinearLayout mBtnBack;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int count=0;
    int validFlag=0;
    int validOfficeFlag=0;
    public static String mDate,mTime,mName,mMobile,mAlternateMobile,mMail,mAddr,mOrderDesc;
    String mBuildName,mHouseNum,mStreetName,mCity,mPin;
    String mCompName,mCompBuildName,mCompStreetName,mCompCity,mCompPin,mCompAddr;
    String serviceName,serviceImage;
    RequestQueue mVolleyRequest;
    protected Dialog mNoConnectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(BookAppointmentActivity.this);

        Intent intent=getIntent();
        serviceName=intent.getStringExtra("name");
        serviceImage=intent.getStringExtra("image");

        setFragment(new AddressFragment());

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentProgress=mProgressBar.getProgress();
                int progress=currentProgress+50;
                ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, progress);
                progressAnimator.setDuration(500);
                progressAnimator.setInterpolator(new LinearInterpolator());

                mDate= SharedPreferencesHelper.getBookDate(BookAppointmentActivity.this);
                mTime= SharedPreferencesHelper.getBookDate(BookAppointmentActivity.this);

                //current frag address
                if(count==0)
                {
                    //Toast.makeText(BookAppointmentActivity.this,count+"",Toast.LENGTH_SHORT).show();

                    if(SharedPreferencesHelper.getIsHome(BookAppointmentActivity.this)==true) {
                        mBuildName = AddressFragment.mEdtBdName.getText().toString();
                        mHouseNum = AddressFragment.mEdtHouseNum.getText().toString();
                        mStreetName = AddressFragment.mEdtStreetName.getText().toString();
                        mCity = AddressFragment.mEdtCity.getText().toString();
                        mPin = AddressFragment.mEdtPin.getText().toString();

                        validateAddress();



                        if(validFlag==0) {
                            mAddr=mBuildName+","+mHouseNum+","+mStreetName+","+mCity+","+mPin;
                            SharedPreferencesHelper.setBookAddress(mAddr,BookAppointmentActivity.this);

                            progressAnimator.start();
                            setFragment(new PersonalDetailsFragment());
                            count++;
                            mBtnContinue.setVisibility(View.VISIBLE);
                            mBtnNext.setVisibility(View.INVISIBLE);
                        }
                    }
                    if(SharedPreferencesHelper.getIsHome(BookAppointmentActivity.this)==false) {
                        mCompName = AddressFragment.mEdtCompName.getText().toString();
                        mCompBuildName = AddressFragment.mEdtCompBdName.getText().toString();
                        mCompStreetName = AddressFragment.mEdtCompStreetName.getText().toString();
                        mCompCity = AddressFragment.mEdtCompCity.getText().toString();
                        mCompPin = AddressFragment.mEdtCompPin.getText().toString();

                        validateOfficeAddress();



                        if(validOfficeFlag==0) {
                            mCompAddr=mCompName+","+mCompBuildName+","+mCompStreetName+","+mCompCity+","+mCompPin;
                            SharedPreferencesHelper.setBookOfficeAddress(mCompAddr,BookAppointmentActivity.this);

                            progressAnimator.start();
                            setFragment(new PersonalDetailsFragment());
                            count++;
                            mBtnContinue.setVisibility(View.VISIBLE);
                            mBtnNext.setVisibility(View.INVISIBLE);
                        }
                    }



                }
            }
        });

        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //current frag personal_details
                //Toast.makeText(BookAppointmentActivity.this,count+"",Toast.LENGTH_SHORT).show();
                if(count==1)
                {

                    mName=PersonalDetailsFragment.mEdtName.getText().toString().trim();
                    mMobile=PersonalDetailsFragment.mEdtMobile.getText().toString().trim();
                    mMail=PersonalDetailsFragment.mEdtMail.getText().toString().trim();
                    mAlternateMobile=PersonalDetailsFragment.mEdtAlterMobile.getText().toString();
                    mOrderDesc=PersonalDetailsFragment.mEdtDesc.getText().toString();

                    SharedPreferencesHelper.setBookName(mName,BookAppointmentActivity.this);
                    SharedPreferencesHelper.setBookMobile(mMobile,BookAppointmentActivity.this);
                    SharedPreferencesHelper.setBookMail(mMail,BookAppointmentActivity.this);

                    validatePersonalData();

                    if(validFlag==0) {
                        SendSchedule();
                    }

                }
            }
        });


        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookAppointmentActivity.super.onBackPressed();
                customBackPress();
            }
        });
    }

    public void validatePersonalData()
    {
        validFlag=0;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(mName==null||mName.equals(""))
        {
            validFlag=1;
            PersonalDetailsFragment.mEdtName.setError("Enter name");
        }
        if(mMobile.length()<10||mMobile.equals(""))
        {
            validFlag=1;
            PersonalDetailsFragment.mEdtMobile.setError("Enter valid number");
        }

        if(mMail.equals("") || !mMail.matches(emailPattern))
        {
            validFlag=1;
            PersonalDetailsFragment.mEdtMail.setError("Enter valid Email");
        }
        if(mOrderDesc==null||mOrderDesc.equals(""))
        {
            validFlag=1;
            PersonalDetailsFragment.mEdtDesc.setError("Enter description");
        }

    }

    public void validateAddress()
    {
        validFlag=0;

        if(mBuildName==null||mBuildName.equals(""))
        {
            validFlag=1;
            AddressFragment.mEdtBdName.setError("Enter Building or house name");
        }
        if(mHouseNum==null||mHouseNum.equals(""))
        {
            validFlag=1;
            AddressFragment.mEdtHouseNum.setError("Enter House number");
        }
        if(mStreetName==null||mStreetName.equals(""))
        {
            validFlag=1;
            AddressFragment.mEdtStreetName.setError("Enter street name");
        }
        if(mCity==null||mCity.equals(""))
        {
            validFlag=1;
            AddressFragment.mEdtCity.setError("Enter city");
        }
        if(mPin==null||mPin.equals(""))
        {
            validFlag=1;
            AddressFragment.mEdtPin.setError("Enter pincode");
        }
    }

    public void validateOfficeAddress()
    {
        validOfficeFlag=0;

        if(mCompName==null||mCompName.equals(""))
        {
            validOfficeFlag=1;
            AddressFragment.mEdtCompName.setError("Enter Company name");
        }
        if(mCompBuildName==null||mCompBuildName.equals(""))
        {
            validOfficeFlag=1;
            AddressFragment.mEdtCompBdName.setError("Enter building/complex name");
        }
        if(mCompStreetName==null||mCompStreetName.equals(""))
        {
            validOfficeFlag=1;
            AddressFragment.mEdtCompStreetName.setError("Enter street name");
        }
        if(mCompCity==null||mCompCity.equals(""))
        {
            validOfficeFlag=1;
            AddressFragment.mEdtCompCity.setError("Enter city");
        }
        if(mCompPin==null||mCompPin.equals(""))
        {
            validOfficeFlag=1;
            AddressFragment.mEdtCompPin.setError("Enter pincode");
        }

    }
    public void setFragment(Fragment fragment)
    {
        fragmentManager=getSupportFragmentManager();
       /* fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //fragment.setSharedElementReturnTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode));
            fragment.setExitTransition(TransitionInflater.from(BookAppointmentActivity.this).inflateTransition(android.R.transition.slide_left));

            // Create new fragment to add (Fragment B)
            //fragment.setSharedElementEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.slide_left));
            fragment.setEnterTransition(TransitionInflater.from(BookAppointmentActivity.this).inflateTransition(android.R.transition.slide_right));

            // Add Fragment B
            fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack("transaction");
            //.addSharedElement(mImgFrame, "MyTransition");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void customBackPress()
    {
        if(mBtnContinue.getVisibility()==View.VISIBLE)
        {
            mBtnNext.setVisibility(View.VISIBLE);
            mBtnContinue.setVisibility(View.INVISIBLE);
        }
        count--;

        if(count==-1)
        {
            finish();
        }

        int currentProgress=mProgressBar.getProgress();
        int progress=currentProgress-50;
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, progress);
        progressAnimator.setDuration(500);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

       customBackPress();
    }


    private void SendSchedule() {


        final ProgressDialog dialog = new ProgressDialog(BookAppointmentActivity.this);
        dialog.setMessage("Sending schedule...");
        dialog.setCancelable(false);
        dialog.show();

        BookSchedule bookSchedule=new BookSchedule();
        bookSchedule.setName(mName);
        if(SharedPreferencesHelper.getIsHome(BookAppointmentActivity.this)==true)
        {
            bookSchedule.setAddress(mAddr);
            bookSchedule.setPincode(mPin);
        }
        else{
            bookSchedule.setAddress(mCompAddr);
            bookSchedule.setPincode(mCompPin);
        }
        bookSchedule.setMob(mMobile);
        bookSchedule.setService(serviceName);
        bookSchedule.setEmail(mMail);
        bookSchedule.setMsg(mOrderDesc);
        bookSchedule.setAlt_mobile(mAlternateMobile);
        bookSchedule.setUser_id(SharedPreferencesHelper.getUserId(BookAppointmentActivity.this));

        Gson gson=new Gson();
        String json_string=gson.toJson(bookSchedule);
        Log.e("Schedule json",json_string);

        HashMap<String,String> param = new HashMap();
        param.put(Constants.JSON_STRING,json_string);

        //url with params
        String url= SupportFunctions.appendParam(MyApplication.URL_SEND_SCHEDULE,param);

        //url without params
        //String url= MyApplication.URL_GET_SERVICES;

        Log.e("Schedule url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            //getting test master array
                            // testMasterDetails = testMasterArray.toString();

                            Log.e("Schedule Json_string",response.toString());
                            Gson gson = new Gson();

                            BookScheduleResponse bookResponse= gson.fromJson(response.toString(), BookScheduleResponse.class);

                            if(bookResponse.getResponse().equals("200")) {

                                new SweetAlertDialog(BookAppointmentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Done")
                                        .setContentText("Order Placed successfully")
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                sDialog.dismiss();
                                                finish();
                                            }
                                        })

                                        .show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                            //Toast.makeText(NavigationActivity.this, "Invalid album id", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                         showNoConnectionDialog();
                        dialog.dismiss();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

    private void showNoConnectionDialog() {
        mNoConnectionDialog = new Dialog(BookAppointmentActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_noconnectivity);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button exit = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_exit);
        Button tryAgain = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_try_again);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendSchedule();
                mNoConnectionDialog.dismiss();
            }
        });
    }

}
