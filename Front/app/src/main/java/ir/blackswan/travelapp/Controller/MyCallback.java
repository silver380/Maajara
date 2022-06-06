package ir.blackswan.travelapp.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.LoadingDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCallback implements Callback<ResponseBody> {
    public static final String TAG = "Response";
    private final AuthActivity authActivity;
    private final OnResponse onResponse;
    private LoadingDialog loadingDialog;
    private boolean hasLoadingDialog = false;

    public MyCallback(AuthActivity authActivity, OnResponse onResponse) {
        this.authActivity = authActivity;
        this.onResponse = onResponse;
    }

    public MyCallback showLoadingDialog() {
        hasLoadingDialog = true;
        loadingDialog = new LoadingDialog(authActivity);
        loadingDialog.show();
        return this;
    }

    public MyCallback reload() {
        if (hasLoadingDialog)
            loadingDialog.show();
        return this;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
        stopLoading();
        if (response.code() / 100 == 2) {
            try {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    onResponse.onSuccess(call, this, new MyResponse(response.code(),
                            responseBody, true));
                } else {
                    throw new IOException();
                }
            } catch (IOException e) {
                onResponse.onFailed(call, this, new MyResponse(response.code(),
                        authActivity.getString(R.string.somthing_went_wrong), false));
                Log.e(TAG, "onResponse: ", e);
            }
        } else if (response.code() == 500) { //something went wrong
            onResponse.onFailed(call, this, new MyResponse(response.code(),
                    authActivity.getString(R.string.somthing_went_wrong), false));
            Log.e(TAG, "onResponse: " + response.message());
        } else {
            try {
                onResponse.onFailed(call, this, new MyResponse(response.code(),
                        ErrorHandler.getStringErrors(authActivity, response.errorBody().string()), false));
                Log.e(TAG, "onResponse: " + response.message());
            } catch (IOException e) {
                Log.e(TAG, "onResponse: ", e);
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        stopLoading();
        Log.e(TAG, "onFailure: ", t);
        onResponse.onFailed(call, this, new MyResponse(MyResponse.NETWORK_ERROR,
                authActivity.getString(R.string.error_connection_lost), false));
    }

    private void stopLoading() {
        if (loadingDialog != null && loadingDialog.getDialog().isShowing())
            loadingDialog.dismiss();
    }
}
