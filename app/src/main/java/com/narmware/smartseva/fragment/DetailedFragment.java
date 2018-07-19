package com.narmware.smartseva.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.narmware.smartseva.helper.SupportFunctions;
import com.narmware.smartseva.pojo.BookSchedule;
import com.narmware.smartseva.pojo.BookScheduleResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SERVICE_ID = "service_id";
    private static final String SERVICE_IMAGE = "img";
    private static final String SERVICE_TITLE = "title";
    private static final String SERVICE_DESC = "desc";

    // TODO: Rename and change types of parameters
    private int service_id;
    private String service_title;
    private String service_img;
    private String service_desc;

    @BindView(R.id.img_serv) ImageView mImgServ;
    @BindView(R.id.txt_serv_name) TextView mtxtServ;
    @BindView(R.id.txt_serv_desc) TextView mtxtDesc;
    @BindView(R.id.btn_submit) Button mBtnSubmit;
    @BindView(R.id.edt_name) EditText mEdtName;
    @BindView(R.id.edt_mobile) EditText mEdtMob;
    @BindView(R.id.edt_addr) EditText mEdtAddr;
    @BindView(R.id.edt_serv) EditText mEdtServ;
    @BindView(R.id.edt_email) EditText mEdtMail;

    @BindView(R.id.img_date) ImageButton mImgDate;
    @BindView(R.id.img_time) ImageButton mImgTime;

    String mName,mMob,mAddr,mServ,mDate,mTime,mMail,mDateTime;
    int validFlag=0;
    public static int validDate=0;

   public static TextView mEdtDate;
    public static TextView mEdtTime;

    private OnFragmentInteractionListener mListener;

    //current date
    static int Currentday;
    static int Currentmonth;
    static int Currentyear;
    public static Context mContext;
    RequestQueue mVolleyRequest;

    public DetailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedFragment newInstance(int ser_id,String service_img,String service_title,String service_desc) {
        DetailedFragment fragment = new DetailedFragment();
        Bundle args = new Bundle();
        args.putInt(SERVICE_ID, ser_id);
        args.putString(SERVICE_IMAGE,service_img);
        args.putString(SERVICE_TITLE, service_title);
        args.putString(SERVICE_DESC, service_desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            service_id = getArguments().getInt(SERVICE_ID);
            service_img=getArguments().getString(SERVICE_IMAGE);
            service_title=getArguments().getString(SERVICE_TITLE);
            service_desc=getArguments().getString(SERVICE_DESC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detailed, container, false);
        mContext=getContext();
        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        mVolleyRequest = Volley.newRequestQueue(getContext());

        mEdtTime=view.findViewById(R.id.edt_time);
        mEdtDate=view.findViewById(R.id.edt_date);

        //used to hide keyboard bydefault
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Picasso.with(getContext())
                .load(service_img)
                .fit()
                .noFade()
                .into(mImgServ);

        mtxtServ.setText(service_title);
        mEdtServ.setText(service_title);

        mtxtDesc.setText(service_desc);

        mImgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "datePicker");

               /* InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
            }
        });

        mImgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName=mEdtName.getText().toString().trim();
                mMob=mEdtMob.getText().toString().trim();
                mAddr=mEdtAddr.getText().toString().trim();
                mServ=mEdtServ.getText().toString().trim();
                mDate=mEdtDate.getText().toString().trim();
                mTime=mEdtTime.getText().toString().trim();
                mMail=mEdtMail.getText().toString().trim();

                validateData();

                if(validFlag==0)
                {
                        mDateTime=mDate+" "+mTime;
                        SendSchedule();
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    public void validateData()
    {
        validFlag=0;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(mName==null||mName.equals(""))
        {
            validFlag=1;
            mEdtName.setError("Enter name");
        }
        if(mMob.length()<10||mMob.equals(""))
        {
            validFlag=1;
            mEdtMob.setError("Enter valid number");
        }
        if(mAddr==null||mAddr.equals(""))
        {
            validFlag=1;
            mEdtAddr.setError("Enter Address");
        }
        if(mServ==null||mServ.equals(""))
        {
        validFlag=1;
        }
        if(mDate==null||mDate.equals(""))
        {
            validFlag=1;
            mEdtDate.setError("Select Date");
        }
        if(mTime==null||mTime.equals(""))
        {
        validFlag=1;
        mEdtTime.setError("Select Time");
        }
        if(mMail.equals("") || !mMail.matches(emailPattern))
        {
            validFlag=1;
            mEdtMail.setError("Enter valid Email");
        }

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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

// Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());

            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
             Currentday = localCalendar.get(Calendar.DATE);
            Currentmonth = localCalendar.get(Calendar.MONTH);
            Currentyear = localCalendar.get(Calendar.YEAR);
            /*Calendar current = Calendar.getInstance();
            current.set(Currentyear, Currentmonth, Currentday);
            SimpleDateFormat currentsdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentformattedDate = currentsdf.format(current.getTime());
            Log.e("Current date",currentformattedDate);*/

            ValidateDate(year,month,day);
            if(validDate==0) {
                mEdtDate.setText(formattedDate);
                Log.e("Date", formattedDate);
            }
        }
    }


    public static void ValidateDate(int selectedyear, int selectedmonth, int selectedday)
    {
        validDate=0;

        if(selectedyear!=0) {
            if (Currentyear > selectedyear) {
                validDate = 1;
                //Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_SHORT).show();
            }
        }

        if(selectedmonth!=0) {
            if (Currentmonth > selectedmonth) {
                validDate = 1;
               // Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_SHORT).show();
            }
        }


        if(selectedyear == Currentyear && selectedmonth == Currentmonth)
        {
            if(Currentday > selectedday)
            {
                validDate = 1;
             //   Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


        public TimePickerFragment() {
            // Required empty public constructor
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker

            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour=String.valueOf(hourOfDay);
            String min=String.valueOf(minute);

            Log.e("Time",hourOfDay+" "+minute+" ");
            mEdtTime.setText(hour+":"+min);
            // Do something with the time chosen by the user
        }
    }


    private void SendSchedule() {


        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Sending schedule...");
        dialog.setCancelable(false);
        dialog.show();

        BookSchedule bookSchedule=new BookSchedule();
        bookSchedule.setName(mName);
        bookSchedule.setAddress(mAddr);
        bookSchedule.setMob(mMob);
        bookSchedule.setService(mServ);
        bookSchedule.setEmail(mMail);

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
                                Toast.makeText(getContext(), "Appointment Scheduled Successfully", Toast.LENGTH_SHORT).show();
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
        mVolleyRequest.add(obreq);
    }


}
