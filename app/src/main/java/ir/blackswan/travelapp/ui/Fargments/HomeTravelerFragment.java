package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.databinding.FragmentHomeBinding;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeTravelerFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthActivity authActivity;
    private TourController tourController;
    private Tour[] pendingTours;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = ((AuthActivity) getActivity());
        tourController = new TourController(authActivity);



        return root;
    }

    private void setRecyclers() {
        TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity(), pendingTours);
        binding.rclCreatedTour.setAdapter(tourRecyclerAdapter);
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


}
