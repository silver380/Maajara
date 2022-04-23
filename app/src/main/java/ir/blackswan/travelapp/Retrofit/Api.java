package ir.blackswan.travelapp.Retrofit;

import ir.blackswan.travelapp.Data.Place;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface Api {

    @GET("/place/all/")
    Call<ResponseBody> getAllPlace(@Header("Authorization") String token);

    @POST("/tour/addtour/")
    @FormUrlEncoded
    Call<ResponseBody> addTour(@Header("Authorization") String token,
                               @Field("tour_name") String tour_name,
                               @Field("tour_capacity") int tour_capacity,
                               @Field("price") long price,
                               @Field("destination") String destination,
                               @Field("residence") String residence,
                               @Field("start_date") String start_date,
                               @Field("end_date") String end_date,
                               @Field("has_breakfast") boolean has_breakfast,
                               @Field("has_lunch") boolean has_lunch,
                               @Field("has_dinner") boolean has_dinner,
                               @Field("has_transportation") String has_transportation,
                               @Field("places") Place[] tour_places
    );

    @GET("/tour/confirmedtours/")
    Call<ResponseBody> getConfirmedTour(@Header("Authorization") String token);

    @GET("/tour/pendingtours/")
    Call<ResponseBody> getPendingTour(@Header("Authorization") String token);

    @GET("/pendingusers/")
    Call<ResponseBody> getPendingUsers(@Header("Authorization") String token);

    @GET("/confirmedusers/")
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
                               @Field("biography") String biography ,
                               @Field("languages") String languages,
                               @Field("phone_number") String phoneNumber,
                               @Field("telegram_id") String telegramId,
                               @Field("whatsapp_id") String whatsappId);
    @GET("/auth/info/")
    Call<ResponseBody> info(@Header("Authorization") String token);


}
