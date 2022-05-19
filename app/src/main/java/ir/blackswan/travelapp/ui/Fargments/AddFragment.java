package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.FragmentAddBinding;


public class AddFragment extends Fragment {

    private AddPlanFragment addPlanFragment;
    private AddTourFragment addTourFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FragmentAddBinding binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        addPlanFragment = new AddPlanFragment();
        addTourFragment = new AddTourFragment();

        setCurrentFragment();

        return root;
    }

    private void setCurrentFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();

        if (HomeFragment.isTourLeader())
            ft.replace(R.id.fragment_container_add, addTourFragment);
        else
            ft.replace(R.id.fragment_container_add, addPlanFragment);

        ft.commit();
    }


}