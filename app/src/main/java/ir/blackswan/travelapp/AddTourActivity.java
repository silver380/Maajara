package ir.blackswan.travelapp;

import android.os.Bundle;
import android.widget.CheckBox;

import ir.blackswan.travelapp.Utils.GroupButtons;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.databinding.ActivityAddTourBinding;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class AddTourActivity extends HasToolbarActivity {
    ActivityAddTourBinding binding;
    GroupButtons groupPlace, groupFood, groupVehicle;
    MaterialPersianDateChooser startDate, finalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddTourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
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
                binding.btnAddTourHome, binding.btnAddTourSweet, binding.btnAddTourHotel);

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


    }

}