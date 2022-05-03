package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.DialogPlanBinding;

public class PlanDialog extends MyDialog {

    DialogPlanBinding binding;


    public PlanDialog(Activity activity, Plan plan) {
        binding = DialogPlanBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_ALERT);
        setTransparent();
        binding.tbPlanDialog.setTitle("برنامه سفر به " + plan.getDestination());
        binding.tvPlanCity.setText(plan.getDestination());
        binding.tvPlanStartDate.setText(plan.getPersianStartDate().getPersianLongDate());
        binding.tvPlanFinalDate.setText(plan.getPersianEndDate().getPersianLongDate());
        binding.llPlanReq.removeAllViews();
        for (String req : plan.getRequestedThings()) {
            ViewGroup viewReq = (ViewGroup)activity.getLayoutInflater().inflate(R.layout.view_request, null)
                    .findViewById(R.id.group_req);
            TextView tvReq = viewReq.findViewById(R.id.tv_req);
            tvReq.setText(req);

            binding.llPlanReq.addView(viewReq);
        }

        binding.btnSendPlanRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: send PlanRequest
            }
        });

        binding.btnSeeRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: start Requests activity
            }
        });



    }


}
