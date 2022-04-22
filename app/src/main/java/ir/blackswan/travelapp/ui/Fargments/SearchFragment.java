package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.FragmentSearchBinding;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private AuthActivity authActivity;
    private boolean toggleTours = true;
    private TourController tourController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        authActivity = (AuthActivity) getActivity();

        binding.toggleSearch.selectButton(R.id.btn_search_tour);
        binding.toggleSearch.setOnSelectListener(themedButton -> {
            toggleTours = !toggleTours;
            reload();
            return Unit.INSTANCE;
        });
        tourController = new TourController(authActivity);
        binding.rclSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        reload();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void reload(){
        if (toggleTours)
            reloadTours();
        else
            reloadPlaces();
    }

    private void reloadTours() {

        tourController.getAllTourFromServer(new OnResponseDialog(authActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);

                binding.rclSearch.setAdapter(new TourRecyclerAdapter(authActivity, tourController.getAllTours()));
            }
        });
    }

    private void reloadPlaces() {

    }
}