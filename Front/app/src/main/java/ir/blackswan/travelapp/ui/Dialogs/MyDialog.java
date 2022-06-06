package ir.blackswan.travelapp.ui.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import ir.blackswan.travelapp.R;

public abstract class MyDialog {
    public static final int DIALOG_TYPE_ALERT = 0,DIALOG_TYPE_BOTTOM_SHEET = 1 ,  DIALOG_TYPE_ROUNDED_BOTTOM_SHEET = 2;
    Dialog dialog;

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public void init(Context context, View view, int type) {
        switch (type) {
            case DIALOG_TYPE_ALERT:
                buildAlertDialog(context, view);
                break;
            case DIALOG_TYPE_ROUNDED_BOTTOM_SHEET:
                buildRoundedBottomSheet(context, view);
                break;
            case DIALOG_TYPE_BOTTOM_SHEET:
                buildBottomSheet(context, view);
                break;
        }
    }

    public void setTransparent(){
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );
    }

    private void buildAlertDialog(Context context, View view) {
        dialog = getAlertDialog(context, view);
    }

    private void buildRoundedBottomSheet(Context context, View view) {
        dialog = getRoundedBottomSheetDialog(context, view);
    }

    private void buildBottomSheet(Context context, View view) {
        dialog = getBottomSheetDialog(context, view);
    }


    public static Dialog getRoundedBottomSheetDialog(Context context, View view) {
        Dialog dialog = new RoundedBottomSheetDialog(context);

        dialog.setContentView(view);
        return dialog;
    }

    public static Dialog getBottomSheetDialog(Context context, View view) {
        Dialog dialog = new BottomSheetDialog(context);

        dialog.setContentView(view);
        return dialog;
    }

    public static AlertDialog getAlertDialog(Context context, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public Dialog getDialog() {
        return dialog;
    }


}

