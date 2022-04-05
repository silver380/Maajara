package ir.blackswan.travelapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startActivity(new Intent(this , AddTourActivity.class));


        Toast.makeText(this , "هعی"  , Toast.LENGTH_LONG , Toast.TYPE_ERROR).show();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);
        NavController navCo = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navCo);

    }

}