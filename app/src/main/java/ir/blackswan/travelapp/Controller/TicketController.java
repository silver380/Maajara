package ir.blackswan.travelapp.Controller;

import android.util.Log;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TicketController extends Controller {

    public TicketController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void increaseTicketToServer(int number_of_tickets, OnResponse onResponse){
        Log.d(MyCallback.TAG, "increaseTicketToServer: ");
        api.increaseTickets(AuthController.getTokenString(), number_of_tickets)
                .enqueue(new MyCallback(authActivity, new OnResponse() {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        onResponse.onSuccess(call, callback, response);
                        int final_ticket = Integer.parseInt(response.getResponseBody());
                        AuthController.getUser().setTicket(final_ticket);
                    }

                    @Override
                    public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        onResponse.onFailed(call, callback, response);
                    }}));
    }


}
