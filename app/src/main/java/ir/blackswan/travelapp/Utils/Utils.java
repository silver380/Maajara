package ir.blackswan.travelapp.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ir.blackswan.travelapp.R;


public class Utils {
    public static final int BUTTON_STYLE_CONTAINED = 0, BUTTON_STYLE_OUTLINE = 1, BUTTON_STYLE_TEXT = 2;
    public static final int INPUT_TYPE_NAME = 0, INPUT_TYPE_EMAIL = 1, INPUT_TYPE_PASSWORD = 2;

    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, value, true);
        return value.data;
    }

    public static boolean isValidMobileNumber(String input) {
        String pattern = "^9[0|1|2|3|9][0-9]{8}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(input);
        return m.matches();
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

    public static void changeStatusColorResource(Activity activity, int colorID) {
        changeStatusColor(activity, activity.getResources().getColor(colorID));
    }

    public static void changeStatusColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);

    }


    public static void setStatusBarColorToTheme(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        final TypedValue value = new TypedValue();
        activity.getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimaryDark, value, true);
        window.setStatusBarColor(value.data);
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


    public static boolean isDateGreater(Date date1, Date date2) {
        return date1.getTime() > date2.getTime();
    }

    public static long numDaysBetween(long fromTime, long toTime) {
        GregorianCalendar c = new GregorianCalendar();
        int result = 0, sign = 1;
        if (toTime < fromTime) {
            sign = -1;
            long t = fromTime;
            fromTime = toTime;
            toTime = t;
        }
        c.setTimeInMillis(toTime);
        final int toYear = c.get(Calendar.YEAR);
        result += c.get(Calendar.DAY_OF_YEAR);
        c.setTimeInMillis(fromTime);
        result -= c.get(Calendar.DAY_OF_YEAR);
        while (c.get(Calendar.YEAR) < toYear) {
            result += c.getActualMaximum(Calendar.DAY_OF_YEAR);
            c.add(Calendar.YEAR, 1);
        }
        result *= sign;
        return result;

    }


    public static boolean openTelegram(Context context, String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + id));
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void openWhatsapp(Context context, String number) {
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void openPhone(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static void openGmail(Context context, String email , String tittle) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.setPackage("com.google.android.gm");
            context.startActivity(Intent.createChooser(intent, tittle));
        } catch (ActivityNotFoundException e) {

        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getFilePath(Context context, String filePrefix, String fileSuffix) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) +
                File.separator + filePrefix + System.currentTimeMillis() + fileSuffix;
    }


    public static Date convertStringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }


    public static String priceToString(long price) {
        StringBuilder priceToString = new StringBuilder();
        char[] priceChar = String.valueOf(price).toCharArray();
        if (priceChar.length < 5)
            return price + "";
        for (int i = priceChar.length - 1; i > -1; i--) {
            int i2 = priceChar.length - 1 - i;
            if (i2 % 3 == 0 && i2 != 0)
                priceToString.insert(0, ",");
            priceToString.insert(0, priceChar[i]);
        }
        return priceToString.toString();
    }

    public static String getPriceString(int price) { //1,300,200
        if (price > 1000000) {
            return priceToString(price / 1000000) + " میلیون تومان";
        }
        else if (price > 1000) {
            return priceToString(price / 1000) + " هزار تومان";
        }

        return priceToString(price) + " تومان";
    }

    public static long getTime() throws Exception {
        String url = "https://time.is/Unix_time_now";
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
        String[] tags = new String[]{
                "div[id=time_section]",
                "div[id=clock0_bg]"
        };
        Elements elements = doc.select(tags[0]);
        for (String tag : tags) {
            elements = elements.select(tag);
        }
        return Long.parseLong(elements.text());
    }

    public static boolean isTimeInPassed(long time){
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        return today.getTimeInMillis() > time;
    }


    public static String createImageFromBitmap(Context context, Bitmap bitmap) {
        String fileName = "myImage";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public static boolean openMapApp(Context context, double latitude, double longitude) {
        String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
