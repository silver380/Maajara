package ir.blackswan.travelapp.Utils;

import android.app.Activity;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import ir.blackswan.travelapp.R;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class MaterialPersianDateChooser {
    private TextInputEditText materialEditText;
    private final PersianDatePickerDialog dialog;
    private PersianPickerDate calendar;
    private Activity activity;

    public MaterialPersianDateChooser(Activity activity, TextInputEditText materialEditText) {
        this.materialEditText = materialEditText;
        materialEditText.setShowSoftInputOnFocus(false);
        this.activity = activity;
        dialog = new PersianDatePickerDialog(activity)
                .setPositiveButtonString("انتخاب")
                .setNegativeButton("لغو")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Utils.getThemePrimaryColor(this.activity))
                .setTitleColor(ContextCompat.getColor(this.activity, R.color.colorNormalText))
                .setTypeFace(Utils.fontToTypeFace(this.activity, R.font.vazir_bold))
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
        materialEditText.setOnClickListener(v -> {
            showPicker();
        });
    }

    private void showPicker() {
        if (calendar != null)
            dialog.setInitDate(calendar, true);
        dialog.show();
    }

    public PersianPickerDate getCalendar() {
        return calendar;
    }


    public String getGregorianY_M_D() {
        return calendar.getGregorianYear() + "-" +
                calendar.getGregorianMonth() + "-" + calendar.getGregorianDay();
    }
}
