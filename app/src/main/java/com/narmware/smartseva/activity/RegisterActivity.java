package com.narmware.smartseva.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.smartseva.MyApplication;
import com.narmware.smartseva.R;
import com.narmware.smartseva.helper.Constants;
import com.narmware.smartseva.helper.SharedPreferencesHelper;
import com.narmware.smartseva.helper.SupportFunctions;
import com.narmware.smartseva.pojo.Register;
import com.narmware.smartseva.pojo.RegisterResponse;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.txt_login) TextView mTxtLogin;
    @BindView(R.id.btn_register) Button mBtnRegister;
    @BindView(R.id.edt_signup_name) EditText mEdtName;
    @BindView(R.id.edt_signup_mobile) EditText mEdtMobile;
    @BindView(R.id.edt_signup_email) EditText mEdtEmail;
    @BindView(R.id.edt_signup_pass) EditText mEdtPass;
    @BindView(R.id.edt_confirm_pass) EditText mEdtConfirmPass;

    String mName,mMobile,mMail,mPass,mConfirmPass;
    protected Dialog mNoConnectionDialog;

    RequestQueue mVolleyRequest;
    int validData=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        //used to hide keyboard bydefault
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        init();
    }

    private void init() {
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(RegisterActivity.this);

        mTxtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();

                if(validData==0) {
                    RegisterUser();

                }
            }
        });
    }

    public void validateData()
    {
        mName=mEdtName.getText().toString().trim();
        mMobile=mEdtMobile.getText().toString().trim();
        mMail=mEdtEmail.getText().toString().trim();
        mPass=mEdtPass.getText().toString().trim();
        mConfirmPass=mEdtConfirmPass.getText().toString().trim();

        validData=0;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(mName.equals(""))
        {
            validData=1;
            mEdtName.setError("Enter name");
        }
        if(mMobile.equals(""))
        {
            validData=1;
            mEdtMobile.setError("Enter mobile number");
        }
            if(mMail.equals("") || !mMail.matches(emailPattern))
        {
            validData=1;
            mEdtEmail.setError("Enter valid email");
        }
        if(mPass.equals(""))
        {
            validData=1;
            mEdtPass.setError("Enter password");
        }
        if(!mConfirmPass.equals(mPass))
        {
            validData=1;
            mEdtConfirmPass.setError("Password is not matching");
        }
        }

    private void RegisterUser() {

        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("Registering You...");
        dialog.setCancelable(false);
        dialog.show();

        Register register=new Register(mName,mMail,mMobile,mPass);
        Gson gson=new Gson();
        String json_string=gson.toJson(register);

        Log.e("Register json",json_string);

        HashMap<String,String> param = new HashMap();
        param.put(Constants.JSON_STRING,json_string);

        //url with params
        String url= SupportFunctions.appendParam(MyApplication.URL_REGISTER,param);

        //url without params
        //String url= MyApplication.URL_GET_SERVICES;

        Log.e("Register url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            Log.e("Register Json_string",response.toString());
                            Gson gson = new Gson();

                            final RegisterResponse registerResponse= gson.fromJson(response.toString(), RegisterResponse.class);

                            int res= Integer.parseInt(registerResponse.getResponse());

                            if(res==Constants.ALREADY_PRESENT) {
                                Toast.makeText(RegisterActivity.this, "Already registered", Toast.LENGTH_SHORT).show();
                            }
                            if(res==Constants.NEW_ENTRY) {
                                new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Registered successfully")
                                        .setContentText("Your credentials sent to your mail id")
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                SharedPreferencesHelper.setIsLogin(true,RegisterActivity.this);
                                                Intent intent = new Intent(RegisterActivity.this, BottomNavigationActivity.class);
                                                startActivity(intent);
                                                finish();

                                                SharedPreferencesHelper.setBookName(mName,RegisterActivity.this);
                                                SharedPreferencesHelper.setBookMail(mMail,RegisterActivity.this);
                                                SharedPreferencesHelper.setBookMobile(mMobile,RegisterActivity.this);
                                                SharedPreferencesHelper.setUserId(registerResponse.getUser_id(),RegisterActivity.this);

                                                sDialog.dismiss();
                                            }
                                        })

                                        .show();
                            }

                            if(res==Constants.ERROR) {
                                Toast.makeText(RegisterActivity.this, "Oops,Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
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
        mNoConnectionDialog = new Dialog(RegisterActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
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
                RegisterUser();
                mNoConnectionDialog.dismiss();
            }
        });
    }
}
