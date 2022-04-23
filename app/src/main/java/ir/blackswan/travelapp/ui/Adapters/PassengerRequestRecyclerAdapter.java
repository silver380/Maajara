package ir.blackswan.travelapp.ui.Adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.blackswan.travelapp.Controller.PassengerRequestsController;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.WebImageView;

public class PassengerRequestRecyclerAdapter extends RecyclerView.Adapter<PassengerRequestRecyclerAdapter.ViewHolder> {
    List<User> allRequestedUsers;
    List<User> confirmedUsers;
    Activity activity;
    private PassengerRequestsController passengerRequestsController;

    public PassengerRequestRecyclerAdapter(List<User> req_of_users, List<User> confirmedUsers, Activity activity) {
        this.allRequestedUsers = req_of_users;
        this.confirmedUsers = confirmedUsers;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.user_req_view_holder , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = allRequestedUsers.get(position);
        holder.userImage.setImagePath(user.getProfilePicturePath());
        holder.userName_Lastname.setText(user.getNameAndLastname());
        if(confirmedUsers.contains(user))
            acceptUser(holder, user);
        else
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo: request for accept

                    passengerRequestsController.addAcceptedUserToServer();
                    onSuccess {
                        confirmedUsers.add(user);
                        acceptUser(holder, user);
                    }
                }
            });
    }

    private void acceptUser(ViewHolder holder , User user){
        holder.accept.setText("تایید شده");
        holder.accept.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.colorSuccess)));

    }

    @Override
    public int getItemCount() {
        return allRequestedUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        WebImageView userImage;
        TextView userName_Lastname;
        Button accept;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_iv);
            userName_Lastname = itemView.findViewById(R.id.user_Name_LastName);
            accept = itemView.findViewById(R.id.btn_rvh_accept);
        }
    }
}