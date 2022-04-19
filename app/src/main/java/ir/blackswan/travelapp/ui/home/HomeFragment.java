package ir.blackswan.travelapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;

import ir.blackswan.travelapp.Controller.OnResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.FakeData;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthActivity authActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = ((AuthActivity) getActivity());

        binding.toggleHome.selectButton(R.id.btn_home_passenger);

        binding.rclCreatedTour.setLayoutManager(new LinearLayoutManager(authActivity,
                LinearLayoutManager.HORIZONTAL, false));

        TourController tourController = new TourController(authActivity);


        TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity(), FakeData.getFakeTours().toArray(
                new Tour[FakeData.getFakeTours().size()]
        ));
        binding.rclCreatedTour.setAdapter(tourRecyclerAdapter);

        /*
        tourController.getCreatedTour(new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
            }

            @Override
            public void onFailed(String message) {

            }
        });

         */
        /*


         */

        //    loadUser();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void setupWithUser(User user) {
        binding.pivHomeProfile.setUser(user);
    }

    public void invisibleToggle() {
        binding.toggleHome.setVisibility(View.INVISIBLE);
    }

}