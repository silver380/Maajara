package ir.blackswan.travelapp.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

import ir.blackswan.travelapp.R;

public class PopupMenuCreator {
    public static PowerMenu create(Context context, List<PowerMenuItem> itemList, View view) {
        PowerMenu powerMenu = new PowerMenu.Builder(context)
                .addItemList(itemList) // list has "Novel", "Poerty", "Art"
                .setAnimation(MenuAnimation.DROP_DOWN) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(context, R.color.colorNormalText))
                .setTextGravity(Gravity.RIGHT)
                .setTextTypeface(ResourcesCompat.getFont(context, R.font.vazir_medium))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setTextSize(14)
                .setSelectedMenuColor(Utils.getThemePrimaryColor(context))
                .build();

        return powerMenu;
    }


}


