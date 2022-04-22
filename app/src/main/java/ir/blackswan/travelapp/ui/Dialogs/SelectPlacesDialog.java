package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;

import ir.blackswan.travelapp.databinding.FragmentSearchBinding;

public class SelectPlacesDialog extends MyDialog {
    FragmentSearchBinding binding;
    Activity activity;
    public SelectPlacesDialog(Activity activity){
        this.activity = activity;
        binding = FragmentSearchBinding.inflate(activity.getLayoutInflater());
        init(activity , binding.getRoot() , DIALOG_TYPE_ALERT);
    }
}
