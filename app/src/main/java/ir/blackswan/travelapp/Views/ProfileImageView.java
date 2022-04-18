package ir.blackswan.travelapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;

public class ProfileImageView extends MaterialCardView {

    ImageView imageView;
    TextView textView;
    @Nullable User user;

    public ProfileImageView(Context context) {
        super(context);
        init();
    }

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext() , R.layout.view_image_profile , this);
        textView = findViewById(R.id.profile_text);
        imageView = findViewById(R.id.profile_image);
    }

    public void setUser(User user){
        this.user = user;
        update();
    }

    @SuppressLint("SetTextI18n")
    public void update(){
        if (user != null){
            textView.setText(user.getName().charAt(0) + "‌"  + user.getLast_name().charAt(0));
            noImageState();
        }
    }



    private void setImageDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        imageState();

    }

    private void noImageState(){
        imageView.setVisibility(GONE);
    }
    private void imageState() {
        imageView.setVisibility(VISIBLE);
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
