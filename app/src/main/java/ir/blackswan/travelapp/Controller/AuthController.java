package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallBack.TAG;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.Retrofit.Api;
import ir.blackswan.travelapp.Retrofit.RetrofitClient;
import ir.blackswan.travelapp.Utils.SharedPrefManager;

public class AuthController {
    private static User user;
    private static Gson gson = new Gson();
    private Context context;
    private Api api;

    public static String getUserToken() {
        return "Token " + user.getToken();
    }

    public static User getUser() {
        return user;
    }

    public AuthController(Context context) {
        this.context = context;
        api = RetrofitClient.getApi();
    }

    private String getTokenFromResponseBody(String responseBody) {
        Map<String, String> map = gson.fromJson(
                responseBody, new TypeToken<HashMap<String, String>>() {
                }.getType());
        return map.get("token");
    }

    public void login(String email, String password, OnResponse onResponse) {
        api.token(email, password).enqueue(new MyCallBack(context, new OnResponse(){
            @Override
            public void onSuccess(String responseBody) {
                if (user == null)
                    user = new User(context, getTokenFromResponseBody(responseBody));
                else
                    user.setToken(context, getTokenFromResponseBody(responseBody));
                Log.d(TAG, "login: " + user);
                onResponse.onSuccess(responseBody);
            }

            @Override
            public void onFailed(String message) {
                onResponse.onFailed(message);
            }
        }));

    }

    public void register(String email, String password, String firstName, String lastName, OnResponse onResponse) {
        api.registerUser(email, password, firstName, lastName).enqueue(new MyCallBack(context, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                user = new User(firstName, lastName, email);
                login(email, password, onResponse);
            }

            @Override
            public void onFailed(String message) {

            }
        }));
    }

    public boolean loadUser(Context context, OnUserLoad onUserLoad) {
        SharedPrefManager sh = new SharedPrefManager(context);
        String token = sh.getString(SharedPrefManager.USER_TOKEN);
        if (token != null) {
            user = new User(context, token);
            completeUserInfo(onUserLoad);
            return true;
        }
        return false;
    }

    private void completeUserInfo(OnUserLoad onUserLoad) {
        String tokenString = getUserToken();
        Log.d(TAG, "completeUserInfo:TOKEN: " + tokenString);
        api.info(tokenString).enqueue(new MyCallBack(context, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                Log.d(TAG, "onSuccess: " + responseBody);

            }

            @Override
            public void onFailed(String message) {

            }
        }));
    }




    public interface OnUserLoad {
        void onLoad(User user);
    }
}
