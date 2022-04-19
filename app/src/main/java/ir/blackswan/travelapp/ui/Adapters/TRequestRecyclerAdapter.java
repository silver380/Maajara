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

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.WebImageView;

public class TRequestRecyclerAdapter extends RecyclerView.Adapter<TRequestRecyclerAdapter.ViewHolder> {
    List<User> req_of_users;
    Activity activity;

    public TRequestRecyclerAdapter(List<User> req_of_users, Activity activity) {
        this.req_of_users = req_of_users;
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
        User user = req_of_users.get(position);
        holder.userImage.setImagePath(user.getProfilePicturePath());
        holder.userName_Lastname.setText(user.getNameAndLastname());
        //todo: check user accepted or not
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: request for accept
                holder.accept.setText("تایید شده");
                holder.accept.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.colorSuccess)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return req_of_users.size();
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
