package ir.blackswan.travelapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.ui.Dialogs.AuthDialog;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public abstract class AuthActivity extends AppCompatActivity {
    private AuthDialog authDialog;
    private AuthController authController;


    public AuthController getAuthController() {
        if (authController == null)
            authController = new AuthController(this);
        return authController;
    }

    public void auth(AuthDialog.OnAuthComplete onAuthComplete) {
        if (!getAuthController().loadUser(this, new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                onAuthComplete.onCompleted();
            }

        }))
            showAuthDialog(onAuthComplete);


    }

    public AuthDialog getAuthDialog() {
        if(authDialog == null)
            authDialog = new AuthDialog(this, true);
        return authDialog;
    }

    public void showAuthDialog(AuthDialog.OnAuthComplete onAuthComplete) {
        getAuthDialog();

        authDialog.restart();
        authDialog.setOnAuthComplete(onAuthComplete);
        authDialog.show();
    }

    public boolean isAuthing() {
        return AuthController.isLoadingUser() || getAuthDialog().isShowing();
    }
}
