package ir.blackswan.travelapp.Controller;

import android.util.Log;

import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TicketController extends Controller {

    public TicketController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void increaseTicketToServer(int value, OnResponse onResponse) {
        Log.d(MyCallback.TAG, "increaseTicketToServer: ");

        api.increaseTickets(AuthController.getTokenString(),
                RequestBody.create(MediaType.parse("application/json"), gson.toJson(new Ticket(value))))
                .enqueue(new MyCallback(authActivity, new OnResponse() {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        int final_ticket = Integer.parseInt(response.getResponseBody());
                        AuthController.getUser().setNumber_of_tickets(final_ticket);
                        onResponse.onSuccess(call, callback, response);
                    }

                    @Override
                    public void onFailed(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        onResponse.onFailed(call, callback, response);
                    }
                }).showLoadingDialog());
    }

    static class Ticket {
        int value;

        public Ticket(int value) {
            this.value = value;
        }
    }
}

