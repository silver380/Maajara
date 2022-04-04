package ir.blackswan.travelapp.Utils;

import android.content.Context;
import android.util.TypedValue;

import ir.blackswan.travelapp.R;


public class Utils {
    public static int getThemePrimaryColor (final Context context) {
        final TypedValue value = new TypedValue ();
        context.getTheme ().resolveAttribute (androidx.appcompat.R.attr.colorPrimary , value, true);
        return value.data;
    }
}
