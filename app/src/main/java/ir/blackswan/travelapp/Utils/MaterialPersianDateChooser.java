package ir.blackswan.travelapp.Utils;

import static ir.blackswan.travelapp.Utils.Utils.getEditableText;

import android.app.Activity;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ir.blackswan.travelapp.R;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.date.PersianDateImpl;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class MaterialPersianDateChooser {
    private TextInputEditText materialEditText;
    private final PersianDatePickerDialog dialog;
    private PersianPickerDate calendar;
    private Activity activity;
    private boolean mustGreaterThanNow = false;

    public MaterialPersianDateChooser(Activity activity, TextInputEditText materialEditText) {
        this.materialEditText = materialEditText;
        materialEditText.setShowSoftInputOnFocus(false);
        this.activity = activity;
        dialog = new PersianDatePickerDialog(activity)
                .setPositiveButtonString(activity.getString(R.string.select))
                .setNegativeButton(activity.getString(R.string.cancel))
                .setTodayButton(activity.getString(R.string.today))
                .setTodayButtonVisible(true)
                .setMinYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Utils.getThemePrimaryColor(this.activity))
                .setTitleColor(ContextCompat.getColor(this.activity, R.color.colorNormalText))
                .setTypeFace(Utils.fontToTypeFace(this.activity, R.font.vazir_bold))
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setListener(new PersianPickerListener() {

                    @Override
                    public void onDateSelected(PersianPickerDate persianPickerDate) {
                        if (mustGreaterThanNow) {
                            GregorianCalendar now = new GregorianCalendar();
                            now.set(Calendar.HOUR_OF_DAY, 0);
                            now.set(Calendar.MINUTE, 0);
                            now.set(Calendar.SECOND, 0);
                            if (persianPickerDate.getTimestamp() < now.getTimeInMillis()) {
                                Toast.makeText(activity, "تاریخ نمی‌تواند برای قبل از امروز باشد",
                                        Toast.LENGTH_SHORT , Toast.TYPE_ERROR).show();
                                showPicker();
                                return;
                            }
                        }
                        MaterialPersianDateChooser.this.calendar = persianPickerDate;
                        materialEditText.setText(calendar.getPersianLongDate());
                        materialEditText.getOnFocusChangeListener().onFocusChange(materialEditText , false);
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

    public void setMustGreaterThanNow(boolean mustGreaterThanNow){
        this.mustGreaterThanNow = mustGreaterThanNow;
    }

    public void load(String date){
        if (date == null)
            return;
        try {
            calendar = new MyPersianCalender(Utils.convertStringToDate(date).getTime());
            materialEditText.setText(calendar.getPersianLongDate());
        } catch (ParseException ignored) { }
    }

    public PersianDatePickerDialog getDialog() {
        return dialog;
    }

    private void showPicker() {
        if (calendar != null)
            dialog.setInitDate(calendar, true);
        dialog.show();
    }

    public void setCalendar(PersianPickerDate calendar) {
        this.calendar = calendar;
    }

    public PersianPickerDate getCalendar() {
        return calendar;
    }


    public String getGregorianY_M_D() {
        if(calendar == null)
            return null;
        return calendar.getGregorianYear() + "-" +
                calendar.getGregorianMonth() + "-" + calendar.getGregorianDay();
    }
}
