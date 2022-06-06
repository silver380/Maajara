package ir.blackswan.travelapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.ui.Activities.FullscreenImageActivity;

public class ProfileImageView extends LoadableImageView {

    private ImageView imageView;
    private AutoResizeTextView textView;
    private MaterialCardView cardView;
    @Nullable
    private String imageServerPath;
    private String fullName;
    private float maxTextSize = 60;


    public ProfileImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public ProfileImageView(@NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileImageView(@NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ProfileImageView(@NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    void loadingState() {
        noImageState();
    }


    private void init() {
        inflate(getContext(), R.layout.view_image_profile, this);
        textView = findViewById(R.id.profile_text);
        imageView = findViewById(R.id.profile_image);
        cardView = findViewById(R.id.profile_card);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardView.setRadius(cardView.getWidth() / 2f);
            }
        });
    }

    public void setDataByUser(User user) {
        setData(user.getPicture(), user.getNameAndLastname());
    }

    public void setData(@Nullable String imageServerPath, String fullName) {
        this.imageServerPath = imageServerPath;
        this.fullName = fullName;
        update();
    }

    @SuppressLint("SetTextI18n")
    public void update() {
        String[] f_lName = fullName.split(" ");
        textView.setText(f_lName[0].charAt(0) + "‌" +
                f_lName[f_lName.length - 1].charAt(0));
        noImageState();
        textView.setMaxTextSize(maxTextSize);
        setImagePath(imageServerPath);

    }

    public void setMaxTextSize(float textSize) {
        textView.setMaxTextSize(textSize);
        maxTextSize = textSize;
    }


    private void setImageDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        imageState();

    }

    public MaterialCardView getCardView() {
        return cardView;
    }

    private void noImageState() {
        imageView.setVisibility(View.GONE);
    }

    private void imageState() {
        imageView.setVisibility(View.VISIBLE);
    }

    public void setImageByFile(File pictureFile) {
        super.setImageByFile(pictureFile);
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureFile.getPath());
        setImageBitmap(myBitmap);
    }

    @Override
    void errorState(boolean b, @androidx.annotation.Nullable String servePath) {
        noImageState();
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        imageState();
        if (fullScreen) {
            setOnClickListener(v -> {
                Utils.createImageFromBitmap(getContext() , bitmap);
                getContext().startActivity(new Intent(getContext(), FullscreenImageActivity.class));

            });
        }
    }

}
