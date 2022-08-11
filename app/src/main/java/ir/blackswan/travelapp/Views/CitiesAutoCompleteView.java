package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

import ir.blackswan.travelapp.R;

public class CitiesAutoCompleteView {
    private final AutoCompleteTextView autoCompleteTextView;
    private String selectedCityId = null;

    public CitiesAutoCompleteView(AutoCompleteTextView autoCompleteTextView) {
        this.autoCompleteTextView = autoCompleteTextView;

        String apiKey = autoCompleteTextView.getContext().getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(autoCompleteTextView.getContext().getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(autoCompleteTextView.getContext());
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Use the builder to create a FindAutocompletePredictionsRequest.
        setListener(placesClient, token);

    }

    private void setListener(PlacesClient placesClient, AutocompleteSessionToken token) {
        Context context = autoCompleteTextView.getContext();
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                selectedCityId = null;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setCountry("Ir")
                        .setTypeFilter(TypeFilter.CITIES)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();


                placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
                    List<String> citiesList = new ArrayList<>();
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        String cityName = prediction.getFullText(null).toString();
                        citiesList.add(cityName.substring(0, cityName.lastIndexOf(context.getString(R.string.comma))));
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(autoCompleteTextView.getContext(), R.layout.auto_complete_text_view, citiesList);

                    autoCompleteTextView.setThreshold(2);

                    autoCompleteTextView.setAdapter(arrayAdapter);

                    autoCompleteTextView.setOnItemClickListener((parent, view, position, id) ->
                            selectedCityId = response.getAutocompletePredictions().get(position).getPlaceId()
                    );


                }).addOnFailureListener((exception) -> {
                    Log.e("CityResponse", "afterTextChanged: ", exception);
                });
            }
        });

    }


    @Nullable
    public String getSelectedCityId() {
        return selectedCityId;
    }
}
