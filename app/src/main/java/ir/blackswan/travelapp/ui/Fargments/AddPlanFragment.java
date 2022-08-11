package ir.blackswan.travelapp.ui.Fargments;

import static ir.blackswan.travelapp.Utils.Utils.createImageFromBitmap;
import static ir.blackswan.travelapp.Utils.Utils.dp2px;
import static ir.blackswan.travelapp.Utils.Utils.getEditableText;
import static ir.blackswan.travelapp.ui.Fargments.FragmentDataHandler.KEY_ADD_PLAN_FRAGMENT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.PopupMenuCreator;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.Views.CitiesAutoCompleteView;
import ir.blackswan.travelapp.databinding.FragmentAddPlanBinding;
import ir.blackswan.travelapp.ui.Activities.MainActivity;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import ir.blackswan.travelapp.ui.Dialogs.SelectPlacesDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddPlanFragment extends Fragment {
    private FragmentAddPlanBinding binding;
    MaterialPersianDateChooser startDate, finalDate;
    private PlanController planController;
    private MainActivity mainActivity;
    ArrayList<TextInputEditText> wantedInputEditTexts = new ArrayList<>();
    SelectPlacesDialog selectPlacesDialog;
    TextInputsChecker checker = new TextInputsChecker();
    CitiesAutoCompleteView citiesAutoCompleteView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        mainActivity = ((MainActivity) getActivity());

        binding.rclPlanPlaces.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL,
                false));
        binding.rclPlanPlaces.setText(getString(R.string.no_place_select));
        binding.rclPlanPlaces.setErrorText(binding.rclPlanPlaces.getText());
        binding.rclPlanPlaces.textState();


        planController = new PlanController(mainActivity);

        citiesAutoCompleteView = new CitiesAutoCompleteView(binding.etPlanDestination);

        setChecker();
        setupDateChooses();
        setListeners();
        selectPlacesDialog = new SelectPlacesDialog(mainActivity, v -> setPlacesRecyclerAdapter());
        setNoWantedListVisibility();


        return root;
    }

    private void setPlacesRecyclerAdapter() {
        Place[] selectedPlaces = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlacesArray();
        binding.rclPlanPlaces.setAdapter(
                new PlacesRecyclerAdapter(mainActivity, selectedPlaces)
        );
    }


    private void setChecker() {
        TextInputsChecker.Error error = editText -> {
            if (editText.getText() == null || editText.getText().toString().isEmpty())
                return editText.getHint() + getString(R.string.it_is_necessary);
            else if (startDate.getCalendar() == null || finalDate.getCalendar() == null)
                return null;

            else if (Utils.isDateGreater(
                    startDate.getCalendar().getGregorianDate(),
                    finalDate.getCalendar().getGregorianDate())) {

                if (editText.equals(binding.etPlanStartDate)) {
                    binding.tilPlanFinalDate.setError(getString(R.string.date_error));
                } else {
                    binding.tilPlanStartDate.setError(getString(R.string.date_error));
                }
                return getString(R.string.date_error);
            } else if (!Utils.isDateGreater(startDate.getCalendar().getGregorianDate(),
                    finalDate.getCalendar().getGregorianDate())) {
                if (editText.equals(binding.etPlanStartDate)) {
                    binding.tilPlanFinalDate.setError(null);
                } else {
                    binding.tilPlanStartDate.setError(null);
                }

            }

            return null;

        };


        checker.add(binding.etPlanDestination, editText -> {
            if (citiesAutoCompleteView.getSelectedCityId() == null)
                return getString(R.string.city_is_not_valid);
            return null;
        });
        checker.add(binding.etPlanStartDate, error);
        checker.add(binding.etPlanFinalDate, error);


    }

    @Override
    public void onStart() {
        super.onStart();
        load();
    }

    public void save() {
        //todo Bug
        /*
        if (binding == null)
            return;
        Log.d("saveFragments", "save: ");
        FragmentDataHandler.save(KEY_ADD_PLAN_FRAGMENT, new FragmentsData(
                getEditableText(binding.etPlanDestination.getText()),
                startDate.getGregorianY_M_D(),
                finalDate.getGregorianY_M_D(),
                selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlacesArray(),
                getWantedList(true).toArray(new String[0])
        ));

         */
    }

    private void load() {
        /*
        //todo Bug
        FragmentsData fragmentsData = FragmentDataHandler.load(KEY_ADD_PLAN_FRAGMENT);
        Log.d("saveFragments", "load: ");
        if (fragmentsData == null)
            return;
        binding.etPlanDestination.setText(fragmentsData.destination);
        startDate.load(fragmentsData.startDate);
        finalDate.load(fragmentsData.finalDate);
        selectPlacesDialog.load(fragmentsData.selectedPlace);
        if (fragmentsData.wantedList != null)
            for (String s : fragmentsData.wantedList)
                addCase(s);

        setPlacesRecyclerAdapter();

         */

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

    private void setNoWantedListVisibility() {
        if (binding.cvPlanSthContainer.getChildCount() > 0)
            binding.tvAddPlanNoWanted.setVisibility(View.GONE);
        else
            binding.tvAddPlanNoWanted.setVisibility(View.VISIBLE);
    }

    private void beginTransaction() {
        TransitionManager.beginDelayedTransition(binding.mcvAddPlanWanted);
    }

    private void refreshHints() {
        for (int i = 0; i < binding.cvPlanSthContainer.getChildCount(); i++) {
            View aCase = binding.cvPlanSthContainer.getChildAt(i);
            TextInputLayout til = aCase.findViewById(R.id.til_case);
            til.setHint("مورد " + (i + 1));
        }
    }

    private void addCase(String text) {
        View aCase = getLayoutInflater().inflate(R.layout.cases_view, null);
        binding.cvPlanSthContainer.addView(aCase);
        if (binding.cvPlanSthContainer.getChildCount() == 1)
            setNoWantedListVisibility();
        TextInputEditText inputEditText = aCase.findViewById(R.id.et_case_input);
        inputEditText.setText(text);
        wantedInputEditTexts.add(inputEditText);
        beginTransaction();
        ImageView deleteButton = aCase.findViewById(R.id.btn_delete);
        TextInputLayout til = aCase.findViewById(R.id.til_case);
        til.setHint("مورد " + wantedInputEditTexts.size());
        deleteButton.setOnClickListener(view ->
        {
            binding.cvPlanSthContainer.removeView(aCase);
            if (binding.cvPlanSthContainer.getChildCount() == 0)
                setNoWantedListVisibility();
            wantedInputEditTexts.remove(inputEditText);
            beginTransaction();
            refreshHints();
        });
    }

    private void setListeners() {
        //wanted list

        binding.ivAddSth.setOnClickListener(v -> {
            addCase("");
        });


        //places
        binding.ivPlanAddPlace.setOnClickListener(v -> selectPlacesDialog.show());

        //create button
        binding.btnPlanSubmit.setOnClickListener(view -> {
            if (checkInputs()) {

                String Destination = binding.etPlanDestination.getText().toString();

                String sDate = startDate.getGregorianY_M_D();

                String fDate = finalDate.getGregorianY_M_D();


                Place[] places = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlacesArray();

                Plan plan = new Plan(Destination, sDate, fDate, getWantedList(false), places);

                planController.addPlanToServer(plan, new OnResponseDialog(mainActivity) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        Log.d(MyCallback.TAG, "onSuccess: " + response.getResponseBody());
                        Toast.makeText(mainActivity, getString(R.string.plan_added_successfully),
                                Toast.LENGTH_SHORT, Toast.TYPE_SUCCESS).show();

                        mainActivity.navigateToId(R.id.navigation_home);
                        binding = null;
                        FragmentDataHandler.clear(KEY_ADD_PLAN_FRAGMENT);
                    }

                });

            } else
                Toast.makeText(mainActivity, getString(R.string.fix_errors), Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();

        });
    }

    private ArrayList<String> getWantedList(boolean addEmpty) {
        ArrayList<String> requestedThings = new ArrayList<>();
        for (TextInputEditText t :
                wantedInputEditTexts) {
            String s = Utils.getEditableText(t.getText());
            if (!s.isEmpty())
                requestedThings.add(s);
            else if (addEmpty)
                requestedThings.add(s);
        }
        return requestedThings;
    }

    private void setupDateChooses() {
        startDate = new MaterialPersianDateChooser(mainActivity, binding.etPlanStartDate);
        finalDate = new MaterialPersianDateChooser(mainActivity, binding.etPlanFinalDate);
        startDate.setMustGreaterThanNow(true);
        finalDate.setMustGreaterThanNow(true);
    }

    private boolean checkInputs() {
        boolean returnValue = !checker.checkAllError();
        if (binding.rclPlanPlaces.getItemCount() == 0) {
            returnValue = false;
            binding.rclPlanPlaces.textState(true);
        }
        return returnValue;
    }

}
