package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.ui.Adapters.HasArray;

public class ShowMoreRecyclerView extends LoadingRecyclerView {
    private ImageView ivShowMore;
    private Object[] data;
    private int maxShow = 2;
    private boolean lessShow = true;

    public ShowMoreRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ShowMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShowMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMaxShow(int maxShow) {
        this.maxShow = maxShow;
    }

    public boolean isLessShow() {
        return lessShow;
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        ivShowMore = findViewById(R.id.iv_vlr_show_more);
        ivShowMore.setOnClickListener(v -> {
            lessShow = !lessShow;
            switchLessMore();
        });
    }

    @Override
    public void loadingState() {
        super.loadingState();
        ivShowMore.setVisibility(GONE);
    }

    @Override
    public void recyclerState() {
        super.recyclerState();
        Log.d("ShowMoreRecyclerView", "recyclerState: " + data.length);
        if (data.length > maxShow)
            ivShowMore.setVisibility(VISIBLE);
        else
            ivShowMore.setVisibility(GONE);
    }

    @Override
    public void textState(boolean error) {
        super.textState(error);
        ivShowMore.setVisibility(GONE);
    }

    @Override
    public void textState() {
        super.textState();
        ivShowMore.setVisibility(GONE);
    }

    public void switchLessMore() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        HasArray hasArray = (HasArray) adapter;
        if (lessShow) {
            ivShowMore.setRotation(90);
            hasArray.setData(Arrays.copyOf(data, Math.min(data.length , maxShow)));
        } else {
            ivShowMore.setRotation(-90);
            hasArray.setData(data);
        }
        recyclerView.setAdapter(adapter);
        TransitionManager.beginDelayedTransition(this);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        HasArray hasArrayAdapter = (HasArray) adapter;
        data = hasArrayAdapter.getData();
        super.setAdapter(adapter);
        switchLessMore();
    }
}
