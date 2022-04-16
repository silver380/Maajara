package ir.blackswan.travelapp.Controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import ir.blackswan.travelapp.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCallBack implements Callback<ResponseBody> {
    public static final String TAG = "Response";
    private final Context context;
    private final OnSuccessResponse onSuccessResponse;

    public MyCallBack(Context context, OnSuccessResponse onSuccessResponse) {
        this.context = context;
        this.onSuccessResponse = onSuccessResponse;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.d(TAG, "onResponse: " + "ResponseMessage:" +
                response.message() + "\nResponseCode" + response.code() + " body: " + response.body()
                + "errorBody: " + response.errorBody());
        if (response.errorBody() != null) {
            try {
                ErrorHandler.showGsonError(context, response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (response.body() != null) {
            try {
                onSuccessResponse.onSuccess(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        Log.e(TAG, "onFailure: ", t);
        ErrorHandler.showGsonError(context, context.getString(R.string.error_connection_lost));
    }
}
