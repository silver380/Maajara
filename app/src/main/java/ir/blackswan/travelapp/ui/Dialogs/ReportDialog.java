package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.DialogReportBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ReportDialog extends MyDialog {

    DialogReportBinding binding;
    private TourController tourController;
    private AuthActivity authActivity;
    private int rate;
    int tourId;
    OnRateUpdateSuccess onRateUpdateSuccess;

    public ReportDialog(Activity activity, int tour_id , OnRateUpdateSuccess onRateUpdateSuccess) {
        binding = DialogReportBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_BOTTOM_SHEET);
        authActivity = ((AuthActivity) activity);
        tourController = new TourController(authActivity);
        tourId = tour_id;
        this.onRateUpdateSuccess = onRateUpdateSuccess;


        setListeners();
    }


    private boolean checkInputs() {
        if (binding.simpleRatingBar.getRating() <= 0) {
            Toast.makeText(authActivity, "امتیاز نمی‌تواند خالی باشد.",
                    Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();
            return false;
        }
        return true;
    }

    private void setListeners() {

        binding.btnSubmitRateReport.setOnClickListener(v ->{
            if(checkInputs()){
                String report = binding.reportText.getText().toString();
                rate = (int) binding.simpleRatingBar.getRating();

                tourController.sendTourRateReportToServer(tourId, rate,
                        report, new OnResponseDialog(authActivity){
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        Log.d("ResponseRate", "onSuccess: " + response.getResponseBody());
                        onRateUpdateSuccess.onSuccess();
                        Toast.makeText(authActivity, "گزارش با موفقیت ارسال شد.", Toast.LENGTH_SHORT,
                                Toast.TYPE_SUCCESS).show();
                        activity.setResult(Activity.RESULT_OK);
                        ReportDialog.this.dialog.dismiss();
                    }
                });
            }
        });

    }

    public interface OnRateUpdateSuccess{
        void onSuccess();
    }

}
