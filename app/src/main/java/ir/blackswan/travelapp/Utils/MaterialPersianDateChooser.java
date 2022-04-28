package ir.blackswan.travelapp.Utils;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import ir.blackswan.travelapp.R;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class MaterialPersianDateChooser {
    private TextInputEditText materialEditText;
    private PersianDatePickerDialog dialog;
    private PersianPickerDate calendar;
    private Context context;

    public MaterialPersianDateChooser(TextInputEditText materialEditText){
        this.materialEditText = materialEditText;
        context = materialEditText.getContext();
        dialog = new PersianDatePickerDialog(context)
                .setPositiveButtonString("انتخاب")
                .setNegativeButton("لغو")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Utils.getThemePrimaryColor(context))
                .setTitleColor(ContextCompat.getColor(context , R.color.colorNormalText))
                .setTypeFace(Utils.fontToTypeFace(context , R.font.vazir_bold))
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setListener(new PersianPickerListener() {

                    @Override
                    public void onDateSelected(PersianPickerDate persianPickerDate) {
                        calendar = persianPickerDate;
                        materialEditText.setText(calendar.getPersianLongDate());

                    }
                    @Override
                    public void onDismissed() {

                    }
                })
                .setShowInBottomSheet(true);

        materialEditText.setFocusable(false);
        materialEditText.setOnClickListener(v -> showPicker());
    }

    private void showPicker(){
        if (calendar != null)
            dialog.setInitDate(calendar , true);
        dialog.show();
    }

    public PersianPickerDate getCalendar() {
        return calendar;
    }
}
