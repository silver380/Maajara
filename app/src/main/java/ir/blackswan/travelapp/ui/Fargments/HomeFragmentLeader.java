package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.Tour;
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
    private Plan[] pendingPlans;

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

        binding.rclCreatedTour.setLayoutManager(new LinearLayoutManager(mainActivity,
                LinearLayoutManager.HORIZONTAL, false));

        binding.rclPendingPlans.setLayoutManager(new LinearLayoutManager(mainActivity,
                LinearLayoutManager.HORIZONTAL, false));


        reload();


        return root;
    }

    public void reload() {
        setCreatedToursRecycler();

    }

    private void setRecyclers() {
        TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity(), createdTours);
        binding.rclCreatedTour.setAdapter(tourRecyclerAdapter);
        binding.rclPendingPlans.setAdapter(new PlanRecyclerAdapter(mainActivity , pendingPlans));
        mainActivity.getHomeFragment().setRefreshing(false);
    }

    private void setCreatedToursRecycler() {

        tourController.getCreatedTourFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                createdTours = tourController.getCreatedTours();
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
                setRecyclers();
            }
        });

    }
}
