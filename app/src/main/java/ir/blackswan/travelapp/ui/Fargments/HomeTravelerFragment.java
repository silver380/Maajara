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
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.databinding.FragmentHomeTravelerBinding;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeTravelerFragment extends Fragment {

    private FragmentHomeTravelerBinding binding;
    private AuthActivity authActivity;
    private TourController tourController;
    private Tour[] pendingTours;
    private Tour[] confirmedTours;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeTravelerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = ((AuthActivity) getActivity());

        binding.rclPendingTour.setLayoutManager(new LinearLayoutManager(authActivity,
                LinearLayoutManager.HORIZONTAL, false));
        tourController = new TourController(authActivity);
        setPendingToursRecycler();
        binding.rclConfirmedTour.setLayoutManager(new LinearLayoutManager(authActivity,
                LinearLayoutManager.HORIZONTAL, false));
        setConfirmedToursRecycler();

        return root;
    }

    private void setRecyclers() {
        TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity(), pendingTours);
        binding.rclPendingTour.setAdapter(tourRecyclerAdapter);

        TourRecyclerAdapter tourRecyclerAdapter2 = new TourRecyclerAdapter(getActivity(), confirmedTours);
        binding.rclConfirmedTour.setAdapter(tourRecyclerAdapter2);
    }

    private void setPendingToursRecycler() {

        tourController.getPendingTourFromServer(new OnResponseDialog(authActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                pendingTours = tourController.getPendingTours();
                setRecyclers();
            }
        });
    }

    private void setConfirmedToursRecycler() {

        tourController.getConfirmedTourFromServer(new OnResponseDialog(authActivity){
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                confirmedTours = tourController.getConfirmedTours();
                setRecyclers();
            }
        });

    }

}
