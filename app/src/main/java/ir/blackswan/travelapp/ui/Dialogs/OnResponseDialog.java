package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.OnResponse;
import ir.blackswan.travelapp.databinding.DialogOnResponseBinding;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class OnResponseDialog extends MyDialog implements OnResponse {
    Activity activity;
    DialogOnResponseBinding binding;

    public OnResponseDialog(Activity activity ) {
        binding = DialogOnResponseBinding.inflate(activity.getLayoutInflater());
        this.activity = activity;
        init(activity , binding.getRoot() , DIALOG_TYPE_ROUNDED_BOTTOM_SHEET);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );
        getDialog().setCancelable(false);
    }


    @Override
    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

    }

    @Override
    public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
        show();
        Log.d("ResponseDialog", "onFailed: " + response);
        binding.tvOnResponseMessage.setText(response.getErrorMessage());
        binding.btnOnResponseTryAgain.setOnClickListener(view -> {
            call.clone().enqueue(callback.reload());
            getDialog().dismiss();
        });
    }
}
