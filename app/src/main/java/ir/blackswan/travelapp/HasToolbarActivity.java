package ir.blackswan.travelapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class HasToolbarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar tb = findViewById(R.id.tb_activity);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null)
            actionbar.setDisplayHomeAsUpEnabled(true);
        tb.setNavigationOnClickListener(view -> onBackPressed());
    }
}
