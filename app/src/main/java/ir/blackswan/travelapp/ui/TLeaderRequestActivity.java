package ir.blackswan.travelapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ir.blackswan.travelapp.Data.PlanRequest;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.databinding.ActivityTleaderRequestBinding;

public class TLeaderRequestActivity extends AppCompatActivity {
    private ActivityTleaderRequestBinding binding;
    private PlanRequest[] pending_tl;
    private PlanRequest[] confirmed_tl;
    ArrayList<PlanRequest> all_tl;
    Intent intent;
    int plan_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityTleaderRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tleader_request);
    }

    private void setRecyclers(){

    }

    private void setPending_tlRecycler(){

    }

    private void setConfirmed_tlRecycler(){

    }
}