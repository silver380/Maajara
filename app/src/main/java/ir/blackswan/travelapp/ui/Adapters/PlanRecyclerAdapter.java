package ir.blackswan.travelapp.ui.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MyPersianCalender;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.ui.AuthActivity;

public class PlanRecyclerAdapter extends RecyclerView.Adapter<PlanRecyclerAdapter.ViewHolder> {

    AuthActivity authActivity;
    Plan[] plans;

    public PlanRecyclerAdapter(AuthActivity authActivity, Plan[] plans) {
        this.authActivity = authActivity;
        this.plans = plans;
    }

    @NonNull
    @Override
    public PlanRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = authActivity.getLayoutInflater().inflate(R.layout.travel_plan_view_holder , parent , false);
        view.getLayoutParams().width = Utils.getScreenWidth() * 30 / 100;
        return new PlanRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanRecyclerAdapter.ViewHolder holder, int position) {
        Plan plan = plans[position];
        MyPersianCalender startDate = plan.getPersianStartDate();

        holder.startDate.setText(startDate.getShortDate());
        holder.city.setText(plan.getDestination());
    }

    @Override
    public int getItemCount() {
        return plans.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView city , startDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.tv_pvh_city);
            startDate = itemView.findViewById(R.id.tv_pvh_startdate);
        }
    }
}
