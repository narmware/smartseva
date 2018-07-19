package com.narmware.smartseva;

import android.support.multidex.MultiDexApplication;

/**
 * Created by rohitsavant on 05/05/18.
 */

public class MyApplication extends MultiDexApplication {

    public static final String URL_SERVER="http://doormojo.com/api/";
    public static final String URL_GET_SERVICES="http://aspiresoftwareweb.com/Asp_SmartSevaa/AndroidServiceServlet?action=serviceList";
    public static final String URL_GET_SUBSERVICES="http://aspiresoftwareweb.com/Asp_SmartSevaa/AndroidServiceServlet";
    public static final String URL_SEND_SCHEDULE=URL_SERVER+"setappointment.php";
    public static final String URL_GET_BANNER_IMAGES=URL_SERVER+"getBanner.php";
    public static final String URL_LOGIN=URL_SERVER+"login.php";
    public static final String URL_REGISTER=URL_SERVER+"register.php";
    public static final String URL_MY_BOOKINGS=URL_SERVER+"mybooking.php";


}
