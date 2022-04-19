package ir.blackswan.travelapp.Controller;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.ui.AuthActivity;

public class TourController extends Controller {

    static Tour[] allTours;
    static Tour[] createdTours;

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

    public void getAllTourFromServer(OnResponse onResponse){
        loadingDialog.show();
        api.getAllTour(AuthController.getUserToken()).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                allTours = gson.fromJson(responseBody , Tour[].class);
                loadingDialog.dismiss();
                onResponse.onSuccess(responseBody);
            }

            @Override
            public void onFailed(String message) {
                loadingDialog.dismiss();
                onResponse.onFailed(message);
            }
        }));
    }

    public void getCreatedTourFromServer(OnResponse onResponse){
        loadingDialog.show();
        api.getCreatedTour(AuthController.getUserToken()).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                createdTours = gson.fromJson(responseBody , Tour[].class);
                loadingDialog.dismiss();
                onResponse.onSuccess(responseBody);
            }

            @Override
            public void onFailed(String message) {
                loadingDialog.dismiss();
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

