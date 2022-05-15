package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.FragmentAddBinding;


public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private AddPlanFragment addPlanFragment;
    private AddTourFragment addTourFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        addPlanFragment = new AddPlanFragment();
        addTourFragment = new AddTourFragment();

        setCurrentFragment();

        return root;
    }

    private void setCurrentFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();

        if (HomeFragment.isTourLeader())
            ft.replace(R.id.nav_host, addTourFragment);
        else
            ft.replace(R.id.nav_host, addPlanFragment);

        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}