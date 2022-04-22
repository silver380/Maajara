package ir.blackswan.travelapp.Controller;

import android.util.Log;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.ui.AuthActivity;
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

    /*
    public void createTour(Tour tour , OnResponse onResponse ) { //for test
        responseMessageDialog.show();

        String tourJson = gson.toJson(tour);
        api.createTour("token" , tourJson).enqueue(new MyCallBack(authActivity, onResponse , responseMessageDialog));
    }

     */

    public void addTourToServer(Tour tour, OnResponse onResponse){
        api.addTour(AuthController.getTokenString(), tour.getTour_name(), tour.getTour_capacity(),
                tour.getResidence(), tour.getDestination(), tour.getStart_date(), tour.getEnd_date(),
                tour.isHas_breakfast(), tour.isHas_lunch(), tour.isHas_dinner(), tour.getHas_transportation()
                , tour.getPlaces())
                .enqueue(new MyCallback(authActivity, onResponse));

    }

    public void getAllTourFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getAllTourFromServer: ");
        api.getAllTour(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                allTours = gson.fromJson(response.getResponseBody(), Tour[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }

    public void getCreatedTourFromServer(OnResponse onResponse) {

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
}

