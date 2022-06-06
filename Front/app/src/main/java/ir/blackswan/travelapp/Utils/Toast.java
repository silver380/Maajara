package ir.blackswan.travelapp.Utils;

import android.content.Context;
import android.view.Gravity;

import ir.blackswan.travelapp.R;

import io.github.muddz.styleabletoast.StyleableToast;


public class Toast {

    public static final int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;
    public static final int TYPE_WARNING = 1;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_SUCCESS = 3;
    public static final int TYPE_INFO = 4;


    public static StyleableToast.Builder makeText(Context context, String text, int length , int type) {

        int color;
        if (type == TYPE_ERROR)
            color = context.getResources().getColor(R.color.colorError);
        else if (type == TYPE_SUCCESS)
            color = context.getResources().getColor(R.color.colorSuccess);
        else if (type == TYPE_INFO)
            color = Utils.getThemePrimaryColor(context);
        else if (type == TYPE_WARNING)
            color = context.getResources().getColor(R.color.colorWarning);
        else
            color = context.getResources().getColor(R.color.colorNormalText);

        return new StyleableToast.Builder(context)
                .length(length)
                .font(R.font.vazir_light)
                .gravity(Gravity.BOTTOM)
                .textSize(14)
                .backgroundColor(color)
                .textColor(context.getResources().getColor(R.color.colorWhite))
                .cornerRadius(10)
                .text(text)
                .build();

    }

}
