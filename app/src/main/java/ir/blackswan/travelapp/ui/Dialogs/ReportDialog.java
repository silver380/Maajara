package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.DialogReportBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ReportDialog extends MyDialog {

    DialogReportBinding binding;
    private TextInputsChecker checker = new TextInputsChecker();
    private TourController tourController;
    private AuthActivity authActivity;
    private int rate;
    int tourId;
    int userId;


    public ReportDialog(Activity activity, int tour_id) {
        binding = DialogReportBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_BOTTOM_SHEET);
        authActivity = ((AuthActivity) activity);
        tourController = new TourController(authActivity);
        tourId = tour_id;

        setListeners();
    }


    private boolean checkInputs() {
        if (checker.checkAllError())
            return false;
        return true;
    }

    private void setListeners() {

        binding.btnSubmitRateReport.setOnClickListener(v ->{
            if(checkInputs()){
                String report = binding.reportText.getText().toString();
                rate = (int) binding.simpleRatingBar.getRating();
                userId = AuthController.getUser().getUser_id();

                tourController.sendTourRateReportToServer(userId, tourId, rate,
                        report, new OnResponseDialog(authActivity){
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        Toast.makeText(authActivity, "گزارش با موفقیت ارسال شد.", Toast.LENGTH_SHORT,
                                Toast.TYPE_SUCCESS).show();

                        ReportDialog.this.dialog.dismiss();
                    }
                });
            }
        });

    }

}
