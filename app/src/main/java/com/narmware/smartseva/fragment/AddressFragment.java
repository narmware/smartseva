package com.narmware.smartseva.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.narmware.smartseva.R;
import com.narmware.smartseva.helper.SharedPreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_EDIT = "is_edit";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean isEdit;
    private String mParam2;
    @BindView(R.id.btn_home) Button mBtnHome;
    @BindView(R.id.btn_office) Button mBtnOffice;

    @BindView(R.id.lin_home) LinearLayout mLinHome;
    @BindView(R.id.lin_office) LinearLayout mLinOffice;

    @BindView(R.id.btn_submit) Button mBtnSubmit;
    int validFlag=0;
    int validOfficeFlag=0;

    private OnFragmentInteractionListener mListener;
    public static EditText mEdtBdName,mEdtHouseNum,mEdtStreetName,mEdtCity,mEdtPin;
    public static EditText mEdtCompName,mEdtCompBdName,mEdtCompStreetName,mEdtCompCity,mEdtCompPin;
    String mBuildName,mHouseNum,mStreetName,mCity,mPin,mHomeAddr;
    String mCompName,mCompBuildName,mCompStreetName,mCompCity,mCompPin,mCompAddr;
    public AddressFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance(boolean isEdit) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_EDIT, isEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isEdit = getArguments().getBoolean(IS_EDIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_address, container, false);
        //used to hide keyboard bydefault
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);

        if(isEdit==true)
        {
            mBtnSubmit.setVisibility(View.VISIBLE);
        }
            mEdtBdName = view.findViewById(R.id.edt_bd_name);
            mEdtHouseNum = view.findViewById(R.id.edt_house_num);
            mEdtStreetName = view.findViewById(R.id.edt_street_name);
            mEdtCity = view.findViewById(R.id.edt_city);
            mEdtPin = view.findViewById(R.id.edt_pin);


            mEdtCompName = view.findViewById(R.id.edt_comp_name);
            mEdtCompBdName = view.findViewById(R.id.edt_comp_bd_name);
            mEdtCompStreetName = view.findViewById(R.id.edt_comp_street_name);
            mEdtCompCity = view.findViewById(R.id.edt_comp_city);
            mEdtCompPin = view.findViewById(R.id.edt_comp_pin);

        setData();

        mBtnHome.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                SharedPreferencesHelper.setIsHome(true,getContext());

                setData();
                HomeEnabled();

            }
        });

        mBtnOffice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                SharedPreferencesHelper.setIsHome(false,getContext());

                setData();
                HomeDisable();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPreferencesHelper.getIsHome(getContext())==true) {
                    mBuildName = AddressFragment.mEdtBdName.getText().toString();
                    mHouseNum = AddressFragment.mEdtHouseNum.getText().toString();
                    mStreetName = AddressFragment.mEdtStreetName.getText().toString();
                    mCity = AddressFragment.mEdtCity.getText().toString();
                    mPin = AddressFragment.mEdtPin.getText().toString();

                    validateAddress();

                    mHomeAddr=mBuildName+","+mHouseNum+","+mStreetName+","+mCity+","+mPin;

                    if(validFlag==0) {
                        SharedPreferencesHelper.setBookAddress(mHomeAddr,getContext());
                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Done")
                                .setContentText("Address changed")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismiss();
                                    }
                                })

                                .show();
                    }
                }
                if(SharedPreferencesHelper.getIsHome(getContext())==false) {
                    mCompName = AddressFragment.mEdtCompName.getText().toString();
                    mCompBuildName = AddressFragment.mEdtCompBdName.getText().toString();
                    mCompStreetName = AddressFragment.mEdtCompStreetName.getText().toString();
                    mCompCity = AddressFragment.mEdtCompCity.getText().toString();
                    mCompPin = AddressFragment.mEdtCompPin.getText().toString();

                    validateOfficeAddress();

                    mCompAddr=mCompName+","+mCompBuildName+","+mCompStreetName+","+mCompCity+","+mCompPin;

                    if(validOfficeFlag==0) {
                        SharedPreferencesHelper.setBookOfficeAddress(mCompAddr,getContext());
                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Done")
                                .setContentText("Address changed")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismiss();
                                    }
                                })

                                .show();
                    }
                }
            }
        });

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
    public void setData()
    {
        if(SharedPreferencesHelper.getIsHome(getContext())==true) {
            HomeEnabled();
            mHomeAddr = SharedPreferencesHelper.getBookAddress(getContext());
            if (mHomeAddr != null) {

                try {
                    String currentString = mHomeAddr;
                    String[] separated = currentString.split(",");
                    mBuildName = separated[0];
                    mHouseNum = separated[1];
                    mStreetName = separated[2];
                    mCity = separated[3];
                    mPin = separated[4];

                    mEdtBdName.setText(mBuildName);
                    mEdtHouseNum.setText(mHouseNum);
                    mEdtStreetName.setText(mStreetName);
                    mEdtCity.setText(mCity);
                    mEdtPin.setText(mPin);
                }catch (Exception e)
                {

                }
            }
        }

        if(SharedPreferencesHelper.getIsHome(getContext())==false) {
            HomeDisable();
            mCompAddr = SharedPreferencesHelper.getBookOfficeAddress(getContext());
            if (mCompAddr != null) {

                try {
                    String currentString = mCompAddr;
                    String[] separated = currentString.split(",");
                    mCompName = separated[0];
                    mCompBuildName = separated[1];
                    mCompStreetName = separated[2];
                    mCompCity = separated[3];
                    mCompPin = separated[4];

                    mEdtCompName.setText(mCompName);
                    mEdtCompBdName.setText(mCompBuildName);
                    mEdtCompStreetName.setText(mCompStreetName);
                    mEdtCompCity.setText(mCompCity);
                    mEdtCompPin.setText(mCompPin);
                }catch (Exception e)
                {

                }
            }
        }
    }
    @SuppressLint("NewApi")
    public void HomeEnabled()
    {
        YoYo.with(Techniques.SlideOutRight)
                .duration(500)
                .playOn(mLinOffice);

        mLinHome.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInLeft)
                .duration(400)
                .playOn(mLinHome);

        mBtnHome.setTextColor(getContext().getResources().getColor(R.color.indigo_700));
        mBtnHome.setBackground(getContext().getResources().getDrawable(R.drawable.button_select));

        mBtnOffice.setTextColor(getContext().getResources().getColor(R.color.grey_500));
        mBtnOffice.setBackground(getContext().getResources().getDrawable(R.drawable.button_deselect));
    }

    @SuppressLint("NewApi")
    public void HomeDisable()
    {
        YoYo.with(Techniques.SlideOutLeft)
                .duration(500)
                .playOn(mLinHome);
        //mLinHome.setVisibility(View.GONE);

        mLinOffice.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInRight)
                .duration(400)
                .playOn(mLinOffice);

        mBtnOffice.setTextColor(getContext().getResources().getColor(R.color.indigo_700));
        mBtnOffice.setBackground(getContext().getResources().getDrawable(R.drawable.button_select));

        mBtnHome.setTextColor(getContext().getResources().getColor(R.color.grey_500));
        mBtnHome.setBackground(getContext().getResources().getDrawable(R.drawable.button_deselect));
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
}
