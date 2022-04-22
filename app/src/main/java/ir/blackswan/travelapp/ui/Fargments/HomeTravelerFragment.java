package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.AuthActivity;

public class HomeTravelerFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthActivity authActivity;
    private TourController tourController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = ((AuthActivity) getActivity());
        tourController = new TourController(authActivity);



        return root;
    }


}