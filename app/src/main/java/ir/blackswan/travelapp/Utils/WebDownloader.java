package ir.blackswan.travelapp.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Retrofit.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebDownloader {

    public static void downloadFile(Context context , String url, String filePrefix,
                                    String fileSuffix, OnDownloadFinishListener onDownloadFinishListener) {

        Call<ResponseBody> call = RetrofitClient.getApi().downloadFile(AuthController.getTokenString(), url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    File outputFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) +
                            File.separator + filePrefix + System.currentTimeMillis() + fileSuffix);

                    InputStream inputStream = null;
                    OutputStream outputStream = null;

                    try {
                        byte[] fileReader = new byte[4096];
                        if (response.body() != null) {
                            long fileSize = response.body().contentLength();
                            long fileSizeDownloaded = 0;

                            inputStream = response.body().byteStream();
                            outputStream = new FileOutputStream(outputFile);

                            while (true) {
                                int read = inputStream.read(fileReader);
                                if (read == -1) {
                                    break;
                                }
                                outputStream.write(fileReader, 0, read);
                                fileSizeDownloaded += read;
                                Log.d(MyCallback.TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                            }
                            outputStream.flush();
                            onDownloadFinishListener.onFinish(outputFile);

                        } else {
                            Log.e(MyCallback.TAG, "downloadFile: BODY NULL");
                        }

                    } catch (IOException e) {
                        Log.e(MyCallback.TAG, "downloadFile: ", e);
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        onDownloadFinishListener.onFinish(null);
                    }
                } catch (IOException e) {
                    Log.e(MyCallback.TAG, "downloadFile: ", e);
                    onDownloadFinishListener.onFinish(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                onDownloadFinishListener.onFinish(null);
            }
        });
    }

    public interface OnDownloadFinishListener {
        void onFinish(@Nullable File downloadedFile);
    }
}
