package ir.blackswan.travelapp.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
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

public class WebFileTransfer {


    public static void downloadFile(Context context, String url, String filePrefix,
                                    String fileSuffix, OnDownloadFinishListener onDownloadFinishListener) {

        Call<ResponseBody> call = RetrofitClient.getApi().downloadFile(AuthController.getTokenString(), url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                new AsyncTask<Void, Void, File>() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    protected File doInBackground(Void[] objects) {
                        File outputFile = new File(Utils.getFilePath(context, filePrefix, fileSuffix));

                        InputStream inputStream;
                        OutputStream outputStream;

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

                                inputStream.close();
                                outputStream.close();


                            } else {
                                Log.e(MyCallback.TAG, "downloadFile: BODY NULL");
                                return null;
                            }

                        } catch (IOException e) {
                            Log.e(MyCallback.TAG, "downloadFile: ", e);
                            return null;
                        }

                        return outputFile;
                    }

                    @Override
                    protected void onPostExecute(File file) {
                        super.onPostExecute(file);
                        onDownloadFinishListener.onFinish(file);
                    }
                }.execute();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                onDownloadFinishListener.onFinish(null);
            }
        });

    }

    /*
    public static void uploadFile(File file, String fileType , OnUploadFinishListener onUploadFinishListener) {
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(fileType), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("files[0]", file.getName(), requestFile);
        RetrofitClient.getApi().uploadFile(AuthController.getTokenString() , body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    }
                });
    }

     */

    public interface OnDownloadFinishListener {
        void onFinish(@Nullable File file);
    }

    public interface OnUploadFinishListener {
        void onFinish(@Nullable File file, String serverPath);
    }
}
