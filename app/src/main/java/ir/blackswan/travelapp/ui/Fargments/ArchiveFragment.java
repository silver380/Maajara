package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;


import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.databinding.FragmentArchiveBinding;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ArchiveFragment extends RefreshingFragment {

    private FragmentArchiveBinding binding;
    private TourController tourController;
    MainActivity mainActivity;
    private Tour[] archivedTours;

    public ArchiveFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArchiveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tourController = new TourController(mainActivity);

        binding.rclArchivedTours.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refresh();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void refresh() {
        reloadTours();
    }

    private void reloadTours() {

        tourController.getArchiveTourFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);

                binding.rclArchivedTours.setAdapter(new TourRecyclerAdapter(mainActivity,
                                                    TourController.getArchiveTours()));
                setRefreshing(false);
            }
        });
    }
}