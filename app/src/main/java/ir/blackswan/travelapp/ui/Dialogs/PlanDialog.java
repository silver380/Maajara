package ir.blackswan.travelapp.ui.Dialogs;

import static ir.blackswan.travelapp.ui.Activities.TLeaderRequestActivity.TRAVEL_PLAN_ID;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.Map;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourLeaderRequestController;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.DialogPlanBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Activities.TLeaderRequestActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PlanDialog extends MyDialog {

    public static final int MODE_CREATED = 0, MODE_PASSENGER = 1, MODE_REQUESTED_TOUR_LEADER = 2, MODE_NOT_REQUESTED_TOUR_LEADER = 3, MODE_CONFIRMED_TOUR_LEADER = 4;
    DialogPlanBinding binding;
    TourLeaderRequestController requestController;
    Plan plan;
    @Nullable
    PlanRequest alreadyRequest;
    AuthActivity authActivity;
    TextInputsChecker checker;


    public PlanDialog(AuthActivity authActivity, Plan plan) {
        binding = DialogPlanBinding.inflate(authActivity.getLayoutInflater());
        init(authActivity, binding.getRoot(), DIALOG_TYPE_ALERT);
        setTransparent();
        this.plan = plan;
        this.authActivity = authActivity;
        setChecker();
        setData();
        setPrice();
        setSendRequestButton();
    }

    private void setChecker() {
        checker = new TextInputsChecker();
        checker.add(binding.etPlanSuggestPrice);
    }

    private void setPrice() {
        binding.etPlanSuggestPrice.addTextChangedListener
                (new TextWatcher() {
                     @Override
                     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                     }

                     @Override
                     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                     }

                     @Override
                     public void afterTextChanged(Editable editable) {
                         String price = Utils.getEditableText(binding.etPlanSuggestPrice.getText());
                         if (price.isEmpty())
                             binding.tilPlanSuggestPrice.setHelperText("");
                         else {
                             String price_helper = Utils.getPriceString(
                                     Integer.parseInt(price));
                             binding.tilPlanSuggestPrice.setHelperText(price_helper);
                         }
                     }
                 }
                );
    }

    private void setData() {
        binding.tbPlanDialog.setTitle("برنامه سفر به " + plan.getDestination());
        requestController = new TourLeaderRequestController(authActivity);
        binding.tvPlanCity.setText(plan.getDestination());
        binding.tvPlanStartDate.setText(plan.getPersianStartDate().getPersianLongDate());
        binding.tvPlanFinalDate.setText(plan.getPersianEndDate().getPersianLongDate());
        binding.rycPlanDialogPlaces.setLayoutManager(new LinearLayoutManager(authActivity, LinearLayoutManager.HORIZONTAL, false));
        //binding.rycPlanDialogPlaces.setAdapter(new PlacesRecyclerAdapter(authActivity , plan.getPlaces())); //todo: active this

        binding.llPlanReq.removeAllViews();
        for (String req : plan.getWanted_list()) {
            ViewGroup viewReq = (ViewGroup) authActivity.getLayoutInflater().inflate(R.layout.view_request, null)
                    .findViewById(R.id.group_req);
            TextView tvReq = viewReq.findViewById(R.id.tv_req);
            tvReq.setText(req);

            binding.llPlanReq.addView(viewReq);
        }
    }

    private void setLoading(boolean loading) {
        if (loading) {
            binding.pbPlanDialogLoading.setVisibility(View.VISIBLE);
            binding.llPlanSendRequest.setVisibility(View.GONE);
            binding.btnSeeRequests.setVisibility(View.GONE);
        } else {
            binding.pbPlanDialogLoading.setVisibility(View.GONE);
        }
    }

    private void setSendRequestButton() {
        User user = AuthController.getUser();
        if (plan.getPlan_creator().equals(user))
            setDialogMode(MODE_CREATED);
        else if (!user.is_tour_leader()) {
            setDialogMode(MODE_PASSENGER);
        } else if (plan.getConfirmed_tour_leader() != null && plan.getConfirmed_tour_leader().equals(user)) {
            setDialogMode(MODE_CONFIRMED_TOUR_LEADER);
        } else {
            setLoading(true);
            requestController.getPendingTLRequestsFromServer(new OnResponseDialog(authActivity) {
                @Override
                public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                    super.onSuccess(call, callback, response);
                    setLoading(false);
                    Map<String, PlanRequest[]> planRequestsMap = TourLeaderRequestController.getMap_planRequests();
                    PlanRequest[] planRequests = planRequestsMap.get(plan.getTravel_plan_id() + "");
                    if (planRequests == null) {
                        planRequests = new PlanRequest[0];
                    }
                    PlanRequest userPlanRequest = null;
                    for (PlanRequest planReq : planRequests) {
                        if (planReq.getTravel_plan().getPlan_creator().equals(user)) {
                            userPlanRequest = planReq;
                            break;
                        }
                    }
                    if (userPlanRequest == null) {
                        setDialogMode(MODE_NOT_REQUESTED_TOUR_LEADER);

                        PlanDialog.this.binding.btnSendPlanRequest.setOnClickListener(v -> {
                            if (!checker.checkAllError()) {
                                PlanRequest planRequest = new PlanRequest(plan.getTravel_plan_id(),
                                        Integer.parseInt(PlanDialog.this.binding.etPlanSuggestPrice.getText().toString()));

                                requestController.addPlanRequest(planRequest, new OnResponseDialog(authActivity) {
                                    @Override
                                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                                        super.onSuccess(call, callback, response);
                                        Toast.makeText(authActivity, "درخواست با موفقیت ارسال شد", Toast.LENGTH_SHORT,
                                                Toast.TYPE_SUCCESS).show();
                                        Log.d(MyCallback.TAG, "addPlanRequest onSuccess: " + response.getResponseBody());
                                        alreadyRequest = new Gson().fromJson(response.getResponseBody(), PlanRequest.class);
                                        setDialogMode(MODE_REQUESTED_TOUR_LEADER);
                                    }
                                });

                            }
                        });


                    } else {
                        alreadyRequest = userPlanRequest;
                        setDialogMode(MODE_REQUESTED_TOUR_LEADER);
                    }


                }
            });


        }

        PlanDialog.this.binding.btnSeeRequests.setOnClickListener(v -> {
            authActivity.startActivityForResult(new Intent(
                    authActivity, TLeaderRequestActivity.class)
                    .putExtra(TRAVEL_PLAN_ID, plan), MainActivity.REQUEST_LEADER_REQUESTS);
        });
    }

    @SuppressLint("SetTextI18n")
    public void setDialogMode(int mode) {
        switch (mode) {
            case MODE_CREATED:
                binding.btnSeeRequests.setVisibility(View.VISIBLE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.tvPlanDialogAlreadyRequested.setVisibility(View.GONE);
                break;
            case MODE_PASSENGER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.tvPlanDialogAlreadyRequested.setVisibility(View.GONE);
                break;
            case MODE_CONFIRMED_TOUR_LEADER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.tvPlanDialogAlreadyRequested.setVisibility(View.VISIBLE);
                binding.tvPlanDialogAlreadyRequested.setText("درخواست شما با قیمت پیشنهادی " + plan.getAccepted_price() + "تایید شده است.");
                break;
            case MODE_REQUESTED_TOUR_LEADER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.tvPlanDialogAlreadyRequested.setVisibility(View.VISIBLE);
                binding.tvPlanDialogAlreadyRequested.setText("درخواست شما با قیمت پیشنهادی " + alreadyRequest.getSuggested_price() + " در انتظار تایید است.");
                break;
            case MODE_NOT_REQUESTED_TOUR_LEADER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.VISIBLE);
                binding.tvPlanDialogAlreadyRequested.setVisibility(View.GONE);
                break;

        }

    }

}
