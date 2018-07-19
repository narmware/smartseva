package com.narmware.smartseva.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.smartseva.MyApplication;
import com.narmware.smartseva.R;
import com.narmware.smartseva.adapter.ViewMoreAdapter;
import com.narmware.smartseva.pojo.MainServiceResponse;
import com.narmware.smartseva.pojo.ServiceMain;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewMoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ArrayList<ServiceMain> services;
    ViewMoreAdapter viewMoreAdapter;
    @BindView(R.id.recycler_view) RecyclerView mMoreRecycler;
    RequestQueue mVolleyRequest;
    protected Dialog mNoConnectionDialog;

    public ViewMoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewMoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewMoreFragment newInstance(String param1, String param2) {
        ViewMoreFragment fragment = new ViewMoreFragment();
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
        View view= inflater.inflate(R.layout.fragment_view_more, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        mVolleyRequest = Volley.newRequestQueue(getContext());

        setHeaderAdapter(new GridLayoutManager(getContext(),2) );
        GetServices();
    }

    public void setHeaderAdapter(RecyclerView.LayoutManager mLayoutManager){
        services=new ArrayList<>();
        /*services.add(new ServiceMain("Cleaning Services","1","http://doormojo.com/admin/pages/uploads/CAT-045731-3.jpg"));
        services.add(new ServiceMain("Carpenting Services","2","http://doormojo.com/admin/pages/uploads/CAT-124257-carpentry.jpg"));
        services.add(new ServiceMain("Painiting Services","3","http://doormojo.com/admin/pages/uploads/CAT-125750-paint.jpg"));
        services.add(new ServiceMain("Corporate Services","4","http://doormojo.com/admin/pages/uploads/SUBCAT-102225-window.png"));
        services.add(new ServiceMain("Marriage Services","5","https://i.ytimg.com/vi/ipd6ZBnnQOo/maxresdefault.jpg"));
        services.add(new ServiceMain("Electrical Services","6","http://doormojo.com/admin/pages/uploads/CAT-045731-3.jpg"));
        services.add(new ServiceMain("Pest Control Services","7","http://doormojo.com/admin/pages/uploads/CAT-124257-carpentry.jpg"));*/

        SnapHelper snapHelper = new LinearSnapHelper();

        viewMoreAdapter = new ViewMoreAdapter(getContext(),services,getActivity().getSupportFragmentManager());
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mMoreRecycler.setLayoutManager(mLayoutManager);
        mMoreRecycler.setItemAnimator(new DefaultItemAnimator());
        snapHelper.attachToRecyclerView(mMoreRecycler);
        mMoreRecycler.setAdapter(viewMoreAdapter);
        mMoreRecycler.setNestedScrollingEnabled(false);
        mMoreRecycler.setFocusable(false);

        viewMoreAdapter.notifyDataSetChanged();

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


    private void GetServices() {


        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("getting services ...");
        dialog.setCancelable(false);
        //dialog.show();

        HashMap<String,String> param = new HashMap();
        // param.put(Constants.REQUEST_ALBUM,mAlbumId);

        //url with params
        //String url= SupportFunctions.appendParam(MyApplication.URL_GET_SERVICES,param);

        //url without params
        String url= MyApplication.URL_GET_SERVICES;

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


                                    services.add(item);
                                    Log.e("Featured img title", item.getService_title());

                                }
                                viewMoreAdapter.notifyDataSetChanged();


                        } catch (Exception e) {

                            e.printStackTrace();
                            //Toast.makeText(NavigationActivity.this, "Invalid album id", Toast.LENGTH_SHORT).show();
                            //dialog.dismiss();
                        }
                        //dialog.dismiss();
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
                        //dialog.dismiss();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

    private void showNoConnectionDialog() {
        mNoConnectionDialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_noconnectivity);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button exit = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_exit);
        Button tryAgain = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_try_again);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetServices();
                mNoConnectionDialog.dismiss();
            }
        });
    }


}
