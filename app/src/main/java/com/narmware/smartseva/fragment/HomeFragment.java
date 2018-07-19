package com.narmware.smartseva.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;
import com.narmware.smartseva.MyApplication;
import com.narmware.smartseva.R;
import com.narmware.smartseva.adapter.HeaderAdapter;
import com.narmware.smartseva.adapter.ServiceRecomAdapter;
import com.narmware.smartseva.helper.Constants;
import com.narmware.smartseva.helper.JSONParser;
import com.narmware.smartseva.helper.SharedPreferencesHelper;
import com.narmware.smartseva.pojo.BannerImages;
import com.narmware.smartseva.pojo.BannerResponse;
import com.narmware.smartseva.pojo.SubServiceResponse;
import com.narmware.smartseva.pojo.SubServices;
import com.narmware.smartseva.pojo.ServiceMain;
import com.squareup.picasso.Picasso;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

   // @BindView(R.id.title_recycler) RecyclerView mTitleList;
    @BindView(R.id.header_recycler) RecyclerView mHeaderRecycler;
    @BindView(R.id.recycler_view_recom) RecyclerView mRecyclerRecom;

    @BindView(R.id.slider) protected SliderLayout mSlider;
    @BindView(R.id.image_bottom) ImageView mImgBottom;
    @BindView(R.id.image_add) ImageView mImgAdd;

    @BindView(R.id.inv_linear1) LinearLayout mLinear1;

    //ServiceMainAdapter demoAdapter;
    HeaderAdapter headerAdapter;
    ServiceRecomAdapter serviceRecomAdapter;
    ArrayList<SubServices> services;
    ArrayList<SubServices> servicesRecom;
    ArrayList<ServiceMain> mainServiceList;
    RequestQueue mVolleyRequest;

    ArrayList<BannerImages> bannerImages;
    JSONParser mJsonParser;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    protected Dialog mNoConnectionDialog;

    int flag=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        SharedPreferencesHelper.setIsMainAct("Dashboard",getContext());
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        mVolleyRequest = Volley.newRequestQueue(getContext());
        mJsonParser=new JSONParser();

        GetBannerImages();
        setHeaderAdapter(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        new GetHeaderServices().execute("12");
        new GetHeaderServices().execute("13");

        setFragment(new ViewMoreFragment());

        Picasso.with(getContext())
                .load("https://www.keatingagency.com/wp-content/uploads/2017/01/bigstock-house-maintain-300x300.jpg")
                .fit()
                .noFade()
                .placeholder(getContext().getResources().getDrawable(R.drawable.placeholder))
                .into(mImgBottom);

        Picasso.with(getContext())
                .load("https://storage.googleapis.com/idx-acnt-gs.ihouseprd.com/AR706484/file_manager/Latelier_geantsduweb.jpg")
                .fit()
                .noFade()
                .placeholder(getContext().getResources().getDrawable(R.drawable.placeholder))
                .into(mImgAdd);

        setRecomAdapter(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        new GetRecomServices().execute("10");
        new GetRecomServices().execute("11");
    }

    public void setFragment(Fragment fragment)
    {
        fragmentManager=getActivity().getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.service_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
  /*  public void setServiceAdapter(RecyclerView.LayoutManager mLayoutManager)
    {
        mainServiceList=new ArrayList<>();

        demoAdapter=new ServiceMainAdapter(getContext(),mainServiceList,getActivity().getSupportFragmentManager());
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mTitleList.setLayoutManager(mLayoutManager);
        mTitleList.setItemAnimator(new DefaultItemAnimator());
        mTitleList.setAdapter(demoAdapter);
        mTitleList.setNestedScrollingEnabled(false);
        mTitleList.setFocusable(false);

    }*/
    public void setHeaderAdapter(RecyclerView.LayoutManager mLayoutManager){
        services=new ArrayList<>();
        SnapHelper snapHelper = new LinearSnapHelper();

        headerAdapter = new HeaderAdapter(getContext(),services);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mHeaderRecycler.setLayoutManager(mLayoutManager);
        mHeaderRecycler.setItemAnimator(new DefaultItemAnimator());
        snapHelper.attachToRecyclerView(mHeaderRecycler);
        mHeaderRecycler.setAdapter(headerAdapter);
        mHeaderRecycler.setNestedScrollingEnabled(false);
        mHeaderRecycler.setFocusable(false);

        headerAdapter.notifyDataSetChanged();

    }


    public void setRecomAdapter(RecyclerView.LayoutManager mLayoutManager){
        servicesRecom=new ArrayList<>();

        SnapHelper snapHelper = new LinearSnapHelper();

        serviceRecomAdapter = new ServiceRecomAdapter(getContext(),servicesRecom,getActivity().getSupportFragmentManager());
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mRecyclerRecom.setLayoutManager(mLayoutManager);
        mRecyclerRecom.setItemAnimator(new DefaultItemAnimator());
        snapHelper.attachToRecyclerView(mRecyclerRecom);
        mRecyclerRecom.setAdapter(serviceRecomAdapter);
        mRecyclerRecom.setNestedScrollingEnabled(false);
        mRecyclerRecom.setFocusable(false);

        serviceRecomAdapter.notifyDataSetChanged();
    }
    private void setSlider() {
        // HashMap<String,Integer> file_maps = new HashMap<String, Integer>();

        HashMap<String,String> file_maps = new HashMap<String, String>();

        for(int i=0;i<bannerImages.size();i++)
        {
            file_maps.put(bannerImages.get(i).getBanner_title(),bannerImages.get(i).getService_image());
        }

        for(String name : file_maps.keySet()){
            //textSliderView displays image with text title
            //TextSliderView textSliderView = new TextSliderView(NavigationActivity.this);

            //DefaultSliderView displays only image
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mSlider.addSlider(textSliderView);
        }
        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        //mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //mSlider.setCustomIndicator(custom_indicator);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setFitsSystemWindows(true);
        mSlider.setDuration(3000);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void GetBannerImages() {


        HashMap<String,String> param = new HashMap();
        // param.put(Constants.REQUEST_ALBUM,mAlbumId);

        //url with params
        //String url= SupportFunctions.appendParam(MyApplication.URL_GET_BANNER_IMAGES,param);

        //url without params
        String url=MyApplication.URL_GET_BANNER_IMAGES;

        Log.e("Banner url",url);
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

                            Log.e("Banner Json_string",response.toString());
                            Gson gson = new Gson();

                            BannerResponse bannerResponse= gson.fromJson(response.toString(), BannerResponse.class);
                            bannerImages=new ArrayList<>();
                            if(bannerResponse.getResponse().equals("100")) {
                                BannerImages[] banner = bannerResponse.getData();
                                for (BannerImages item : banner) {


                                    bannerImages.add(item);
                                    Log.e("Banner desc", item.getBanner_title());

                                }
                                setSlider();

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                            //Toast.makeText(NavigationActivity.this, "Invalid album id", Toast.LENGTH_SHORT).show();
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
                        // showNoConnectionDialog();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }



    class GetRecomServices extends AsyncTask<String, String, String> {
        String id=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mLinear1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            id=strings[0];
            Log.e("Service id",id);
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
                    SubServices[] service = subServiceResponse.getData();
                    for (SubServices item : service) {

                        SubServices sub=new SubServices(item.getSer_img_url(),item.getSer_name(),item.getSub_service_id(),id,item.getSub_service_desc());
                        servicesRecom.add(sub);

                    }
                    serviceRecomAdapter.notifyDataSetChanged();
                    mLinear1.setVisibility(View.INVISIBLE);



            }catch (Exception e)
            {
                //Toast.makeText(InvoiceActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
            }
        }
    }


    class GetHeaderServices extends AsyncTask<String, String, String> {
        String id=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            id=strings[0];
            Log.e("Service id",id);
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
                    SubServices[] service = subServiceResponse.getData();
                    for (SubServices item : service) {

                        SubServices sub=new SubServices(item.getSer_img_url(),item.getSer_name(),item.getSub_service_id(),id,item.getSub_service_desc());
                        services.add(sub);

                    }
                    headerAdapter.notifyDataSetChanged();



            }catch (Exception e)
            {
                //Toast.makeText(InvoiceActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
            }
        }
    }


}

