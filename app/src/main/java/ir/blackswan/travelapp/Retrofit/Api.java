package ir.blackswan.travelapp.Retrofit;

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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Api {

    @POST("/tour/addrate/")
    @FormUrlEncoded
    Call<ResponseBody> sendTourRateReport(@Header("Authorization") String token,
                                    @Field("tour_id") int tour_id,
                                    @Field("tour_rate") int rate,
                                    @Field("tour_report") String report);

    @GET("/tour/getrate/{tour_id}/")
    Call<ResponseBody> getRateStatus(@Header("Authorization") String token,
                                     @Path("tour_id") String tour_id);
    //todo >> change the URL
    @GET("/tour/archivedtl/")
    Call<ResponseBody> getArchiveToursTl(@Header("Authorization") String token);

    @GET("/tour/archiveduser/")
    Call<ResponseBody> getArchiveToursPs(@Header("Authorization") String token);

    @POST("/auth/increaseticket/")
    Call<ResponseBody> increaseTickets(@Header("Authorization") String token , @Body RequestBody ticket);


    @GET("/tour/suggestion/{tour_id}/")
    Call<ResponseBody> getSuggestionTours(@Header("Authorization") String token, @Path("tour_id") String tour_id);

    @Multipart
    @POST("/place/")
    Call<ResponseBody> addPlace(@Header("Authorization") String token,
                                @Part MultipartBody.Part file,
                                @Part MultipartBody.Part name, @Part MultipartBody.Part city , @Part MultipartBody.Part dest
     ,@Part MultipartBody.Part lat , @Part MultipartBody.Part lng );


    @GET("/travelplan/mypendingreqs/")
    Call<ResponseBody> getPendingTLRequests(@Header("Authorization") String token);


    @POST("/travelplan/addplanreq/")
    Call<ResponseBody> addPlanReq(@Header("Authorization") String token, @Body RequestBody planRequest);

    @POST("/travelplan/accepttourleader/")
    @FormUrlEncoded
    Call<ResponseBody> acceptLeader(@Header("Authorization") String token,
                                    @Field("travel_plan_id") int travel_plan_id,
                                    @Field("user_id") int user_id);


    @GET("/travelplan/myconfirmedplans/")
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


    @PATCH("/auth/upgrade/")
    Call<ResponseBody> upgradeData(@Header("Authorization") String token,
                                   @Body RequestBody user );
    @Multipart
    @PATCH("/auth/upgrade/")
    Call<ResponseBody> upgradeFiles(@Header("Authorization") String token,
                                    @Part MultipartBody.Part image ,
                                    @Part MultipartBody.Part doc
    );


    @GET("/auth/info/")
    Call<ResponseBody> info(@Header("Authorization") String token);


    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Header("Authorization") String token, @Url String fileUrl);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadFile(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );
}
