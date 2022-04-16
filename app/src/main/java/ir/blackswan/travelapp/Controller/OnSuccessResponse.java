package ir.blackswan.travelapp.Controller;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public interface OnSuccessResponse {
    void onSuccess(String responseBody);
}
