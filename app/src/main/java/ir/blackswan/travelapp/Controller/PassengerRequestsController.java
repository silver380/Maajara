package ir.blackswan.travelapp.Controller;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PassengerRequestsController extends Controller {

    static Map<String, User[]> map_TID_UserPending;
    static Map<String, User[]> map_TID_UserConfirmed;

    public PassengerRequestsController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void getConfirmedUsersFromServer(OnResponse onResponse){

        api.getConfirmedUsers(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                map_TID_UserConfirmed = gson.fromJson(response.getResponseBody(), new
                        TypeToken<HashMap <String, User[]>> () { }.getType());
                Log.d(MyCallback.TAG, map_TID_UserConfirmed.toString());

                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    public void getPendingUsersFromServer(OnResponse onResponse){
        api.getPendingUsers(AuthController.getTokenString()).enqueue((new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                map_TID_UserPending = gson.fromJson(response.getResponseBody(), new
                        TypeToken<HashMap <String, User[]>> () {}.getType());
                Log.d(MyCallback.TAG, map_TID_UserPending.toString());
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        })));
    }

    public static Map<String, User[]> getAllPendingUsers(){return map_TID_UserPending;}
    public static Map<String, User[]> getAllConfirmedUsers(){return  map_TID_UserConfirmed;}

    public void addAcceptedUserToServer(User user, int tour_id, OnResponse onResponse){
        api.acceptUser(AuthController.getTokenString(), tour_id, user.getUser_id())
        .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());

    }

    public void rejectUserToServer(User user, int tour_id, OnResponse onResponse){
        api.rejectUser(AuthController.getTokenString(), tour_id, user.getUser_id())
                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());

    }
}