package ir.blackswan.travelapp.ui.Activities;

import static ir.blackswan.travelapp.Utils.Utils.getEditableText;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.powermenu.PowerMenuItem;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Cacher;
import ir.blackswan.travelapp.Utils.FileUtil;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.MyInputTypes;
import ir.blackswan.travelapp.Utils.TextInputsChecker;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.Utils.WebFileTransfer;
import ir.blackswan.travelapp.databinding.SettingsActivityBinding;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SettingActivity extends ToolbarActivity {
    SettingsActivityBinding binding;
    User user;
    private Cacher cacher;
    private static final int BIO_MAX_LENGTH = 350;
    private MaterialPersianDateChooser birthDate;
    private File selectedClearanceDoc, profileFile;
    private List<Boolean> gender;
    private AuthController authController;
    private TextInputsChecker checker = new TextInputsChecker();
    private boolean checkForLeaderInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        user = AuthController.getUser();
        authController = getAuthController();
        setVisibilities(false);
        setInputTypes();
        setListeners();
        setDataToInputs();
        setTourLeaderStatus();
    }

    private void setTourLeaderStatus() {
        //todo: @Sara complete this
    }

    private void setInputTypes() {
        birthDate = new MaterialPersianDateChooser(this, binding.etSettingBirthday);
        birthDate.getDialog().setMinYear(1310).setMaxYear(1390);
        gender = MyInputTypes.spinner(binding.etSettingGender, Arrays.asList(new PowerMenuItem("مرد"),
                new PowerMenuItem("زن")));

        MyInputTypes.showFileChooser(binding.etSettingClearanceDoc, this, "application/pdf", result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    try {
                        Uri uri = result.getData().getData();
                        Log.d("FileChooser", "DocFile path: " + uri.getPath());
                        selectedClearanceDoc = FileUtil.from(this, uri);
                        binding.etSettingClearanceDoc.setText(selectedClearanceDoc.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        });


        MyInputTypes.showFileChooser(binding.ivSettingEditProfile, this, "image/*", result -> {
            if (result.getData() != null) {

                Uri uri = result.getData().getData();
                Log.d("FileChooser", "Image path: " + uri.getPath());
                UCrop.of(uri, Uri.fromFile(new File(Utils.getFilePath(
                        this, "Cropped Image", ".jpg"))))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(512, 512)
                        .start(this);

            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            try {
                final Uri resultUri = UCrop.getOutput(data);
                binding.pivSetting.setImageBitmap(BitmapFactory.decodeFile(resultUri.getPath()));
                profileFile = FileUtil.from(this, resultUri);

            } catch (IOException e) {
                Log.e("FileChooser", "onActivityResult:Crop error ", e);

            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("FileChooser", "onActivityResult:Crop error ", cropError);
            Toast.makeText(this, "خطا در شناسایی تصویر. لطفا دوباره سعی کنید", Toast.LENGTH_LONG,
                    Toast.TYPE_ERROR).show();
        }
    }


    private void setVisibilities(boolean visibleCard) {
        int btnVis;
        int leaderVis;
        if (visibleCard) {
            checkForLeaderInfo = true;
            leaderVis = View.VISIBLE;
            btnVis = View.GONE;
        } else {
            if (user.is_tour_leader()) {
                checkForLeaderInfo = true;
                binding.ivCloseLeader.setVisibility(View.GONE);
                leaderVis = View.VISIBLE;
                btnVis = View.GONE;
            } else {
                checkForLeaderInfo = false;
                leaderVis = View.GONE;
                btnVis = View.VISIBLE;
            }
        }
        setChecker();
        TransitionManager.beginDelayedTransition(binding.getRoot());

        binding.crdSettingTourLeader.setVisibility(leaderVis);
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

        binding.etSettingTelegram.setEnabled(false);
        binding.etSettingMobile.setEnabled(false);
        binding.etSettingWhatsapp.setEnabled(false);

        binding.cbSettingMobile.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.cbSettingTelegram.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.cbSettingWhatsapp.setOnCheckedChangeListener(onCheckedChangeListener);

        binding.btnSettingLeaderSubmit.setOnClickListener(v -> {
            if (checkInputs()) {

                String gender = this.gender.get(0) ? "Male" : "Female";
                List<String> connectStrings = getConnectStrings();

                User user;
                if (checkForLeaderInfo)
                    user = new User(binding.etSettingName.getText().toString(), binding.etSettingLastname.getText().toString(),
                            AuthController.getUser().getEmail(), binding.etSettingSsn.getText().toString(),
                            birthDate.getGregorianY_M_D(), gender, binding.etSettingBio.getText().toString()
                            , binding.etSettingLanguageSkills.getText().toString()
                            , connectStrings.get(0), connectStrings.get(1), connectStrings.get(2));
                else
                    user = new User(binding.etSettingName.getText().toString(), binding.etSettingLastname.getText().toString(),
                            AuthController.getUser().getEmail());


                authController.upgrade(user, profileFile, selectedClearanceDoc, new OnResponseDialog(this) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        Toast.makeText(SettingActivity.this, "تغییرات با موفقیت ذخیره شد"
                                , Toast.LENGTH_SHORT, Toast.TYPE_SUCCESS).show();
                        setResult(RESULT_OK);
                    }

                });
            } else {
                Toast.makeText(this, getString(R.string.fix_errors), Toast.LENGTH_SHORT, Toast.TYPE_ERROR).show();
            }
        });

        binding.ivSettingDeleteProfile.setOnClickListener(v -> {
            profileFile = null;
            binding.pivSetting.setData(null, user.getNameAndLastname());
        });

        binding.ivCloseLeader.setOnClickListener(v -> {
            setVisibilities(false);
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

    private void setChecker() {
        checker = new TextInputsChecker();
        if (checkForLeaderInfo) {
            checker.add(Arrays.asList(binding.etSettingName, binding.etSettingLastname,
                    binding.etSettingBio, binding.etSettingGender, binding.etSettingClearanceDoc));
            checker.add(binding.etSettingSsn, editText -> {
                if (getEditableText(editText.getText()).length() != 10)
                    return "کد ملی حتما باید ۱۰ رقم باشد";
                else
                    return null;
            });
            checker.add(binding.etSettingBirthday, editText -> {
                if (getEditableText(editText.getText()).isEmpty())
                    return editText.getHint() + " ضروری است";
                else if (Utils.numDaysBetween(birthDate.getCalendar().getGregorianDate().getTime()
                        , Calendar.getInstance().getTime().getTime()) < 365 * 18)
                    return "سن شما باید بزرگتر از ۱۸ باشد";

                return null;
            });
        } else {
            checker.add(Arrays.asList(binding.etSettingName, binding.etSettingLastname));
        }

    }

    private void setDataToInputs() {
        binding.pivSetting.setData(user.getPicture(), user.getNameAndLastname());
        binding.etSettingName.setText(user.getFirst_name());
        binding.etSettingLastname.setText(user.getLast_name());
        binding.etSettingEmail.setText(user.getEmail());
        binding.tvSettingBioCounter.setText("0/" + BIO_MAX_LENGTH);
        binding.etSettingSsn.setText(user.getSsn());
        binding.etSettingBio.setText(user.getBiography());
        if (birthDate.getCalendar() != null) {
            birthDate.setCalendar(user.getPersianBirthDate());
            binding.etSettingBirthday.setText(birthDate.getCalendar().getPersianLongDate());
        }
        binding.etSettingLanguageSkills.setText(user.getLanguagesWithEnter());
        if (user.getGender().equals("Male"))
            gender.set(0, true);
        else if (user.getGender().equals("Female"))
            gender.set(1, true);
        binding.etSettingGender.setText(user.getPersianGender());
        setContactInfo(user.getPhone_number(), binding.cbSettingMobile, binding.etSettingMobile);
        setContactInfo(user.getWhatsapp_id(), binding.cbSettingWhatsapp, binding.etSettingWhatsapp);
        setContactInfo(user.getTelegram_id(), binding.cbSettingTelegram, binding.etSettingTelegram);


        cacher = new Cacher(this);
        String docFilePath = cacher.getLocalPathByServerPath(user.getCertificate());
        if (docFilePath == null) {
            WebFileTransfer.downloadFile(this, user.getCertificate(), "certificate", ".pdf",
                    file -> {
                        if (file != null) {
                            cacher.saveLocalPath(user.getCertificate(), file.getPath());
                            selectedClearanceDoc = file;
                            binding.etSettingClearanceDoc.setText(selectedClearanceDoc.getName());
                        }
                        binding.pbSettingDoc.setVisibility(View.GONE);
                    });
        } else {
            selectedClearanceDoc = new File(docFilePath);
            binding.etSettingClearanceDoc.setText(selectedClearanceDoc.getName());
            binding.pbSettingDoc.setVisibility(View.GONE);
        }
    }

    private void setContactInfo(String contactInfo, MaterialCheckBox checkBox, TextInputEditText editText) {
        if (!isStringEmptyOrNull(contactInfo)) {
            editText.setEnabled(true);
            editText.setText(user.getPhone_number());
            checkBox.setChecked(true);
        } else {
            editText.setEnabled(false);
            checkBox.setChecked(false);
        }
    }

    private static boolean isStringEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }

}