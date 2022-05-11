package ir.blackswan.travelapp.ui.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MyPersianCalender;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.PlanDialog;

public class PlanRecyclerAdapter extends RecyclerView.Adapter<PlanRecyclerAdapter.ViewHolder> {

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
        else {
            int marginH = Utils.dp2px(authActivity , authActivity.getResources().getDimension(R.dimen.margin_medium));
            int marginV = Utils.dp2px(authActivity , authActivity.getResources().getDimension(R.dimen.margin_small));

            ((ViewGroup.MarginLayoutParams)view.getLayoutParams()).setMargins(marginH , marginV , marginH , marginV);
        }


        return new PlanRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanRecyclerAdapter.ViewHolder holder, int position) {
        Plan plan = plans[position];
        MyPersianCalender startDate = plan.getPersianStartDate();

        holder.startDate.setText(startDate.getShortDate());
        holder.city.setText(plan.getDestination());
        PlanDialog planDialog = new PlanDialog(authActivity , plan);
        holder.itemView.setOnClickListener(v -> planDialog.show());
        holder.requests.setOnClickListener(v->{
            //todo: start TourLeaderRequests activity
        });
    }

    @Override
    public int getItemCount() {
        return plans.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView city , startDate , requests;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.tv_pvh_city);
            startDate = itemView.findViewById(R.id.tv_pvh_startdate);
            requests = itemView.findViewById(R.id.tv_pvh_requests);
        }
    }
}
