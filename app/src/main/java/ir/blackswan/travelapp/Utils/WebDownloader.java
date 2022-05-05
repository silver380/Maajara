package ir.blackswan.travelapp.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ir.blackswan.travelapp.Controller.MyCallback;

public class WebDownloader extends AsyncTask<String, Void, File> {

    Context context;
    OnDownloadFinishListener onFinish;
    String token;

    public WebDownloader(Context context , String token, OnDownloadFinishListener onFinish) {
        this.context = context;
        this.onFinish = onFinish;
        this.token = token;
    }

    @Nullable
    public File downloadFile(String url) {
        File outputFile;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            Log.d("WebDownloader", "downloadFile:TOKEN " + token);
            conn.setRequestProperty("Authorization", token);
            conn.setRequestMethod("GET");

            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();
            outputFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) +
                    File.separator + "Image" + System.currentTimeMillis() / 1000 + ".jpg");

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (IOException e) {

            Log.e("WebDownloader", "downloadFile: ", e);
            return null; // swallow a 404
        }
        return outputFile;
    }

    @Override
    protected File doInBackground(String... path) {
        return downloadFile(path[0]);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        onFinish.onFinish(file);
    }

    public interface OnDownloadFinishListener {
        void onFinish(File downloadedFile);
    }
}
