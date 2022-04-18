package ir.blackswan.travelapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.ui.Dialogs.AuthDialog;

public abstract class AuthActivity extends AppCompatActivity {
    private AuthDialog authDialog;
    private AuthController authController;

    public AuthDialog getAuthDialog() {
        if (authDialog == null)
            authDialog = new AuthDialog(this , true);
        return authDialog;
    }

    public AuthController getAuthController() {
        if (authController == null)
            authController = new AuthController(this);
        return authController;
    }
}
