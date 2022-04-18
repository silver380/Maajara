package ir.blackswan.travelapp.ui;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import ir.blackswan.travelapp.Controller.OnResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.FakeData;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Utils.GroupButtons;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.ActivityAddTourBinding;

public class AddTourActivity extends ToolbarActivity {
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

        Tour tour = FakeData.getFakeTours().get(0);
        //todo: remove these lines
        TourController tourController = new TourController(this);
        tourController.createTour(tour, new OnResponse() {
            @Override
            public void onSuccess(String responseBody) {
                finish();
            }

            @Override
            public void onFailed(String message) {

            }
        });

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