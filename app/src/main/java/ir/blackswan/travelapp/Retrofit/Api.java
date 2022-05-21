package ir.blackswan.travelapp.Retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Api {

    //todo >> change the URL
    @GET("/???/")
    Call<ResponseBody> getArchiveTours(@Header("Authorization") String token);

    @POST("/auth/increaseticket/")
    Call<ResponseBody> increaseTickets(@Header("Authorization") String token,
                                       @Body RequestBody requestBody);

    @GET("/travelplan/mypendingreqs/")
    Call<ResponseBody> getPendingTLRequests(@Header("Authorization") String token);

    @POST("/travelplan/addplanreq/")
    Call<ResponseBody> addPlanReq(@Header("Authorization") String token, @Body RequestBody planRequest);

    @POST("/travelplan/accepttourleader/")
    @FormUrlEncoded
    Call<ResponseBody> acceptLeader(@Header("Authorization") String token,   @Field("travel_plan_id") int travel_plan_id,
                                         @Field("user_id") int user_id);

    //todo >> change the URL
    @GET("/???/")
    Call<ResponseBody> getConfirmedPlans(@Header("Authorization") String token);

    @GET("/travelplan/mypendingplans/")
    Call<ResponseBody> getPendingPlans(@Header("Authorization") String token);

    @GET("/travelplan/createdplans/")
    Call<ResponseBody> getCreatedPlans(@Header("Authorization") String token);

    @GET("/travelplan/all/")
    Call<ResponseBody> searchPlans(@Header("Authorization") String token , @Query("search") String search);


    @POST("/travelplan/addplan/")
    Call<ResponseBody> addPlan(@Header("Authorization") String token,
                               @Body RequestBody plan);

    @GET("/place/")
    Call<ResponseBody> searchPlaces(@Header("Authorization") String token, @Query("search") String search);

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
    Call<ResponseBody> searchTours(@Header("Authorization") String token , @Query("search") String search);

    @GET("/tour/createdtours/")
    Call<ResponseBody> getCreatedTour(@Header("Authorization") String token);

    @POST("/auth/users/")
    @FormUrlEncoded
    Call<ResponseBody> registerUser(@Field("email") String email, @Field("password") String password
            , @Field("first_name") String firstName, @Field("last_name") String lastName);

    @POST("/auth/token/login/")
    @FormUrlEncoded
    Call<ResponseBody> token(@Field("email") String email, @Field("password") String password);

    @Multipart
    @PATCH("/auth/upgrade/")
    Call<ResponseBody> upgrade(@Header("Authorization") String token,
                               @Part("Body") RequestBody user ,
                               @Part MultipartBody.Part image ,
                               @Part MultipartBody.Part doc
    );


    @GET("/auth/info/")
    Call<ResponseBody> info(@Header("Authorization") String token);


    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Header("Authorization") String token, @Url String fileUrl);

}
