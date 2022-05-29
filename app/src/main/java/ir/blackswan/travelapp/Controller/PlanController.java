package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Arrays;

import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PlanController extends Controller {

    static Plan[] allPlans = new Plan[0];
    static Plan[] createdPlans = new Plan[0];
    static PlanRequest[] pendingPlans = new PlanRequest[0];
    static PlanRequest[] confirmedPlans = new PlanRequest[0];



    public PlanController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void addPlanToServer(Plan plan, OnResponse onResponse) {
        String json = gsonExpose.toJson(plan);

        json = json.replace("\"places_ides\":", "\"places\":");
        Log.d(TAG, "addPlanToServer: " + json);
        api.addPlan(AuthController.getTokenString(), RequestBody.create(MediaType.parse("application/json"), json))
                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());
    }

    //search == null ? get all
    public void getAllPlanFromServer(OnResponse onResponse, @Nullable String search) {
        Log.d(MyCallback.TAG, "getAllPlanFromServer: ");
        Call<ResponseBody> call;
        if (search == null)
            search = "";

        call = api.searchPlans(AuthController.getTokenString(), search);
        call.enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                allPlans = gson.fromJson(response.getResponseBody(), Plan[].class);
                Log.d("ResponsePlan", "onSuccess: " + allPlans.length + "  " + Arrays.toString(allPlans));
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    public void getCreatedPlanFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getCreatedPlanFromServer...: ");
        api.getCreatedPlans(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(TAG, "getCreatedPlanFromServer: onSuccess: " + response.getResponseBody());
                createdPlans = gson.fromJson(response.getResponseBody(), Plan[].class);
                Log.d(TAG, "getCreatedPlanFromServer: onSuccess: " +
                        Arrays.toString(createdPlans));
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    public void getPendingPlanFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getPendingPlanFromServer: ");
        api.getPendingPlans(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(MyCallback.TAG, "getPendingPlanFromServer: " + response);
                pendingPlans = gson.fromJson(response.getResponseBody(), PlanRequest[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    public void getConfirmedPlansFromServer(OnResponse onResponse){
        Log.d(MyCallback.TAG, "getConfirmedPlansFromServer: ");
        api.getConfirmedPlans(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                confirmedPlans = gson.fromJson(response.getResponseBody(), PlanRequest[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    /*
    public void getConfirmedPlanFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getConfirmedPlanFromServer: ");
        api.getConfirmedPlans(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                pendingPlans = gson.fromJson(response.getResponseBody(), Plan[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }

     */

    public static Plan[] getAllPlans() {
        return allPlans;
    }

    public static Plan[] getCreatedPlans() {
        return createdPlans;
    }

    public static PlanRequest[] getPendingPlans() {
        return pendingPlans;
    }

    public static PlanRequest[] getConfirmedPlans() {
        return confirmedPlans;
    }
}
