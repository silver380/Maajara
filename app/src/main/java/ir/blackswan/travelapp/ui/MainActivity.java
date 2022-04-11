package ir.blackswan.travelapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.ActivityMainBinding;
import ir.blackswan.travelapp.ui.Dialogs.PlaceDialog;
import ir.blackswan.travelapp.ui.Dialogs.RegisterLoginDialog;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utils.changeStatusColor(this, R.color.colorMainStatusBar);

        //startActivity(new Intent(this, AddTourActivity.class));

        RegisterLoginDialog dialog = new RegisterLoginDialog(this , true);
        //dialog.show();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);


        Place place = new Place("مکان‍‍۱", "شهر۱", "توضیحات",
                null,
                "https://photokade.com/wp-content/uploads/lovegraphy-photokade-com-12.jpg");
        new PlaceDialog(this, place).show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
