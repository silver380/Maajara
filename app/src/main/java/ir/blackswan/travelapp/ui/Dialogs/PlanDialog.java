package ir.blackswan.travelapp.ui.Dialogs;

import static ir.blackswan.travelapp.ui.Activities.TLeaderRequestActivity.TRAVEL_PLAN_ID;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourLeaderRequestController;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.DialogPlanBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Activities.TLeaderRequestActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PlanDialog extends MyDialog {

    DialogPlanBinding binding;
    TourLeaderRequestController requestController;


    public PlanDialog(AuthActivity authActivity, Plan plan) {
        binding = DialogPlanBinding.inflate(authActivity.getLayoutInflater());
        init(authActivity, binding.getRoot(), DIALOG_TYPE_ALERT);
        setTransparent();
        binding.tbPlanDialog.setTitle("برنامه سفر به " + plan.getDestination());
        requestController = new TourLeaderRequestController(authActivity);
        binding.tvPlanCity.setText(plan.getDestination());
        binding.tvPlanStartDate.setText(plan.getPersianStartDate().getPersianLongDate());
        binding.tvPlanFinalDate.setText(plan.getPersianEndDate().getPersianLongDate());
        binding.llPlanReq.removeAllViews();

        for (String req : plan.getWanted_list()) {
            ViewGroup viewReq = (ViewGroup) authActivity.getLayoutInflater().inflate(R.layout.view_request, null)
                    .findViewById(R.id.group_req);
            TextView tvReq = viewReq.findViewById(R.id.tv_req);
            tvReq.setText(req);

            binding.llPlanReq.addView(viewReq);
        }

        binding.btnSendPlanRequest.setOnClickListener(v -> {
            PlanRequest planRequest = new PlanRequest(plan.getTravel_plan_id(),
                    Integer.parseInt(binding.etPlanSuggestPrice.getText().toString()));

            requestController.addPlanRequest(planRequest, new OnResponseDialog(authActivity) {
                @Override
                public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                    super.onSuccess(call, callback, response);
                    Toast.makeText(authActivity, "درخواست با موفقیت ارسال شد", Toast.LENGTH_SHORT,
                            Toast.TYPE_SUCCESS).show();
                }
            });

        });

        binding.btnSeeRequests.setOnClickListener(v -> {
            authActivity.startActivity(new Intent(
                    authActivity , TLeaderRequestActivity.class)
            .putExtra(TRAVEL_PLAN_ID , plan));
        });


    }


}
