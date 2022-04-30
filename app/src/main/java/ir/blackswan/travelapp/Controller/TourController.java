package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TourController extends Controller {

    static Tour[] allTours;
    static Tour[] createdTours;
    static Tour[] pendingTours;
    static Tour[] confirmedTours;

    public TourController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void register(int tourId, OnResponse onResponse){
        Log.d(MyCallback.TAG, "register: ");
        api.register(AuthController.getTokenString(), tourId)
                .enqueue(new MyCallback(authActivity, onResponse));

    }

    public void addTourToServer(Tour tour, OnResponse onResponse){
        Log.d(TAG, "addTourToServer " + tour);

        String json = gsonExpose.toJson(tour);
        json = json.replace("\"places_ids\":" ,"\"places\":" );

        api.addTour(AuthController.getTokenString(), RequestBody.create(MediaType.parse("application/json"), json))
                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());
    }

    public void getAllTourFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getAllTourFromServer: ");
        api.getAllTour(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d("ResponseTour", "onSuccess: " + response.getResponseBody());
                allTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                Log.d("ResponseTour", "onSuccess: " + Arrays.toString(allTours));
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }

    public void getCreatedTourFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getCreatedTourFromServer: ");
        api.getCreatedTour(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                createdTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }

    public void getPendingTourFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getPendingTourFromServer: ");
        api.getPendingTour(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                pendingTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }

    public void getConfirmedTourFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getConfirmedTourFromServer: ");
        api.getConfirmedTour(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                confirmedTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }

    public static Tour[] getAllTours() {
        return allTours;
    }

    public static Tour[] getCreatedTours() {
        return createdTours;
    }

    public static Tour[] getPendingTours() { return pendingTours; }

    public static Tour[] getConfirmedTours() { return confirmedTours; }
}
