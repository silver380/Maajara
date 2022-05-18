package ir.blackswan.travelapp.Controller;

import android.util.Log;

import androidx.annotation.Nullable;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PlaceController extends Controller {

    static Place[] allPlaces;

    public PlaceController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void getAllPlacesFromServer(OnResponse onResponse , @Nullable String search) {
        Log.d("ResponsePlaces", "getAllPlacesFromServer: ..." );
        Call<ResponseBody> call;
        if (search == null)
            search = "";

        call = api.searchPlaces(AuthController.getTokenString(), search);
        call.enqueue(new MyCallback(authActivity, new OnResponse() {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                Log.d("ResponsePlaces", "onSuccess: " + response.getResponseBody());
                allPlaces = gson.fromJson(response.getResponseBody(), Place[].class);
                onResponse.onSuccess(call, callback, response);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                onResponse.onFailed(call, callback, response);
            }
        }));
    }

    public static Place[] getAllPlaces() {
        return allPlaces;
    }
}
