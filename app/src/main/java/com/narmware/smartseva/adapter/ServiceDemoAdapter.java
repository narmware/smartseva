package com.narmware.smartseva.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.narmware.smartseva.MyApplication;
import com.narmware.smartseva.R;
import com.narmware.smartseva.fragment.ViewMoreFragment;
import com.narmware.smartseva.helper.Constants;
import com.narmware.smartseva.helper.JSONParser;
import com.narmware.smartseva.pojo.SubServiceResponse;
import com.narmware.smartseva.pojo.SubServices;
import com.narmware.smartseva.pojo.ServiceMain;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Lincoln on 31/03/16.
 */

public class ServiceDemoAdapter extends RecyclerView.Adapter<ServiceDemoAdapter.MyViewHolder> {

     ArrayList<ServiceMain> mainServiceList;
    static Context mContext;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int flag=0;
    JSONParser mJsonParser;
    MyViewHolder holder;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SubServiceRecyclerAdapter mAdapter;
        public TextView mthumb_title;
        Button mBtnViewMore;
        ServiceMain mItem;
        RecyclerView mRecyclerView;
        ArrayList<SubServices> services;

        public MyViewHolder(View view) {
            super(view);
            mthumb_title= view.findViewById(R.id.txt_title);
            mRecyclerView=view.findViewById(R.id.recycler_view);
            mBtnViewMore=view.findViewById(R.id.btn_more);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    }
            });

        }
    }

    public ServiceDemoAdapter(Context context, ArrayList<ServiceMain> mainServiceList,FragmentManager fragmentManager) {
        mJsonParser=new JSONParser();
        this.mContext = context;
        this.mainServiceList = mainServiceList;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main_service, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ServiceMain service = mainServiceList.get(position);
        holder.mItem=service;
        if(position==0)
        {
            holder.mBtnViewMore.setVisibility(View.VISIBLE);
        }
        else{
            holder.mBtnViewMore.setVisibility(View.INVISIBLE);
        }
        holder.mBtnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "View More", Toast.LENGTH_SHORT).show();
                setFragment(new ViewMoreFragment());
            }
        });
        holder.mthumb_title.setText(mainServiceList.get(position).getService_title());

            Log.e("SubService id", holder.mItem.getService_id());
            //GetSubServices(holder.mItem.getService_id(), holder);


        this.holder=holder;
        setAdapter(holder);

            new GetCoupons().execute(holder.mItem.getService_id());

    }

    public void setAdapter(MyViewHolder holder)
    {
        holder.services=new ArrayList<>();

        SnapHelper snapHelper = new LinearSnapHelper();
        holder.mAdapter = new SubServiceRecyclerAdapter(mContext,holder.services, fragmentManager);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        snapHelper.attachToRecyclerView(holder.mRecyclerView);
        holder.mRecyclerView.setAdapter(holder.mAdapter);
        holder.mRecyclerView.setNestedScrollingEnabled(false);
        holder.mRecyclerView.setFocusable(false);

    }

    public void setFragment(Fragment fragment)
    {
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public int getItemCount() {
        return mainServiceList.size();
    }

/*
    private void GetSubServices(String id, final MyViewHolder holder) {

        RequestQueue mVolleyRequest;

        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage("getting sub services ...");
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String,String> param = new HashMap();
        param.put(Constants.SERVICE_ID,id);

        //url with params
        String url= SupportFunctions.appendParam(MyApplication.URL_GET_SUBSERVICES,param);

        //url without params
        //String url= MyApplication.URL_GET_SERVICES;

        Log.e("SubService Size url",url);
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

                            Log.e("SubService Size Json",response.toString());
                            Gson gson = new Gson();
                            SubServiceResponse subServiceResponse= gson.fromJson(response.toString(), SubServiceResponse.class);
                            if(subServiceResponse.getResponse().equals("100")) {
                                SubServices[] service = subServiceResponse.getData();
                                for (SubServices item : service) {

                                    SubServices sub=new SubServices(item.getSer_img_url(),item.getSer_name(),item.getSub_service_id(),holder.mItem.getService_id(),item.getSub_service_desc());
                                    holder.services.add(sub);
                                    Log.e("SubService desc", item.getSub_service_desc());

                                }
                                Log.e("Service Size",holder.services.size()+"");

                                SnapHelper snapHelper = new LinearSnapHelper();
                                holder.mAdapter = new SubServiceRecyclerAdapter(mContext,holder.services,fragmentManager);
                                //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
                                holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                                holder.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                snapHelper.attachToRecyclerView(holder.mRecyclerView);
                                holder.mRecyclerView.setAdapter(holder.mAdapter);
                                holder.mRecyclerView.setNestedScrollingEnabled(false);
                                holder.mRecyclerView.setFocusable(false);

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
                        // showNoConnectionDialog();
                        dialog.dismiss();

                    }
                }

        );

        mVolleyRequest=Volley.newRequestQueue(mContext);
        mVolleyRequest.add(obreq);
        flag=1;
    }
*/


    class GetCoupons extends AsyncTask<String, String, String> {
        ArrayList<SubServices> subServices;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {
            String id=strings[0];
            String json = null;
            try
            {
                Gson gson = new Gson();
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.SERVICE_ID,id);

                String url =MyApplication.URL_GET_SUBSERVICES;
                Log.e("SubService url",url);

                JSONObject ob=mJsonParser.makeHttpRequest(url, "GET",params);

                if (ob == null) {
                    Log.d("RESPONSE", "ERRORRRRR");
                }
                else {
                    json = ob.toString();
                }
            }
            catch (Exception ex) {

                ex.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                Gson gson = new Gson();
                subServices=new ArrayList<>();
                if (s != null)
                    Log.e("SubService data", s);

                    //getting test master array
                    // testMasterDetails = testMasterArray.toString();

                    SubServiceResponse subServiceResponse= gson.fromJson(s.toString(), SubServiceResponse.class);
                        SubServices[] service = subServiceResponse.getData();
                        for (SubServices item : service) {

                            SubServices sub=new SubServices(item.getSer_img_url(),item.getSer_name(),item.getSub_service_id(),holder.mItem.getService_id(),item.getSub_service_desc());
                            holder.services.add(sub);
                            Log.e("SubService desc", item.getSub_service_desc());
                            holder.mAdapter.notifyDataSetChanged();

                        }

                        for(int i=0;i<holder.services.size();i++)
                        {
                            if(holder.services.get(i).getMain_service_id()== holder.mItem.getService_id())
                            {
                                Log.e("ITEM match",holder.services.get(i).getSer_name()+" "+holder.services.get(i).getMain_service_id());
                            }
                        }
                        Log.e("SubService Size",holder.services.size()+"");



            }catch (Exception e)
            {
                //Toast.makeText(InvoiceActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
            }
        }
    }

}