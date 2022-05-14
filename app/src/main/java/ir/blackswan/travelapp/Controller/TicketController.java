package ir.blackswan.travelapp.Controller;

import android.util.Log;

import ir.blackswan.travelapp.ui.Activities.AuthActivity;

public class TicketController extends Controller {

    public TicketController(AuthActivity authActivity) {
        super(authActivity);
    }

    public void increaseTicketToServer(int number_of_tickets, OnResponse onResponse){
        Log.d(MyCallback.TAG, "increaseTicketToServer: ");
        api.increaseTickets(AuthController.getTokenString(), number_of_tickets)
                .enqueue(new MyCallback(authActivity, onResponse));
    }


}
