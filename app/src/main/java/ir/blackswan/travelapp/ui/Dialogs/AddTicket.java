package ir.blackswan.travelapp.ui.Dialogs;

import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.TicketController;
import ir.blackswan.travelapp.databinding.DialogAddTicketBinding;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddTicket extends MyDialog {
    DialogAddTicketBinding binding;
    TicketController ticketController;

    public AddTicket(AuthActivity authActivity, OnTicketChange onTicketChange) {
        binding = DialogAddTicketBinding.inflate(authActivity.getLayoutInflater());
        init(authActivity, binding.getRoot(), DIALOG_TYPE_ROUNDED_BOTTOM_SHEET);
        ticketController = new TicketController(authActivity);
        ticketController.increaseTicketToServer(Integer.parseInt(binding.textInputNumOfTicket.getText().toString())
                , new OnResponseDialog(authActivity) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        onTicketChange.onChange();

                    }
                });
    }

    public interface OnTicketChange {
        public void onChange();
    }
}
