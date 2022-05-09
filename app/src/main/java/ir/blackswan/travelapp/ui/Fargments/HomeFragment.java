package ir.blackswan.travelapp.ui.Fargments;

import static ir.blackswan.travelapp.Utils.SharedPrefManager.IS_TOUR_LEADER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.PopupMenuCreator;
import ir.blackswan.travelapp.Utils.SharedPrefManager;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.SettingActivity;
import kotlin.Unit;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthActivity authActivity;
    private static boolean tourLeader = true;
    private HomeFragmentLeader homeFragmentLeader;
    private HomeFragmentPassenger homeFragmentPassenger;
    SharedPrefManager sharedPrefManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        authActivity = ((AuthActivity) getActivity());

        sharedPrefManager = new SharedPrefManager(authActivity);

        homeFragmentPassenger = new HomeFragmentPassenger(authActivity);
        homeFragmentLeader = new HomeFragmentLeader(authActivity);


        binding.toggleHome.setOnSelectListener(themedButton -> {
            boolean tourLeader2 = binding.toggleHome.getSelectedButtons()
                    .contains(binding.btnHomeGuide);
            if (tourLeader2 == tourLeader)
                return Unit.INSTANCE;

            tourLeader = !tourLeader;
            saveToggle();
            setCurrentFragment();
            return Unit.INSTANCE;
        });

        reload();

        PowerMenuItem powerMenuItem = new PowerMenuItem("خروج");
        powerMenuItem.setIcon(R.drawable.ic_logout_padding);
        PowerMenuItem pmiSetting = new PowerMenuItem("تنظیمات");
        pmiSetting.setIcon(R.drawable.ic_logout_padding);
        List<PowerMenuItem> powerMenuItems = Arrays.asList(pmiSetting, powerMenuItem);
        binding.ivHomeProfile.setOnClickListener(v -> {
            PowerMenu powerMenu = PopupMenuCreator.create(authActivity, powerMenuItems, v);
            powerMenu.showAsDropDown(v);
            powerMenu.setOnMenuItemClickListener((position, item) -> {
                powerMenu.dismiss();
                switch (position) {
                    case 1:
                        AuthController.logout(authActivity);
                        authActivity.showAuthDialog(() -> {
                            reload();
                        });
                        break;
                    case 0:
                        startActivity(new Intent(authActivity, SettingActivity.class));
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
        tourLeader = sharedPrefManager.getBoolean(IS_TOUR_LEADER , true);
        if (user != null) {
            binding.ivHomeProfile.setUser(user);
            if (user.is_tour_leader()) {
                binding.toggleHome.setVisibility(View.VISIBLE);
            } else {
                tourLeader = false;
                saveToggle();
                binding.toggleHome.setVisibility(View.GONE);
            }
            setToggle();
        }
    }

    private void setToggle(){
        Log.d("setToggle", "setToggle: " + tourLeader);
        if (tourLeader)
            binding.toggleHome.selectButton(R.id.btn_home_guide);
        else
            binding.toggleHome.selectButton(R.id.btn_home_passenger);

    }

    private void saveToggle(){
        sharedPrefManager.putBoolean(IS_TOUR_LEADER, tourLeader);
    }


}