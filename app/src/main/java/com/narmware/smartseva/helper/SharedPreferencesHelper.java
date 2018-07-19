package com.narmware.smartseva.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by comp16 on 12/19/2017.
 */

public class SharedPreferencesHelper {

    private static final String IS_LOGIN="login";
    private static final String IS_HOME="home";
    private static final String DELETE_FLAG="delete";
    private static final String VIEW_MORE_FLAG="view_more";
    private static final String BOOK_DATE="date";
    private static final String BOOK_TIME="time";
    private static final String BOOK_NAME="name";
    private static final String BOOK_MOBILE="mobile";
    private static final String BOOK_MAIL="mail";
    private static final String BOOK_ADDRESS="address";
    private static final String BOOK_OFFICE_ADDRESS="off_address";
    private static final String BOOK_LOCALITY="locality";
    private static final String USER_ID="user_id";
    private static final String IS_MAIN_ACT="main_act";


    public static void setUserId(String user_id, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_ID,user_id);
        edit.commit();
    }

    public static String getUserId(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String user_id=pref.getString(USER_ID,null);
        return user_id;
    }

    public static void setViewMoreFlag(boolean view_more, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(VIEW_MORE_FLAG,view_more);
        edit.commit();
    }

    public static boolean getViewMoreFlag(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean view_more=pref.getBoolean(VIEW_MORE_FLAG,false);
        return view_more;
    }

    public static void setDeleteFalg(boolean flag, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(DELETE_FLAG,flag);
        edit.commit();
    }

    public static boolean getDeleteFlag(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean flag=pref.getBoolean(DELETE_FLAG,false);
        return flag;
    }

    public static void setBookDate(String date, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(BOOK_DATE,date);
        edit.commit();
    }

    public static String getBookDate(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String date=pref.getString(BOOK_DATE,null);
        return date;
    }


    public static void setBookTime(String time, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(BOOK_TIME,time);
        edit.commit();
    }

    public static String getBookTime(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String time=pref.getString(BOOK_TIME,null);
        return time;
    }

    public static void setBookName(String name, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(BOOK_NAME,name);
        edit.commit();
    }

    public static String getBookName(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String name=pref.getString(BOOK_NAME,null);
        return name;
    }


    public static void setBookMobile(String mobile, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(BOOK_MOBILE,mobile);
        edit.commit();
    }

    public static String getBookMobile(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String mobile=pref.getString(BOOK_MOBILE,null);
        return mobile;
    }

    public static void setBookMail(String mail, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(BOOK_MAIL,mail);
        edit.commit();
    }

    public static String getBookMail(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String mail=pref.getString(BOOK_MAIL,null);
        return mail;
    }

    public static void setBookAddress(String addr, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(BOOK_ADDRESS,addr);
        edit.commit();
    }

    public static String getBookAddress(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String addr=pref.getString(BOOK_ADDRESS,null);
        return addr;
    }

    public static void setBookOfficeAddress(String addr, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(BOOK_OFFICE_ADDRESS,addr);
        edit.commit();
    }

    public static String getBookOfficeAddress(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String addr=pref.getString(BOOK_OFFICE_ADDRESS,null);
        return addr;
    }

    public static void setIsLogin(boolean login, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_LOGIN,login);
        edit.commit();
    }

    public static boolean getIsLogin(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean login=pref.getBoolean(IS_LOGIN,false);
        return login;
    }


    public static void setIsHome(boolean home, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_HOME,home);
        edit.commit();
    }

    public static boolean getIsHome(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean home=pref.getBoolean(IS_HOME,true);
        return home;
    }

    public static void setIsMainAct(String main_act, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(IS_MAIN_ACT,main_act);
        edit.commit();
    }

    public static String getIsMainAct(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String main_act=pref.getString(IS_MAIN_ACT,null);
        return main_act;
    }
}
