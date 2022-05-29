package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.databinding.DialogPlaceBinding;
import ir.blackswan.travelapp.ui.Activities.FullscreenImageActivity;

public class PlaceDialog extends MyDialog {
    DialogPlaceBinding binding;

    public PlaceDialog(Activity activity, Place place) {
        binding = DialogPlaceBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_BOTTOM_SHEET);

        binding.ivPlaceImage.setFullScreen(true);
        binding.ivPlaceImage.setImagePath(place.getPicture());
        binding.tvPlaceName.setText(place.getName());
        binding.tvPlaceCity.setText(place.getCity());
        binding.tvPlaceText.setText(place.getDescription());
    }
}
