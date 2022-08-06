package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.blackswan.travelapp.R;

public class ErrorHandler {
    public static final Gson gson = new Gson();

    public static String getStringErrors(Context context, String gsonError) {
        Log.d(TAG, "getStringErrors:" + "  " + gsonError);
        try {
            Map<String, Object> retMap = gson.fromJson(
                    gsonError, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );
            StringBuilder errors = new StringBuilder();
            for (String key : retMap.keySet()) {
                Object obj = retMap.get(key);
                if (obj instanceof List) {
                    List<String> messages = (List<String>) obj;
                    for (String msg : messages) {
                        errors.append(translate(context, msg)).append("\n");
                    }
                } else if (obj instanceof String)
                    errors.append(translate(context, (String) obj));
            }
            Log.d(TAG, "getStringErrors:" + "  " + errors);
            return errors.toString().trim();
        } catch (Exception e) {
            Log.e(TAG, "getStringErrors: ", e);
        }
        return context.getString(R.string.something_went_wrong);
    }

    private static String translate(Context context, String msg) {
        switch (msg) {
            case "email exists.":
            case "my user with this email already exists.":
                return context.getString(R.string.error_email_exist);
            case "Unable to log in with provided credentials.":
                return context.getString(R.string.error_email_or_password_incorrect);
            case "Invalid token.":
                return context.getString(R.string.error_login_again);
            case "Not enough tickets":
                return context.getString(R.string.error_ticket_not_enough);

        }
        return msg;
    }


}
