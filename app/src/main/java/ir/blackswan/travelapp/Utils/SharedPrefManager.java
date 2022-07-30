package ir.blackswan.travelapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
public class SharedPrefManager {
    private static String SHARED_PREF_NAME = "MajaraShared";
    public static final String USER_TOKEN = "userToken";

    public static final String IS_TOUR_LEADER = "isTourLeader";

    public static final String LANGUAGE = "lang";


    private final Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public void putInt(String name, int item) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, item);
        editor.apply();
    }

    public Integer getInt(String name) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(name, -1);
    }

    public Integer getInt(String name , int defaultValue) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(name, defaultValue);
    }

    public void putString(String name, String item) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, item);
        editor.apply();

    }


    public String getString(String name) {
        return getString(name , null);
    }
    public String getString(String name , String defValue) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, defValue);
    }

    public void putBoolean(String name, Boolean item) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, item);
        editor.apply();
    }

    public Boolean getBoolean(String name) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name, false);
    }
    public Boolean getBoolean(String name , boolean defValue) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name, defValue);
    }

    public void putLong(String name, Long item) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(name, item);
        editor.apply();

    }

    public Long getLong(String name) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(name, -1L);
    }
    public Long getLong(String name , long defaultValue) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(name, defaultValue);
    }

    public void putFloat(String name , Float item){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(name, item);
        editor.apply();
    }

    public Float getFloat(String name) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(name, -1F);
    }



}
