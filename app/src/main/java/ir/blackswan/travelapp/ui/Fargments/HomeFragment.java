package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.AuthActivity;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthActivity authActivity;
    private boolean tourLeader = true;
    private HomeFragmentLeader homeFragmentLeader;
    private HomeFragmentPassenger homeFragmentPassenger;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = ((AuthActivity) getActivity());
        homeFragmentPassenger = new HomeFragmentPassenger(authActivity);
        homeFragmentLeader = new HomeFragmentLeader(authActivity);

        binding.toggleHome.selectButton(R.id.btn_home_guide);

        binding.toggleHome.setOnSelectListener(new Function1<ThemedButton, Unit>() {
            @Override
            public Unit invoke(ThemedButton themedButton) {
                tourLeader = !tourLeader;
                setCurrentFragment();
                return Unit.INSTANCE;
            }
        });

        setCurrentFragment();

        return root;
    }

    private void setCurrentFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (tourLeader) {
            ft.replace(R.id.fragment_container_home, homeFragmentLeader);
            homeFragmentLeader.reload();
        } else {
            ft.replace(R.id.fragment_container_home, homeFragmentPassenger);
            homeFragmentPassenger.reload();
        }
        ft.commit();
    }

    public void reload() {
        if (!AuthController.isLoadingUser()) {
            setupWithUser();
        }
    }


    private void setupWithUser() {
        User user = AuthController.getUser();
        if (user != null) {
            binding.profileImageView.setUser(user);
            if (user.is_tour_leader()) {
                tourLeader = true;
                binding.toggleHome.setVisibility(View.VISIBLE);
            } else {
                tourLeader = false;
                binding.toggleHome.setVisibility(View.GONE);
            }
            setCurrentFragment();

        }
    }


}