package ir.blackswan.travelapp.ui.Dialogs;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.OnResponse;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.DialogOnResponseBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class OnResponseDialog extends MyDialog implements OnResponse {
    AuthActivity activity;
    DialogOnResponseBinding binding;
    private static boolean showing = false;

    public OnResponseDialog(AuthActivity activity ) {
        binding = DialogOnResponseBinding.inflate(activity.getLayoutInflater());
        this.activity = activity;
        init(activity , binding.getRoot() , DIALOG_TYPE_ROUNDED_BOTTOM_SHEET);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );

    }


    private void showErrorToast(String error){
        Toast.makeText(activity , error , Toast.LENGTH_SHORT , Toast.TYPE_ERROR).show();
    }

    @Override
    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

    }

    @Override
    public void show() {
        if (!showing) {
            showing = true;
            super.show();
        }
    }

    @Override
    public void dismiss() {
        showing = false;
        super.dismiss();
    }

    public void onTryAgain(){

    }

    @Override
    public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
        if (response.getCode() == 401) {
            showErrorToast(response.getErrorMessage());
            Log.d(MyCallback.TAG, "onFailed: UnAuth  showAuthDialog:");
            activity.showAuthDialog(() -> call.clone().enqueue(callback.reload()));
        }
        else if (response.getCode() == MyResponse.NETWORK_ERROR) {
            show();
            Log.d("ResponseDialog", "onFailed: " + response);
            binding.tvOnResponseMessage.setText(response.getErrorMessage());
            binding.btnOnResponseTryAgain.setText("تلاش مجدد");
            binding.btnOnResponseTryAgain.setOnClickListener(view -> {
                call.clone().enqueue(callback.reload());
                dismiss();
                onTryAgain();
            });
            getDialog().setCancelable(false);
        }else {
            binding.btnOnResponseTryAgain.setText("باشه");
            getDialog().setCancelable(true);
            binding.tvOnResponseMessage.setText(response.getErrorMessage());
            show();
            binding.btnOnResponseTryAgain.setOnClickListener(v -> {
                dismiss();
            });
        }
    }
}
