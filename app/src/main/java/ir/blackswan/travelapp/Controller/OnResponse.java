package ir.blackswan.travelapp.Controller;

public interface OnResponse {
    void onSuccess(String responseBody);

    void onFailed(String message);
}