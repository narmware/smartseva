package com.narmware.smartseva.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.narmware.smartseva.R;
import com.narmware.smartseva.adapter.HeaderAdapter;
import com.narmware.smartseva.adapter.ServiceDemoAdapter;
import com.narmware.smartseva.pojo.SubServices;
import com.narmware.smartseva.pojo.ServiceMain;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity
{
    //@BindView(R.id.title_recycler) RecyclerView mTitleList;
    @BindView(R.id.header_recycler) RecyclerView mHeaderRecycler;
    @BindView(R.id.slider) protected SliderLayout mSlider;
    ServiceDemoAdapter demoAdapter;
    HeaderAdapter headerAdapter;
    ArrayList<SubServices> services;
    ArrayList<ServiceMain> titleList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getSupportActionBar().hide();
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        ButterKnife.bind(this);

        titleList=new ArrayList<>();
        titleList.add(new ServiceMain("Recommended Services","1",null));
        titleList.add(new ServiceMain("Recommended Services","1",null));
        demoAdapter=new ServiceDemoAdapter(NavigationActivity.this,titleList,getSupportFragmentManager());
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
      /*  mTitleList.setLayoutManager(new LinearLayoutManager(NavigationActivity.this));
        mTitleList.setItemAnimator(new DefaultItemAnimator());
        mTitleList.setAdapter(demoAdapter);
        mTitleList.setNestedScrollingEnabled(false);
        mTitleList.setFocusable(false);*/


        setSlider();
        setHeaderAdapter(new LinearLayoutManager(NavigationActivity.this,LinearLayoutManager.HORIZONTAL,false));
    }

    public void setHeaderAdapter(RecyclerView.LayoutManager mLayoutManager){
        services=new ArrayList<>();
       /* services.add(new SubServices("null","Electrical"));
        services.add(new SubServices("http://doormojo.com/admin/pages/uploads/CAT-123558-plumbing.jpg","Cleaning"));
        services.add(new SubServices("http://doormojo.com/admin/pages/uploads/CAT-124439-ElectricalInstallation.jpg","Electrical"));
        services.add(new SubServices("http://doormojo.com/admin/pages/uploads/CAT-123558-plumbing.jpg","Cleaning"));        services.add(new SubServices("http://doormojo.com/admin/pages/uploads/CAT-124439-ElectricalInstallation.jpg","Electrical"));
        services.add(new SubServices("http://doormojo.com/admin/pages/uploads/CAT-123558-plumbing.jpg","Cleaning"));*/

        SnapHelper snapHelper = new LinearSnapHelper();

        headerAdapter = new HeaderAdapter(NavigationActivity.this,services);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mHeaderRecycler.setLayoutManager(mLayoutManager);
        mHeaderRecycler.setItemAnimator(new DefaultItemAnimator());
        snapHelper.attachToRecyclerView(mHeaderRecycler);
        mHeaderRecycler.setAdapter(headerAdapter);
        mHeaderRecycler.setNestedScrollingEnabled(false);
        mHeaderRecycler.setFocusable(false);

        headerAdapter.notifyDataSetChanged();

    }
    private void setSlider() {
        // HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        HashMap<String,String> file_maps = new HashMap<String, String>();
        file_maps.put("Key","https://i.ytimg.com/vi/RUn5NE0RP5Y/maxresdefault.jpg");
        file_maps.put("key1","https://i.ytimg.com/vi/ipd6ZBnnQOo/maxresdefault.jpg");

        for(String name : file_maps.keySet()){
            //textSliderView displays image with text title
            //TextSliderView textSliderView = new TextSliderView(NavigationActivity.this);

            //DefaultSliderView displays only image
            DefaultSliderView textSliderView = new DefaultSliderView(NavigationActivity.this);
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


    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
