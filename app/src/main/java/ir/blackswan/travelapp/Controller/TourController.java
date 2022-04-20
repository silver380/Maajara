package ir.blackswan.travelapp.Controller;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

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
            public void onSuccess(Call<ResponseBody> call,String responseBody) {
                allTours = gson.fromJson(responseBody , Tour[].class);
                loadingDialog.dismiss();
                onResponse.onSuccess(call , responseBody);
            }

            @Override
            public void onFailed(Call<ResponseBody> call,String message) {
                loadingDialog.dismiss();
                onResponse.onFailed(call , message);
            }
        }));
    }

    public void getCreatedTourFromServer(OnResponse onResponse){
        loadingDialog.show();
        api.getCreatedTour(AuthController.getUserToken()).enqueue(new MyCallBack(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call,String responseBody) {
                createdTours = gson.fromJson(responseBody , Tour[].class);
                loadingDialog.dismiss();
                onResponse.onSuccess(call , responseBody);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, String message) {
                loadingDialog.dismiss();
                onResponse.onFailed(call , message);
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

