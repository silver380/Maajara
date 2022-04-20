package ir.blackswan.travelapp.Controller;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface OnResponse {

    void onSuccess(Call<ResponseBody> call, String responseBody);

    void onFailed(Call<ResponseBody> call, String message);
}