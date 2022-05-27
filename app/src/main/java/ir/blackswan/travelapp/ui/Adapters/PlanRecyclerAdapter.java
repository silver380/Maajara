package ir.blackswan.travelapp.ui.Adapters;

import static ir.blackswan.travelapp.ui.Activities.TLeaderRequestActivity.TRAVEL_PLAN_ID;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MyPersianCalender;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Activities.TLeaderRequestActivity;
import ir.blackswan.travelapp.ui.Dialogs.PlanDialog;

public class PlanRecyclerAdapter extends RecyclerView.Adapter<PlanRecyclerAdapter.ViewHolder>
        implements HasArray<Plan> {

    AuthActivity authActivity;
    Plan[] plans;
    RecyclerView recyclerView;

    public PlanRecyclerAdapter(AuthActivity authActivity, Plan[] plans) {
        this.authActivity = authActivity;
        this.plans = plans;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public PlanRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = authActivity.getLayoutInflater().inflate(R.layout.travel_plan_view_holder , parent , false);
        if (!(recyclerView.getLayoutManager() instanceof GridLayoutManager))
            view.getLayoutParams().width = Utils.getScreenWidth() * 32 / 100;



        return new PlanRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanRecyclerAdapter.ViewHolder holder, int position) {
        Plan plan = plans[position];
        MyPersianCalender startDate = plan.getPersianStartDate();

        holder.startDate.setText(startDate.getShortDate());
        holder.city.setText(plan.getDestination());
        holder.itemView.setOnClickListener(v ->  new PlanDialog(authActivity , plan).show());
        if (plan.getPlan_creator().equals(AuthController.getUser())) {
            holder.groupRequest.setVisibility(View.VISIBLE);
            holder.requests.setOnClickListener(v -> {
                authActivity.startActivityForResult(new Intent(
                        authActivity, TLeaderRequestActivity.class)
                        .putExtra(TRAVEL_PLAN_ID, plan) , MainActivity.REQUEST_LEADER_REQUESTS);
            });
        }
        else
            holder.groupRequest.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return plans.length;
    }

    @Override
    public Plan[] getData() {
        return plans;
    }

    @Override
    public void setData(Plan[] data) {
        plans = data;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView city , startDate , requests;
        ViewGroup groupRequest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.tv_pvh_city);
            startDate = itemView.findViewById(R.id.tv_pvh_startdate);
            requests = itemView.findViewById(R.id.tv_pvh_requests);
            groupRequest = itemView.findViewById(R.id.ll_pvh_requests);
        }
    }
}
