package ir.blackswan.travelapp.Controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.ResponseMessageDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCallBack implements Callback<ResponseBody> {
    public static final String TAG = "Response";
    private final AuthActivity authActivity;
    private final OnResponse onResponse;
    private ResponseMessageDialog responseMessageDialog;

    public MyCallBack(AuthActivity authActivity, OnResponse onResponse) {
        this.authActivity = authActivity;
        this.onResponse = onResponse;
    }
    public MyCallBack(AuthActivity authActivity, OnResponse onResponse , ResponseMessageDialog responseMessageDialog) {
        this.authActivity = authActivity;
        this.onResponse = onResponse;
        this.responseMessageDialog = responseMessageDialog;
    }


    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.d(TAG, "onResponse: " + "ResponseMessage:" +
                response.message() + "\nResponseCode" + response.code() + " body: " + response.body()
                + "errorBody: " + response.errorBody());
        stopLoading();
        if (response.code() / 100 == 4) {
            try {
                onResponse.onFailed(ErrorHandler.getStringErrors(authActivity, response.errorBody().string()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (response.code() / 100 == 2) {
            try {
                onResponse.onSuccess(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        Log.e(TAG, "onFailure: ", t);
        stopLoading();
        onResponse.onFailed(authActivity.getString(R.string.error_connection_lost));
    }

    private void stopLoading(){
        if(responseMessageDialog != null)
            responseMessageDialog.dismiss();
    }
}
