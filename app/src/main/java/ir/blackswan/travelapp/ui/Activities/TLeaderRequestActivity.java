package ir.blackswan.travelapp.ui.Activities;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourLeaderRequestController;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.ActivityTleaderRequestBinding;
import ir.blackswan.travelapp.ui.Adapters.TLeaderRequestRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TLeaderRequestActivity extends ToolbarActivity {
    private ActivityTleaderRequestBinding binding;
    private PlanRequest[] pending_tl;
    private PlanRequest confirmed_tl;
    ArrayList<PlanRequest> all_tl;
    Intent intent;
    int plan_id;
    private TourLeaderRequestController tourLeaderRequestController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityTleaderRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tleader_request);
        tourLeaderRequestController = new TourLeaderRequestController(this);

        intent = getIntent();
        plan_id = intent.getIntExtra("travel_plan_id", -1);
    }

    private void setRecycler() {
        TLeaderRequestRecyclerAdapter leader_adapter = new TLeaderRequestRecyclerAdapter(all_tl,
                new ArrayList<>(Arrays.asList(confirmed_tl)), this, plan_id);
        binding.rscTLeaderReq.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rscTLeaderReq.setAdapter(leader_adapter);
    }

    private void setPending_tlRecycler() {
        tourLeaderRequestController.getPendingTLRequestsFromServer(new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                Map<String, PlanRequest[]> pending_leaders = tourLeaderRequestController.getMap_planRequests();

                setRecycler();
            }
        });
    }
}