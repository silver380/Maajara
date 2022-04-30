package ir.blackswan.travelapp.ui.Fargments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.databinding.FragmentAddBinding;
import ir.blackswan.travelapp.ui.AddTourActivity;
import ir.blackswan.travelapp.ui.MainActivity;


public class AddFragment extends Fragment {

    private FragmentAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.testBtnAddTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity() , AddTourActivity.class));
            }
        });


        if (!((MainActivity)getActivity()).getHomeFragment().isTourLeader() ) {
            binding.testBtnAddTour.setVisibility(View.GONE);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}