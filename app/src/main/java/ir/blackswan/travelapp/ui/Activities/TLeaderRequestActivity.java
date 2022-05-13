package ir.blackswan.travelapp.ui.Activities;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.Map;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourLeaderRequestController;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.ActivityTleaderRequestBinding;
import ir.blackswan.travelapp.ui.Adapters.TLeaderRequestRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TLeaderRequestActivity extends ToolbarActivity {
    private ActivityTleaderRequestBinding binding;
    Intent intent;
    Plan plan;
    PlanRequest[] planRequests;
    private TourLeaderRequestController tourLeaderRequestController;
    public static final String TRAVEL_PLAN_ID = "travel_plan_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityTleaderRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tleader_request);
        tourLeaderRequestController = new TourLeaderRequestController(this);

        intent = getIntent();

        plan = (Plan) intent.getSerializableExtra(TRAVEL_PLAN_ID);
        setPending_tlRecycler();
    }

    private void setRecycler() {
        TLeaderRequestRecyclerAdapter leader_adapter = new TLeaderRequestRecyclerAdapter(
                planRequests, plan.getConfirmed_tour_leader(),this);

        binding.rscTLeaderReq.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rscTLeaderReq.setAdapter(leader_adapter);
    }

    private void setPending_tlRecycler() {
        tourLeaderRequestController.getPendingTLRequestsFromServer(new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                Map<String, PlanRequest[]> planRequestsMap = tourLeaderRequestController.getMap_planRequests();
                planRequests = planRequestsMap.get(plan.getTravel_plan_id() + "");
                setRecycler();
            }
        });
    }
}