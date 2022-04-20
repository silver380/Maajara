package ir.blackswan.travelapp.ui;

import android.app.Activity;
import android.view.View;

import ir.blackswan.travelapp.Controller.OnResponse;
import ir.blackswan.travelapp.databinding.DialogOnResponseBinding;
import ir.blackswan.travelapp.databinding.DialogResponseMessgeBinding;
import ir.blackswan.travelapp.ui.Dialogs.MyDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class OnResponseDialog extends MyDialog implements OnResponse {
    Activity activity;
    DialogOnResponseBinding binding;

    public OnResponseDialog(Activity activity) {
        binding = DialogOnResponseBinding.inflate(activity.getLayoutInflater());
        this.activity = activity;
    }


    @Override
    public void onSuccess(Call<ResponseBody> call, Callback<ResponseBody> callback, String responseBody) {

    }

    @Override
    public void onFailed(Call<ResponseBody> call, Callback<ResponseBody> callback, String message) {
        binding.tvOnResponseMessage.setText(message);
        binding.btnOnResponseTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call.enqueue(callback);
            }
        });
    }
}
