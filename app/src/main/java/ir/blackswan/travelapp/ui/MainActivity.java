package ir.blackswan.travelapp.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.ActivityMainBinding;

public class MainActivity extends HasLoginDialogActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utils.changeStatusColor(this, R.color.colorMainStatusBar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);

        loadUser();

        Log.d("Response", "onCreate: ");
        getRegisterLoginDialog().show();
    }

    private void loadUser() {
        AuthController.loadUser(this, user -> {

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
