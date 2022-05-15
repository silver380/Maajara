package ir.blackswan.travelapp.ui.Fargments;

import static ir.blackswan.travelapp.Utils.SharedPrefManager.IS_TOUR_LEADER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.Arrays;
import java.util.List;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.PopupMenuCreator;
import ir.blackswan.travelapp.Utils.SharedPrefManager;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.Activities.IntroActivity;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Activities.SettingActivity;
import kotlin.Unit;

public class HomeFragment extends RefreshingFragment {


    private FragmentHomeBinding binding;
    private MainActivity mainActivity;
    private static boolean tourLeader = true;
    private HomeFragmentLeader homeFragmentLeader;
    private HomeFragmentPassenger homeFragmentPassenger;
    SharedPrefManager sharedPrefManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        init(root);
        mainActivity = ((MainActivity) getActivity());

        sharedPrefManager = new SharedPrefManager(mainActivity);

        homeFragmentPassenger = new HomeFragmentPassenger(mainActivity);
        homeFragmentLeader = new HomeFragmentLeader(mainActivity);


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

        setPopupMenu();



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
        Log.d("TAG", "reload: ");
        if (!mainActivity.isAuthing()) {
            setupWithUser();
            setCurrentFragment();
        }
    }



    public void refresh(){
        if (tourLeader)
            homeFragmentLeader.reload();
        else
            homeFragmentPassenger.reload();
    }


    private void setupWithUser() {
        User user = AuthController.getUser();
        tourLeader = sharedPrefManager.getBoolean(IS_TOUR_LEADER, true);
        if (user != null) {
            binding.ivHomeProfile.setDataByUser(user);
            if (user.is_tour_leader()) {
                binding.tvHomeTicket.setText(user.getTicket() + "");
                binding.toggleHome.setVisibility(View.VISIBLE);
            } else {
                ((ViewGroup)binding.tvHomeTicket.getParent()).setVisibility(View.GONE);
                tourLeader = false;
                saveToggle();
                binding.toggleHome.setVisibility(View.GONE);
            }
            setToggle();
        }
    }

    private void setToggle() {
        Log.d("setToggle", "setToggle: " + tourLeader);
        if (tourLeader)
            binding.toggleHome.selectButton(R.id.btn_home_guide);
        else
            binding.toggleHome.selectButton(R.id.btn_home_passenger);

    }

    private void saveToggle() {
        sharedPrefManager.putBoolean(IS_TOUR_LEADER, tourLeader);
    }


    private void setPopupMenu() {
        PowerMenuItem powerMenuItem = new PowerMenuItem("خروج");
        powerMenuItem.setIcon(R.drawable.ic_logout_padding);
        PowerMenuItem pmiSetting = new PowerMenuItem("تنظیمات");
        pmiSetting.setIcon(R.drawable.ic_setting);
        List<PowerMenuItem> powerMenuItems = Arrays.asList(pmiSetting, powerMenuItem);
        binding.ivHomeProfile.setOnClickListener(v -> {
            PowerMenu powerMenu = PopupMenuCreator.create(mainActivity, powerMenuItems, v);
            powerMenu.showAsDropDown(v);
            powerMenu.setOnMenuItemClickListener((position, item) -> {
                powerMenu.dismiss();
                switch (position) {
                    case 0:
                        mainActivity.startActivityForResult(new Intent(mainActivity, SettingActivity.class) , MainActivity.REQUEST_SETTING);
                        break;
                    case 1:
                        AuthController.logout(mainActivity);
                        mainActivity.finish();
                        startActivity(new Intent(mainActivity, IntroActivity.class));
                        break;
                }
            });
        });
    }

}