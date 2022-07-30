package ir.blackswan.travelapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

import ir.blackswan.travelapp.Utils.SharedPrefManager;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        updateLanguage(this);
        super.onCreate();
    }

    public static void updateLanguage(Context ctx)
    {
        SharedPrefManager sh = new SharedPrefManager(ctx);
        String lang = sh.getString("locale_override", "");
        updateLanguage(ctx, lang);
    }

    public static void updateLanguage(Context ctx, String lang)
    {
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            cfg.locale = new Locale(lang);
        else
            cfg.locale = Locale.getDefault();

        Log.d("Language", "updateLanguage: " + cfg.locale.getLanguage());
        ctx.getResources().updateConfiguration(cfg, null);
    }
}
