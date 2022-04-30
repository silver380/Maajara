package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Utils;

public class TourLeaderVerticalView extends MaterialCardView {

    ProfileImageView image;
    TextView name, bio, rate;
    ImageView telegram, whatsapp, mail, phone;


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

    public TourLeaderVerticalView setData(User user) {
        name.setText(user.getNameAndLastname());
        bio.setText(user.getBiography());
        rate.setText(user.getRate() + "");
        image.setUser(user);

        String telegramID = user.getTelegram_id();
        String whatsappNum = user.getWhatsapp_id();
        String phoneNum = user.getPhone_number();
        if (telegramID != null)
            telegram.setOnClickListener(v -> Utils.openTelegram(getContext(), user.getTelegram_id()));
        else
            telegram.setVisibility(INVISIBLE);
        if (whatsappNum != null)
            whatsapp.setOnClickListener(v -> Utils.openWhatsapp(getContext(), user.getWhatsapp_id()));
        else
            whatsapp.setVisibility(INVISIBLE);
        if (phoneNum != null)
            phone.setOnClickListener(v -> Utils.openPhone(getContext(), user.getPhone_number()));
        else
            phone.setVisibility(GONE);

        mail.setOnClickListener(v -> Utils.openGmail(getContext(), user.getEmail()));

        return this;
    }


    private void init() {
        inflate(getContext(), R.layout.tl_vertical_view_holer, this);
        image = findViewById(R.id.tl_iv);
        name = findViewById(R.id.tl_name);
        bio = findViewById(R.id.tv_tl_bio);
        rate = findViewById(R.id.tv_tl_rate);
        telegram = findViewById(R.id.tel_iv);
        whatsapp = findViewById(R.id.w_app_iv);
        mail = findViewById(R.id.e_mail_iv);
        phone = findViewById(R.id.phone_iv);
    }

    public ProfileImageView getProfileImageView() {
        return image;
    }
}
