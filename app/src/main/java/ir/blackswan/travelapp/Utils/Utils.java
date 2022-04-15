package ir.blackswan.travelapp.Utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ir.blackswan.travelapp.R;


public class Utils {
    public static final int BUTTON_STYLE_CONTAINED = 0, BUTTON_STYLE_OUTLINE = 1, BUTTON_STYLE_TEXT = 2;
    public static final int INPUT_TYPE_NAME = 0, INPUT_TYPE_EMAIL = 1, INPUT_TYPE_PASSWORD = 2;

    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, value, true);
        return value.data;
    }

    public static int dp2px(Context context, float dp) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void copy(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("code", text);
        clipboardManager.setPrimaryClip(clip);
    }

    public static Typeface fontToTypeFace(Context context, int fontId) {
        return ResourcesCompat.getFont(context, fontId);
    }

    public static void changeStatusColor(Activity activity, int colorID) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(colorID));

    }

    public static String getEditableText(Editable editable) {
        if (editable != null)
            return editable.toString();
        return "";
    }

    public static void setMaterialButtonStyle(MaterialButton button, int style) {
        int rippleAlpha = 50;
        Context context = button.getContext();
        if (style == BUTTON_STYLE_CONTAINED) {
            button.setBackgroundColor(getThemePrimaryColor(context));
            button.setStrokeColorResource(R.color.colorTransparent);
            button.setTextColor(context.getColor(R.color.colorWhite));
            button.setRippleColor(ColorStateList.valueOf(
                    context.getColor(R.color.colorWhite)).withAlpha(rippleAlpha));
        } else if (style == BUTTON_STYLE_OUTLINE) {
            button.setBackgroundColor(context.getColor(R.color.colorTransparent));
            button.setStrokeColor(ColorStateList.valueOf(getThemePrimaryColor(context)));
            button.setTextColor(getThemePrimaryColor(context));
            button.setRippleColor(ColorStateList.valueOf(
                    getThemePrimaryColor(context)).withAlpha(rippleAlpha));
        } else if (style == BUTTON_STYLE_TEXT) {
            button.setBackgroundColor(context.getColor(R.color.colorTransparent));
            button.setStrokeColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorTransparent)));
            button.setTextColor(getThemePrimaryColor(context));
            button.setRippleColor(ColorStateList.valueOf(
                    getThemePrimaryColor(context)).withAlpha(rippleAlpha));
        }
    }


    public static String convertTimeInSecondToMMSS(long timeInSecond) {
        long m = timeInSecond / 60;
        long s = timeInSecond - m * 60;

        return String.format("%02d:%02d", m, s);
    }


    public static boolean checkEditTextError(TextInputEditText editText, int inputType) {
        boolean success = true;
        switch (inputType) {
            case INPUT_TYPE_NAME:
                success = isValidName(getEditableText(editText.getText()));
                break;
            case INPUT_TYPE_EMAIL:
                success = isValidEmailAddress(getEditableText(editText.getText()));
                break;
            case INPUT_TYPE_PASSWORD:
                success = isValidPassword(getEditableText(editText.getText()));
                break;

        }
        return success;
    }

    public static boolean isValidName(String name) {
        return !name.isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
