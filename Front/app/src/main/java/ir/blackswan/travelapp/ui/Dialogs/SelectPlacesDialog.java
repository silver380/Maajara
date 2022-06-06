package ir.blackswan.travelapp.ui.Dialogs;

import static ir.blackswan.travelapp.Utils.Utils.getEditableText;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import java.util.Arrays;
import java.util.HashSet;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlaceController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.databinding.DialogSelectPlacesBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Fargments.SearchFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SelectPlacesDialog extends MyDialog {
    PlacesRecyclerAdapter placesRecyclerAdapter;
    PlaceController placeController;
    DialogSelectPlacesBinding binding;
    AuthActivity activity;
    private final Handler searchHandler = new Handler();

    public SelectPlacesDialog(AuthActivity activity, View.OnClickListener onSubmitClick) {
        this.activity = activity;
        binding = DialogSelectPlacesBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_ALERT);
        placeController = new PlaceController(activity);
        binding.rclSelectPlaces.setLayoutManager(new GridLayoutManager(activity, 2));

        setRecycler("");

        setListeners(onSubmitClick);
    }

    private void setRecycler(String search) {
        placeController.getAllPlacesFromServer(new OnResponseDialog(activity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                HashSet<Place> selectedPlace = null;
                if (placesRecyclerAdapter != null)
                    selectedPlace = placesRecyclerAdapter.getSelectedPlacesHashset();
                placesRecyclerAdapter = new PlacesRecyclerAdapter(activity, PlaceController.getAllPlaces(), true, selectedPlace);
                SelectPlacesDialog.this.binding.rclSelectPlaces.setAdapter(placesRecyclerAdapter);
            }
        }, search);

    }

    private void setListeners(View.OnClickListener onSubmitClick) {
        binding.etSelectPlacesSearch.addTextChangedListener(new TextWatcher() {
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
        binding.btnSelectPlaceSubmit.setOnClickListener(v -> {
            onSubmitClick.onClick(v);
            dialog.dismiss();
        });
    }

    public void load(Place[] selectedPlaces) {
        placesRecyclerAdapter = new PlacesRecyclerAdapter(activity, PlaceController.getAllPlaces(),
                true, new HashSet<>(Arrays.asList(selectedPlaces)));
    }

    private void restartSearchHandler() {
        binding.rclSelectPlaces.loadingState();
        searchHandler.removeCallbacksAndMessages(null);
        searchHandler.postDelayed(() -> {
                    setRecycler(getEditableText(binding.etSelectPlacesSearch.getText()));
                }
                , SearchFragment.SEARCH_DELAY);
    }

    public PlacesRecyclerAdapter getPlacesRecyclerAdapter() {
        return placesRecyclerAdapter;
    }


}
