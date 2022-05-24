package ir.blackswan.travelapp.Views;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.ybq.android.spinkit.SpinKitView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;

import ir.blackswan.travelapp.R;

public class WebImageView extends LoadableImageView {

    RoundedImageView imageView;
    SpinKitView loadingView;
    LinearLayout errorContainer;
    View downloadAgain;
    TextView tvError;
    View gradient;
    boolean gradientVisibility = false;

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

    public void setScale(float scale) {
        errorContainer.setScaleX(scale);
        errorContainer.setScaleY(scale);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.web_image_view, this);

        if (attrs != null)
            imageView = new RoundedImageView(getContext(), attrs);
        else
            imageView = new RoundedImageView(getContext());
        addView(imageView);
        gradient = new View(getContext());
        gradient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bac_grad_black));
        addView(gradient);
        gradient.setVisibility(GONE);

        errorContainer = findViewById(R.id.ll_image_not_found_wiv);
        errorContainer.setVisibility(GONE);
        downloadAgain = findViewById(R.id.iv_download_again_wiv);
        loadingView = findViewById(R.id.loading_wiv);
        tvError = findViewById(R.id.tv_error_wiv);

    }

    public void setGradient(boolean grad) {
        gradientVisibility = grad;
        setGradientVisible();
    }

    private void setGradientVisible() {
        if (loadingView.getVisibility() == VISIBLE) {
            gradient.setVisibility(GONE);
        } else
            gradient.setVisibility(gradientVisibility ? VISIBLE : GONE);
    }


    void loadingState() {
        imageView.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        errorContainer.setVisibility(GONE);
        gradient.setVisibility(GONE);
        setGradientVisible();
    }

    void errorState(boolean againVisibility, @Nullable String serverPath) {
        if (serverPath != null)
            errorContainer.setOnClickListener(v -> {
                setImagePath(serverPath);
            });

        imageView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        errorContainer.setVisibility(VISIBLE);
        gradient.setVisibility(GONE);
        if (againVisibility) {
            downloadAgain.setVisibility(VISIBLE);
            tvError.setText(R.string.picture_download_failed);
        } else {
            downloadAgain.setVisibility(GONE);
            tvError.setText(R.string.picture_not_found);
        }
        setGradientVisible();

    }

    private void imageState() {
        imageView.setVisibility(VISIBLE);
        loadingView.setVisibility(GONE);
        errorContainer.setVisibility(GONE);
        setGradientVisible();
    }

    public void setImageDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        imageState();

    }

    public void setImageByFile(File pictureFile) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingState();
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap myBitmap = BitmapFactory.decodeFile(pictureFile.getPath());
                return myBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                setImageBitmap(bitmap);
                imageState();
            }
        }.execute();


    }

    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);

        imageState();
    }

    public Drawable getDrawable(){
        return imageView.getDrawable();
    }
    public void setCornerRadius(float radius) {
        imageView.setCornerRadius(radius);
    }


}
