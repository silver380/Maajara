package ir.blackswan.travelapp.Controller;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TourLeaderRequestController extends Controller {

    static Map<String, PlanRequest[]> map_planRequests;

    public TourLeaderRequestController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void addPlanRequest(PlanRequest planRequest, OnResponse onResponse) {
        String json = gsonExpose.toJson(planRequest);
        Log.d(TAG, "addPlanRequest... " + json);
        api.addPlanReq(AuthController.getTokenString(), RequestBody.create(MediaType.parse("application/json"), json))
                .enqueue(new MyCallback(authActivity, new OnResponse() {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        Log.d(TAG, "addPlanRequest onSuccess: " + response);
                        AuthController.getUser().setNumber_of_tickets(
                                gson.fromJson(response.getResponseBody() , PlanRequest.class)
                                        .getTour_leader().getNumber_of_tickets()
                        );
                        onResponse.onSuccess(call, callback, response);
                    }

                    @Override
                    public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        onResponse.onFailed(call, callback, response);
                    }
                }).showLoadingDialog());
    }

    public void getPendingTLRequestsFromServer(OnResponse onResponse) {
        Log.d(TAG, "getPendingTLRequestsFromServer: ....");
        api.getPendingTLRequests(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(TAG, "getPendingTLRequestsFromServer onSuccess: " + response.getResponseBody());

                map_planRequests = gson.fromJson(response.getResponseBody(), new
                        TypeToken<HashMap<String, PlanRequest[]>>() {
                        }.getType());
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }));
    }


    public void acceptLeader(int leaderId, int planId, OnResponse onResponse) {
        Log.d(TAG, "acceptLeader: ....");
        api.acceptLeader(AuthController.getTokenString(), planId, leaderId)
                .enqueue(new MyCallback(authActivity, onResponse).showLoadingDialog());

    }


    public static Map<String, PlanRequest[]> getMap_planRequests() {
        return map_planRequests;
    }
}
