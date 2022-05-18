package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlaceController;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.FragmentSearchBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Adapters.PlanRecyclerAdapter;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import kotlin.Unit;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class SearchFragment extends RefreshingFragment {

    private final static int TOGGLE_TOUR = 0, TOGGLE_PLACE = 1, TOGGLE_PLAN = 2;
    private static final int SEARCH_DELAY = 500;
    private static int toggle = -1;
    private FragmentSearchBinding binding;
    private AuthActivity authActivity;
    private TourController tourController;
    private PlaceController placeController;
    private PlanController planController;
    private final Handler searchHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init(root);
        authActivity = (AuthActivity) getActivity();

        setToggle();
        setListeners();

        tourController = new TourController(authActivity);
        planController = new PlanController(authActivity);
        placeController = new PlaceController(authActivity);

        binding.rclSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refresh();


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

    private void restartSearchHandler(){
        searchHandler.removeCallbacksAndMessages(null);
        searchHandler.postDelayed(() -> {
            //todo: request for search
            Log.d("search", "restartSearchHandler: ");
        }, SEARCH_DELAY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setToggle(){
        if (HomeFragment.isTourLeader()) {
            binding.btnSearchTour.setVisibility(View.GONE);
            binding.btnSearchPlan.setVisibility(View.VISIBLE);
            binding.toggleSearch.selectButton(R.id.btn_search_plan);

            toggle = TOGGLE_PLAN;
        } else {
            binding.btnSearchTour.setVisibility(View.VISIBLE);
            binding.btnSearchPlan.setVisibility(View.GONE);
            binding.toggleSearch.selectButton(R.id.btn_search_tour);

            toggle = TOGGLE_TOUR;
        }

        binding.toggleSearch.setOnSelectListener(themedButton -> {
            if (themedButton.getId() == R.id.btn_search_tour)
                toggle = TOGGLE_TOUR;
            else if (themedButton.getId() == R.id.btn_search_plan)
                toggle = TOGGLE_PLAN;
            else if (themedButton.getId() == R.id.btn_search_place)
                toggle = TOGGLE_PLACE;

            refresh();

            return Unit.INSTANCE;
        });
    }

    public void refresh() {
        setRefreshing(true);
        binding.rclSearch.loadingState();
        if (toggle == TOGGLE_TOUR)
            reloadTours();
        else if (toggle == TOGGLE_PLAN)
            reloadPlans();
        else if (toggle == TOGGLE_PLACE)
            reloadPlaces();
    }

    private void reloadPlans() {
        planController.getAllPlanFromServer(new OnResponseDialog(authActivity){
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                binding.rclSearch.setAdapter(new PlanRecyclerAdapter(authActivity , PlanController.getAllPlans()));
                setRefreshing(false);
            }
        });
    }

    private void reloadTours() {

        tourController.getAllTourFromServer(new OnResponseDialog(authActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);

                binding.rclSearch.setAdapter(new TourRecyclerAdapter(authActivity, TourController.getAllTours()));
                setRefreshing(false);
            }
        });
    }

    private void reloadPlaces() {
        placeController.getAllPlacesFromServer(new OnResponseDialog(authActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                binding.rclSearch.setAdapter(new PlacesRecyclerAdapter(authActivity, PlaceController.getAllPlaces()));
                setRefreshing(false);
            }
        });
    }


}