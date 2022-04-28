package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;

import ir.blackswan.travelapp.Controller.PathController;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.databinding.DialogPlaceBinding;

public class PlaceDialog extends MyDialog {
    DialogPlaceBinding binding;

    public PlaceDialog(Activity activity, Place place) {
        binding = DialogPlaceBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_BOTTOM_SHEET);

        //binding.ivPlaceImage.setImagePath(place.getPicturePath());
        binding.ivPlaceImage.setImagePath(PathController.getRandomPath(activity));
        binding.tvPlaceName.setText(place.getName());
        binding.tvPlaceCity.setText(place.getCity());
        binding.tvPlaceText.setText(place.getDescription());
    }
}
