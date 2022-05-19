package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;

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
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
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
    private AuthActivity authActivity;
    private TextInputsChecker checker = new TextInputsChecker();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddTourBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        authActivity = ((AuthActivity) getActivity());
        binding.rclAddTourPlaces.setLayoutManager(new LinearLayoutManager(authActivity, LinearLayoutManager.HORIZONTAL, false));
        binding.rclAddTourPlaces.setText(getString(R.string.no_place_select));
        binding.rclAddTourPlaces.setErrorText(binding.rclAddTourPlaces.getText());
        binding.rclAddTourPlaces.textState();
        selectPlacesDialog = new SelectPlacesDialog(authActivity, v -> {
            Place[] selectedPlaces = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlaces();
            binding.rclAddTourPlaces.setAdapter(
                    new PlacesRecyclerAdapter(authActivity, selectedPlaces)
            );
        });
        tourController = new TourController(authActivity);
        setChecker();
        setupGroupButtons();
        setupDateChooses();
        setListeners();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void setupDateChooses() {
        startDate = new MaterialPersianDateChooser(authActivity, binding.etAddTourStartDate);
        finalDate = new MaterialPersianDateChooser(authActivity, binding.etAddTourFinalDate);
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
                String price = Utils.getEditableText(binding.etAddTourPrice.getText());
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

                Place[] places = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlaces();

                int residenceIndex = groupPlace.getFirstSelectedIndex();
                String residence = residenceIndex == -1 ? "None" : residents[residenceIndex];

                int tranIndex = groupVehicle.getFirstSelectedIndex();
                String transportation = tranIndex == -1 ? "None" : vehicle[tranIndex];

                boolean[] food = groupFood.getSelected();

                Tour tour = new Tour(tourName, capacity, price, destination, sDate, fDate, places, residence,
                        food[0], food[1], food[2], transportation);

                tourController.addTourToServer(tour, new OnResponseDialog(authActivity) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        Log.d(MyCallback.TAG, "onSuccess: " + response.getResponseBody());
                        Toast.makeText(authActivity, "تور با موفقیت اضافه شد",
                                Toast.LENGTH_SHORT, Toast.TYPE_SUCCESS).show();

                    }

                });

            } else {
                Toast.makeText(authActivity, getString(R.string.fix_errors), Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();
            }

        });

    }

    private boolean checkInputs() {
        boolean returnValue = !checker.checkAllError();
        if (binding.rclAddTourPlaces.getItemCount() == 0){
            returnValue = false;
            binding.rclAddTourPlaces.textState(true);
        }
        return returnValue;
    }

    private void setChecker() {
        TextInputsChecker.Error error = editText -> {
            if (Utils.getEditableText(editText.getText()).isEmpty())
                return editText.getHint() + " ضروری است";
            else if (startDate.getCalendar() == null || finalDate.getCalendar() == null)
                return null;
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
