package ir.blackswan.travelapp.Controller;

import android.util.Log;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PlaceController extends Controller {

    static Place[] allPlaces;

    public PlaceController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void getAllPlacesFromServer(OnResponse onResponse) {
        Log.d("ResponsePlaces", "getAllPlacesFromServer: ..." );
        api.getAllPlace(AuthController.getTokenString()).enqueue(new MyCallback(authActivity, new OnResponse() {
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
        }).showLoadingDialog());
    }

    public static Place[] getAllPlaces() {
        return allPlaces;
    }
}
