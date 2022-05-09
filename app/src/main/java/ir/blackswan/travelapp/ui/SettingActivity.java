package ir.blackswan.travelapp.ui;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.MyInputTypes;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.SettingsActivityBinding;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SettingActivity extends ToolbarActivity {
    private static final int FILE_CHOOSE_REQUEST = 1;
    SettingsActivityBinding binding;
    User user;
    private static final int BIO_MAX_LENGTH = 350;
    private MaterialPersianDateChooser birthDate;
    private File selectedClearanceDoc;
    private List<Boolean> gender;
    private AuthController authController;
    private TextInputsChecker checker = new TextInputsChecker();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        user = AuthController.getUser();
        authController = new AuthController(this);
        setVisibilities(false);
        setDataToInputs();
        setInputTypes();
        setChecker();
        setListeners();
    }

    private void setInputTypes() {
        birthDate = new MaterialPersianDateChooser(this, binding.etSettingBirthday);
        birthDate.getDialog().setMinYear(1310).setMaxYear(1382);
        gender = MyInputTypes.spinner(binding.etSettingGender, Arrays.asList(new PowerMenuItem("مرد"),
                new PowerMenuItem("زن")));

        MyInputTypes.showFileChooser(binding.etSettingClearanceDoc, this, result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Uri uri = result.getData().getData();
                    Log.d("FileChooser", "File path: " + uri.getPath());
                    File file = new File(uri.getPath());
                    selectedClearanceDoc = file;
                    binding.etSettingClearanceDoc.setText(file.getPath());
                } else {
                    Toast.makeText(this, "خطا در شناسایی فایل. لطفا دوباره سعی کنید", Toast.LENGTH_LONG,
                            Toast.TYPE_ERROR).show();
                }
            }
        });

    }

    private void setVisibilities(boolean visibleCard) {
        int btnVis;
        int leaderVis;
        if (visibleCard) {
            leaderVis = View.VISIBLE;
            btnVis = View.GONE;
        } else {
            if (user.is_tour_leader()) {
                leaderVis = View.VISIBLE;
                btnVis = View.GONE;
            } else {
                leaderVis = View.GONE;
                btnVis = View.VISIBLE;
            }
        }
        TransitionManager.beginDelayedTransition(binding.getRoot());

        binding.crdSettingTourLeader.setVisibility(leaderVis);
        binding.btnSettingLeaderSubmit.setVisibility(leaderVis);
        binding.btnSettingShowLeaderInfo.setVisibility(btnVis);

    }

    private void setListeners() {
        binding.btnSettingShowLeaderInfo.setOnClickListener(v -> setVisibilities(true));
        binding.etSettingBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean error = s.length() > BIO_MAX_LENGTH;
                if (error)
                    binding.tvSettingBioCounter.setTextColor(getColor(R.color.colorError));
                else
                    binding.tvSettingBioCounter.setTextColor(getColor(R.color.colorHint));

                binding.tvSettingBioCounter.setText(s.length() + "/" + BIO_MAX_LENGTH);
            }
        });
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {

            if (buttonView.getId() == R.id.cb_setting_telegram)
                binding.etSettingTelegram.setEnabled(isChecked);
            if (buttonView.getId() == R.id.cb_setting_mobile)
                binding.etSettingMobile.setEnabled(isChecked);
            if (buttonView.getId() == R.id.cb_setting_whatsapp)
                binding.etSettingWhatsapp.setEnabled(isChecked);
        };
        binding.cbSettingMobile.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.cbSettingTelegram.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.cbSettingWhatsapp.setOnCheckedChangeListener(onCheckedChangeListener);

        binding.btnSettingLeaderSubmit.setOnClickListener(v -> {
            if (checkInputs()) {
                String gender = this.gender.get(0) ? "Male" : "Female";
                List<String> connectStrings = getConnectStrings();
                User user = new User(binding.etSettingName.getText().toString(), binding.etSettingLastname.getText().toString(),
                        binding.etSettingEmail.getText().toString(), birthDate.getGregorianY_M_D(), gender
                        , binding.etSettingBio.getText().toString(), new Gson().toJson(binding.etSettingLanguageSkills.toString().split("\\n"))
                        , connectStrings.get(0), connectStrings.get(1), connectStrings.get(2));

                authController.upgrade(user , new OnResponseDialog(this){
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);

                        binding.tvLeaderInfoStatus.setText("در انتظار تایید");
                        binding.tvLeaderInfoStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorWarning)));
                    }
                });
            }else {
                Toast.makeText(this, getString(R.string.fix_errors), Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();
            }
        });
    }

    private List<String> getConnectStrings() {

        return Arrays.asList(binding.cbSettingMobile.isChecked() ? addIranCode(binding.etSettingMobile.getText().toString()) : null,
                binding.cbSettingTelegram.isChecked() ? binding.etSettingTelegram.getText().toString() : null,
                binding.cbSettingWhatsapp.isChecked() ? addIranCode(binding.etSettingMobile.getText().toString()) : null);
    }

    private String addIranCode(String number) {
        return "+98" + number;
    }

    private boolean checkInputs() {
        return !checker.checkAllError();
    }

    private void setChecker(){
        checker.add(Arrays.asList(binding.etSettingBio , binding.etSettingGender , binding.etSettingClearanceDoc ,
                binding.etSettingSsn ));
        checker.add(binding.etSettingBirthday, editText -> {
            if (Utils.getEditableText(editText.getText()).isEmpty())
                return editText.getHint() + " ضروری است";
            else if (Utils.numDaysBetween(birthDate.getCalendar().getGregorianDate().getTime()
                    , Calendar.getInstance().getTime().getTime()) > 365 * 18)
                return "سن شما باید بزرگتر از ۱۸ باشد";

            return null;
        });

    }

    private void setDataToInputs() {
        binding.pivSetting.setUser(user);
        binding.etSettingName.setText(user.getFirst_name());
        binding.etSettingLastname.setText(user.getLast_name());
        binding.etSettingEmail.setText(user.getEmail());
        binding.tvSettingBioCounter.setText("0/" + BIO_MAX_LENGTH);

    }


}