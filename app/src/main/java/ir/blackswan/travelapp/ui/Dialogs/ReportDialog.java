package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;

import ir.blackswan.travelapp.databinding.DialogReportBinding;

public class ReportDialog extends MyDialog {

    DialogReportBinding binding;
    public ReportDialog(Activity activity) {
        binding = DialogReportBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_BOTTOM_SHEET);
    }
}
