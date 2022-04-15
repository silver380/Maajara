package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;

public class TourLeaderVerticalView extends MaterialCardView {

    WebImageView image;
    TextView name , bio , rate;
    ImageView telegram , whatsapp , mail , phone;


    public TourLeaderVerticalView(Context context) {
        super(context);
        init();
    }

    public TourLeaderVerticalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TourLeaderVerticalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TourLeaderVerticalView setData(User user){
        name.setText(user.getNameAndLastname());
        return this;
        //bio.setText(user.getBio());
        //rate.setText(user.getRate() + "");
        //image.setImagePath(user.getProfilePicturePath());
    }

    private void init(){
        inflate(getContext() , R.layout.tl_vertical_view_holer , this);
        image = findViewById(R.id.tl_iv);
        name = findViewById(R.id.tl_name);
        bio = findViewById(R.id.tv_tl_bio);
        rate = findViewById(R.id.tv_tl_rate);
        telegram = findViewById(R.id.tel_iv);
        whatsapp = findViewById(R.id.w_app_iv);


        image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                image.setCornerRadius(image.getWidth());
                image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
