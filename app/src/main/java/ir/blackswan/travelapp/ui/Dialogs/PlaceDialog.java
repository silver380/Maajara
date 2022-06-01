package ir.blackswan.travelapp.ui.Dialogs;

import static ir.blackswan.travelapp.Utils.Utils.openMapApp;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.databinding.DialogPlaceBinding;

public class PlaceDialog extends MyDialog implements OnMapReadyCallback {
    DialogPlaceBinding binding;
    Place place;
    Activity activity;

    public PlaceDialog(Activity activity, Place place) {
        binding = DialogPlaceBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_BOTTOM_SHEET);
        this.place = place;
        this.activity = activity;

        binding.placeDialogMapview.onCreate(null);
        binding.placeDialogMapview.onResume();
        binding.placeDialogMapview.getMapAsync(PlaceDialog.this);

        binding.ivPlaceImage.setFullScreen(true);
        binding.ivPlaceImage.setImagePath(place.getPicture());
        binding.tvPlaceName.setText(place.getName());
        binding.tvPlaceCity.setText(place.getCity());
        binding.tvPlaceText.setText(place.getDescription());
    }


    @Override
    public void dismiss() {
        super.dismiss();
        binding.placeDialogMapview.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        googleMap.setOnMapClickListener(latLng1 -> {
            openMapApp(activity, latLng.latitude, latLng.longitude);
        });
        googleMap.setOnMarkerClickListener(marker -> {
            openMapApp(activity, latLng.latitude, latLng.longitude);
            return false;
        });
    }


}
