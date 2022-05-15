package ir.blackswan.travelapp.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ir.blackswan.travelapp.Retrofit.Api;
import ir.blackswan.travelapp.Retrofit.RetrofitClient;
import ir.blackswan.travelapp.Utils.Toast;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;


public abstract class Controller {

    AuthActivity authActivity;
    static Gson gson = new Gson();
    Gson gsonExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    Api api;


    public Controller(AuthActivity authActivity) {
        this.authActivity = authActivity;
        api = RetrofitClient.getApi();
    }

    public void showErrorWithToast(String message){
        Toast.makeText(authActivity , message , Toast.LENGTH_SHORT , Toast.TYPE_ERROR).show();
    }
}
