package ir.blackswan.travelapp.ui.Fargments;

import static ir.blackswan.travelapp.Utils.SharedPrefManager.IS_TOUR_LEADER;
import static ir.blackswan.travelapp.Utils.Utils.dp2px;

import android.annotation.SuppressLint;
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
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.PopupMenuCreator;
import ir.blackswan.travelapp.Utils.SharedPrefManager;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.Activities.IntroActivity;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Activities.SettingActivity;
import ir.blackswan.travelapp.ui.Dialogs.AddTicket;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeFragment extends RefreshingFragment {


    private FragmentHomeBinding binding;
    private MainActivity mainActivity;
    private static boolean tourLeader = true;
    private HomeFragmentLeader homeFragmentLeader;
    private HomeFragmentPassenger homeFragmentPassenger;
    SharedPrefManager sharedPrefManager;
    PowerMenuItem switchPowerMenu;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        init(root);
        mainActivity = ((MainActivity) getActivity());

        sharedPrefManager = new SharedPrefManager(mainActivity);

        homeFragmentPassenger = new HomeFragmentPassenger(mainActivity);
        homeFragmentLeader = new HomeFragmentLeader(mainActivity);


        setListeners();

        reload();


        return root;
    }

    private void setListeners() {

        binding.groupHomeTicket.setOnClickListener(v -> new AddTicket(mainActivity, this::updateTicketView).show());
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
        if (!mainActivity.isAuthing()) {
            setupWithUser();
            setCurrentFragment();
            Log.d("TAG", "reload: ");
        }
    }


    public void refresh() {
        mainActivity.getAuthController().completeUserInfo(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                updateTicketView();
            }
        });
        if (tourLeader)
            homeFragmentLeader.reload();
        else
            homeFragmentPassenger.reload();
    }

    @SuppressLint("SetTextI18n")
    public void updateTicketView() {
        if (!tourLeader)
            binding.groupHomeTicket.setVisibility(View.GONE);
        else {
            binding.groupHomeTicket.setVisibility(View.VISIBLE);
            binding.tvHomeTicket.setText(AuthController.getUser().getNumber_of_tickets() + "");
        }
    }

    private void setupWithUser() {
        User user = AuthController.getUser();
        tourLeader = sharedPrefManager.getBoolean(IS_TOUR_LEADER, true);
        if (user != null) {
            binding.ivHomeProfile.setDataByUser(user);
            binding.tvHomeName.setText(user.getNameAndLastname());
            if (user.is_tour_leader()) {
                updateTicketView();
                binding.groupHomeTicket.setOnClickListener(v -> new AddTicket(mainActivity, this::updateTicketView).show());
            } else {
                ((ViewGroup) binding.tvHomeTicket.getParent()).setVisibility(View.GONE);
                tourLeader = false;
                saveToggle();
            }
            updateToggleTv();
            setSwitch();
            setPopupMenu();
        }
    }

    private void updateToggleTv() {
        if (tourLeader)
            binding.tvHomeMode.setText("راهنمای سفر");
        else
            binding.tvHomeMode.setText("مسافر");
    }

    private void setSwitch() {
        if (switchPowerMenu == null) {
            switchPowerMenu = new PowerMenuItem("");
            switchPowerMenu.setTag(2);
        }
        if (tourLeader) {
            switchPowerMenu.setTitle("تغییر به مسافر");
            switchPowerMenu.setIcon(R.drawable.ic_padding_user);
        } else {
            switchPowerMenu.setTitle("تغییر به راهنما");
            switchPowerMenu.setIcon(R.drawable.ic_padding_guide);
        }

    }

    private void saveToggle() {
        sharedPrefManager.putBoolean(IS_TOUR_LEADER, tourLeader);
    }

    private void switchAccount() {
        tourLeader = !tourLeader;
        updateTicketView();
        saveToggle();
        setCurrentFragment();
        setSwitch();
        updateToggleTv();
    }

    private void setPopupMenu() {
        PowerMenuItem exitMenu = new PowerMenuItem("خروج");
        exitMenu.setIcon(R.drawable.ic_logout_padding);
        exitMenu.setTag(0);
        PowerMenuItem pmiSetting = new PowerMenuItem("تنظیمات");
        pmiSetting.setIcon(R.drawable.ic_setting);
        pmiSetting.setTag(1);

        List<PowerMenuItem> powerMenuItems;
        if (AuthController.getUser().is_tour_leader()) {
            powerMenuItems = Arrays.asList(switchPowerMenu, pmiSetting, exitMenu);
            setSwitch();
        } else
            powerMenuItems = Arrays.asList(pmiSetting, exitMenu);

        binding.ivHomeProfile.setOnClickListener(v -> {
            PowerMenu powerMenu = PopupMenuCreator.create(mainActivity, powerMenuItems, v);
            powerMenu.setWidth(dp2px(mainActivity, 250));
            powerMenu.showAsDropDown(v);
            powerMenu.setOnMenuItemClickListener((position, item) -> {
                powerMenu.dismiss();
                switch ((int) item.getTag()) {
                    case 2:
                        switchAccount();
                        break;
                    case 1:
                        mainActivity.startActivityForResult(new Intent(mainActivity, SettingActivity.class), MainActivity.REQUEST_SETTING);
                        break;
                    case 0:
                        AuthController.logout(mainActivity);
                        mainActivity.finish();
                        startActivity(new Intent(mainActivity, IntroActivity.class));
                        break;
                }
            });
        });
    }

}