package com.narmware.smartseva.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.smartseva.R;
import com.narmware.smartseva.fragment.AddressFragment;
import com.narmware.smartseva.fragment.BookingFragment;
import com.narmware.smartseva.fragment.DetailedFragment;
import com.narmware.smartseva.fragment.HomeFragment;
import com.narmware.smartseva.fragment.ProfileFragment;
import com.narmware.smartseva.fragment.SubSrvicesFragment;
import com.narmware.smartseva.fragment.ViewMoreFragment;
import com.narmware.smartseva.helper.SharedPreferencesHelper;

public class BottomNavigationActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,BookingFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener,
        ViewMoreFragment.OnFragmentInteractionListener,DetailedFragment.OnFragmentInteractionListener,SubSrvicesFragment.OnFragmentInteractionListener,AddressFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        try {
            if (SharedPreferencesHelper.getIsMainAct(BottomNavigationActivity.this).equals("SubServices")) {
                SharedPreferencesHelper.setIsMainAct("Dashboard", BottomNavigationActivity.this);
            }
        }catch (Exception e)
        {}

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(new HomeFragment());
    }

    @Override
    public void onBackPressed() {

        /*if (getFragmentManager().getBackStackEntryCount() == 0) {
            Fragment f = getSupportFragmentManager().findFragmentByTag("dashboard");
            if (f instanceof DashboardFragment) {
                Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
            }
        }*/
        if(SharedPreferencesHelper.getIsMainAct(BottomNavigationActivity.this).equals("Dashboard"))
        {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit app", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
        else {
            super.onBackPressed();
        }

        if(SharedPreferencesHelper.getIsMainAct(BottomNavigationActivity.this).equals("SubServices"))
        {
            super.onBackPressed();
            SharedPreferencesHelper.setIsMainAct("Dashboard",BottomNavigationActivity.this);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_search:
                    setFragment(new HomeFragment());
                    return true;
                case R.id.nav_booking:
                    //setFragment(new BookingFragment());
                    return true;
                case R.id.nav_profile:
                    //setFragment(new ProfileFragment());
                    return true;
            }
            return false;
        }
    };

    public void setFragment(Fragment fragment)
    {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
