package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.Arrays;
import java.util.List;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.PopupMenuCreator;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.AuthActivity;
import kotlin.Unit;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthActivity authActivity;
    private static boolean tourLeader = true;
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

        binding.toggleHome.setOnSelectListener(themedButton -> {
            tourLeader = !tourLeader;
            setCurrentFragment();
            return Unit.INSTANCE;
        });

        reload();

        PowerMenuItem powerMenuItem = new PowerMenuItem("خروج");
        powerMenuItem.setIcon(R.drawable.ic_logout);
        List<PowerMenuItem> powerMenuItems = Arrays.asList(powerMenuItem);
        binding.ivHomeProfile.setOnClickListener(v -> {
            PowerMenu powerMenu = PopupMenuCreator.create(authActivity, powerMenuItems, v);
            powerMenu.showAsDropDown(v);
            powerMenu.setOnMenuItemClickListener((position, item) -> {
                powerMenu.dismiss();
                switch (position) {
                    case 0:
                        AuthController.logout(authActivity);
                        authActivity.showAuthDialog(() -> {
                            reload();
                        });
                        break;
                }
            });
        });



        return root;
    }

    public static boolean isTourLeader() {
        return tourLeader;
    }

    private void setCurrentFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (tourLeader) {
            ft.replace(R.id.fragment_container_home, homeFragmentLeader);
        } else {
            ft.replace(R.id.fragment_container_home, homeFragmentPassenger);
        }
        ft.commit();
    }

    public void reload() {
        if (!authActivity.isAuthing()) {
            setupWithUser();
            setCurrentFragment();
        }
    }


    private void setupWithUser() {
        User user = AuthController.getUser();
        if (user != null) {
            binding.ivHomeProfile.setUser(user);
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