package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import ir.blackswan.travelapp.Utils.Cacher;
import ir.blackswan.travelapp.Utils.WebFileTransfer;

public abstract class LoadableImageView extends FrameLayout {

    public LoadableImageView(@NonNull Context context) {
        super(context);
    }

    public LoadableImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadableImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadableImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setImagePath(String serverPath) {
        loadingState();
        Cacher cacher = new Cacher(getContext());
        String localPath = null;
        if (serverPath != null)
            localPath = cacher.getLocalPathByServerPath(serverPath);
        if (localPath != null) {
            File imageFile = new File(localPath);
            if (imageFile.exists())
                setImageByFile(new File(localPath));
            else {
                cacher.saveLocalPath(serverPath, null);
                setImagePath(serverPath);
            }
        } else if (serverPath != null) {

            WebFileTransfer.downloadFile(getContext(), serverPath, "Image ", ".jpg", downloadedFile -> {
                if (downloadedFile != null) {
                    setImageByFile(downloadedFile);
                    cacher.saveLocalPath(serverPath, downloadedFile.getPath());
                    Log.d("WebDownloader", "setImageLocalPath: " + downloadedFile.getPath());
                } else {

                    errorState(true, serverPath);
                }
                Log.d("WebDownloader", "setImagePath: onDownloadFinish");
            });

        } else {
            errorState(false, null);
        }
    }

    abstract void loadingState();

    abstract void setImageByFile(File file);

    abstract void errorState(boolean b, @Nullable String servePath);
}
