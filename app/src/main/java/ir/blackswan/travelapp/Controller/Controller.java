package ir.blackswan.travelapp.Controller;

import com.google.gson.Gson;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.Retrofit.Api;
import ir.blackswan.travelapp.Retrofit.RetrofitClient;
import ir.blackswan.travelapp.ui.AuthActivity;
import ir.blackswan.travelapp.ui.Dialogs.ResponseMessageDialog;

public abstract class Controller {

    AuthActivity authActivity;
    static Gson gson = new Gson();
    Api api;
    ResponseMessageDialog responseMessageDialog;

    public Controller(AuthActivity authActivity) {
        this.authActivity = authActivity;
        api = RetrofitClient.getApi();
        responseMessageDialog = new ResponseMessageDialog(authActivity);
    }
}
