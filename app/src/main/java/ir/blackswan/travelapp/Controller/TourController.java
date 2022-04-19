package ir.blackswan.travelapp.Controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Retrofit.Api;
import ir.blackswan.travelapp.Retrofit.RetrofitClient;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.ResponseMessageDialog;

public class TourController extends Controller {

    Tour[] tours;

    public TourController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void createTour(Tour tour , OnResponse onResponse ) { //for test
        responseMessageDialog.show();

        String tourJson = gson.toJson(tour);
        api.createTour("token" , tourJson).enqueue(new MyCallBack(authActivity, onResponse , responseMessageDialog));
    }

    public void getAllTour(OnResponse onResponse){
        responseMessageDialog.show();
        api.getAllTour(AuthController.getUserToken()).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                tours = gson.fromJson(responseBody , Tour[].class);
                responseMessageDialog.dismiss();
                onResponse.onSuccess(responseBody);
            }

            @Override
            public void onFailed(String message) {
                responseMessageDialog.dismiss();
                onResponse.onFailed(message);
            }
        }));
    }

    public Tour[] getTours() {
        return tours;
    }
}

