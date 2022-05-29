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

    public void sendTourRateReportToServer(int tour_id, int rate, String report, OnResponse onResponse) {
        api.sendTourRateReport(AuthController.getTokenString(), tour_id, rate, report)
                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());
    }

    public void getRateStatusFromServer(OnResponse onResponse, String tour_id) {
        Call<ResponseBody> call = api.getRateStatus(AuthController.getTokenString(), tour_id);
        Log.d(TAG, "getRateStatusFromServer: ..." + call.request().url());

        call.enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                CurrentTour currentTour = gson.fromJson(response.getResponseBody(), CurrentTour.class);
                canRate = currentTour.can_rate;
                rate = currentTour.current_rate.tour_rate;
                Log.d(TAG, "getRateStatusFromServer: ..." + response);
                if(rate == null)
                    rate = -1;
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }));



    }

    public void register(int tourId, OnResponse onResponse){
        Log.d(MyCallback.TAG, "register: ");
        api.register(AuthController.getTokenString(), tourId)
                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());

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

    public void addTourToServer(Tour tour, OnResponse onResponse) {
        Log.d(TAG, "addTourToServer " + tour);

        String json = gsonExpose.toJson(tour);
        json = json.replace("\"places_ids\":", "\"places\":");

        api.addTour(AuthController.getTokenString(), RequestBody.create(MediaType.parse("application/json"), json))
                .enqueue(new MyCallback(authActivity, new OnResponse() {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        Log.d(TAG, "addTourToServer onSuccess: " + response);
                        AuthController.getUser().setNumber_of_tickets(
                                gson.fromJson(response.getResponseBody() , Tour.class)
                                        .getCreator().getNumber_of_tickets()
                        );
                        onResponse.onSuccess(call, callback, response);
                    }

                    @Override
                    public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        onResponse.onFailed(call, callback, response);
                    }
                }).showLoadingDialog());
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

    public static Tour[] getPendingTours() {
        return pendingTours;
    }

    public static Tour[] getConfirmedTours() {
        return confirmedTours;
    }


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

class CurrentTour {
    boolean can_rate;
    CurrentRate current_rate;

    public CurrentTour(boolean can_rate, Integer tourRate, String tourReport) {
        this.can_rate = can_rate;
        current_rate = new CurrentRate();
        current_rate.tour_rate = tourRate;
        current_rate.tour_report = tourReport;
    }

    static class CurrentRate {
        Integer tour_rate;
        String tour_report;
    }

}

