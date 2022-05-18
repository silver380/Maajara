package ir.blackswan.travelapp.ui.Dialogs;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlaceController;
import ir.blackswan.travelapp.Views.LoadingRecyclerView;
import ir.blackswan.travelapp.databinding.DialogSelectPlacesBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SelectPlacesDialog extends MyDialog {
    PlacesRecyclerAdapter placesRecyclerAdapter;
    PlaceController placeController;
    DialogSelectPlacesBinding binding;
    AuthActivity activity;
    public SelectPlacesDialog(AuthActivity activity , View.OnClickListener onSubmitClick){
        this.activity = activity;
        binding = DialogSelectPlacesBinding.inflate(activity.getLayoutInflater());
        init(activity , binding.getRoot() , DIALOG_TYPE_ALERT);
        placeController = new PlaceController(activity);
        binding.rclSelectPlaces.setLayoutManager(new GridLayoutManager(activity, 2));
        setRecycler();
        binding.btnSelectPlaceSubmit.setOnClickListener(v -> {
            onSubmitClick.onClick(v);
            dialog.dismiss();
        });
    }

    private void setRecycler(){
        placeController.getAllPlacesFromServer(new OnResponseDialog(activity){
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                placesRecyclerAdapter = new PlacesRecyclerAdapter(activity , PlaceController.getAllPlaces() , true);
                SelectPlacesDialog.this.binding.rclSelectPlaces.setAdapter(placesRecyclerAdapter);
            }
        });

    }

    public PlacesRecyclerAdapter getPlacesRecyclerAdapter() {
        return placesRecyclerAdapter;
    }


}
