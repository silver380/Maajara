package ir.blackswan.travelapp;

import static ir.blackswan.travelapp.Utils.Utils.dp2px;
import static ir.blackswan.travelapp.Utils.Utils.getScreenHeight;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import ir.blackswan.travelapp.Data.FakeData;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.Views.TourLeaderVerticalView;
import ir.blackswan.travelapp.databinding.ActivityTourPagePictureBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.AuthActivity;

public class TourPageActivity extends AuthActivity {

    ActivityTourPagePictureBinding binding;
    boolean bottomViewIsOpen = false;
    int topNamesMarginRight;
    int maxScrollY;
    int namesPadding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivityTourPagePictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareActivity();

        setScrollListener();

        setTouchListener();

        binding.ivTourPageOpen.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up_reverse));

        binding.llTourPageLeader.addView(new TourLeaderVerticalView(this).setData(FakeData.getFakeUser()));

        binding.rycTourPagePlaces.setAdapter(new PlacesRecyclerAdapter(this ,
                FakeData.getFakePlaces()));
        binding.rycTourPagePlaces.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
    }


    private void changeBackBottomVisibility() {
        /*
        if (backBottomVisibility)
            invisibleBackBottom();
        else
            visibleBackBottom();

         */
    }

    private void openBottomView() {
        binding.scTourPage.post(() -> {
            binding.scTourPage.smoothScrollTo(0, maxScrollY);
        });
        bottomViewIsOpen = true;
    }

    private void closeBottomView() {
        binding.scTourPage.post(() -> binding.scTourPage.smoothScrollTo(0, 0));
        bottomViewIsOpen = false;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener() {
        AtomicLong lastActionDownTime = new AtomicLong(-1);
        AtomicReference<Float> lastDownY = new AtomicReference<>();
        lastDownY.set(-1f);
        binding.scTourPage.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                long dTime = event.getEventTime() - lastActionDownTime.get();
                float dy = event.getY() - lastDownY.get();
                float absDy = Math.abs(dy);
                boolean scrollUp = dy < 0;
                Log.d("scroll", "onCreate: " + event.getY() + " " +
                        binding.vgTourPageBottomView.getScrollY());
                if (dTime < 150 && scrollUp && absDy > 70)
                    openBottomView();
                else if (dTime < 150 && !scrollUp && absDy > 70)
                    closeBottomView();
                else if (absDy < 10 && event.getY() < binding.llTourPageNames.getHeight()) {
                    changeBackBottomVisibility();
                    closeBottomView();
                } else {
                    if (scrollUp && binding.scTourPage.getScrollY() > getScreenHeight() * 10 / 100)
                        openBottomView();
                    else if (!scrollUp && binding.scTourPage.getScrollY() < getScreenHeight() * 60 / 100)
                        closeBottomView();
                    else if (bottomViewIsOpen)
                        openBottomView();
                    else
                        closeBottomView();

                    Log.d("scroll", "onTouchEvent: ACTION_UP" + binding.scTourPage.getScrollY());

                }
                Log.d("scroll", "onTouchEvent: ACTION_UP" + "  time: " + dTime
                        + " dY: " + dy);
            } else if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                lastActionDownTime.set(event.getEventTime());
                lastDownY.set(event.getY());
                Log.d("scroll", "onCreate: ACTION_DOWN");
            }


            return false;

        });

        binding.scTourPageBottom.setOnTouchListener(new View.OnTouchListener() {
            float lastDownY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    float dy = event.getY() - lastDownY;
                    boolean scrollUp = dy < 0;
                    if (scrollUp && binding.scTourPage.getScrollY() > getScreenHeight() * 10 / 100)
                        openBottomView();
                    else if (!scrollUp && binding.scTourPage.getScrollY() < getScreenHeight() * 70 / 100)
                        closeBottomView();
                    else if (bottomViewIsOpen)
                        openBottomView();
                    else
                        closeBottomView();
                } else if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    lastDownY = event.getY();
                }
                return false;
            }
        });
    }


    private void setScrollListener() {
        binding.scTourPage.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (binding.scTourPage.getHeight() == 0)
                return;
            if (scrollY > maxScrollY)
                maxScrollY = scrollY;
            float unit = (float) scrollY / maxScrollY;

            binding.viewTourPageAlpha.setAlpha(unit * .7f);

            float max2 = maxScrollY * 80 / 100f;
            float scroll2 = scrollY - max2;
            int margin = 0;
            if (scroll2 > -1) {
                float maxScroll2 = maxScrollY - max2;
                margin = (int) (scroll2 / maxScroll2 * topNamesMarginRight);
            }

            binding.llTourPageNames.setPadding(namesPadding, namesPadding, namesPadding + margin, namesPadding);

        });
    }


    private void prepareActivity() {
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.viewForShowingImage.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight()
                        ));

                        maxScrollY = binding.scTourPage.getChildAt(0).getHeight() - getScreenHeight();

                        binding.scTourPageBottom.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        getScreenHeight() - Utils.getStatusBarHeight(TourPageActivity.this) -
                                                binding.llTourPageNames.getHeight() -
                                                dp2px(TourPageActivity.this, getResources().getDimension(R.dimen.margin_small))));

                        topNamesMarginRight = ((FrameLayout.LayoutParams) binding.cardBackTourPage.getLayoutParams()).rightMargin
                                + binding.cardBackTourPage.getWidth();
                        namesPadding = binding.llTourPageNames.getPaddingLeft();


                        binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this); //!!!! don't remove this line
                    }
                });

        //binding.ivTourPageBack.setOnClickListener(v -> { closeBottomView();onBackPressed(); });
    }

    @Override
    public void onBackPressed() {
        if (bottomViewIsOpen)
            closeBottomView();
        else
            super.onBackPressed();


    }
}
