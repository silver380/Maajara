package ir.blackswan.travelapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.HashSet;

import ir.blackswan.travelapp.Controller.OnResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.FakeData;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Utils.GroupButtons;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.ActivityAddTourBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.SelectPlacesDialog;

public class AddTourActivity extends ToolbarActivity {
    ActivityAddTourBinding binding;
    GroupButtons groupPlace, groupFood, groupVehicle;
    MaterialPersianDateChooser startDate, finalDate;
    SelectPlacesDialog selectPlacesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddTourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        binding.rclAddTourPlaces.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        selectPlacesDialog = new SelectPlacesDialog(this, v -> {
            HashSet<Place> selectedPlaces = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlaces();
            binding.rclAddTourPlaces.setAdapter(
                    new PlacesRecyclerAdapter(this , selectedPlaces.toArray(new Place[0]))
            );
        });
        setupGroupButtons();
        setupDateChooses();
        setListeners();


    }

    private void setupDateChooses() {
        startDate = new MaterialPersianDateChooser(binding.etAddTourStartDate);
        finalDate = new MaterialPersianDateChooser(binding.etAddTourFinalDate);
    }


    private void setupGroupButtons() {

        groupPlace = new GroupButtons(true,
                binding.btnAddTourHome, binding.btnAddTourSweet, binding.btnAddTourHotel , binding.btnAddTourVila);

        groupFood = new GroupButtons(false,
                binding.btnAddTourBreakfast, binding.btnAddTourLunch, binding.btnAddTourDinner);

        groupVehicle = new GroupButtons(true, binding.btnAddTourCar, binding.btnAddTourMinibus,
                binding.btnAddTourBus, binding.btnAddTourVan);

        GroupButtons[] groupButtons = {groupPlace, groupFood, groupVehicle};
        CheckBox[] checkBoxes = {binding.cbAddTourPlace, binding.cbAddTourFood, binding.cbAddTourVehicle};

        for (int i = 0; i < groupButtons.length; i++) {
            int finalI = i;
            groupButtons[i].setOnSelectListener((groupButtons1, index, reselect) -> {
                checkBoxes[finalI].setChecked(groupButtons[finalI].hasOneSelected());
            });
        }
    }

    private void setListeners() {
        binding.ivAddTourAddPlace.setOnClickListener(v -> {
            selectPlacesDialog.show();
        });
    }

}