package ir.blackswan.travelapp.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PassengerRequestsController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.databinding.ActivityUserRequestsBinding;
import ir.blackswan.travelapp.ui.Adapters.PassengerRequestRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserRequestActivity extends ToolbarActivity{

    private ActivityUserRequestsBinding binding;
    private PassengerRequestsController passengerRequestsController;
    private User[] confirmedUsers;
    private User[] pendingUsers;
    ArrayList<User> tourUsers;
    Intent intent;
    Tour tour;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActivityUserRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        passengerRequestsController = new PassengerRequestsController(this);
        setPendingUsersRecycler();
        binding.rscUserReq.loadingState();
        intent = getIntent();
        tour = (Tour) intent.getSerializableExtra("tour");
    }

    private void setRecyclers() {
        PassengerRequestRecyclerAdapter userRecyclerAdapter = new PassengerRequestRecyclerAdapter(tourUsers,
                new ArrayList<>(Arrays.asList(confirmedUsers)) ,this, tour);
        binding.rscUserReq.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        binding.rscUserReq.setAdapter(userRecyclerAdapter);
    }

    private void setPendingUsersRecycler() {

        passengerRequestsController.getPendingUsersFromServer(new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);

                Map<String, User[]> allPendingUsers_map = passengerRequestsController.getAllPendingUsers();
                pendingUsers = allPendingUsers_map.get(tour.getTour_id() + "");
                if (pendingUsers == null)
                    pendingUsers = new User[0];
                Log.d("Response", "onSuccess: " + Arrays.toString(pendingUsers));
                setConfirmedUsersRecycler();
            }
        });

    }

    private void setConfirmedUsersRecycler() {
        passengerRequestsController.getConfirmedUsersFromServer(new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                Map<String, User[]> allConfirmedUsers_map = passengerRequestsController.getAllConfirmedUsers();
                confirmedUsers = allConfirmedUsers_map.get(tour.getTour_id() + "");
                tourUsers = new ArrayList<>();
                if (confirmedUsers != null)
                    tourUsers.addAll(Arrays.asList(confirmedUsers));
                else
                    confirmedUsers = new User[0];
                if (pendingUsers != null)
                    tourUsers.addAll(Arrays.asList(pendingUsers));
                else
                    pendingUsers = new User[0];
                setRecyclers();
            }
        });
    }


}


