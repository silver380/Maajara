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
import ir.blackswan.travelapp.ui.AuthActivity;

public class AuthController extends Controller {

    private static User user;

    public AuthController(AuthActivity authActivity){
        super(authActivity);
    }

    public static String getUserToken() {
        //todo: return "Token " + user.getToken();
        return "Token e798c795e07649dd603039f8b6c624479854fa34";
    }

    public static User getUser() {
        return user;
    }

    public boolean loadUser(Context context , OnAuthorization onAuthorization ) {
        SharedPrefManager sh = new SharedPrefManager(context);
        String token = sh.getString(SharedPrefManager.USER_TOKEN);
        if (token != null) {
            user = new User(context, token);
            completeUserInfo(onAuthorization);
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
        api.token(email, password).enqueue(new MyCallBack(authActivity, new OnResponse(){
            @Override
            public void onSuccess(String responseBody) {
                if (user == null)
                    user = new User(authActivity, getTokenFromResponseBody(responseBody));
                else
                    user.setToken(authActivity, getTokenFromResponseBody(responseBody));
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
        api.registerUser(email, password, firstName, lastName).enqueue(new MyCallBack(authActivity, new OnResponse() {
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



    private void completeUserInfo(OnAuthorization onAuthorization) {
        responseMessageDialog.show();
        String tokenString = getUserToken();
        Log.d(TAG, "completeUserInfo:TOKEN: " + tokenString);
        api.info(tokenString).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                user = gson.fromJson(responseBody , User.class);
                onAuthorization.onAuth(user);
                responseMessageDialog.dismiss();
            }

            @Override
            public void onFailed(String message) {
                responseMessageDialog.dismiss(); //todo: try again for login
            }
        }));
    }




    public interface OnAuthorization {
        void onAuth(User user);
    }
}
