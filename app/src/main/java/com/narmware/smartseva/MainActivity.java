package com.narmware.smartseva;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.smartseva.adapter.ServiceMainAdapter;
import com.narmware.smartseva.pojo.MainServiceResponse;
import com.narmware.smartseva.pojo.ServiceMain;
import com.narmware.smartseva.pojo.SubServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    //@BindView(R.id.edt_alb_id) EditText mEdtAlbumId;
    RequestQueue mVolleyRequest;
    ArrayList<ServiceMain> mainServiceList;
    ArrayList<SubServices> services;
    @BindView(R.id.recycler_view) RecyclerView mTitleList;
    ServiceMainAdapter demoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(MainActivity.this);

        setServiceAdapter(new LinearLayoutManager(MainActivity.this));
        GetServices();
    }

    public void setServiceAdapter(RecyclerView.LayoutManager mLayoutManager)
    {
        mainServiceList=new ArrayList<>();

        demoAdapter=new ServiceMainAdapter(MainActivity.this,mainServiceList,getSupportFragmentManager());
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mTitleList.setLayoutManager(mLayoutManager);
        mTitleList.setItemAnimator(new DefaultItemAnimator());
        mTitleList.setAdapter(demoAdapter);
        mTitleList.setNestedScrollingEnabled(false);
        mTitleList.setFocusable(false);

    }

    private void GetServices() {


        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("getting services ...");
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String,String> param = new HashMap();
        // param.put(Constants.REQUEST_ALBUM,mAlbumId);

        //url with params
        //String url= SupportFunctions.appendParam(MyApplication.URL_GET_SERVICES,param);

        //url without params
        String url=MyApplication.URL_GET_SERVICES;

        Log.e("Service url",url);
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

                            Log.e("Service Json_string",response.toString());
                            Gson gson = new Gson();

                            MainServiceResponse mainServiceResponse= gson.fromJson(response.toString(), MainServiceResponse.class);

                                ServiceMain[] service = mainServiceResponse.getData();
                                for (ServiceMain item : service) {


                                    mainServiceList.add(item);
                                    Log.e("Service desc", item.getService_desc());

                                }
                                demoAdapter.notifyDataSetChanged();



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
                        // showNoConnectionDialog();
                        dialog.dismiss();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

}
