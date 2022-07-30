package ir.blackswan.travelapp.ui.Activities;

import static ir.blackswan.travelapp.Utils.Utils.changeStatusColor;
import static ir.blackswan.travelapp.Utils.Utils.getEditableText;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlaceController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.FileUtil;
import ir.blackswan.travelapp.Utils.MyInputTypes;
import ir.blackswan.travelapp.Utils.PermissionRequest;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.ActivityAddPlaceBinding;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddPlaceActivity extends ToolbarActivity implements OnMapReadyCallback {
    private static final String TAG = "GoogleMap";
    private static final int REQUEST_LOCATION_CHANGE = 5;
    com.google.android.gms.location.LocationRequest locationRequest;
    Marker marker;
    private final LatLng defaultLang = new LatLng(35.7219, 51.3347);
    LatLng lang;
    ActivityAddPlaceBinding binding;
    GoogleMap map;
    File image;
    TextInputsChecker checker = new TextInputsChecker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);


        binding.placeMapview.onCreate(savedInstanceState);

        binding.placeMapview.getMapAsync(this);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setMapLoading(true);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                locationRequest = com.google.android.gms.location.LocationRequest.create();
                locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(5000);
                locationRequest.setFastestInterval(2000);


                getCurrentLocation();
                return null;
            }
        }.execute();

        setChecker();

        setPlacePicture();

        binding.btnSubmitPlace.setOnClickListener(v -> {
            if (checkInputs()) {
                new PlaceController(this).addPlace(new Place(
                                getEditableText(binding.etPlaceName.getText()), getEditableText(binding.etPlaceCity.getText()),
                                getEditableText(binding.etPlaceDescription.getText()), (float) lang.latitude, (float) lang.longitude), image,
                        new OnResponseDialog(this) {
                            @Override
                            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                                super.onSuccess(call, callback, response);
                                Log.d(MyCallback.TAG, "addPlace onSuccess: " + response);
                                Toast.makeText(AddPlaceActivity.this, "درخواست اضافه کردن مکان با موفقیت ارسال شد", Toast.LENGTH_SHORT
                                        , Toast.TYPE_SUCCESS).show();
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                                super.onFailed(call, callback, response);
                                Log.d(MyCallback.TAG, "addPlace onFailed: " + response);
                            }
                        });
            }else {
                Toast.makeText(this, getString(R.string.fix_errors), Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();
            }
        });
    }

    private void setChecker() {
        checker.add(Arrays.asList(binding.etPlaceName , binding.etPlaceCity));
    }

    boolean checkInputs(){
        boolean returnValue = !checker.checkAllError();
        if (image == null || lang == null) {
            returnValue = false;
        }
        return returnValue;

    }

    private void setMapLoading(boolean loading) {
        binding.groupPlaceMapLoading.setVisibility(loading ? View.VISIBLE : View.GONE);

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
            } else {
                lang = defaultLang;
                setMarker();
            }
        }
        if (requestCode == REQUEST_LOCATION_CHANGE) {
            if (resultCode == RESULT_OK) {
                lang = new LatLng(data.getDoubleExtra("lat", -1),
                        data.getDoubleExtra("lang", -1));
                updateMarker();
            }
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            try {
                image = FileUtil.from(this, resultUri);
                binding.placeImg.setOnClickListener(v -> {
                    startActivity(new Intent(this , FullscreenImageActivity.class).putExtra(
                            FullscreenImageActivity.IMAGE_URL , image.getPath()
                    ));
                });
                binding.placeImg.setImageBitmap(BitmapFactory.decodeFile(resultUri.getPath()));
                binding.placeImg.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                binding.placeImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                binding.groupPlaceImg.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            } catch (IOException e) {
                Log.d(TAG, "onActivityResult: " , e);
            }

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
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPS_on()) {
                    getCurrentLocation();
                } else {
                    turnOnGPS();
                }
            } else {
                lang = defaultLang;
                setMarker();
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
                        lang = defaultLang;
                        setMarker();
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
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
    }

    private void setMarker() {
        Log.d("GoogleMap", "setMarker: " + map + "  " + lang);
        if (map != null) {
            if (lang != null) {
                if (marker != null) {
                    marker.remove();
                }
                updateMarker();
                map.setOnMarkerClickListener(marker -> {
                    startMapsActivity();
                    return false;
                });
                map.setOnMapClickListener(v -> startMapsActivity());
                setMapLoading(false);
            }
        }
    }

    private void updateMarker() {
        marker = map.addMarker(new MarkerOptions().position(lang).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lang, 12));
    }

    private void startMapsActivity() {
        startActivityForResult(new Intent(this, MapsActivity.class).putExtra("lat",
                lang.latitude).putExtra("lang", lang.longitude)
                , REQUEST_LOCATION_CHANGE);
    }


}