package com.narmware.smartseva.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.narmware.smartseva.helper.JSONParser;
import com.narmware.smartseva.helper.SupportFunctions;
import com.narmware.smartseva.pojo.ServiceMain;
import com.narmware.smartseva.pojo.SubServiceResponse;
import com.narmware.smartseva.pojo.SubServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Lincoln on 31/03/16.
 */

public class ServiceMainAdapter extends RecyclerView.Adapter<ServiceMainAdapter.MyViewHolder> {

    ArrayList<ServiceMain> mainServiceList;
    Context mContext;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    JSONParser mJsonParser;
    RequestQueue mVolleyRequest;
    MyViewHolder mHolder;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        TextView mthumb_title;
        Button mBtnViewMore;
        ServiceMain mItem;
        ArrayList<SubServices> services=new ArrayList<>();
        SubServiceRecyclerAdapter adapter;
        ArrayList<SubServices> subServices;

        public MyViewHolder(View view) {
            super(view);
            mthumb_title= view.findViewById(R.id.txt_title);
            mRecyclerView=view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
           /* adapter = new SubServiceRecyclerAdapter(mContext, subServices, fragmentManager);
            mRecyclerView.setAdapter(adapter);*/

            Log.e("On","MyViewHolder");

            mBtnViewMore=view.findViewById(R.id.btn_more);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    }
            });

        }
    }

    public ServiceMainAdapter(Context context, ArrayList<ServiceMain> mainServiceList, FragmentManager fragmentManager) {
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
        holder.mthumb_title.setText(service.getService_title());
        mHolder=holder;
        new GetCoupons().execute(service.getService_id());
        // getSubCategories(service.getService_id(), holder.mRecyclerView);

    }

    private void getSubCategories(final String id, final RecyclerView recycler) {

        HashMap<String,String> param = new HashMap();
        param.put(Constants.SERVICE_ID, id);
        String url= SupportFunctions.appendParam(MyApplication.URL_GET_SUBSERVICES,param);
        Log.d("sub url", url);

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
                            String data = response.toString();
                            Log.d("DTA", data);
                            Gson gson = new Gson();
                            SubServiceResponse sub = gson.fromJson(data, SubServiceResponse.class);
                            SubServices[] services = sub.getData();
                            List<SubServices> alist = Arrays.asList(services);
                            ArrayList<SubServices> subServices=new ArrayList<>();

                            for(SubServices item : alist) {
                                SubServices s=new SubServices(item.getSer_img_url(),item.getSer_name(),item.getSub_service_id(),id,item.getSub_service_desc());
                                subServices.add(s);
                                Log.d("ITEM ", item.getSer_name());
                            }
                            SubServiceRecyclerAdapter adapter = new SubServiceRecyclerAdapter(mContext, subServices, fragmentManager);
                            recycler.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");

                    }
                }

        );

        mVolleyRequest= Volley.newRequestQueue(mContext);
        mVolleyRequest.add(obreq);

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



    class GetCoupons extends AsyncTask<String, String, String> {

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
                if (s != null)
                    Log.e("SubService data", s);

                //getting test master array
                // testMasterDetails = testMasterArray.toString();

                SubServiceResponse subServiceResponse= gson.fromJson(s.toString(), SubServiceResponse.class);
                mHolder.subServices=new ArrayList<>();

                    SubServices[] service = subServiceResponse.getData();
                    for (SubServices item : service) {

                        SubServices sub=new SubServices(item.getSer_img_url(),item.getSer_name(),item.getSub_service_id(),null,item.getSub_service_desc());
                        mHolder.subServices.add(sub);
                        Log.e("SubService desc", item.getSub_service_desc());

                    }
                    mHolder.adapter = new SubServiceRecyclerAdapter(mContext, mHolder.subServices, fragmentManager);
                    mHolder.mRecyclerView.setAdapter(mHolder.adapter);
                    mHolder.adapter.notifyDataSetChanged();

                    Log.e("SubService Size",mHolder.subServices.size()+"");



            }catch (Exception e)
            {
                //Toast.makeText(InvoiceActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
            }
        }
    }

}