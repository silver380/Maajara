package ir.blackswan.travelapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import ir.blackswan.travelapp.ui.Dialogs.RegisterLoginDialog;

public abstract class HasLoginDialogActivity extends AppCompatActivity {
    private RegisterLoginDialog registerLoginDialog;

    public RegisterLoginDialog getRegisterLoginDialog() {
        if (registerLoginDialog == null)
            registerLoginDialog = new RegisterLoginDialog(this , true);
        return registerLoginDialog;
    }
}
