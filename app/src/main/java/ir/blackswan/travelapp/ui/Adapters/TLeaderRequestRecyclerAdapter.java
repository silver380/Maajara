package ir.blackswan.travelapp.ui.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourLeaderRequestController;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.ProfileImageView;
import ir.blackswan.travelapp.Views.TourLeaderVerticalView;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class TLeaderRequestRecyclerAdapter extends RecyclerView.Adapter<TLeaderRequestRecyclerAdapter.ViewHolder>
implements HasArray<PlanRequest>{

    PlanRequest[] allTourLeaders_req;
    User confirmedTourLeader;
    AuthActivity activity;
    RecyclerView recyclerView;


    public TLeaderRequestRecyclerAdapter(PlanRequest[] allTourLeaders_req, User confirmed_tLeaders, AuthActivity activity) {
        this.confirmedTourLeader = confirmed_tLeaders;
        this.allTourLeaders_req = allTourLeaders_req;
        this.activity = activity;
        setConfirmedToTop();
    }

    private void setConfirmedToTop() {
        if (confirmedTourLeader == null)
            return;
        ArrayList<PlanRequest> planRequests = new ArrayList<>(Arrays.asList(allTourLeaders_req));
        PlanRequest confirmedRequest = new PlanRequest(confirmedTourLeader , allTourLeaders_req[0].getTravel_plan());
        planRequests.remove(confirmedRequest);
        planRequests.add(0 , confirmedRequest);
        allTourLeaders_req = planRequests.toArray(new PlanRequest[0]);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.tl_request_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanRequest tourLeader_req = allTourLeaders_req[position];
        holder.leaderImageView.setDataByUser(tourLeader_req.getTour_leader());
        holder.userName_Lastname.setText(tourLeader_req.getTour_leader().getFullNameWithPrefix());
        holder.biography.setText(tourLeader_req.getTour_leader().getBiography());
        holder.price.setText(tourLeader_req.getSuggested_priceString());
        holder.avg.setText(tourLeader_req.getTour_leader().getAvg_rate() + "");

        TourLeaderVerticalView.setContactWays(tourLeader_req.getTour_leader() , holder.telegram ,
                holder.whatsapp , holder.phone , holder.mail);

        if (confirmedTourLeader != null) {
            if (confirmedTourLeader.equals(tourLeader_req.getTour_leader())) {
                acceptTourLeader(holder);
                holder.price.setText(tourLeader_req.getTravel_plan().getAccepted_price());
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
                                confirmedTourLeader = tourLeader_req.getTour_leader();
                                recyclerView.setAdapter(TLeaderRequestRecyclerAdapter.this);
                                setConfirmedToTop();
                                activity.setResult(Activity.RESULT_OK);
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

    @Override
    public PlanRequest[] getData() {
        return allTourLeaders_req;
    }

    @Override
    public void setData(PlanRequest[] data) {
        allTourLeaders_req = data;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ProfileImageView leaderImageView;
        TextView userName_Lastname;
        TextView biography;
        TextView price;
        TextView avg;
        MaterialButton accept;
        ImageView telegram , whatsapp , phone , mail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leaderImageView = itemView.findViewById(R.id.iv_tl);
            userName_Lastname = itemView.findViewById(R.id.tv_tl_name);
            biography = itemView.findViewById(R.id.bio_tv);
            price = itemView.findViewById(R.id.price_tv);
            accept = itemView.findViewById(R.id.confirm_button_tl);
            telegram = itemView.findViewById(R.id.tel_iv);
            whatsapp = itemView.findViewById(R.id.w_app_iv);
            phone = itemView.findViewById(R.id.phone_iv);
            mail = itemView.findViewById(R.id.e_mail_iv);
            avg = itemView.findViewById(R.id.pointsOf_tl_tv);
        }
    }

}
