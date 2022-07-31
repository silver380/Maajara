package ir.blackswan.travelapp.ui.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Language;

public abstract class ToolbarActivity extends AuthActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar(this);
    }

    public static void setupToolbar(Activity activity) {
        Toolbar tb = activity.findViewById(R.id.tb_activity);
        if (activity instanceof AppCompatActivity) {
            ActionBar actionbar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionbar != null)
                actionbar.setDisplayHomeAsUpEnabled(true);
        }

        if (tb != null) {
            if (Language.loadLang(activity).equals(Language.LANG_ENGLISH))
                tb.setNavigationIcon(getRotateDrawable(tb.getNavigationIcon(), 180));
            tb.setNavigationOnClickListener(view -> activity.onBackPressed());
        }
    }

    private static Drawable getRotateDrawable(final Drawable d, final float angle) {
        final Drawable[] arD = {d};
        return new LayerDrawable(arD) {
            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(angle, d.getBounds().width() / 2, d.getBounds().height() / 2);
                super.draw(canvas);
                canvas.restore();
            }
        };
    }
}
