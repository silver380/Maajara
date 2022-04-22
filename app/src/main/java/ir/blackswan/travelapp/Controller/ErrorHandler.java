package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.R;

public class ErrorHandler {
    public static final Gson gson = new Gson();

    public static String getStringErrors(Context context, String gsonError) {
        Log.d(TAG, "getStringErrors:" + "  " + gsonError);

        Map<String, Object> retMap = gson.fromJson(
                gsonError, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        StringBuilder errors = new StringBuilder();
        for (String key : retMap.keySet()) {
            Object obj = retMap.get(key);
            if (obj instanceof String[]) {
                String[] messages = (String[]) obj;
                for (String msg : messages) {
                    errors.append(translate(context, msg)).append("\n");
                }
            }else if (obj instanceof String)
                errors.append(translate(context , (String) obj));
        }
        Log.d(TAG, "getStringErrors:" + "  " + errors);
        return errors.toString().trim();
    }

    private static String translate(Context context, String msg) {
        switch (msg) {
            case "email exists.":
                return context.getString(R.string.error_email_exist);
            case "Unable to log in with provided credentials.":
                return "ایمیل یا رمز عبور نادرست است";
            case "Invalid token.":
                return "برای ادامه باید دوباره وارد برنامه شوید";

        }
        return msg;
    }



}
