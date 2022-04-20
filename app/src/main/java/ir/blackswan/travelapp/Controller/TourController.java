package ir.blackswan.travelapp.Controller;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

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

    public void getAllTourFromServer(OnResponse onResponse) {
        loadingDialog.show();
        api.getAllTour(AuthController.getUserToken()).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Callback<ResponseBody> callback, String responseBody) {
                allTours = gson.fromJson(responseBody, Tour[].class);
                loadingDialog.dismiss();

                onResponse.onSuccess(call, callback, responseBody);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, Callback<ResponseBody> callback, String message) {
                loadingDialog.dismiss();
                onResponse.onFailed(call, callback, message);
            }
        }));
    }

    public void getCreatedTourFromServer(OnResponse onResponse) {
        loadingDialog.show();
        api.getCreatedTour(AuthController.getUserToken()).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Callback<ResponseBody> callback, String responseBody) {
                createdTours = gson.fromJson(responseBody, Tour[].class);
                loadingDialog.dismiss();
                onResponse.onSuccess(call, callback, responseBody);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, Callback<ResponseBody> callback, String message) {
                loadingDialog.dismiss();
                onResponse.onFailed(call, callback, message);
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

