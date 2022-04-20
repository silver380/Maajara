package ir.blackswan.travelapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.ui.Dialogs.AuthDialog;

public abstract class AuthActivity extends AppCompatActivity {
    private AuthDialog authDialog;
    private AuthController authController;


    public AuthController getAuthController() {
        if (authController == null)
            authController = new AuthController(this);
        return authController;
    }

    public void auth(AuthController.OnAuthorization onAuthorization) {
        if (!getAuthController().loadUser(this, onAuthorization))
            showAuthDialog(onAuthorization);


    }

    public void showAuthDialog(AuthController.OnAuthorization onAuthorization){
        if (authDialog == null)
            authDialog = new AuthDialog(this , onAuthorization ,true);
        authDialog.show();
    }
}
