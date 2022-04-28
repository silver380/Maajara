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

    ImageView imageView;
    TextView textView;
    MaterialCardView cardView;
    @Nullable
    User user;

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

    public void setSize(int size){
        FrameLayout.LayoutParams params = (LayoutParams) cardView.getLayoutParams();
        params.width = size;
        params.height = size;
        cardView.setRadius(size / 2f);
        cardView.setLayoutParams(params);
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
        }
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

    private void setImageByFile(File pictureFile) {
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureFile.getPath());
        setImageBitmap(myBitmap);

        imageState();
    }

    private void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);

        imageState();
    }
}
