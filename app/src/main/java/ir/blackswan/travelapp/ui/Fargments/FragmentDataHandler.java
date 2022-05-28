package ir.blackswan.travelapp.ui.Fargments;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

import ir.blackswan.travelapp.Utils.SharedPrefManager;

public class FragmentDataHandler {
    static HashMap<String , FragmentsData> data = new HashMap<>();
    public static final String KEY_ADD_TOUR_FRAGMENT = "FAddTour" , KEY_ADD_PLAN_FRAGMENT = "FAddPlan" ;
    public static void save(String key, FragmentsData fragmentsData){
        data.put(key, fragmentsData);
    }
    public static void clear(String key){
        data.put(key , null);
    }
    public static FragmentsData load(String key){
        return data.get(key);
    }
}
