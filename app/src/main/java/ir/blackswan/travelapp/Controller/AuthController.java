package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.Utils.SharedPrefManager;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthController extends Controller {

    private static User user;
    private static String token;
    private static boolean loadingUser = false;

    public AuthController(AuthActivity authActivity) {
        super(authActivity);
    }

    public static String getTokenString() {
        if (token == null)
            return null;
        return "Token " + token;
    }

    private static void setToken(Context context ,String token){
        AuthController.token = token;
        new SharedPrefManager(context).putString(SharedPrefManager.USER_TOKEN, token);
    }

    public static User getUser() {
        return user;
    }

    public static void logout(Context context) {
        AuthController.token = null;
        new SharedPrefManager(context).putString(SharedPrefManager.USER_TOKEN, null);
    }

    public boolean loadUser(Context context, OnResponse onResponse) {
        SharedPrefManager sh = new SharedPrefManager(context);
        token = sh.getString(SharedPrefManager.USER_TOKEN);
        if (token != null) {
            completeUserInfo(onResponse);
            return true;
        }
        return false;
    }

    private String getTokenFromResponseBody(String responseBody) {
        Map<String, String> map = gson.fromJson(
                responseBody, new TypeToken<HashMap<String, String>>() {
                }.getType());
        return map.get("token");
    }

    public void login(String email, String password, OnResponse onResponse) {
        Log.d(TAG, "login: " + email);
        api.token(email, password).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                setToken(authActivity , getTokenFromResponseBody(response.getResponseBody()));
                completeUserInfo(onResponse);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));

    }

    public void register(String email, String password, String firstName, String lastName, OnResponse onResponse) {
        Log.d(TAG, "Register " + email);
        api.registerUser(email, password, firstName, lastName).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                login(email, password, onResponse);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));
    }


    private void completeUserInfo(OnResponse onResponse) {
        loadingUser = true;
        String tokenString = getTokenString();
        Log.d(TAG, "completeUserInfo:TOKEN: " + tokenString);
        api.info(tokenString).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                user = gson.fromJson(response.getResponseBody(), User.class);
                Log.d(TAG, "completeUserInfo: onSuccess user: " + user);
                loadingUser = false;
                onResponse.onSuccess(call, callback, response);
                
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
                
            }
        }).showLoadingDialog());
    }

    public static boolean isLoadingUser() {
        return loadingUser;
    }
}
