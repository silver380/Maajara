package ir.blackswan.travelapp.ui.Dialogs;

import android.app.Activity;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import ir.blackswan.travelapp.databinding.DialogResponseMessgeBinding;


public class LoadingDialog extends MyDialog {
    DialogResponseMessgeBinding binding;

    public LoadingDialog(Activity activity) {
        binding = DialogResponseMessgeBinding.inflate(activity.getLayoutInflater());
        init(activity, binding.getRoot(), DIALOG_TYPE_ALERT);
        setTransparent();
        getDialog().setCancelable(false);
    }

    public void dismiss(){
        dialog.dismiss();
    }


}
