package ir.blackswan.travelapp.Retrofit;

import ir.blackswan.travelapp.Data.Tour;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @GET("/place/all/")
    Call<ResponseBody> getAllPlace(@Header("Authorization") String token);

    @POST("/tour/register/")
    @FormUrlEncoded
    Call<ResponseBody> register(@Header("Authorization") String token, @Field("tour_id") int tour_id);


    @POST("/tour/addtour/")
    Call<ResponseBody> addTour(@Header("Authorization") String token,
                               @Body RequestBody tour
    );


    @POST("/tour/acceptuser/")
    @FormUrlEncoded
    Call<ResponseBody> acceptUser(@Header("Authorization") String token,
                                  @Field("tour_id") int tour_id,
                                  @Field("user_id") int user_id
    );

    @GET("/tour/confirmedtours/")
    Call<ResponseBody> getConfirmedTour(@Header("Authorization") String token);

    @GET("/tour/pendingtours/")
    Call<ResponseBody> getPendingTour(@Header("Authorization") String token);

    @GET("/tour/pendingusers/")
    Call<ResponseBody> getPendingUsers(@Header("Authorization") String token);

    @GET("/tour/confirmedusers/")
    Call<ResponseBody> getConfirmedUsers(@Header("Authorization") String token);

    @GET("/tour/all/")
    Call<ResponseBody> getAllTour(@Header("Authorization") String token);

    @GET("/tour/createdtours/")
    Call<ResponseBody> getCreatedTour(@Header("Authorization") String token);

    @POST("/auth/register/")
    @FormUrlEncoded
    Call<ResponseBody> registerUser(@Field("email") String email, @Field("password") String password
            , @Field("first_name") String firstName, @Field("last_name") String lastName);

    @POST("/auth/token/")
    @FormUrlEncoded
    Call<ResponseBody> token(@Field("username") String email, @Field("password") String password);

    @POST("/auth/upgrade/")
    @FormUrlEncoded
    Call<ResponseBody> upgrade(@Header("Authorization") String token,
                               @Field("date-of-birth") String date,
                               @Field("gender") String gender,
                               @Field("biography") String biography,
                               @Field("languages") String languages,
                               @Field("phone_number") String phoneNumber,
                               @Field("telegram_id") String telegramId,
                               @Field("whatsapp_id") String whatsappId);

    @GET("/auth/info/")
    Call<ResponseBody> info(@Header("Authorization") String token);


}
