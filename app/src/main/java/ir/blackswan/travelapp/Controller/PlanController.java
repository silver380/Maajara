package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.util.Log;

import java.util.Arrays;

import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PlanController extends Controller {

    static Plan[] allPlans = new Plan[0];
    static Plan[] createdPlans = new Plan[0];
    static Plan[] pendingPlans = new Plan[0];
    static Plan[] confirmedPlans = new Plan[0];

    /*
    static {
        //todo: remove these (FAKE PLANS!)
        List<String> requestedThing = Arrays.asList("سه وعده غذا" , "وسیله نقلیه" , "بازدید از حداقل ۵ مکان گردشگری");
        allPlans = new Plan[]{
                new Plan("شهرکرد" , "2022-07-12" , "2022-08-17" , requestedThing),
                new Plan("اصفهان" , "2022-01-12" , "2022-05-16" , requestedThing),
                new Plan("نجف‌آباد" , "2022-05-10" , "2022-05-10" , requestedThing),
                new Plan("اردستان" , "2022-03-14" , "2022-05-14" , requestedThing),
                new Plan("کاشان" , "2022-04-13" , "2022-05-15" , requestedThing),
                new Plan("اردکان" , "2022-05-10" , "2022-05-10" , requestedThing),
        };

    }

     */

    public PlanController(AuthActivity authActivity) { super(authActivity); }

    public void addPlanToServer(Plan plan, OnResponse onResponse){
        String json = gsonExpose.toJson(plan);

        api.addPlan(AuthController.getTokenString(), RequestBody.create(MediaType.parse("application/json"), json))
                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog() );
    }


    public void getAllPlanFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getAllPlanFromServer: ");
        api.getAllPlans(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d("ResponsePlan", "onSuccess: " + response.getResponseBody());
                allPlans = gson.fromJson(response.getResponseBody(), Plan[].class);
                Log.d("ResponsePlan", "onSuccess: " + Arrays.toString(allPlans));
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }

    public void getCreatedPlanFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getCreatedPlanFromServer...: ");
        api.getCreatedPlans(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(TAG, "getCreatedPlanFromServer: onSuccess: " + response.getResponseBody());
                createdPlans = gson.fromJson(response.getResponseBody(), Plan[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }

    /*
    public void getPendingPlanFromServer(OnResponse onResponse) {
        Log.d(MyCallback.TAG, "getPendingPlanFromServer: ");
        api.getPendingPlans(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
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

    public static Plan[] getAllPlans(){ return allPlans; }

    public static Plan[] getCreatedPlans(){ return createdPlans; }

}
