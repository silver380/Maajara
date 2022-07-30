package ir.blackswan.travelapp.ui.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.ActivityMapsBinding;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        latLng = new LatLng(intent.getDoubleExtra("lat", 0),
                intent.getDoubleExtra("lang", 0));

        binding.btnSubmitPlaceLocation.setOnClickListener(v -> {
            setResult(RESULT_OK, new Intent().putExtra("lat", latLng.latitude)
                    .putExtra("lang", latLng.longitude));
            finish();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraMoveListener(() -> {
            latLng = mMap.getCameraPosition().target;
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

    }


}