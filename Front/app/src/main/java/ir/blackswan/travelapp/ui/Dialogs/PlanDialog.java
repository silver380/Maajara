package ir.blackswan.travelapp.ui.Dialogs;

import static ir.blackswan.travelapp.ui.Activities.TLeaderRequestActivity.TRAVEL_PLAN_ID;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Controller.TourLeaderRequestController;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.DialogPlanBinding;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Activities.TLeaderRequestActivity;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PlanDialog extends MyDialog {

    public static final int MODE_CREATED = 0, MODE_PASSENGER = 1, MODE_REQUESTED_TOUR_LEADER = 2, MODE_NOT_REQUESTED_TOUR_LEADER = 3,
            MODE_CONFIRMED_TOUR_LEADER = 4, MODE_REJECTED_TOUR_LEADER = 5;
    private static final int MODE_ALREADY_HAS_TOUR_LEADER = 6;
    DialogPlanBinding binding;
    TourLeaderRequestController requestController;
    PlanController planController;
    Plan plan;
    @Nullable
    PlanRequest alreadyRequest;
    MainActivity mainActivity;
    TextInputsChecker checker;


    public PlanDialog(MainActivity mainActivity, Plan plan) {
        binding = DialogPlanBinding.inflate(mainActivity.getLayoutInflater());
        init(mainActivity, binding.getRoot(), DIALOG_TYPE_ALERT);
        setTransparent();
        this.plan = plan;
        this.mainActivity = mainActivity;
        planController = new PlanController(mainActivity);
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
        binding.tbPlanDialog.setTitle("ماجرایی به " + plan.getDestination());
        requestController = new TourLeaderRequestController(mainActivity);
        binding.tvPlanCreator.setText(plan.getPlan_creator().getFullNameWithPrefix());
        binding.tvPlanCity.setText(plan.getDestination());
        binding.tvPlanStartDate.setText(plan.getPersianStartDate().getPersianLongDate());
        binding.tvPlanFinalDate.setText(plan.getPersianEndDate().getPersianLongDate());
        binding.rycPlanDialogPlaces.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        binding.rycPlanDialogPlaces.setAdapter(new PlacesRecyclerAdapter(mainActivity, plan.getPlaces()));

        binding.llPlanReq.removeAllViews();
        for (String req : plan.getWanted_list()) {
            ViewGroup viewReq = mainActivity.getLayoutInflater().inflate(R.layout.view_request, null)
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
            binding.groupPlanRequest.setVisibility(View.GONE);
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
            planController.getPendingPlanFromServer(new OnResponseDialog(mainActivity) {
                @Override
                public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                    super.onSuccess(call, callback, response);
                    planController.getConfirmedPlansFromServer(new OnResponseDialog(mainActivity) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                            super.onSuccess(call, callback, response);
                            setLoading(false);
                            PlanRequest[] confirmedPlanRequests = PlanController.getConfirmedPlans();
                            PlanRequest confirmedReq = findPlanReq(confirmedPlanRequests);
                            PlanRequest[] pendingPlanRequests = PlanController.getPendingPlans();
                            PlanRequest pendingReq = findPlanReq(pendingPlanRequests);
                            User confirmedUser = plan.getConfirmed_tour_leader();
                            Log.d("PlanDialog", "PendingReq: " + pendingReq);
                            Log.d("PlanDialog", "ConfirmedReq: " + confirmedReq);
                            Log.d("PlanDialog", "ConfirmedUser: " + confirmedUser);
                            if (confirmedUser == null) {
                                if (confirmedReq == null) {
                                    if (pendingReq == null) {
                                        setupForRequest();
                                        setDialogMode(MODE_NOT_REQUESTED_TOUR_LEADER);
                                    } else {
                                        alreadyRequest = pendingReq;
                                        setDialogMode(MODE_REQUESTED_TOUR_LEADER);
                                    }
                                } else {
                                    alreadyRequest = confirmedReq;
                                    setDialogMode(MODE_CONFIRMED_TOUR_LEADER);
                                }
                            } else {
                                if (confirmedUser.equals(user)) {
                                    plan.setAccepted_price(confirmedReq.getSuggested_price());
                                    setDialogMode(MODE_CONFIRMED_TOUR_LEADER);
                                } else {
                                    if (pendingReq != null) {
                                        alreadyRequest = pendingReq;
                                        setDialogMode(MODE_REJECTED_TOUR_LEADER);
                                    } else {
                                        setDialogMode(MODE_ALREADY_HAS_TOUR_LEADER);
                                    }
                                }
                            }

                        }
                    });


                }
            });


        }

        PlanDialog.this.binding.btnSeeRequests.setOnClickListener(v -> {
            mainActivity.startActivityForResult(new Intent(
                    mainActivity, TLeaderRequestActivity.class)
                    .putExtra(TRAVEL_PLAN_ID, plan), MainActivity.REQUEST_LEADER_REQUESTS);
        });
    }

    private void setupForRequest() {
        setDialogMode(MODE_NOT_REQUESTED_TOUR_LEADER);

        PlanDialog.this.binding.btnSendPlanRequest.setOnClickListener(v -> {
            if (!checker.checkAllError()) {
                PlanRequest planRequest = new PlanRequest(plan.getTravel_plan_id(),
                        Integer.parseInt(PlanDialog.this.binding.etPlanSuggestPrice.getText().toString()));

                requestController.addPlanRequest(planRequest, new OnResponseDialog(mainActivity) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        Toast.makeText(mainActivity, "درخواست با موفقیت ارسال شد.", Toast.LENGTH_SHORT,
                                Toast.TYPE_SUCCESS).show();
                        Log.d(MyCallback.TAG, "addPlanRequest onSuccess: " + response.getResponseBody());
                        alreadyRequest = new Gson().fromJson(response.getResponseBody(), PlanRequest.class);
                        mainActivity.getHomeFragment().refresh();
                        setDialogMode(MODE_REQUESTED_TOUR_LEADER);
                    }
                });

            }
        });
    }

    private PlanRequest findPlanReq(PlanRequest[] planRequests) {
        PlanRequest userPlanRequest = null;
        for (PlanRequest planReq : planRequests) {

            if (planReq.getTour_leader().equals(AuthController.getUser()) && planReq
                    .getTravel_plan().equals(plan)) {
                userPlanRequest = planReq;
                break;
            }
        }
        return userPlanRequest;
    }

    @SuppressLint("SetTextI18n")
    public void setDialogMode(int mode) {
        setLoading(false);
        if (mode == MODE_ALREADY_HAS_TOUR_LEADER) {
            binding.tvYourRequestLabel.setVisibility(View.GONE);
            binding.tvPlanDialogRequestStatus.setVisibility(View.GONE);
        } else {
            binding.tvYourRequestLabel.setVisibility(View.VISIBLE);
            binding.tvPlanDialogRequestStatus.setVisibility(View.VISIBLE);
        }

        switch (mode) {
            case MODE_CREATED:
                binding.btnSeeRequests.setVisibility(View.VISIBLE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.groupPlanRequest.setVisibility(View.GONE);
                break;
            case MODE_PASSENGER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.groupPlanRequest.setVisibility(View.GONE);
                break;
            case MODE_CONFIRMED_TOUR_LEADER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.groupPlanRequest.setVisibility(View.VISIBLE);
                binding.tvPlanDialogRequestPrice.setText(plan.getAccepted_price());
                binding.tvPlanDialogRequestStatus.setBackgroundTintList(
                        ColorStateList.valueOf(mainActivity.getColor(R.color.colorSuccess))
                );
                binding.tvPlanDialogRequestStatus.setText("تایید شده");
                break;
            case MODE_REJECTED_TOUR_LEADER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.groupPlanRequest.setVisibility(View.VISIBLE);
                binding.tvPlanDialogRequestPrice.setText(alreadyRequest.getSuggested_priceString());
                binding.tvPlanDialogRequestStatus.setBackgroundTintList(
                        ColorStateList.valueOf(mainActivity.getColor(R.color.colorError))
                );
                binding.tvPlanDialogRequestStatus.setText("رد شده");
                break;
            case MODE_REQUESTED_TOUR_LEADER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.groupPlanRequest.setVisibility(View.VISIBLE);
                binding.tvPlanDialogRequestPrice.setText(alreadyRequest.getSuggested_priceString());
                binding.tvPlanDialogRequestStatus.setBackgroundTintList(
                        ColorStateList.valueOf(mainActivity.getColor(R.color.colorWarning))
                );
                binding.tvPlanDialogRequestStatus.setText("در انتظار تایید");
                break;

            case MODE_ALREADY_HAS_TOUR_LEADER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.GONE);
                binding.groupPlanRequest.setVisibility(View.VISIBLE);
                binding.tvPlanDialogRequestPrice.setText("راهنمای سفر این ماجرا ثبت شده است.");
                break;

            case MODE_NOT_REQUESTED_TOUR_LEADER:
                binding.btnSeeRequests.setVisibility(View.GONE);
                binding.llPlanSendRequest.setVisibility(View.VISIBLE);
                binding.groupPlanRequest.setVisibility(View.GONE);
                break;

        }

    }

}
