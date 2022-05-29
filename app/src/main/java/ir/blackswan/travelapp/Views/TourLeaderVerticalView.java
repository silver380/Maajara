package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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
        name.setText(user.getFullNameWithPrefix());
        bio.setText(getBioPlusLang(user));
        rate.setText(user.getAvg_rate() + "");
        image.setFullScreen(true);
        image.setDataByUser(user);

        setContactWays(user, telegram, whatsapp, phone, mail);


        return this;
    }

    public static String getBioPlusLang(User user) {
        String langs = user.getLanguagesWithSeparator("، ");
        if (!langs.isEmpty())
            return user.getBiography() + " مسلط به زبان‌های: " + langs;
        else
            return user.getBiography();
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

    public static void setContactWays(User user, View telegram, View whatsapp, View phone, View mail) {
        Context context = telegram.getContext();
        String telegramID = user.getTelegram_id();
        String whatsappNum = user.getWhatsapp_id();
        String phoneNum = user.getPhone_number();
        if (telegramID != null)
            telegram.setOnClickListener(v -> Utils.openTelegram(context, user.getTelegram_id()));
        else
            telegram.setVisibility(GONE);
        if (whatsappNum != null)
            whatsapp.setOnClickListener(v -> Utils.openWhatsapp(context, user.getWhatsapp_id()));
        else
            whatsapp.setVisibility(GONE);
        if (phoneNum != null)
            phone.setOnClickListener(v -> Utils.openPhone(context, user.getPhone_number()));
        else
            phone.setVisibility(GONE);

        mail.setOnClickListener(v -> Utils.openGmail(context, user.getEmail()));
    }
}
