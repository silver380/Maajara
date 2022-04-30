package ir.blackswan.travelapp.ui;

import static ir.blackswan.travelapp.Controller.MyCallback.TAG;
import static ir.blackswan.travelapp.Utils.Utils.getScreenHeight;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import ir.blackswan.travelapp.Controller.AuthController;
import ir.blackswan.travelapp.Controller.MyCallback;
import ir.blackswan.travelapp.Controller.MyResponse;
import ir.blackswan.travelapp.Controller.PathController;
import ir.blackswan.travelapp.Controller.TourController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.Data.User;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.Views.TourLeaderVerticalView;
import ir.blackswan.travelapp.databinding.ActivityTourPageBinding;
import ir.blackswan.travelapp.ui.Adapters.PlacesRecyclerAdapter;
import ir.blackswan.travelapp.ui.Dialogs.OnResponseDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TourPageActivity extends ToolbarActivity {
    public static final String EXTRA_TOUR = "tour";
    private static final int REQUEST_USERS_ACTIVITY = 0;
    ActivityTourPageBinding binding;
    boolean bottomViewIsOpen = false;
    int maxScrollY;
    Tour tour;
    HashSet<Tour> pendingTours, confirmTours;
    private TourController tourController;
    private int actionBarHeight;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityTourPageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        tourController = new TourController(this);
        user = AuthController.getUser();
        setPendingUsers();
        tour = (Tour) getIntent().getSerializableExtra(EXTRA_TOUR);
        setTourData();

        setGlobalTree();

        setScrollListener();

        setTouchListener();

        binding.ivTourPageOpen.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up_reverse));

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

    }

    private void setGlobalTree() {
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.viewForShowingImage.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight()
                        ));

                        binding.viewForShowingImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                maxScrollY = binding.getRoot().getHeight();
                                closeBottomView();
                                Log.d("scroll", "setScrollListener: " + maxScrollY);

                                binding.scTourPageBottom.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        getScreenHeight()));
                                binding.scTourPage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        getScreenHeight()));


                                binding.viewForShowingImage.getViewTreeObserver().removeOnGlobalLayoutListener(this); //!!!! don't remove this line
                            }
                        });


                        binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this); //!!!! don't remove this line
                    }
                });

    }


    private void setPendingUsers() {

        tourController.getPendingTourFromServer(new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                Tour[] tours = TourController.getPendingTours();
                if (tours != null)
                    pendingTours = new HashSet<>(Arrays.asList(tours));
                else
                    pendingTours = new HashSet<>();

                setConfirmTours();
            }
        });

    }

    private void setConfirmTours() {
        tourController.getConfirmedTourFromServer(new OnResponseDialog(this) {
            @Override
            public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                super.onSuccess(call, callback, response);
                Tour[] tours = TourController.getConfirmedTours();
                if (tours != null)
                    confirmTours = new HashSet<>(Arrays.asList(tours));
                else
                    confirmTours = new HashSet<>();
                Log.d(TAG, "onSuccess: " + confirmTours);
                onLoadUsers();
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("FullScreen", "onAttachedToWindow: ");
        fullScreen();
    }

    private void fullScreen() {
        /*
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

         */
        if (Build.VERSION.SDK_INT >= 30) {
            binding.ivTourPageImage.getWindowInsetsController().hide(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            binding.ivTourPageImage.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

    }

    private void actionBar() {
        if (Build.VERSION.SDK_INT >= 30) {
            binding.ivTourPageImage.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            binding.ivTourPageImage.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
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
                    closeBottomView();
                } else {
                    if (scrollUp && binding.scTourPage.getScrollY() > getScreenHeight() * 10 / 100)
                        openBottomView();
                    else if (!scrollUp && binding.scTourPage.getScrollY() < getScreenHeight() * 90 / 100)
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

            if (scrollY < maxScrollY) {
                fullScreen();
            } else
                actionBar();

            float unit = (float) scrollY / maxScrollY;
            float unitRe = (float) (maxScrollY - scrollY) / maxScrollY;

            binding.viewTourPageAlpha.setAlpha(unit * .7f);

            LinearLayout.LayoutParams tbParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    actionBarHeight);
            tbParams.setMargins(0, (int) (-1 * (unitRe * actionBarHeight)), 0, 0);
            binding.tbActivity.setLayoutParams(tbParams);

            binding.cardBackTourPage.setAlpha(unitRe * .6f);

        });
    }

    private void onLoadUsers() {
        if (tour.getCreator().getUser_id() == user.getUser_id()) {
            binding.btnTourPageRegister.setText("درخواست‌ها");
            binding.btnTourPageRegister.setEnabled(true);
            binding.btnTourPageRegister.setOnClickListener(v -> {
                startActivityForResult(new Intent(this, UserRequestActivity.class).putExtra("tour_id", tour.getTour_id())
                        , REQUEST_USERS_ACTIVITY);
            });
        } else if (confirmTours.contains(tour)) {
            binding.btnTourPageRegister.setText("قطعی شده");
            binding.btnTourPageRegister.setEnabled(false);
            binding.btnTourPageRegister.setOnClickListener(v -> {
            });
        } else if (pendingTours.contains(tour)) {
            binding.btnTourPageRegister.setText("در انتظار تایید");
            binding.btnTourPageRegister.setEnabled(false);
            binding.btnTourPageRegister.setOnClickListener(v -> {
            });
        } else {
            binding.btnTourPageRegister.setText("ثبت‌نام");
            binding.btnTourPageRegister.setEnabled(true);
            binding.btnTourPageRegister.setOnClickListener(v -> {
                tourController.register(tour.getTour_id(), new OnResponseDialog(this) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, MyCallback callback, MyResponse response) {
                        super.onSuccess(call, callback, response);
                        pendingTours.add(tour);
                        onLoadUsers();
                    }
                });
            });
        }

    }

    private void setTourData() {
        binding.ivTourPageImage.setImagePath(PathController.getRandomPath(this));
        binding.tvTourPageName.setText(tour.getTour_name());
        binding.tbActivity.setTitle(tour.getTour_name());
        binding.tvTourPageLeaderName.setText(tour.getCreator().getNameAndLastname());

        //details
        binding.tvTourPagePrice.setText(tour.getPriceString());
        binding.tvTourPageStartDate.setText(tour.getPersianStartDate().getShortDate());
        binding.tvTourPageFinalDate.setText(tour.getPersianEndDate().getShortDate());
        binding.tvTourPageCity.setText(tour.getDestination());

        //options
        boolean breakfast = tour.isHas_breakfast(), lunch = tour.isHas_lunch(), dinner = tour.isHas_dinner();
        String foods = "";
        String resident = translate(tour.getResidence());
        String vehicle = translate(tour.getHas_transportation());
        if (breakfast)
            foods += "صبحانه";
        if (lunch) {
            if (breakfast)
                foods += "،" + " ";
            foods += "ناهار";
        }
        if (dinner) {
            if (!foods.isEmpty())
                foods += "،" + " ";
            foods += "شام";
        }
        if (foods.isEmpty())
            binding.groupTourPageFood.setVisibility(View.GONE);
        else
            binding.tvTourPageFood.setText(foods);

        if (resident.isEmpty())
            binding.groupTourPagePlace.setVisibility(View.GONE);
        else
            binding.tvTourPagePlace.setText(resident);

        if (vehicle.isEmpty())
            binding.groupTourPageVehicle.setVisibility(View.GONE);
        else
            binding.tvTourPageVehicle.setText(vehicle);


        Log.d("tourPlaces", "setTourData: " + Arrays.toString(tour.getPlaces()));
        binding.rycTourPagePlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rycTourPagePlaces.setAdapter(new PlacesRecyclerAdapter(this, tour.getPlaces()));


        TourLeaderVerticalView tourLeaderView = new TourLeaderVerticalView(this).setData(tour.getCreator());

        binding.llTourPageLeader.addView(tourLeaderView);

        binding.cardBackTourPage.setOnClickListener(v -> {
            closeBottomView();
            onBackPressed();
        });

    }

    private String translate(String word) {
        switch (word) {
            case "None":
                return "";
            case "House":
                return "خانه";
            case "Hotel":
                return "هتل";
            case "Villa":
                return "ویلا";
            case "Suite":
                return "سوئیت";
            case "Car":
                return "ماشین";
            case "Minibus":
                return "مینی بوس";
            case "Bus":
                return "اتوبوس";
            case "Van":
                return "ون";
        }
        return word;
    }

    @Override
    public void onBackPressed() {
        if (bottomViewIsOpen)
            closeBottomView();
        else
            super.onBackPressed();


    }
}
