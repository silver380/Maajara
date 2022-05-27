package ir.blackswan.travelapp.ui.Fargments;

import static ir.blackswan.travelapp.Utils.Utils.getEditableText;
import static ir.blackswan.travelapp.ui.Fargments.FragmentDataHandler.KEY_ADD_TOUR_FRAGMENT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.Calendar;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.GroupButtons;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.FragmentAddTourBinding;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import ir.blackswan.travelapp.ui.Dialogs.SelectPlacesDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddTourFragment extends Fragment {
    private FragmentAddTourBinding binding;
    GroupButtons groupPlace, groupFood, groupVehicle;
    MaterialPersianDateChooser startDate, finalDate;
    SelectPlacesDialog selectPlacesDialog;
    static String[] residents = {"Hotel", "Suite", "House", "Villa"};
    static String[] vehicle = {"Car", "Minibus", "Bus", "Van"};
    private TourController tourController;
    private MainActivity mainActivity;
    private final static String SAVED_DATA = "data";
    private TextInputsChecker checker = new TextInputsChecker();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddTourBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        mainActivity = ((MainActivity) getActivity());
        binding.rclAddTourPlaces.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        binding.rclAddTourPlaces.setText(getString(R.string.no_place_select));
        binding.rclAddTourPlaces.setErrorText(binding.rclAddTourPlaces.getText());
        binding.rclAddTourPlaces.textState();
        Log.d("Response", "AddTour onCreateView ");
        selectPlacesDialog = new SelectPlacesDialog(mainActivity, v -> {
            setPlacesRecyclerAdapter();
        });
        tourController = new TourController(mainActivity);
        setChecker();
        setupGroupButtons();
        setupDateChooses();
        setListeners();

        return root;
    }

    private void setPlacesRecyclerAdapter() {
        Place[] selectedPlaces = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlacesArray();
        binding.rclAddTourPlaces.setAdapter(
                new PlacesRecyclerAdapter(mainActivity, selectedPlaces)
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        load();
    }

    public void save() {
        if (binding == null)
            return;
        Log.d("saveFragments", "save: ");
        FragmentDataHandler.save(mainActivity, KEY_ADD_TOUR_FRAGMENT, new FragmentsData(
                getEditableText(binding.etAddTourName.getText()),
                getEditableText(binding.etAddTourCapacity.getText()),
                getEditableText(binding.etAddTourPrice.getText()),
                getEditableText(binding.etAddTourDestination.getText()),
                startDate.getGregorianY_M_D(),
                finalDate.getGregorianY_M_D(), selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlacesArray(),
                groupPlace.getSelected(), groupFood.getSelected(), groupVehicle.getSelected()
        ));
    }

    private void load() {
        FragmentsData fragmentsData = FragmentDataHandler.load(mainActivity, KEY_ADD_TOUR_FRAGMENT);
        Log.d("saveFragments", "load: ");
        if (fragmentsData == null)
            return;
        binding.etAddTourName.setText(fragmentsData.name);
        binding.etAddTourCapacity.setText(fragmentsData.capacity);
        binding.etAddTourPrice.setText(fragmentsData.price);
        binding.etAddTourDestination.setText(fragmentsData.destination);
        startDate.load(fragmentsData.startDate);
        finalDate.load(fragmentsData.finalDate);
        selectPlacesDialog.load(fragmentsData.selectedPlace);
        setPlacesRecyclerAdapter();

        groupFood.load(fragmentsData.groupFood);
        binding.cbAddTourFood.setChecked(groupFood.hasOneSelected());

        groupPlace.load(fragmentsData.groupPlace);
        binding.cbAddTourPlace.setChecked(groupPlace.hasOneSelected());

        groupVehicle.load(fragmentsData.groupVehicle);
        binding.cbAddTourVehicle.setChecked(groupVehicle.hasOneSelected());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        save();
    }

    private void setupDateChooses() {
        startDate = new MaterialPersianDateChooser(mainActivity, binding.etAddTourStartDate);
        finalDate = new MaterialPersianDateChooser(mainActivity, binding.etAddTourFinalDate);
        startDate.setMustGreaterThanNow(true);
        finalDate.setMustGreaterThanNow(true);
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

        //helper text for price
        binding.etAddTourPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String price = getEditableText(binding.etAddTourPrice.getText());
                if (price.isEmpty())
                    binding.tilAddTourPrice.setHelperText("");
                else {
                    String price_helper = Utils.getPriceString(
                            Integer.parseInt(price));
                    binding.tilAddTourPrice.setHelperText(price_helper);
                }
            }
        });

        binding.ivAddTourAddPlace.setOnClickListener(v -> {
            selectPlacesDialog.show();
        });
        binding.btnAddTourSubmit.setOnClickListener(view -> {

            if (checkInputs()) {

                String tourName = binding.etAddTourName.getText().toString();

                String sDate = startDate.getGregorianY_M_D();

                String fDate = startDate.getGregorianY_M_D();

                int capacity = Integer.parseInt(binding.etAddTourCapacity.getText().toString());

                int price = Integer.parseInt(binding.etAddTourPrice.getText().toString());

                String destination = binding.etAddTourDestination.getText().toString();

                Place[] places = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlacesArray();

                int residenceIndex = groupPlace.getFirstSelectedIndex();
                String residence = residenceIndex == -1 ? "None" : residents[residenceIndex];

                int tranIndex = groupVehicle.getFirstSelectedIndex();
                String transportation = tranIndex == -1 ? "None" : vehicle[tranIndex];

                boolean[] food = groupFood.getSelected();

                Tour tour = new Tour(tourName, capacity, price, destination, sDate, fDate, places, residence,
                        food[0], food[1], food[2], transportation);

                tourController.addTourToServer(tour, new OnResponseDialog(mainActivity) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        Log.d(MyCallback.TAG, "onSuccess: " + response.getResponseBody());
                        Toast.makeText(mainActivity, "تور با موفقیت اضافه شد",
                                Toast.LENGTH_SHORT, Toast.TYPE_SUCCESS).show();

                        mainActivity.navigateToId(R.id.navigation_home);
                        binding = null;
                        FragmentDataHandler.clear(mainActivity, KEY_ADD_TOUR_FRAGMENT);


                    }

                });

            } else {
                Toast.makeText(mainActivity, getString(R.string.fix_errors), Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();
            }

        });

    }


    private boolean checkInputs() {
        boolean returnValue = !checker.checkAllError();
        if (binding.rclAddTourPlaces.getItemCount() == 0) {
            returnValue = false;
            binding.rclAddTourPlaces.textState(true);
        }
        return returnValue;
    }

    private void setChecker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR , 0);calendar.set(Calendar.MINUTE , 0);calendar.set(Calendar.SECOND , 0);
        TextInputsChecker.Error error = editText -> {
            if (getEditableText(editText.getText()).isEmpty())
                return editText.getHint() + " ضروری است";
            else if (startDate.getCalendar() == null || finalDate.getCalendar() == null)
                return null;
            else if (startDate.getCalendar().getTimestamp() < calendar.getTimeInMillis()
                    || finalDate.getCalendar().getTimestamp() < calendar.getTimeInMillis() ){
                return getString(R.string.date_greater_of_now);
            }
            else if (Utils.isDateGreaterOrEqual(
                    startDate.getCalendar().getGregorianDate(),
                    finalDate.getCalendar().getGregorianDate())) {

                if (editText.equals(binding.etAddTourStartDate)) {
                    binding.tilAddTourFinalDate.setError(getString(R.string.date_error));
                } else {
                    binding.tilAddTourStartDate.setError(getString(R.string.date_error));
                }
                return getString(R.string.date_error);
            } else if (!Utils.isDateGreaterOrEqual(startDate.getCalendar().getGregorianDate(),
                    finalDate.getCalendar().getGregorianDate())) {
                if (editText.equals(binding.etAddTourStartDate)) {
                    binding.tilAddTourFinalDate.setError(null);
                } else {
                    binding.tilAddTourStartDate.setError(null);
                }

            }

            return null;

        };
        checker.add(Arrays.asList(binding.etAddTourDestination, binding.etAddTourCapacity, binding.etAddTourName,
                binding.etAddTourPrice));
        checker.add(binding.etAddTourStartDate, error);
        checker.add(binding.etAddTourFinalDate, error);
    }
}
