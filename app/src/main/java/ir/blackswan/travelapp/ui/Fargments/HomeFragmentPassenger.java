package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Arrays;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.CustomGridLayoutManager;
import ir.blackswan.travelapp.databinding.FragmentHomeTravelerBinding;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Adapters.PlanRecyclerAdapter;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeFragmentPassenger extends Fragment {

    private FragmentHomeTravelerBinding binding;
    private MainActivity mainActivity;
    private TourController tourController;
    private PlanController planController;
    private Tour[] pendingTours;
    private Tour[] confirmedTours;
    private Tour[] allTours;

    public HomeFragmentPassenger(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        tourController = new TourController(mainActivity);
        planController = new PlanController(mainActivity);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeTravelerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.rclCreatedPlans.setLayoutManager(new CustomGridLayoutManager(mainActivity, 2));

        binding.rclConfirmedTour.setLayoutManager(new CustomGridLayoutManager(mainActivity, 2));

        binding.rclPendingTour.setLayoutManager(new CustomGridLayoutManager(mainActivity, 2));

        binding.rclHomeTours.setLayoutManager(new CustomGridLayoutManager(mainActivity , 2));
        setListeners();

        reload();


        return root;
    }

    private void setListeners() {
        binding.btnHomeGotoSearch.setOnClickListener(v ->
                mainActivity.navigateToId(R.id.navigation_search));
    }
    private void updateGotoSearchVisibility(){
        binding.btnHomeGotoSearch.setVisibility(allTours.length > 6 ? View.VISIBLE : View.GONE);
    }

    public void reload() {
        setConfirmedToursRecycler();
    }

    private void setConfirmedToursRecycler() {
        tourController.getConfirmedTourFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                confirmedTours = tourController.getConfirmedTours();

                TourRecyclerAdapter tourRecyclerAdapter2 = new TourRecyclerAdapter(getActivity(), confirmedTours);
                binding.rclConfirmedTour.setAdapter(tourRecyclerAdapter2);
                setPendingToursRecycler();
            }
            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.rclConfirmedTour.textState(true);
                setPendingToursRecycler();
            }
        });

    }

    private void setPendingToursRecycler() {
        tourController.getPendingTourFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                pendingTours = tourController.getPendingTours();

                TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity(), pendingTours);
                binding.rclPendingTour.setAdapter(tourRecyclerAdapter);
                setCreatedPlansRecycler();
            }
            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.rclPendingTour.textState(true);
                setCreatedPlansRecycler();
            }
        });
    }

    private void setCreatedPlansRecycler() {
        planController.getCreatedPlanFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                binding.rclCreatedPlans.setAdapter(
                        new PlanRecyclerAdapter(mainActivity, PlanController.getCreatedPlans())
                );
                setTourRecycler();
            }
            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.rclCreatedPlans.textState(true);
                setTourRecycler();
            }
        });

    }

    private void setTourRecycler() {
        tourController.getAllTourFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                allTours = TourController.getAllTours();
                binding.rclHomeTours.setAdapter(
                        new TourRecyclerAdapter(mainActivity ,
                                Arrays.copyOf(allTours , Math.min(allTours.length , 6)))
                );
                updateGotoSearchVisibility();
                mainActivity.getHomeFragment().setRefreshing(false);
            }
            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.rclHomeTours.textState(true);
                mainActivity.getHomeFragment().setRefreshing(false);
            }
        }, "");
    }

}
