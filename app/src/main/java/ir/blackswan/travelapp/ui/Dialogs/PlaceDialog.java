package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.databinding.DialogPlaceBinding;

public class PlaceDialog extends MyDialog {
    DialogPlaceBinding binding;

    public PlaceDialog(Activity activity, Place place) {
        binding = DialogPlaceBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_BOTTOM_SHEET);

        binding.ivPlaceImage.setImagePath(place.getPicturePath());
        binding.tvPlaceName.setText(place.getName());
        binding.tvPlaceCity.setText(place.getCityName());
        binding.tvPlaceText.setText(place.getDetails());
    }
}
