package ir.blackswan.travelapp.Utils;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

public class Cacher {
    private static HashMap<String , String> serverToLocal;
    private static Gson gson = new Gson();
    private static String SH_KEY = "serverToLocal";
    private static boolean hasBeenLoaded = false;
    private SharedPrefManager sh;

    public Cacher(Context context) {
        sh = new SharedPrefManager(context);
        load(sh);
    }

    public String getLocalPathByServerPath(String serverPath){
        return serverToLocal.get(serverPath);
    }

    public void saveLocalPath(String serverPath , String localPath){
        serverToLocal.put(serverPath , localPath);
        save(sh);
    }

    private static void load(SharedPrefManager sh){
        if (hasBeenLoaded)
            return;
        hasBeenLoaded = true;
        String mapJson = sh.getString(SH_KEY);
        if (mapJson != null)
            serverToLocal = gson.fromJson(mapJson , HashMap.class);
        else
            serverToLocal = new HashMap<>();

    }
    private static void save(SharedPrefManager sh){
        load(sh);
        sh.putString(SH_KEY , gson.toJson(serverToLocal));
    }
}
