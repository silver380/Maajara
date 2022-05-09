package ir.blackswan.travelapp.ui.Adapters;

import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.ProfileImageView;
import ir.blackswan.travelapp.ui.AuthActivity;

public class TLeaderRequestRecyclerAdapter extends RecyclerView.Adapter<TLeaderRequestRecyclerAdapter.ViewHolder> {

    ArrayList<PlanRequest> allTourLeaders;
    ArrayList<PlanRequest> confirmedTourLeaders;
    AuthActivity activity;
    int plan_id;

    public TLeaderRequestRecyclerAdapter(ArrayList<PlanRequest> allTourLeaders, ArrayList<PlanRequest> confirmed_tLeaders, AuthActivity activity, int plan_id) {
        this.confirmedTourLeaders = confirmed_tLeaders;
        this.allTourLeaders = allTourLeaders;
        this.activity = activity;
        this.plan_id = plan_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.tl_request_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanRequest tourLeader_req = allTourLeaders.get(position);
        holder.leaderImageView.setUser(tourLeader_req.getTour_leader());
        holder.userName_Lastname.setText(tourLeader_req.getTour_leader().getNameAndLastname());
        holder.biography.setText(tourLeader_req.getTour_leader().getBiography());
        holder.price.setText(tourLeader_req.getPrice());
        if(confirmedTourLeaders.contains(tourLeader_req)){
            acceptTourLeader(holder);
        } else {
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return allTourLeaders.size();
    }

    private void acceptTourLeader(ViewHolder holder) {
        holder.accept.setText("تایید شده");
        holder.accept.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.colorSuccess)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProfileImageView leaderImageView;
        TextView userName_Lastname;
        TextView biography;
        TextView price;
        Button accept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leaderImageView = itemView.findViewById(R.id.iv_tl);
            userName_Lastname = itemView.findViewById(R.id.tv_tl_name);
            biography = itemView.findViewById(R.id.bio_tv);
            price = itemView.findViewById(R.id.price_tv);
            accept = itemView.findViewById(R.id.confirm_button_tl);
        }
    }

}
