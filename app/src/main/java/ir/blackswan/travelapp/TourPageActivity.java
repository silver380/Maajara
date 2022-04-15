package ir.blackswan.travelapp;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.databinding.ActivityTourPagePictureBinding;

public class TourPageActivity extends AppCompatActivity {

    ActivityTourPagePictureBinding binding;
    boolean bottomViewIsOpen = false;
    boolean backBottomVisibility = false;

    int backIconSize;
    int backMargin;
    int maxScrollY;

    List<Place> places;
    RecyclerView placeRecycler;

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

        //places recycler

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
            binding.scTourPage.smoothScrollTo(0, binding.scTourPage.getMaxScrollAmount());
        });
        bottomViewIsOpen = true;
    }

    private void closeBottomView() {
        binding.scTourPage.post(() -> binding.scTourPage.smoothScrollTo(0, 0));
        bottomViewIsOpen = false;
    }

    private void invisibleBackBottom() {
        if (!backBottomVisibility)
            return;
        backBottomVisibility = false;
        Animation close = AnimationUtils.loadAnimation(this, R.anim.exit_up);
        close.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.cardBackTourPage.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.cardBackTourPage.startAnimation(close);
    }

    private void visibleBackBottom() {
        if (backBottomVisibility)
            return;
        backBottomVisibility = true;
        binding.cardBackTourPage.setVisibility(View.VISIBLE);
        binding.cardBackTourPage.startAnimation(AnimationUtils.
                loadAnimation(this, R.anim.enter_down));
    }

    private void updateBackButtonPosition(float unit) {
        int minMarginTop = -backIconSize - backMargin;
        int marginDif = (int) (unit * (backMargin + -1 * minMarginTop));
        FrameLayout.LayoutParams backParams = new FrameLayout.LayoutParams(backIconSize, backIconSize);
        backParams.setMargins(backMargin, minMarginTop + marginDif, backMargin, 0);
        binding.cardBackTourPage.setLayoutParams(backParams);
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
                    else if (!scrollUp && binding.scTourPage.getScrollY() < getScreenHeight() * 35 / 100)
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
    }

    private void setScrollListener() {
        binding.scTourPage.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (binding.scTourPage.getHeight() == 0)
                return;
            if (scrollY > maxScrollY)
                maxScrollY = scrollY;
            float unit = (float) scrollY / maxScrollY;

            binding.viewTourPageAlpha.setAlpha(unit * .7f);
            updateBackButtonPosition(unit);

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
                        backIconSize = binding.cardBackTourPage.getWidth();
                        backMargin = ((FrameLayout.LayoutParams) binding.cardBackTourPage.getLayoutParams()).topMargin;
                        updateBackButtonPosition(0);
                        maxScrollY = binding.scTourPage.getChildAt(0).getHeight() - getScreenHeight();

                        binding.cardBackTourPage.getViewTreeObserver().removeOnGlobalLayoutListener(this); //!!!! don't remove this line
                    }
                });

        binding.cardBackTourPage.setOnClickListener(v -> {
            closeBottomView();
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        if (bottomViewIsOpen)
            closeBottomView();
        else
            super.onBackPressed();


    }
}
