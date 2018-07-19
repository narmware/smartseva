package com.narmware.smartseva.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.narmware.smartseva.pojo.Login;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 23;
    @BindView(R.id.txt_register) TextView mTxtRegister;
    @BindView(R.id.edt_login_mobile) EditText mEdtMobile;
    @BindView(R.id.edt_login_pass) EditText mEdtPassword;
    @BindView(R.id.btn_verify) Button mBtnVerfify;
    RequestQueue mVolleyRequest;

    String mMobile,mPassword;

    int validFlag=0;
    protected Dialog mNoConnectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //used to hide keyboard bydefault
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        init();


       /* View view=getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);*/

       /* if (isCallAllowed()) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.READ_SMS") != 0) {

            } else {
            }
            return;
        }
        requestCallPermission();*/
    }

    private void init() {
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(LoginActivity.this);

        mTxtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBtnVerfify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();

                if(validFlag==0) {
                    LoginUser();
                }
            }
        });
    }

    public void validateData()
    {
        mMobile=mEdtMobile.getText().toString().trim();
        mPassword=mEdtPassword.getText().toString().trim();

        validFlag=0;

        if(mMobile.length()<10)
        {
            validFlag=1;
            mEdtMobile.setError("Enter valid mobile number");
        }
        if(mPassword.equals(""))
        {
            validFlag=1;
            mEdtPassword.setError("Enter Password");
        }
    }
    private boolean isCallAllowed() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_SMS") == 0) {
            return true;
        }
        return false;
    }

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, String.valueOf(new String[]{"android.permission.READ_SMS"}))) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}, this.STORAGE_PERMISSION_CODE);
            Toast.makeText(this, "Permission accepted", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}, this.STORAGE_PERMISSION_CODE);
            Toast.makeText(this, "Oops Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != this.STORAGE_PERMISSION_CODE) {
            return;
        }
    }


    private void LoginUser() {


        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Verifying your number...");
        dialog.setCancelable(false);
        dialog.show();

        Gson gson=new Gson();
       // String json_string=gson.toJson(bookSchedule);
        //Log.e("Schedule json",json_string);

        HashMap<String,String> param = new HashMap();
        param.put(Constants.MOBILE_NUMBER,mMobile);
        param.put(Constants.PASSWORD,mPassword);

        //url with params
        String url= SupportFunctions.appendParam(MyApplication.URL_LOGIN,param);

        //url without params
        //String url= MyApplication.URL_GET_SERVICES;

        Log.e("Login url",url);
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

                            Log.e("Login Json_string",response.toString());
                            Gson gson = new Gson();

                            Login loginResponse= gson.fromJson(response.toString(), Login.class);

                            int res= Integer.parseInt(loginResponse.getResponse());
                            if(res==Constants.NEW_ENTRY) {
                                SharedPreferencesHelper.setIsLogin(true,LoginActivity.this);
                                SharedPreferencesHelper.setBookName(loginResponse.getName(),LoginActivity.this);
                                SharedPreferencesHelper.setBookMail(loginResponse.getEmail(),LoginActivity.this);
                                SharedPreferencesHelper.setBookMobile(loginResponse.getMobile(),LoginActivity.this);
                                SharedPreferencesHelper.setUserId(loginResponse.getUser_id(),LoginActivity.this);

                                Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                                    startActivity(intent);
                                    finish();
                            }
                            if(res==Constants.ERROR) {
                                Toast.makeText(LoginActivity.this, "Invalid mobile number or password", Toast.LENGTH_SHORT).show();
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
        mNoConnectionDialog = new Dialog(LoginActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
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
               LoginUser();
                mNoConnectionDialog.dismiss();
            }
        });
    }
}
