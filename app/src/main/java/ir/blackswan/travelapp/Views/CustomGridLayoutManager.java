package ir.blackswan.travelapp.Views;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = false;

    public CustomGridLayoutManager(Context context , int spanCount) {
        super(context , spanCount );
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled;
    }

}