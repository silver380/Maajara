package ir.blackswan.travelapp.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface Api {

    @POST("/tour/")
    @FormUrlEncoded
    Call<ResponseBody> createTour(@Header("Authorization") String token , @Field("tour") String tour);//test

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