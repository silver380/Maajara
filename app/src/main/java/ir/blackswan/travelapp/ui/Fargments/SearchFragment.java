package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.databinding.FragmentSearchBinding;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private AuthActivity authActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = (AuthActivity) getActivity();

        binding.rclSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        TourController tourController = new TourController(authActivity);
        tourController.getAllTourFromServer(new OnResponseDialog(authActivity));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}