package ir.blackswan.travelapp.Controller;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import java.util.Map;

public class TourLeaderRequestController extends Controller {

    static Map<String, PlanRequest[]> map_planRequests;

    public TourLeaderRequestController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void getPendingTLRequestsFromServer(OnResponse onResponse){
        api.getPendingTLRequests(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                map_planRequests = gson.fromJson(response.getResponseBody(), new
                        TypeToken<HashMap<String, PlanRequest[]>>() { }.getType());
                Log.d(MyCallback.TAG, map_planRequests.toString());
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
