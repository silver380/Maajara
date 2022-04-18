package ir.blackswan.travelapp.Controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Retrofit.Api;
import ir.blackswan.travelapp.Retrofit.RetrofitClient;
import ir.blackswan.travelapp.ui.Dialogs.ResponseMessageDialog;

public class TourController {
    Activity activity;
    Api api;
    Gson gson = new Gson();
    ResponseMessageDialog responseMessageDialog;
    List<Tour> tours;
    public TourController(Activity activity) {
        this.activity = activity;
        api = RetrofitClient.getApi();
        responseMessageDialog = new ResponseMessageDialog(activity);
    }

    public void createTour(Tour tour , OnResponse onResponse ) { //for test
        responseMessageDialog.show();

        String tourJson = gson.toJson(tour);
        api.createTour("token" , tourJson).enqueue(new MyCallBack(activity, onResponse , responseMessageDialog));
    }

    public void getAllTour(OnResponse onResponse){

        api.getAllTour(AuthController.getUserToken()).enqueue(new MyCallBack(activity, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                Log.d(MyCallBack.TAG, "onSuccess: " + responseBody);
                onResponse.onSuccess(responseBody);
            }

            @Override
            public void onFailed(String message) {

                onResponse.onFailed(message);
            }
        }));
    }

}

