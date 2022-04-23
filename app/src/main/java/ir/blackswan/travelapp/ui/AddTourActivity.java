package ir.blackswan.travelapp.ui;

import android.os.Bundle;
import android.widget.CheckBox;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.HashSet;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Utils.GroupButtons;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.ActivityAddTourBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import ir.blackswan.travelapp.ui.Dialogs.SelectPlacesDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddTourActivity extends ToolbarActivity {
    ActivityAddTourBinding binding;
    GroupButtons groupPlace, groupFood, groupVehicle;
    MaterialPersianDateChooser startDate, finalDate;
    SelectPlacesDialog selectPlacesDialog;
    static String[] residents = {"Hotel" , "Suite", "House", "Villa"};
    static String[] vehicle = {"Car", "Minibus", "Bus", "Van"};
    private TourController tourController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddTourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        binding.rclAddTourPlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectPlacesDialog = new SelectPlacesDialog(this, v -> {
            HashSet<Place> selectedPlaces = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlaces();
            binding.rclAddTourPlaces.setAdapter(
                    new PlacesRecyclerAdapter(this, selectedPlaces.toArray(new Place[0]))
            );
        });
        tourController = new TourController(this);
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
                binding.btnAddTourHome, binding.btnAddTourSweet, binding.btnAddTourHotel, binding.btnAddTourVila);

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
        binding.btnAddTourSubmit.setOnClickListener(view -> {
            String tourName = binding.etAddTourName.getText().toString();

            String sDate = startDate.getCalendar().getPersianYear() + "-" +
                    startDate.getCalendar().getPersianMonth() + "-" +
                    startDate.getCalendar().getPersianDay();

            String fDate = finalDate.getCalendar().getPersianYear() + "-" +
                    finalDate.getCalendar().getPersianMonth() + "-" +
                    finalDate.getCalendar().getPersianDay();

            int capacity = Integer.parseInt(binding.etAddTourCapacity.getText().toString());

            int price = Integer.parseInt(binding.etAddTourPrice.getText().toString());

            String destination = binding.etAddTourDestination.getText().toString();

            Place[] places = (Place[]) selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlaces().toArray();

            int residenceIndex =  groupPlace.getFirstSelectedIndex();
            String residence = residenceIndex == -1 ? "None" : residents[residenceIndex];

            int tranIndex = groupVehicle.getFirstSelectedIndex();
            String transportation = tranIndex == -1? "None": vehicle[tranIndex];

            boolean[] food = groupFood.getSelected();

            Tour tour = new Tour(tourName, capacity , price , destination , sDate , fDate , places , residence ,
                    food[0] , food[1] , food[2] , transportation);

            tourController.addTourToServer(tour , new OnResponseDialog(this){
                @Override
                public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                    super.onSuccess(call, callback, response);
                    Toast.makeText(AddTourActivity.this , "تور با موفقیت اضافه شد" ,
                            Toast.LENGTH_SHORT , Toast.TYPE_SUCCESS).show();
                    finish();
                }

                @Override
                public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                    super.onFailed(call, callback, response);
                }
            });

        });
    }


}