package ir.blackswan.travelapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;

import com.skydoves.powermenu.PowerMenuItem;

import java.io.File;
import java.util.Arrays;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MaterialPersianDateChooser;
import ir.blackswan.travelapp.Utils.MyInputTypes;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.databinding.SettingsActivityBinding;

public class SettingActivity extends ToolbarActivity {
    private static final int FILE_CHOOSE_REQUEST = 1;
    SettingsActivityBinding binding;
    User user;
    private static final int BIO_MAX_LENGTH = 350;
    private MaterialPersianDateChooser birthDate;
    private File selectedClearanceDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        user = AuthController.getUser();
        setVisibilities(false);
        setDataToInputs();
        setInputTypes();
        setListeners();
    }

    private void setInputTypes() {
        birthDate = new MaterialPersianDateChooser(this, binding.etSettingBirthday);
        MyInputTypes.spinner(binding.etSettingGender, Arrays.asList(new PowerMenuItem("مرد"),
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
    }

    private void setDataToInputs() {
        binding.pivSetting.setUser(user);

        binding.etSettingName.setText(user.getFirst_name());
        binding.etSettingLastname.setText(user.getLast_name());
        binding.etSettingEmail.setText(user.getEmail());
        binding.tvSettingBioCounter.setText("0/" + BIO_MAX_LENGTH);

    }


}