package ir.blackswan.travelapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;

public class ProfileImageView extends FrameLayout {

    private ImageView imageView;
    private AutoResizeTextView textView;
    private MaterialCardView cardView;
    @Nullable
    private User user;
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


    private void init() {
        inflate(getContext(), R.layout.view_image_profile, this);
        textView = findViewById(R.id.profile_text);
        imageView = findViewById(R.id.profile_image);
        cardView = findViewById(R.id.profile_card);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardView.setRadius(cardView.getWidth()/2f);
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        update();
    }

    @SuppressLint("SetTextI18n")
    public void update() {
        if (user != null) {
            textView.setText(user.getFirst_name().charAt(0) + "‌" + user.getLast_name().charAt(0));
            noImageState();
            textView.setMaxTextSize(maxTextSize);
        }
    }
    public void setMaxTextSize(float textSize){
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
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureFile.getPath());
        setImageBitmap(myBitmap);
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);

        imageState();
    }
}
