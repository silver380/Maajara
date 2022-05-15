package ir.blackswan.travelapp.ui.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import ir.blackswan.travelapp.R;

public abstract class ToolbarActivity extends AuthActivity {
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
