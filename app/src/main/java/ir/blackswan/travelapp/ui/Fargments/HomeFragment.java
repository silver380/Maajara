package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.OnResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthActivity authActivity;
    private TourController tourController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = ((AuthActivity) getActivity());

        binding.toggleHome.selectButton(R.id.btn_home_passenger);

        binding.rclCreatedTour.setLayoutManager(new LinearLayoutManager(authActivity,
                LinearLayoutManager.HORIZONTAL, false));

        tourController = new TourController(authActivity);
        setupWithUser();
        setupRecyclers();


        return root;
    }

    private void setupRecyclers() {
        Tour[] createdTours = tourController.getCreatedTours();
        Log.d("TourRecycler", "setupRecyclers: " + createdTours);
        if (createdTours != null) {
            TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity(), createdTours);
            binding.rclCreatedTour.setAdapter(tourRecyclerAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void setupWithUser() {
        User user = AuthController.getUser();
        if (user != null && user.getFirst_name() != null) {
            binding.profileImageView.setUser(user);
            tourController.getCreatedTourFromServer(new OnResponse() {
                @Override
                public void onSuccess(String responseBody) {
                    setupRecyclers();
                }

                @Override
                public void onFailed(String message) {

                }
            });
        }
    }

    public void invisibleToggle() {
        binding.toggleHome.setVisibility(View.INVISIBLE);
    }


}