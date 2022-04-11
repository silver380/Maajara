package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;

import ir.blackswan.travelapp.Data.Path;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.WebDownloader;

public class WebImageView extends FrameLayout {

    RoundedImageView imageView;
    SpinKitView loadingView;
    LinearLayout errorContainer;
    View downloadAgain;
    TextView tvError;

    public WebImageView(Context context) {
        super(context);
        init(null);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.web_image_view, this);

        if (attrs != null)
            imageView = new RoundedImageView(getContext(), attrs);
        else
            imageView = new RoundedImageView(getContext());
        addView(imageView);

        errorContainer = findViewById(R.id.ll_image_not_found_wiv);

        downloadAgain = findViewById(R.id.iv_download_again_wiv);
        loadingView = findViewById(R.id.loading_wiv);
        tvError = findViewById(R.id.tv_error_wiv);
    }

    public void setImagePath(Path path) {
        loadingState();
        if (path.getLocalPath() != null) {
            setImageByFile(new File(path.getLocalPath()));
        } else if (path.getServerPath() != null) {

            new WebDownloader(getContext(), downloadedFile -> {
                if (downloadedFile != null) {
                    setImageByFile(downloadedFile);
                    path.setLocalPath(downloadedFile.getPath());
                    Log.d("WebDownloader", "setImageLocalPath: " + downloadedFile.getPath());
                } else {
                    errorContainer.setOnClickListener(v -> {
                        setImagePath(path);
                    });
                    errorState(true);
                }
                Log.d("WebDownloader", "setImagePath: onDownloadFinish");
            }).execute(path.getServerPath());
        } else {
            errorState(false);
        }

    }

    private void loadingState() {
        imageView.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        errorContainer.setVisibility(GONE);
    }

    private void errorState(boolean againVisibility) {
        imageView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        errorContainer.setVisibility(VISIBLE);
        if (againVisibility) {
            downloadAgain.setVisibility(VISIBLE);
            tvError.setText(R.string.picture_download_failed);
        }
        else {
            downloadAgain.setVisibility(GONE);
            tvError.setText(R.string.picture_not_found);
        }

    }

    private void imageState() {
        imageView.setVisibility(VISIBLE);
        loadingView.setVisibility(GONE);
        errorContainer.setVisibility(GONE);
    }

    public void setImageDrawable(Drawable drawable) {
        imageState();
        imageView.setImageDrawable(drawable);
    }

    public void setImageByFile(File pictureFile) {
        imageState();
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureFile.getPath());
        setImageBitmap(myBitmap);
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageState();
        imageView.setImageBitmap(bitmap);
    }


}