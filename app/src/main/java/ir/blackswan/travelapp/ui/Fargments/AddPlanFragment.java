package ir.blackswan.travelapp.ui.Fargments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PlanController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.FragmentAddBinding;
import ir.blackswan.travelapp.databinding.FragmentAddPlanBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.AddTourActivity;
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
    ArrayList<TextInputEditText> inputEditTexts = new ArrayList<>();
    SelectPlacesDialog selectPlacesDialog;

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
        setupDateChooses();
        setListeners();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setListeners(){
        //wanted list
        binding.ivAddSth.setOnClickListener(v ->{
            View aCase = getLayoutInflater().inflate(R.layout.cases_view, null);
            binding.cvPlanSthContainer.addView(aCase);
            TextInputEditText inputEditText = aCase.findViewById(R.id.et_case_input);
            inputEditTexts.add(inputEditText);
        });

        //places
        binding.ivPlanAddPlace.setOnClickListener(v -> {
            selectPlacesDialog.show();
        });

        //create button
        binding.btnPlanSubmit.setOnClickListener(view ->{
            if (checkInputs()) {

                String Destination = binding.etPlanDestination.getText().toString();

                String sDate = startDate.getCalendar().getGregorianYear() + "-" +
                        startDate.getCalendar().getGregorianMonth() + "-" +
                        startDate.getCalendar().getGregorianDay();

                String fDate = finalDate.getCalendar().getGregorianYear() + "-" +
                        finalDate.getCalendar().getGregorianMonth() + "-" +
                        finalDate.getCalendar().getGregorianDay();

                ArrayList<String> requestedThings = new ArrayList<>();
                for (TextInputEditText t:
                        inputEditTexts) {
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

            } else {
                Toast.makeText(authActivity, "لطفا تمام مقادیر را وارد کنید.",
                        Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();
            }
        });
    }

    private void setupDateChooses() {
        startDate = new MaterialPersianDateChooser(binding.etPlanStartDate);
        finalDate = new MaterialPersianDateChooser(binding.etPlanFinalDate);
    }

    private boolean checkInputs() {
        TextInputEditText[] inputEditTexts = new TextInputEditText[]{
                binding.etPlanDestination, binding.etPlanStartDate, binding.etPlanFinalDate
        };

        for (TextInputEditText input : inputEditTexts) {
            if (input.getText().toString().isEmpty())
                return false;
        }

//        return binding.rclPlanPlaces.getChildCount() > 0;

        return true;
    }
}
