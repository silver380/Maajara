package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;
import android.view.View;

import ir.blackswan.travelapp.databinding.DialogResponseMessgeBinding;

public class AddTicket extends MyDialog {
    DialogResponseMessgeBinding binding;

    public AddTicket(Activity activity, View.OnClickListener onAddClick) {
        binding = DialogResponseMessgeBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_ROUNDED_BOTTOM_SHEET);
        // todo
    }
}
