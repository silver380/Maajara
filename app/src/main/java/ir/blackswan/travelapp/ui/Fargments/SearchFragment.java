package ir.blackswan.travelapp.ui.Fargments;

import static ir.blackswan.travelapp.Utils.Utils.getEditableText;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlaceController;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.databinding.FragmentSearchBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Adapters.PlanRecyclerAdapter;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class SearchFragment extends RefreshingFragment {

    private final static int TOGGLE_TOUR = 0, TOGGLE_PLACE = 1, TOGGLE_PLAN = 2;
    public static final int SEARCH_DELAY = 500;
    private static int toggle = -1;
    private FragmentSearchBinding binding;
    private MainActivity mainActivity;
    private TourController tourController;
    private PlaceController placeController;
    private PlanController planController;
    private final Handler searchHandler = new Handler();
    private static String lastSearch = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init(root);
        mainActivity = (MainActivity) getActivity();

        setToggle();
        setListeners();

        tourController = new TourController(mainActivity);
        planController = new PlanController(mainActivity);
        placeController = new PlaceController(mainActivity);

        binding.rclSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.etSearch.setText(lastSearch);
        refresh();

        Log.d("Response", "SearchFragment onCreateView ");

        return root;
    }

    private void setListeners() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                restartSearchHandler();
            }
        });
    }

    private void restartSearchHandler() {
        binding.rclSearch.loadingState();
        searchHandler.removeCallbacksAndMessages(null);
        searchHandler.postDelayed(() -> {
            lastSearch = getEditableText(binding.etSearch.getText());
            refresh(lastSearch);
            Log.d("search", "restartSearchHandler: " + lastSearch);
        }, SEARCH_DELAY);
    }


    private void setToggle() {

        if (HomeFragment.isTourLeader()) {
            binding.toggleSearch.setButtonsTextByArray("برنامه سفر", "مکان");
            toggle = TOGGLE_PLAN;
        } else {
            binding.toggleSearch.setButtonsTextByArray("تور", "مکان");
            toggle = TOGGLE_TOUR;
        }

        binding.toggleSearch.setOnToggleListener((i, button) -> {

            if (i == 0) {
                if (HomeFragment.isTourLeader())
                    toggle = TOGGLE_PLAN;
                else
                    toggle = TOGGLE_TOUR;
            } else {
                toggle = TOGGLE_PLACE;
            }
            binding.rclSearch.loadingState();
            refresh();
        });
    }

    @Override
    public void onDestroyView() {
        if (binding != null)
            lastSearch = getEditableText(binding.etSearch.getText());

        super.onDestroyView();
    }

    public void refresh() {
        refresh(lastSearch);
    }

    public void refresh(@Nullable String search) {

        if (toggle == TOGGLE_TOUR)
            reloadTours(search);
        else if (toggle == TOGGLE_PLAN)
            reloadPlans(search);
        else if (toggle == TOGGLE_PLACE)
            reloadPlaces(search);
    }

    private void reloadPlans(String search) {
        planController.getAllPlanFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                binding.rclSearch.setAdapter(new PlanRecyclerAdapter(mainActivity, PlanController.getAllPlans()));
                setRefreshing(false);
            }
        }, search);
    }

    private void reloadTours(String search) {

        tourController.getAllTourFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                binding.rclSearch.setAdapter(new TourRecyclerAdapter(mainActivity, TourController.getAllTours()));
                setRefreshing(false);
            }
        }, search);
    }

    private void reloadPlaces(String search) {
        placeController.getAllPlacesFromServer(new OnResponseDialog(mainActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                binding.rclSearch.setAdapter(new PlacesRecyclerAdapter(mainActivity, PlaceController.getAllPlaces()));
                setRefreshing(false);
            }
        }, search);
    }


}