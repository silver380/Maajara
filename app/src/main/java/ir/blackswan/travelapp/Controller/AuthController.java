package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.Utils.SharedPrefManager;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthController extends Controller {

    public static final String FILE_TYPE_IMAGE = "image/jpg", FILE_TYPE_PDF = "application/pdf";
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

    private static void setToken(Context context, String token) {
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
        return map.get("auth_token");
    }

    public void login(String email, String password, OnResponse onResponse) {
        Log.d(TAG, "login... : " + email);
        api.token(email, password).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(TAG, "login success " + email);
                setToken(authActivity, getTokenFromResponseBody(response.getResponseBody()));
                SharedPrefManager.changeSharedName(token);
                completeUserInfo(onResponse);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));

    }

    public void register(String email, String password, String firstName, String lastName, OnResponse onResponse) {
        Log.d(TAG, "Register... " + email);
        api.registerUser(email, password, firstName, lastName).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(TAG, "Register success " + email);
                login(email, password, onResponse); //todo: remove this
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));
    }


    public void completeUserInfo(OnResponse onResponse) {
        loadingUser = true;
        String tokenString = getTokenString();
        Log.d(TAG, "On completeUserInfo...:TOKEN: " + tokenString);
        api.info(tokenString).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                user = gson.fromJson(response.getResponseBody(), User.class);
                Log.d(TAG, "completeUserInfo Success   user: " + user);
                loadingUser = false;
                onResponse.onSuccess(call, callback, response);

            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);

            }
        }));
    }


    public void upgrade(User user, File pictureFile, File certificateFile, OnResponse onResponse) {
        String json = gsonExpose.toJson(user);
        Log.d(TAG, "upgradeFiles... " + json);
        RequestBody userRequestBody = RequestBody.create(MediaType.parse("application/json"), json);


        Call<ResponseBody> call = api.upgradeData(getTokenString(), userRequestBody);

        call.enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                if (pictureFile == null && certificateFile == null) {
                    completeUserInfo(onResponse);
                    return;
                }
                api.upgradeFiles(getTokenString(), pictureFile != null ? createMultipartBody(pictureFile, "picture") : null,
                        certificateFile != null ? createMultipartBody(certificateFile, "certificate") : null)
                        .enqueue(new MyCallback(authActivity, new OnResponse() {
                            @Override
                            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                                completeUserInfo(onResponse);
                            }

                            @Override
                            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                                onResponse.onFailed(call, callback, response);
                            }
                        }));
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    public static boolean isLoadingUser() {
        return loadingUser;
    }

    public static MultipartBody.Part createMultipartBody(File file, String field) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(field, file.getName(), requestFile);
    }
}
