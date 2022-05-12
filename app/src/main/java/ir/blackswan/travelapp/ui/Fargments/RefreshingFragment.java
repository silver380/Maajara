package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ir.blackswan.travelapp.R;

public abstract class RefreshingFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;

    public void init(View root) {
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getContext().getColor(R.color.colorIconTint));
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
    }

    public abstract void refresh();

    public void setRefreshing(boolean refreshing){
        swipeRefreshLayout.setRefreshing(refreshing);
    }

}
