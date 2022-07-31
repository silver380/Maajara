package ir.blackswan.travelapp.Utils;

import static ir.blackswan.travelapp.Utils.SharedPrefManager.LANGUAGE;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import java.util.Locale;

public class Language {

    public static final String LANG_DEFAULT = "default", LANG_ENGLISH = "en", LANG_PERSIAN = "fa";


    public static void applyLanguage(Context context, String lang) {

        Configuration config = context.getResources().getConfiguration();
        Locale locale;
        if (!lang.equals(LANG_DEFAULT))
            locale = new Locale(lang);
        else
            locale = Locale.getDefault();

        Locale.setDefault(locale);
        config.setLocale(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.createConfigurationContext(config);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        Log.d("updateLanguage", "updateLanguage: "
                + context.getResources().getConfiguration().locale.getLanguage());
    }

    public static String toggleLang(Context context) {
        String lang = context.getResources().getConfiguration().locale.getLanguage();
        if (lang.equals(LANG_ENGLISH))
            lang = LANG_PERSIAN;
        else {
            lang = LANG_ENGLISH;
        }
        saveLang(context, lang);
        applyLanguage(context, lang);
        return lang;
    }

    private static void saveLang(Context context, String lang) {
        SharedPrefManager sh = new SharedPrefManager(context);
        sh.putString(LANGUAGE, lang);
    }

    public static String loadLang(Context context) {
        SharedPrefManager sh = new SharedPrefManager(context);
        return sh.getString(LANGUAGE, LANG_DEFAULT);
    }

    public static Boolean isEnglish(Context context){
        return context.getResources().getConfiguration().locale.getLanguage().equals(LANG_ENGLISH);
    }

}
