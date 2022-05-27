package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Utils;

public class ToggleView extends ConstraintLayout {
    MaterialButton[] buttons;
    OnToggleListener onToggleListener = (i , b) -> {};
    int selectedIndex = -1;

    public ToggleView(@NonNull Context context) {
        super(context);
        init();
    }

    public ToggleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToggleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ToggleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext() , R.layout.toggle_view , this);
        buttons = new MaterialButton[2];
        buttons[0] = findViewById(R.id.toggle_button1);
        buttons[1] = findViewById(R.id.toggle_button2);
        setSelectedIndex(0);
        buttons[0].setOnClickListener(v->{
            if (selectedIndex != 0) {
                setSelectedIndex(0);
            }
        });
        buttons[1].setOnClickListener(v->{
            if (selectedIndex != 1) {
                setSelectedIndex(1);
            }
        });
    }

    public void setButtonsTextByArray(String... texts){
        for (int i = 0; i < 2; i++)
            buttons[i].setText(texts[i]);

    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex != this.selectedIndex) {
            this.selectedIndex = selectedIndex;
            for (int i = 0; i < buttons.length; i++) {
                if (i == selectedIndex) {
                    buttons[i].setTextColor(Utils.getThemePrimaryColor(getContext()));
                } else {
                    buttons[i].setTextColor(getResources().getColor(R.color.colorHint));
                }
            }
            onToggleListener.onToggle(selectedIndex , buttons[selectedIndex]);
        }
    }

    public void setOnToggleListener(OnToggleListener onToggleListener) {
        this.onToggleListener = onToggleListener;
    }

    public interface OnToggleListener {
        void onToggle(int i, MaterialButton button);
    }
}
