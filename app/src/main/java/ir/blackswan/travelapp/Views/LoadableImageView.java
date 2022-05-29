package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import ir.blackswan.travelapp.Utils.Cacher;
import ir.blackswan.travelapp.Utils.WebFileTransfer;
import ir.blackswan.travelapp.ui.Activities.FullscreenImageActivity;

public abstract class LoadableImageView extends FrameLayout {

    boolean fullScreen = false;
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

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public void setImagePath(String serverPath , boolean fullScreenOnClick) {
        fullScreen = fullScreenOnClick;
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
    public void setImagePath(String serverPath) {
        setImagePath(serverPath , fullScreen);
    }

    abstract void loadingState();

    public void setImageByFile(File file){
        if (fullScreen) {
            setOnClickListener(v -> {

                getContext().startActivity(new Intent(getContext(), FullscreenImageActivity.class).putExtra(
                        FullscreenImageActivity.IMAGE_URL, file.getPath()
                ));

            });
        }
    }

    abstract void errorState(boolean b, @Nullable String servePath);
}
