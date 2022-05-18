package ir.blackswan.travelapp.ui.Adapters;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourLeaderRequestController;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.ProfileImageView;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class TLeaderRequestRecyclerAdapter extends RecyclerView.Adapter<TLeaderRequestRecyclerAdapter.ViewHolder> {

    PlanRequest[] allTourLeaders_req;
    User confirmedTourLeaders;
    AuthActivity activity;


    public TLeaderRequestRecyclerAdapter(PlanRequest[] allTourLeaders_req, User confirmed_tLeaders, AuthActivity activity) {
        this.confirmedTourLeaders = confirmed_tLeaders;
        this.allTourLeaders_req = allTourLeaders_req;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.tl_request_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanRequest tourLeader_req = allTourLeaders_req[position];
        holder.leaderImageView.setDataByUser(tourLeader_req.getTour_leader());
        holder.userName_Lastname.setText(tourLeader_req.getTour_leader().getNameAndLastname());
        holder.biography.setText(tourLeader_req.getTour_leader().getBiography());
        holder.price.setText(tourLeader_req.getSuggested_price());
        if (confirmedTourLeaders != null) {
            if (confirmedTourLeaders.equals(tourLeader_req.getTour_leader())) {
                acceptTourLeader(holder);
            } else {
                holder.accept.setEnabled(false);
            }

        } else {
            holder.accept.setOnClickListener(view -> {
                TourLeaderRequestController tourLeaderRequestController = new TourLeaderRequestController(activity);
                tourLeaderRequestController.acceptLeader(tourLeader_req.getTour_leader().getUser_id()
                        , tourLeader_req.getTravel_plan().getTravel_plan_id(), new OnResponseDialog(activity) {
                            @Override
                            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                                super.onSuccess(call, callback, response);
                                tourLeaderRequestController.getPendingTLRequestsFromServer(new OnResponseDialog(activity){
                                    @Override
                                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                                        super.onSuccess(call, callback, response);
                                        allTourLeaders_req = TourLeaderRequestController.getMap_planRequests().get(
                                                tourLeader_req.getTravel_plan().getTravel_plan_id() + "");
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        });
            });
        }
    }

    @Override
    public int getItemCount() {
        return allTourLeaders_req.length;
    }

    private void acceptTourLeader(ViewHolder holder) {
        holder.accept.setText("تایید شده");
        holder.accept.setRippleColor(ColorStateList.valueOf(activity.getColor(R.color.colorTransparent)));
        holder.accept.setClickable(false);
        holder.accept.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.colorSuccess)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProfileImageView leaderImageView;
        TextView userName_Lastname;
        TextView biography;
        TextView price;
        MaterialButton accept;

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
