package ir.blackswan.travelapp.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PassengerRequestsController;
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
    List<User> tourUsers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        passengerRequestsController = new PassengerRequestsController(this);
        setPendingUsersRecycler();
    }

    private void setRecyclers() {
        PassengerRequestRecyclerAdapter userRecyclerAdapter = new PassengerRequestRecyclerAdapter(tourUsers, Arrays.asList(confirmedUsers) ,this);
        binding.rscUserReq.setAdapter(userRecyclerAdapter);
    }

    private void setPendingUsersRecycler() {

        passengerRequestsController.getPendingUsersFromServer(new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                //pendingUsers = passengerRequestsController.getPendingUsers();
                Map<String, User[]> allPendingUsers_map = passengerRequestsController.getAllPendingUsers();
                pendingUsers = allPendingUsers_map.get("0"); //todo edit the element 0
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
                confirmedUsers = allConfirmedUsers_map.get("0"); //todo edit the element 0

                tourUsers = Arrays.asList(confirmedUsers);
                tourUsers.addAll(Arrays.asList(pendingUsers));
                setRecyclers();
            }
        });
    }


}


