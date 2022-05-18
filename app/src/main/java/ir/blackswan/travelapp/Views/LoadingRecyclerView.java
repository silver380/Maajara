package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import ir.blackswan.travelapp.R;

public class LoadingRecyclerView extends FrameLayout {
    RecyclerView recyclerView;
    ProgressBar loading;
    TextView textView;
    String text, errorText;
    private static final int DEFAULT_ERROR_TEXT = R.string.recycler_default_error, DEFAULT_TEXT = R.string.recycler_default_text;


    public LoadingRecyclerView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public LoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public LoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        inflate(getContext(), R.layout.view_loading_recycler, this);
        if (attributeSet != null)
            recyclerView = new RecyclerView(getContext(), attributeSet);
        else
            recyclerView = new RecyclerView(getContext());
        recyclerView.setId(R.id.loading_recycler);
        addView(recyclerView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loading = findViewById(R.id.loading_vlr);
        textView = findViewById(R.id.tv_vlr);
        errorText = getContext().getString(DEFAULT_ERROR_TEXT);
        text = getContext().getString(DEFAULT_TEXT);
        loadingState();
    }

    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
        textView.setText(errorText);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        if (recyclerView.getAdapter().getItemCount() > 0)
            recyclerState();
        else
            textState();
    }

    public void loadingState() {
        loading.setVisibility(VISIBLE);
        textView.setVisibility(GONE);
        recyclerView.setVisibility(GONE);
    }

    public void textState() {
        textState(false);
    }

    public void textState(boolean error) {
        loading.setVisibility(GONE);
        recyclerView.setVisibility(GONE);
        textView.setVisibility(VISIBLE);
        if (error) {
            textView.setTextColor(getResources().getColor(
                    com.google.android.material.R.color.design_default_color_error));
            textView.setText(errorText);
        } else {
            textView.setTextColor(getResources().getColor(R.color.colorNormalText));
            textView.setText(text);
        }
    }

    public void recyclerState() {
        loading.setVisibility(GONE);
        textView.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ProgressBar getLoading() {
        return loading;
    }

    public TextView getTextView() {
        return textView;
    }

    public String getText() {
        return text;
    }

    public String getErrorText() {
        return errorText;
    }

    public int getItemCount(){
        return recyclerView.getAdapter() == null ? 0 : recyclerView.getAdapter().getItemCount();
    }
}
