package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallBack.TAG;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.Utils.SharedPrefManager;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthController extends Controller {

    private static User user;

    public AuthController(AuthActivity authActivity){
        super(authActivity);
    }

    public static String getUserToken() {
        //return "Token e798c795e07649dd603039f8b6c624479854fa34";
        String token = user.getToken();
        if (token == null)
            return null;
        return "Token " + user.getToken();
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
            public void onSuccess(Call<ResponseBody> call,  String responseBody) {
                if (user == null)
                    user = new User(authActivity, getTokenFromResponseBody(responseBody));
                else
                    user.setToken(authActivity, getTokenFromResponseBody(responseBody));
                Log.d(TAG, "login: " + user);
                onResponse.onSuccess(call ,responseBody);
            }

            @Override
            public void onFailed(Call<ResponseBody> call,String message) {
                onResponse.onFailed(call ,message);
            }
        }));

    }

    public void register(String email, String password, String firstName, String lastName, OnResponse onResponse) {
        api.registerUser(email, password, firstName, lastName).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call,String responseBody) {
                user = new User(firstName, lastName, email);
                login(email, password, onResponse);
            }

            @Override
            public void onFailed(Call<ResponseBody> call,String message) {

            }
        }));
    }



    private void completeUserInfo(OnAuthorization onAuthorization) {
        loadingDialog.show();
        String tokenString = getUserToken();
        Log.d(TAG, "completeUserInfo:TOKEN: " + tokenString);
        api.info(tokenString).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call,String responseBody) {
                Log.d(TAG, "completeUserInfo: onSuccess: " + responseBody);
                String token = user.getToken();
                user = gson.fromJson(responseBody , User.class);
                user.setToken(authActivity , token);
                Log.d(TAG, "completeUserInfo: onSuccess user: " + user);
                onAuthorization.onAuth(user);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailed(Call<ResponseBody> call,String message) {
                loadingDialog.dismiss(); //todo: try again for login
                authActivity.showAuthDialog(onAuthorization);
            }
        }));
    }



    public interface OnAuthorization {
        void onAuth(User user);
    }
}
