package ir.blackswan.travelapp.Controller;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Retrofit.Api;
import ir.blackswan.travelapp.Retrofit.RetrofitClient;
import ir.blackswan.travelapp.ui.Dialogs.ResponseMessageDialog;

public class TourController {
    Activity activity;
    Api api;
    Gson gson = new Gson();
    ResponseMessageDialog responseMessageDialog;
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

}

