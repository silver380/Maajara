package ir.blackswan.travelapp.ui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.OnResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.ActivityMainBinding;
import ir.blackswan.travelapp.ui.Fargments.HomeFragment;
import ir.blackswan.travelapp.ui.Fargments.RefreshingFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AuthActivity {

    private ActivityMainBinding binding;
    public static final int REQUEST_SETTING = 0, REQUEST_LEADER_REQUESTS = 1, REQUEST_TOUR_PAGE = 2;
    private HomeFragment homeFragment;

    NavHostFragment navHostFragment;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utils.changeStatusColorResource(this, R.color.colorMainStatusBar);

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);

        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);


        homeFragment = (HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);

//startActivity(new Intent(this, MapsActivity.class));


    }

    private void setFakeData() { //todo: After debug and test remove this
        TourController tourController = new TourController(this);
        PlanController planController = new PlanController(this);
        for (int i = 0; i < 10; i++) {
            Place[] place = new Place[]{new Place(1)};
            Tour tour = new Tour("تست " + i , 2 , 20000 , "شهر تست " + i
            , "2022-10-10" , "2022-10-20" ,place  , "None"
             , true , true , true , "None");
            tourController.addTourToServer(tour, new OnResponse() {
                @Override
                public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                }

                @Override
                public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                }
            });
            Plan plan = new Plan("شهر تست " + i , "2022-10-12" , "2022-10-20" ,
                    new ArrayList<>() , place);
            planController.addPlanToServer(plan, new OnResponse() {
                @Override
                public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                }

                @Override
                public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TLeaderRecycler", "onActivityResult: " + requestCode + "RESULT: " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SETTING) {
                homeFragment.reload();
            } else if (requestCode == REQUEST_LEADER_REQUESTS || requestCode == REQUEST_TOUR_PAGE) {
                homeFragment.refresh();
                List<Fragment> childFragments = navHostFragment.getChildFragmentManager().getFragments();
                for (Fragment frag : childFragments) {
                    if (frag instanceof RefreshingFragment) {
                        ((RefreshingFragment) frag).refresh();
                        Log.d("TLeaderRecycler", "REFRESHED " + frag);
                    }
                }

            }
        }
    }


    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void resetTicket() {
        homeFragment.updateTicketView();
    }

    public void navigateToId(int id) {
        binding.navView.setSelectedItemId(id);
    }
}
