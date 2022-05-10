package ir.blackswan.travelapp.Utils;

import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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


    public static void showFileChooser(View view, AppCompatActivity activity
            , String intentType, ActivityResultCallback<ActivityResult> callback) {

        ActivityResultLauncher<Intent> launcher =
                activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
        view.setOnClickListener(v -> {
            try {
                if(PermissionRequest.storage(activity , 0)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType(intentType);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    launcher.launch(Intent.createChooser(intent, "انتخاب"));
                }
            } catch (android.content.ActivityNotFoundException ignored) {
                Toast.makeText(activity , "خطا در اجرای انتخابگر فایل. انتخابگری جدید نصب کنید" ,
                        Toast.LENGTH_LONG , Toast.TYPE_ERROR).show();
            }
        });

    }

}
