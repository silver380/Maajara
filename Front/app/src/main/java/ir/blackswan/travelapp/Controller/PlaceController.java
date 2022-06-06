package ir.blackswan.travelapp.Controller;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PlaceController extends Controller {

    static Place[] allPlaces;

    public PlaceController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void addPlace(Place place , File image , OnResponse onResponse){
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg") , image);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", image.getName(), requestFile);
        MultipartBody.Part name = MultipartBody.Part.createFormData("name", place.getName());
        MultipartBody.Part city = MultipartBody.Part.createFormData("city", place.getCity());
        MultipartBody.Part des = MultipartBody.Part.createFormData("description", place.getDescription());
        MultipartBody.Part lat = MultipartBody.Part.createFormData("latitude", place.getLatitude() + "");
        MultipartBody.Part lng = MultipartBody.Part.createFormData("longitude", place.getLongitude() + "");

        Call<ResponseBody> call = api.addPlace(AuthController.getTokenString() , body , name , city , des , lat , lng);
        call.enqueue(new MyCallback(authActivity ,onResponse).showLoadingDialog());
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
