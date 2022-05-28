package ir.blackswan.travelapp.ui.Adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PassengerRequestsController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.ProfileImageView;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PassengerRequestRecyclerAdapter extends RecyclerView.Adapter<PassengerRequestRecyclerAdapter.ViewHolder>
        implements HasArray<User> {
    ArrayList<User> allRequestedUsers;
    ArrayList<User> confirmedUsers;
    AuthActivity activity;
    private PassengerRequestsController passengerRequestsController;
    Tour tour;

    public PassengerRequestRecyclerAdapter(ArrayList<User> req_of_users, ArrayList<User> confirmedUsers, AuthActivity activity, Tour tour) {
        this.allRequestedUsers = req_of_users;
        this.confirmedUsers = confirmedUsers;
        this.activity = activity;
        this.tour = tour;
        passengerRequestsController = new PassengerRequestsController(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.user_req_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = allRequestedUsers.get(position);
        holder.userName_Lastname.setText(user.getNameAndLastname());
        holder.userImage.setDataByUser(user);
        if (confirmedUsers.contains(user))
            acceptUser(holder);
        else if (tour.isFull())
            disable(holder);
        else
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    passengerRequestsController.addAcceptedUserToServer(user, tour.getTour_id(), new OnResponseDialog(activity) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                            super.onSuccess(call, callback, response);
                            acceptUser(holder);
                            confirmedUsers.add(user);
                            //todo: current confirmed update
                            activity.setResult(Activity.RESULT_OK);
                        }
                    });

                }
            });
    }

    private void acceptUser(ViewHolder holder) {
        holder.accept.setText("تایید شده");
        holder.accept.setRippleColor(ColorStateList.valueOf(activity.getColor(R.color.colorTransparent)));
        holder.accept.setClickable(false);
        holder.accept.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.colorSuccess)));
    }

    private void disable(ViewHolder holder) {
        holder.accept.setText("ظرفیت تکمیل");
        holder.accept.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return allRequestedUsers.size();
    }

    @Override
    public User[] getData() {
        return allRequestedUsers.toArray(new User[0]);
    }

    @Override
    public void setData(User[] data) {
        allRequestedUsers = new ArrayList<>(Arrays.asList(data));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProfileImageView userImage;
        TextView userName_Lastname;
        MaterialButton accept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_iv);
            userName_Lastname = itemView.findViewById(R.id.user_Name_LastName);
            accept = itemView.findViewById(R.id.btn_rvh_accept);
        }
    }
}