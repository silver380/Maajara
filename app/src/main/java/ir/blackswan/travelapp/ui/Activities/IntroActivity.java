package ir.blackswan.travelapp.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.databinding.ActivityIntroBinding;
import ir.blackswan.travelapp.ui.Dialogs.AuthDialog;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class IntroActivity extends AuthActivity {

    ActivityIntroBinding binding;
    private static final int START_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long startTime = System.currentTimeMillis();
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setFullscreen();
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);


        auth(() -> {
            long dif = System.currentTimeMillis() - startTime;
            int delay = 0;
            if (dif < START_DELAY)
                delay = START_DELAY;
            new Handler().postDelayed((Runnable) () ->
                            startActivity(new Intent(this, MainActivity.class)),
                    delay);
        });

    }

    @Override
    public void auth(AuthDialog.OnAuthComplete onAuthComplete) {
        if (!getAuthController().loadUser(this, new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                onAuthComplete.onCompleted();
            }

            @Override
            public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onFailed(call, callback, response);
                binding.loadingIntro.setVisibility(View.GONE);
            }

            @Override
            public void onTryAgain() {
                super.onTryAgain();
                binding.loadingIntro.setVisibility(View.VISIBLE);
            }
        })) {
            new Handler().postDelayed(() -> {
                binding.loadingIntro.setVisibility(View.INVISIBLE);
                showAuthDialog(onAuthComplete);
            }, START_DELAY);

        }
    }

    private void setFullscreen() {

        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }
}