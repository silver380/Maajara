package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.Arrays;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.CustomGridLayoutManager;
import ir.blackswan.travelapp.databinding.FragmentHomeLeaderBinding;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Adapters.PlanRecyclerAdapter;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeFragmentLeader extends Fragment {
    FragmentHomeLeaderBinding binding;
    MainActivity mainActivity;
    private TourController tourController;
    private PlanController planController;
    private Tour[] createdTours;
    private PlanRequest[] pendingPlans;
    private PlanRequest[] confirmedPlans;
    private Plan[] allPlans;

    public HomeFragmentLeader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        tourController = new TourController(mainActivity);
        planController = new PlanController(mainActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeLeaderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.rclCreatedTour.setLayoutManager( new CustomGridLayoutManager(mainActivity, 2));
        binding.rclPendingPlans.setLayoutManager( new CustomGridLayoutManager(mainActivity, 2));
        binding.rclConfirmedPlans.setLayoutManager( new CustomGridLayoutManager(mainActivity, 2));
        binding.rclHomePlans.setLayoutManager(new CustomGridLayoutManager(mainActivity , 2));

        setListeners();
        reload();

        return root;
    }

    private void setListeners() {
        binding.btnHomeGotoSearch.setOnClickListener(v ->
                mainActivity.navigateToId(R.id.navigation_search));
    }

    public void reload() {
        setCreatedToursRecycler();
    }


    private void setCreatedToursRecycler() {

        tourController.getCreatedTourFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                createdTours = tourController.getCreatedTours();
                TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity(), createdTours);
                binding.rclCreatedTour.setAdapter(tourRecyclerAdapter);
                setPendingPlansRecycler();
            }
            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.rclCreatedTour.textState(true);
                setPendingPlansRecycler();
            }
        });

    }

    private void setPendingPlansRecycler() {

        planController.getPendingPlanFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                pendingPlans = PlanController.getPendingPlans();
                binding.rclPendingPlans.setAdapter(new PlanRecyclerAdapter(mainActivity , pendingPlans));
                setConfirmedPlansRecycler();
            }
            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.rclPendingPlans.textState(true);
                setConfirmedPlansRecycler();
            }
        });

    }

    private void setConfirmedPlansRecycler() {

        planController.getConfirmedPlansFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                confirmedPlans = PlanController.getConfirmedPlans();
                binding.rclConfirmedPlans.setAdapter(new PlanRecyclerAdapter(mainActivity, confirmedPlans));
                setPlansRecycler();
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.rclConfirmedPlans.textState(true);
                setPlansRecycler();
            }
        });

    }

    private void setPlansRecycler() {
        planController.getAllPlanFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                allPlans = PlanController.getAllPlans();
                Plan[] plans = Arrays.copyOf(allPlans , Math.min(allPlans.length, 6));
                Log.d("ResponsePlan", "setPlansRecycler onSuccess: " + Arrays.toString(plans));
                binding.rclHomePlans.setAdapter(new PlanRecyclerAdapter(
                        mainActivity , plans));
                mainActivity.getHomeFragment().setRefreshing(false);
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.rclHomePlans.textState(true);
                mainActivity.getHomeFragment().setRefreshing(false);
            }
        }, "");
    }
}
