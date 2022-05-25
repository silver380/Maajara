package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Arrays;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TourController extends Controller {

    static Tour[] allTours;
    static Tour[] createdTours;
    static Tour[] pendingTours;
    static Tour[] confirmedTours;
    static Tour[] archiveTours;
    static boolean canRate;
    static Integer rate;
    static Tour[] suggestionTours;

    public TourController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void register(int tourId, OnResponse onResponse){
        Log.d(MyCallback.TAG, "register: ");
        api.register(AuthController.getTokenString(), tourId)
                .enqueue(new MyCallback(authActivity, onResponse));

    }

    public void getSuggestionToursFromServer(int tourId, OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getSuggestionToursFromServer: ");
        api.getSuggestionTours(AuthController.getTokenString(), Integer.toString(tourId))
                .enqueue(new MyCallback(authActivity, new OnResponse() {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        suggestionTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                        Log.d(TAG, "getSuggestionToursFromServer: onSuccess: " + response.getResponseBody());
                        onResponse.onSuccess(call, callback, response);
                    }

                    @Override
                    public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        onResponse.onFailed(call, callback, response);
                    }
                }));
    }

    public void sendTourRateReportToServer(int user_id, int tour_id, int rate, String report, OnResponse onResponse){
        //api.sendTourRateReport(AuthController.getTokenString(), user_id, tour_id, rate, report).enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog()); todo
    }

    //todo complete bellow
    public void getRateStatusFromServer(OnResponse onResponse){
        //if null >> -1

        //if rate is null >> -1
//        String json = gsonExpose.toJson(TourReport);
//
//        api.sendTourReport(AuthController.getTokenString(), json)
//                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());
    }

    public void getArchiveTourFromServer(OnResponse onResponse){
        Log.d(MyCallback.TAG, "getArchiveTourFromServer: ");
        api.getArchiveTours(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                archiveTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    public void addTourToServer(Tour tour, OnResponse onResponse){
        Log.d(TAG, "addTourToServer " + tour);

        String json = gsonExpose.toJson(tour);
        json = json.replace("\"places_ids\":" ,"\"places\":" );

        api.addTour(AuthController.getTokenString(), RequestBody.create(MediaType.parse("application/json"), json))
                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());
    }

    public void getAllTourFromServer(OnResponse onResponse , @Nullable String search) {
        Log.d(MyCallback.TAG, "getAllTourFromServer: ");
        Call<ResponseBody> call;
        if (search == null)
            search = "";

        call = api.searchTours(AuthController.getTokenString(), search);
        call.enqueue(new MyCallback(authActivity, new OnResponse() {
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
        }));
    }

    public void getCreatedTourFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getCreatedTourFromServer...: ");
        api.getCreatedTour(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(TAG, "getCreatedTourFromServer: onSuccess: " + response.getResponseBody());
                createdTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }));
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
        }));
    }

    public void getConfirmedTourFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getConfirmedTourFromServer:...");
        api.getConfirmedTour(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(MyCallback.TAG, "getConfirmedTourFromServerSuccess: ConfirmedTour: " + response.getResponseBody());
                confirmedTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    public static Tour[] getAllTours() {
        return allTours;
    }

    public static Tour[] getCreatedTours() {
        return createdTours;
    }

    public static Tour[] getPendingTours() { return pendingTours; }

    public static Tour[] getConfirmedTours() { return confirmedTours; }


    public static Tour[] getSuggestionTours() {
        return suggestionTours;
    }

    public static Integer getRate() {
        return rate;
    }

    public static boolean canRate() {
        return canRate;
    }

    public static Tour[] getArchiveTours() {
        return archiveTours;
    }
}

