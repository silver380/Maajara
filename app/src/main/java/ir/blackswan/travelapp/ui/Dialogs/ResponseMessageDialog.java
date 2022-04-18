package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;

import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import ir.blackswan.travelapp.databinding.DialogResponseMessgeBinding;


public class ResponseMessageDialog extends MyDialog {
    DialogResponseMessgeBinding binding;
    TextView message;
    SpinKitView loading;
    ImageView status;

    public ResponseMessageDialog(Activity activity) {
        binding = DialogResponseMessgeBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_ALERT);

        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );
        getDialog().setCancelable(false);

    }

    public void dismiss(){
        dialog.dismiss();
    }
}
