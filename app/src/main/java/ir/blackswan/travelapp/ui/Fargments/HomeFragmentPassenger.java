package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
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

    public HomeFragmentPassenger(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        tourController = new TourController(mainActivity);
        planController = new PlanController(mainActivity);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeTravelerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.rclPendingTour.setLayoutManager(new LinearLayoutManager(mainActivity,
                LinearLayoutManager.HORIZONTAL, false));

        binding.rclConfirmedTour.setLayoutManager(new LinearLayoutManager(mainActivity,
                LinearLayoutManager.HORIZONTAL, false));

        binding.rclCreatedPlans.setLayoutManager(new LinearLayoutManager(mainActivity,
                LinearLayoutManager.HORIZONTAL, false));

        reload();


        return root;
    }


    public void reload() {
        mainActivity.getHomeFragment().setRefreshing(true);
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
                mainActivity.getHomeFragment().setRefreshing(false);
            }
        });

    }

}
