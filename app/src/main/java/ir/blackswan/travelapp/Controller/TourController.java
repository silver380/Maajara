package ir.blackswan.travelapp.Controller;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.ui.AuthActivity;

public class TourController extends Controller {

    Tour[] allTours;
    Tour[] createdTours;

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

    public void getAllTour(OnResponse onResponse){
        responseMessageDialog.show();
        api.getAllTour(AuthController.getUserToken()).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                allTours = gson.fromJson(responseBody , Tour[].class);
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

    public void getCreatedTour(OnResponse onResponse){
        responseMessageDialog.show();
        api.getCreatedTour(AuthController.getUserToken()).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                createdTours = gson.fromJson(responseBody , Tour[].class);
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

    public Tour[] getAllTours() {
        return allTours;
    }

    public Tour[] getCreatedTours() {
        return createdTours;
    }
}

