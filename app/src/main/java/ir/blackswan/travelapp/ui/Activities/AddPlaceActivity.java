package ir.blackswan.travelapp.ui.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Arrays;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MyInputTypes;
import ir.blackswan.travelapp.Utils.PermissionRequest;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.ActivityAddPlaceBinding;

public class AddPlaceActivity extends ToolbarActivity implements OnMapReadyCallback {
    private static final String TAG = "GoogleMap";
    com.google.android.gms.location.LocationRequest locationRequest;
    Marker marker;
    private final LatLng defaultLang = new LatLng(35.7219, 51.3347);
    LatLng lang;
    ActivityAddPlaceBinding binding;
    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);


        binding.placeMapview.onCreate(savedInstanceState);

        binding.placeMapview.getMapAsync(this);

        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        getCurrentLocation();


        setPlacePicture();
    }

    @Override
    public void onResume() {
        binding.placeMapview.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        binding.placeMapview.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.placeMapview.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.placeMapview.onLowMemory();
    }

    public void setPlacePicture() {
        MyInputTypes.showFileChooser(binding.btnChooseImg, this, "image/*", result -> {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                Log.d("FileChooser", "Image path: " + uri.getPath());
                UCrop.of(uri, Uri.fromFile(new File(Utils.getFilePath(
                        this, "Cropped Image", ".jpg"))))
                        .withMaxResultSize(512, 512)
                        .start(this);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + "  " + resultCode);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                getCurrentLocation();
            }
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            //todo: upload file
            binding.placeImg.setImageBitmap(BitmapFactory.decodeFile(resultUri.getPath()));
            binding.placeImg.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            binding.placeImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            binding.groupPlaceImg.setBackgroundColor(getResources().getColor(R.color.colorTransparent));

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("FileChooser", "onActivityResult:Crop error ", cropError);
            Toast.makeText(this, "خطا در شناسایی تصویر. لطفا دوباره سعی کنید", Toast.LENGTH_LONG,
                    Toast.TYPE_ERROR).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: " + requestCode + "  " + Arrays.toString(grantResults));
        if (requestCode == 1) {
            if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPS_on()) {
                    getCurrentLocation();
                } else {
                    turnOnGPS();
                }
            }
        }
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d("GoogleMap", "getCurrentLocation: ");
            if (isGPS_on()) {
                LocationServices.getFusedLocationProviderClient(AddPlaceActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(AddPlaceActivity.this)
                                        .removeLocationUpdates(this);

                                if (locationResult.getLocations().size() > 0) {

                                    int index = locationResult.getLocations().size() - 1;
                                    double latitude = locationResult.getLocations().get(index).getLatitude();
                                    double longitude = locationResult.getLocations().get(index).getLongitude();

                                    lang = new LatLng(latitude, longitude);
                                    Log.d("GoogleMap", "getCurrentLocation: " + lang);
                                    setMarker();
                                }
                            }
                        }, Looper.getMainLooper());
            } else {
                turnOnGPS();
            }
        } else {
            PermissionRequest.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, 1);
            Log.d("GoogleMap", "getCurrentLocation: " + "DENIED");
        }

    }


    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {

            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
//                    Toast.makeText(MainActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();
                getCurrentLocation();

            } catch (ApiException e) {
                Log.d(TAG, "turnOnGPS: " + e.getStatusCode());
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(AddPlaceActivity.this, 2);
                        } catch (IntentSender.SendIntentException ex) {
                            Log.e(TAG, "turnOnGPS: ", e);
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Device does not have location
                        break;
                }
            }
        });

    }

    private boolean isGPS_on() {
        LocationManager locationManager;
        boolean isON;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isON = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("GoogleMap", "isGPS_on: " + isON);
        return isON;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

    }

    private void setMarker() {
        Log.d("GoogleMap", "setMarker: " + map + "  " + lang);
        if (map != null) {
            if (lang != null) {
                if (marker != null) {
                    marker.remove();
                }
                marker = map.addMarker(new MarkerOptions().position(lang));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lang, 12));
            }
        }
    }
}