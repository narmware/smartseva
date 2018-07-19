package com.narmware.smartseva.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.narmware.smartseva.R;
import com.narmware.smartseva.helper.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DateTimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateTimeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.img_serv) ImageView mImgServ;
    @BindView(R.id.txt_serv_name) TextView mTxtServName;
    @BindView(R.id.img_time) ImageButton mImgTime;
    @BindView(R.id.img_date) ImageButton mImgDate;

    public static TextView mTxtDate,mTxtTime;
    String mDate,mTime;

    //current date
    static int Currentday;
    static int Currentmonth;
    static int Currentyear;
    public static Context mContext;
    public static int validDate=0;

    public DateTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DateTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DateTimeFragment newInstance(String param1, String param2) {
        DateTimeFragment fragment = new DateTimeFragment();
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
        View view =inflater.inflate(R.layout.fragment_date_time, container, false);
        mContext=getContext();
        ButterKnife.bind(this,view);

        mTxtDate=view.findViewById(R.id.edt_date);
        mTxtTime=view.findViewById(R.id.edt_time);

        mTime=SharedPreferencesHelper.getBookTime(getContext());
        mDate=SharedPreferencesHelper.getBookDate(getContext());

        if(mTime!=null && mDate!=null)
        {
            mTxtDate.setText(mDate);
            mTxtTime.setText(mTime);
        }
        mTxtServName.setText(mParam1);
        Picasso.with(getContext())
                .load(mParam2)
                .into(mImgServ);

        mTxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "datePicker");

               /* InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
            }
        });

        mTxtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });
        return view;
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
                mTxtDate.setText(formattedDate);
                SharedPreferencesHelper.setBookDate(formattedDate,getContext());
                //BookAppointmentActivity.mDate=formattedDate;
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
                Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_SHORT).show();
            }
        }


        if(selectedyear == Currentyear && selectedmonth == Currentmonth)
        {
            if(Currentday > selectedday)
            {
                validDate = 1;
                Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_SHORT).show();
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
            mTxtTime.setText(hour+":"+min);
            SharedPreferencesHelper.setBookTime(hour+":"+min,getContext());
            //BookAppointmentActivity.mTime=hour+":"+min;
            // Do something with the time chosen by the user
        }
    }
}
