package ir.blackswan.travelapp.Controller;

import ir.blackswan.travelapp.Data.PlanRequest;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import java.util.Map;

public class TourLeaderRequestController extends Controller {

    static Map<String, PlanRequest[]> map_planRequests;

    public TourLeaderRequestController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void addPlanRequest(PlanRequest planRequest, OnResponse onResponse){
        String json = gsonExpose.toJson(planRequest);
        Log.d(MyCallback.TAG, "addPlanRequest... " + json);
        api.addPlanReq(AuthController.getTokenString() ,RequestBody.create(
                MediaType.parse("application/json"), json)).enqueue(new MyCallback(authActivity , onResponse));
    }

    public void getPendingTLRequestsFromServer(OnResponse onResponse){
        Log.d(MyCallback.TAG, "getPendingTLRequestsFromServer: ....");
        api.getPendingTLRequests(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d(MyCallback.TAG, "getPendingTLRequestsFromServer onSuccess: " +  response.getResponseBody());

                map_planRequests = gson.fromJson(response.getResponseBody(), new
                        TypeToken<HashMap<String, PlanRequest[]>>() { }.getType());
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                onResponse.onFailed(call, callback, response);
            }
        }).showLoadingDialog());
    }


    public static Map<String, PlanRequest[]> getMap_planRequests() {
        return map_planRequests;
    }
}
