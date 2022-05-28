package ir.blackswan.travelapp.ui.Dialogs;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ir.blackswan.travelapp.Utils.Utils.INPUT_TYPE_EMAIL;
import static ir.blackswan.travelapp.Utils.Utils.INPUT_TYPE_NAME;
import static ir.blackswan.travelapp.Utils.Utils.INPUT_TYPE_PASSWORD;
import static ir.blackswan.travelapp.Utils.Utils.getEditableText;

import android.app.Activity;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Stack;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.SharedPrefManager;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.DialogRegisterLoginBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class AuthDialog extends MyDialog {
    public static final int STEP_LOGIN = 0, STEP_REGISTER = 1, STEP_VERIFY = 2;
    DialogRegisterLoginBinding binding;
    private final AuthActivity mActivity;
    int step;
    boolean forLogin;
    private Stack<Integer> stepsStack = new Stack<>();
    private boolean loading = false; //if true, submit button disable
    private final ActiveCodeTimer activeCodeTimer;
    private String[] errors;
    private int[] inputTypes = {INPUT_TYPE_NAME, INPUT_TYPE_NAME, INPUT_TYPE_EMAIL, INPUT_TYPE_PASSWORD};
    private TextInputEditText[] textInputs;

    public ActiveCodeTimer getActiveCodeTimer() {
        return activeCodeTimer;
    }

    private AuthController authController;
    private OnResponseDialog onResponseDialog;

    public AuthDialog(AuthActivity activity, boolean forLogin) {
        binding = DialogRegisterLoginBinding.inflate(activity.getLayoutInflater());
        this.forLogin = forLogin;
        if (forLogin)
            step = STEP_LOGIN;
        else
            step = STEP_REGISTER;

        authController = new AuthController(activity);


        mActivity = activity;
        errors = new String[]
                {activity.getString(R.string.nameError), activity.getString(R.string.lastNameError),
                        activity.getString(R.string.emailError), activity.getString(R.string.passwordError)};
        textInputs = new TextInputEditText[]{binding.etLoginName, binding.etLoginLastName,
                binding.etLoginEmail, binding.etLoginPassword};

        init(activity, binding.getRoot(), DIALOG_TYPE_ROUNDED_BOTTOM_SHEET);

        setFields();
        addListeners();
        setTexts();
        activeCodeTimer = new ActiveCodeTimer();

        dialog.setCancelable(false);


    }

    public void setOnAuthComplete(OnAuthComplete onAuthComplete) {
        onResponseDialog = new OnResponseDialog(mActivity) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                AuthDialog.this.dismiss();
                onAuthComplete.onCompleted(); //IMPORTANT: call this after dismiss!!!!
                Log.d(MyCallback.TAG, "onAuthComplete onSuccess: ");
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                stopLoadingAnimation();
            }
        };
    }

    private void changeTypeAndStep(boolean forLogin, int step, boolean back) {
        dialog.dismiss();
        if (!back)
            stepsStack.add(this.step);
        if (forLogin != this.forLogin)
            stepsStack = new Stack<>();
        this.forLogin = forLogin;
        this.step = step;
        setFields();
        setTexts();
        dialog.show();
    }


    public boolean isLoading() {
        return loading;
    }

    private void setFields() {
        binding.ivLoginBack.setVisibility(stepsStack.isEmpty() ? GONE : VISIBLE);
        clearError();

        if (step == STEP_VERIFY) {
            binding.llLoginEditTexes.setVisibility(GONE);
            binding.llLoginActiveCode.setVisibility(VISIBLE);
            binding.tvLoginSendCode.setText(
                    "کد فعالسازی برای " + binding.etLoginEmail.getText() + " ارسال شد. لطفا آن را وارد کنید"
            );
        } else {
            binding.llLoginActiveCode.setVisibility(GONE);
            binding.llLoginEditTexes.setVisibility(VISIBLE);
            if (step == STEP_LOGIN) {
                binding.tilLoginName.setVisibility(GONE);
                binding.tilLoginLastName.setVisibility(GONE);
            } else {
                binding.tilLoginName.setVisibility(VISIBLE);
                binding.tilLoginLastName.setVisibility(VISIBLE);
            }
        }
    }

    private void addListeners() {

        binding.btnLogin.setOnClickListener(v -> {
            if (loading)
                return;

            if (checkInputs()) {
                startLoadingAnimation();
                if (step == STEP_LOGIN) {
                    authController.login(getEditableText(binding.etLoginEmail.getText()).toLowerCase()
                            , getEditableText(binding.etLoginPassword.getText()), onResponseDialog);

                } else if (step == STEP_REGISTER) {
                    String name = getEditableText(binding.etLoginName.getText());
                    String lastName = getEditableText(binding.etLoginLastName.getText());
                    String email = getEditableText(binding.etLoginEmail.getText()).toLowerCase();
                    authController.register(email,
                            getEditableText(binding.etLoginPassword.getText()), name, lastName, onResponseDialog);
                    //todo: go to login
                } else if (step == STEP_VERIFY) {
                    Editable editable = binding.pinLogin.getText();
                    if (Utils.getEditableText(editable).length() < 6) {
                        stopLoadingAnimation();
                        return;
                    }

                }

            } else {
                //todo
                //getStringErrors error
            }

        });

        binding.btnLoginGoToAnother.setOnClickListener(v -> {
            if (forLogin) {
                changeTypeAndStep(false, STEP_REGISTER, false);
                binding.etLoginName.requestFocus();
                clearError();
            }
            else {
                changeTypeAndStep(true, STEP_LOGIN, false);
                binding.etLoginEmail.requestFocus();
                clearError();
            }

        });


        binding.ivLoginBack.setOnClickListener(v -> {
            if (stepsStack.isEmpty())
                return;

            changeTypeAndStep(forLogin, stepsStack.pop(), true);
        });

        for (int i = 0; i < textInputs.length; i++) {
            int finalI = i;
            textInputs[i].setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus)
                    setErrorIfHas(finalI, true);
            });
            textInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setErrorIfHas(finalI, false);
                }
            });
        }
    }

    public void restart(){
        for (TextInputEditText ti : textInputs) {
            ti.setText("");
        }
        stopLoadingAnimation();
        stepsStack.clear();
        changeTypeAndStep(true , STEP_LOGIN , true);
        binding.etLoginEmail.requestFocus();
        clearError();
    }

    private void setTexts() {
        final String login = "ورود", register = "ثبت‌نام",
                goToLogin = "قبلا ثبت نام کرده‌اید؟";
        final String goToRegister = String.format("حسابی در %s ندارید؟" , mActivity.getString(R.string.app_name));

        if (forLogin) {
            binding.tvLoginTittle.setText(login);
            binding.btnLogin.setText(login);
            binding.tvLoginGoToAnother.setText(goToRegister);
            binding.btnLoginGoToAnother.setText(register);
        } else {
            binding.tvLoginTittle.setText(register);
            binding.btnLogin.setText(register);
            binding.tvLoginGoToAnother.setText(goToLogin);
            binding.btnLoginGoToAnother.setText(login);
        }
        if (step == STEP_REGISTER) {
            binding.btnLogin.setText("ارسال ایمیل تایید");

        } else if (step == STEP_VERIFY) {
            binding.btnLogin.setText("تایید");

        }


    }

    public void stopLoadingAnimation() {
        binding.btnLogin.setTextColor(mActivity.getColor(R.color.colorWhite));
        binding.loadingLogin.setVisibility(GONE);
        loading = false;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void startLoadingAnimation() {
        binding.btnLogin.setTextColor(mActivity.getColor(R.color.colorTransparent));
        binding.loadingLogin.setVisibility(VISIBLE);
        loading = true;
    }

    public void changeToVerify() {
        if (step != STEP_VERIFY) {
            changeTypeAndStep(forLogin, STEP_VERIFY, false);
        }
    }

    public void finish(int resultCode) {
        mActivity.setResult(resultCode);
        mActivity.finish();
    }

    private void clearError() {
        for (int i = 0; i < binding.llLoginEditTexes.getChildCount(); i++) {
            ((TextInputLayout) (binding.llLoginEditTexes.getChildAt(i))).setError(null);
        }
    }

    private boolean setErrorIfHas(int index, boolean show) {
        TextInputLayout textInputLayout = (TextInputLayout) binding.llLoginEditTexes.getChildAt(index);
        if (!Utils.checkEditTextError(textInputs[index], inputTypes[index])) {
            if (show)
                textInputLayout.setError(errors[index]);
            return false;
        }
        textInputLayout.setError(null);
        return true;
    }

    private boolean checkInputs() {

        boolean success = true;
        for (int i = 0; i < inputTypes.length; i++) {
            success = setErrorIfHas(i, true);
        }

        return success;
    }

    public interface OnAuthComplete {
        void onCompleted();
    }

    public class ActiveCodeTimer {
        String email, emailKey = "phoneNumberKey", lastEmailTimeKey = "lastSmsTime";
        long lastEmailTime, dif;
        TextView btnSendAgain;
        Handler handler = new Handler();
        SharedPrefManager sh;

        ActiveCodeTimer() {
            btnSendAgain = binding.btnLoginSendAgain;
            sh = new SharedPrefManager(mActivity);
            lastEmailTime = sh.getLong(lastEmailTimeKey);
            email = sh.getString(emailKey, "");
        }

        void sendCodeAndStartTimer() {
            dif = System.currentTimeMillis() / 1000 - lastEmailTime;

            if (dif >= 30 || !email.equals(Utils.getEditableText(binding.etLoginEmail.getText()))) {
                //registerUser(metName.getText(), metPhone.getText(), RegisterLoginDialog.this);
            } else {
                changeToVerify();
                startTimer();
            }
        }

        public void updateLastSmsTimeAndStartTimer() {
            email = Utils.getEditableText(binding.etLoginEmail.getText());
            lastEmailTime = System.currentTimeMillis() / 1000;
            sh.putString(emailKey, email);
            sh.putLong(lastEmailTimeKey, lastEmailTime);
            startTimer();
        }


        private void startTimer() {
            handler.removeCallbacksAndMessages(null);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    dif = System.currentTimeMillis() / 1000 - lastEmailTime;
                    Log.d("timer", "run: " + dif);
                    if (dif < 120) {
                        btnSendAgain.setOnClickListener(v -> {
                        });
                        btnSendAgain.setTextColor(mActivity.getResources().getColor(R.color.colorHint));
                        btnSendAgain.setText("ارسال مجدد کد(" + Utils.convertTimeInSecondToMMSS(120 - dif) + ")");
                        handler.postDelayed(this, 1000);
                    } else {
                        btnSendAgain.setText("ارسال مجدد کد");
                        btnSendAgain.setOnClickListener(v -> sendCodeAndStartTimer());
                        btnSendAgain.setTextColor(Utils.getThemePrimaryColor(mActivity));
                    }
                }
            });
        }
    }
}

