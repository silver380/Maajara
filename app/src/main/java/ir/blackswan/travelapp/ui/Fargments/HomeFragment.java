package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthActivity authActivity;
    private TourController tourController;
    private Tour[] createdTours;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = ((AuthActivity) getActivity());

        binding.toggleHome.selectButton(R.id.btn_home_guide);

        binding.rclCreatedTour.setLayoutManager(new LinearLayoutManager(authActivity,
                LinearLayoutManager.HORIZONTAL, false));

        tourController = new TourController(authActivity);
        reload();

        return root;
    }

    public void reload() {
        if (!AuthController.isLoadingUser()) {
            setupWithUser();
            setCreatedToursRecycler();
        }
    }

    private void setRecyclers() {
        TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity(), createdTours);
        binding.rclCreatedTour.setAdapter(tourRecyclerAdapter);
    }

    private void setCreatedToursRecycler() {

        tourController.getCreatedTourFromServer(new OnResponseDialog(authActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                createdTours = tourController.getCreatedTours();
                setRecyclers();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void setupWithUser() {
        User user = AuthController.getUser();
        if (user != null && user.getFirst_name() != null) {
            binding.profileImageView.setUser(user);
        }
    }

    public void invisibleToggle() {
        binding.toggleHome.setVisibility(View.INVISIBLE);
    }


}