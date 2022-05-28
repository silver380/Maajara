package ir.blackswan.travelapp.Retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    private final static String URL = "https://tourleaderapp.herokuapp.com/" ,
    URL2 = "http://maajara.pythonanywhere.com/";
    private final static String LOCAL_URL = "http://10.0.2.2:8000/";
    private static final Api api;

    static {
        Retrofit retrofit = RetrofitClient.getInstance();
        api = retrofit.create(Api.class);
    }

    public static Api getApi() {
        return api;
    }

    public static Retrofit getInstance() {
        if (instance == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .build();

            instance = new Retrofit.Builder()
                    .baseUrl(URL2)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return instance;
    }
}
