package ir.blackswan.travelapp.Controller;

public class MyResponse {
    public static final int NETWORK_ERROR = -1;
    private int code;

    private String responseBody;
    private String errorMessage;

    public MyResponse(int code, String responseOrError , boolean success) {
        this.code = code;
        if (success)
            this.responseBody = responseOrError;
        else
            this.errorMessage = responseOrError;
    }

    public int getCode() {
        return code;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "MyResponse{" +
                "code=" + code +
                ", responseBody='" + responseBody + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
