package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlaceController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.databinding.FragmentSearchBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SelectPlacesDialog extends MyDialog {
    PlacesRecyclerAdapter placesRecyclerAdapter;
    PlaceController placeController;
    FragmentSearchBinding binding;
    AuthActivity activity;
    public SelectPlacesDialog(AuthActivity activity){
        this.activity = activity;
        binding = FragmentSearchBinding.inflate(activity.getLayoutInflater());
        init(activity , binding.getRoot() , DIALOG_TYPE_ALERT);
        placeController = new PlaceController(activity);
        binding.toggleSearch.setVisibility(View.GONE);
        binding.rclSearch.setLayoutManager(new GridLayoutManager(activity, 2));
        setRecycler();
    }

    private void setRecycler(){
        placeController.getAllPlacesFromServer(new OnResponseDialog(activity){
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                placesRecyclerAdapter = new PlacesRecyclerAdapter(activity , PlaceController.getAllPlaces());
                SelectPlacesDialog.this.binding.rclSearch.setAdapter(placesRecyclerAdapter);
            }
        });

    }

}
