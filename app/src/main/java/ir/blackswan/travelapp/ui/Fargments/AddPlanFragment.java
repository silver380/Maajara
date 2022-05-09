package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.FragmentAddPlanBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import ir.blackswan.travelapp.ui.Dialogs.SelectPlacesDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddPlanFragment extends Fragment {
    private FragmentAddPlanBinding binding;
    MaterialPersianDateChooser startDate, finalDate;
    private PlanController planController;
    private AuthActivity authActivity;
    ArrayList<TextInputEditText> wantedInputEditTexts = new ArrayList<>();
    SelectPlacesDialog selectPlacesDialog;
    TextInputsChecker checker = new TextInputsChecker();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        authActivity = ((AuthActivity) getActivity());

        binding.rclPlanPlaces.setLayoutManager(new LinearLayoutManager(authActivity, LinearLayoutManager.HORIZONTAL,
                false));
        selectPlacesDialog = new SelectPlacesDialog(authActivity, v -> {
            Place[] selectedPlaces = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlaces();
            binding.rclPlanPlaces.setAdapter(
                    new PlacesRecyclerAdapter(authActivity, selectedPlaces)
            );
        });

        planController = new PlanController(authActivity);
        setChecker();
        setupDateChooses();
        setListeners();

        return root;
    }

    private void setChecker() {
        TextInputsChecker.Error error = editText -> {
            if (editText.getText() == null || editText.getText().toString().isEmpty())
                return editText.getHint() + " ضروری است";
            else if (startDate.getCalendar() == null || finalDate.getCalendar() == null)
                return null;
            else if (Utils.isDateGreaterOrEqual(
                    startDate.getCalendar().getGregorianDate(),
                    finalDate.getCalendar().getGregorianDate())) {

                if (editText.equals(binding.etPlanStartDate)) {
                    binding.tilPlanFinalDate.setError(getString(R.string.date_error));
                } else {
                    binding.tilPlanStartDate.setError(getString(R.string.date_error));
                }
                return getString(R.string.date_error);
            }else if (!Utils.isDateGreaterOrEqual(startDate.getCalendar().getGregorianDate(),
                    finalDate.getCalendar().getGregorianDate())){
                if (editText.equals(binding.etPlanStartDate)) {
                    binding.tilPlanFinalDate.setError(null);
                } else {
                    binding.tilPlanStartDate.setError(null);
                }

            }

            return null;

        };
        checker.add(binding.etPlanDestination);
        checker.add(binding.etPlanStartDate, error);
        checker.add(binding.etPlanFinalDate, error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setListeners() {
        //wanted list
        binding.ivAddSth.setOnClickListener(v -> {
            View aCase = getLayoutInflater().inflate(R.layout.cases_view, null);
            binding.cvPlanSthContainer.addView(aCase);
            TextInputLayout til = aCase.findViewById(R.id.til_case);
            TextInputEditText inputEditText = aCase.findViewById(R.id.et_case_input);
            wantedInputEditTexts.add(inputEditText);
            til.setHint("مورد " + wantedInputEditTexts.size());
        });

        //places
        binding.ivPlanAddPlace.setOnClickListener(v -> {
            selectPlacesDialog.show();
        });

        //create button
        binding.btnPlanSubmit.setOnClickListener(view -> {
            if (checkInputs()) {

                String Destination = binding.etPlanDestination.getText().toString();

                String sDate = startDate.getGregorianY_M_D();

                String fDate = finalDate.getGregorianY_M_D();

                ArrayList<String> requestedThings = new ArrayList<>();
                for (TextInputEditText t :
                        wantedInputEditTexts) {
                    requestedThings.add(t.getText().toString());
                }

                Place[] places = selectPlacesDialog.getPlacesRecyclerAdapter().getSelectedPlaces();

                Plan plan = new Plan(Destination, sDate, fDate, requestedThings, places);

                planController.addPlanToServer(plan, new OnResponseDialog(authActivity) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        Log.d(MyCallback.TAG, "onSuccess: " + response.getResponseBody());
                        Toast.makeText(authActivity, "برنامه سفر با موفقیت اضافه شد.",
                                Toast.LENGTH_SHORT, Toast.TYPE_SUCCESS).show();
                    }

                });

            }else
                Toast.makeText(authActivity, getString(R.string.fix_errors), Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();

        });
    }

    private void setupDateChooses() {
        startDate = new MaterialPersianDateChooser(authActivity, binding.etPlanStartDate);
        finalDate = new MaterialPersianDateChooser(authActivity, binding.etPlanFinalDate);
    }

    private boolean checkInputs() {
        if (checker.checkAllError())
            return false;
        return binding.rclPlanPlaces.getChildCount() > 0;
    }

}
