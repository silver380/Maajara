package ir.blackswan.travelapp.ui.Fargments;

import android.content.Context;

import com.google.gson.Gson;

import ir.blackswan.travelapp.Utils.SharedPrefManager;

public class FragmentDataHandler {
    static Gson gson = new Gson();
    public static final String KEY_ADD_TOUR_FRAGMENT = "FAddTour" , KEY_ADD_PLAN_FRAGMENT = "FAddPlan" ;
    public static void save(Context context , String key , FragmentsData fragmentsData){
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.putString(key , gson.toJson(fragmentsData));
    }
    public static void clear(Context context , String key){
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.putString(key , null);
    }
    public static FragmentsData load(Context context , String key){
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        String json = sharedPrefManager.getString(key);
        return gson.fromJson(json , FragmentsData.class);
    }
}
