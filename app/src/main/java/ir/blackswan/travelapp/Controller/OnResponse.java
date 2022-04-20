package ir.blackswan.travelapp.Controller;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public interface OnResponse {

    void onSuccess(Call<ResponseBody> call , Callback<ResponseBody> callback , String responseBody);

    void onFailed(Call<ResponseBody> call,Callback<ResponseBody> callback , String message);
}