package ir.blackswan.travelapp.Utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;

import ir.blackswan.travelapp.R;

import java.util.Objects;

public class MyDialog {

    Dialog dialog;

    void buildAlertDialog(Context context , View view){
        dialog = getAlertDialog(context , view);
    }

    public void buildRoundedSheet(Context context , View view){
        dialog = getRoundSheetDialog(context , view);
    }

    public static Dialog getRoundSheetDialog(Context context , View view){
        Dialog dialog = new RoundedBottomSheetDialog(context);

        dialog.setContentView(view);
        return dialog;
    }

    public static AlertDialog getAlertDialog(Context context, View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    public void showDialog(){
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }


    public Dialog getDialog() {
        return dialog;
    }



}

