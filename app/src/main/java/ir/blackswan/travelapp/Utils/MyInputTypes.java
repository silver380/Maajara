package ir.blackswan.travelapp.Utils;

import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.List;

import ir.blackswan.travelapp.ui.AuthActivity;

public class MyInputTypes {

    public static List<Boolean> spinner(TextInputEditText editText, List<PowerMenuItem> options) {
        ArrayList<Boolean> selected = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            selected.add(false);
        }

        PowerMenu powerMenu = PopupMenuCreator.create(editText.getContext(), options, editText);
        powerMenu.setTextGravity(Gravity.CENTER);
        powerMenu.setTextSize(15);

        editText.setOnClickListener(v -> {
            powerMenu.showAsDropDown(v);
            powerMenu.setOnMenuItemClickListener((position, item) -> {
                powerMenu.dismiss();
                for (int i = 0; i < options.size(); i++) {
                    selected.set(i, false);
                }
                selected.set(position, true);
                editText.setText(options.get(position).getTitle());
            });
        });
        return selected;
    }


    public static void showFileChooser(TextInputEditText editText, AuthActivity activity, ActivityResultCallback<ActivityResult> callback) {

        ActivityResultLauncher<Intent> launcher =
                activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
        editText.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                launcher.launch(intent);
            } catch (android.content.ActivityNotFoundException ignored) {
                Toast.makeText(activity , "خطا در اجرای انتخابگر فایل. انتخابگری جدید نصب کنید" ,
                        Toast.LENGTH_LONG , Toast.TYPE_ERROR).show();
            }
        });

    }

    private void setIntentType(Intent intent) {

        String[] mimeTypes =
                {"application/pdf"};


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

    }

}
