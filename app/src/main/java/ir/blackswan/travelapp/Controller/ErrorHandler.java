package ir.blackswan.travelapp.Controller;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Toast;

public class ErrorHandler {
    public static final Gson gson = new Gson();

    public static String getStringErrors(Context context, String gsonError) {
        Map<String, String[]> retMap = gson.fromJson(
                gsonError, new TypeToken<HashMap<String, String[]>>() {
                }.getType()
        );
        Log.d(MyCallBack.TAG, "getStringErrors:" + "  " + gsonError);
        StringBuilder errors = new StringBuilder();
        for (String key : retMap.keySet()) {
            String[] messages = retMap.get(key);
            if (messages != null) {
                for (String msg : messages) {
                    errors.append(translate(context, msg)).append("\n");
                }
            }
        }
        Log.d(MyCallBack.TAG, "getStringErrors:" + "  " + errors);
        return errors.toString().trim();
    }

    public static String translate(Context context, String msg) {
        switch (msg) {
            case "email exists.":
                return context.getString(R.string.error_email_exist);
            case "Unable to log in with provided credentials.":
                return "ایمیل یا رمز عبور نادرست است";


        }
        return null;
    }


    public static void showGsonError(Context context, String gsonError) {
        String errors = getStringErrors(context, gsonError);
        showStringError(context, errors);
    }

    public static void showStringError(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG, Toast.TYPE_ERROR).show();
    }


}
