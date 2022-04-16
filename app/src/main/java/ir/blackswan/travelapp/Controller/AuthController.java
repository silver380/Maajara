package ir.blackswan.travelapp.Controller;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.Retrofit.Api;
import ir.blackswan.travelapp.Retrofit.RetrofitClient;

public class AuthController {
    private static User user;
    private static Gson gson = new Gson();
    private Context context;
    private Api api;

    public AuthController(Context context) {
        this.context = context;
        api = RetrofitClient.getApi();
    }

    private String getTokenFromResponseBody(String responseBody){
        Map<String , String> map = gson.fromJson(
                responseBody, new TypeToken<HashMap<String, String>>() {
                }.getType());
        return map.get("token");
    }

    public void login(String email, String password, onSuccessAuth onSuccessAuth) {
        api.token(email, password).enqueue(new MyCallBack(context, responseBody -> {
            user = new User(context , getTokenFromResponseBody(responseBody));
            Log.d(MyCallBack.TAG, "login: " + user);
            onSuccessAuth.onSuccess(responseBody);
        }));

    }

    public void register(String email, String password, String firstName, String lastName, onSuccessAuth onSuccessAuth) {
        api.registerUser(email, password, firstName, lastName).enqueue(new MyCallBack(context, responseBody -> {
            user = new User(firstName , lastName , email);
            Log.d(MyCallBack.TAG, "login: " + user);
            onSuccessAuth.onSuccess(responseBody);
        }));
    }


    public interface onSuccessAuth {
        void onSuccess(String responseBody);
    }
}
