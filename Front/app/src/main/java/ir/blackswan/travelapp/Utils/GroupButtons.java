package ir.blackswan.travelapp.Utils;

import android.content.Context;

import com.google.android.material.button.MaterialButton;

public class GroupButtons {
    private MaterialButton[] buttons;
    private boolean[] selected;
    private Context context;
    private boolean radio;
    private onSelectListener onSelectListener = (groupButtons, index, reselect) -> {
    };

    public GroupButtons(boolean radio, MaterialButton... buttons) {
        this.buttons = buttons;
        selected = new boolean[buttons.length];
        this.context = buttons[0].getContext();
        this.radio = radio;
        for (int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i].setOnClickListener(v -> {
                if (radio)
                    setOthersUnselected(finalI);

                if (selected[finalI])
                    setButtonUnselected(finalI);
                else
                    setButtonSelected(finalI);

                onSelectListener.onSelect(this, finalI, selected[finalI]);


            });
        }
    }

    public void load(boolean[] selected){
        this.selected = selected;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i])
                setButtonSelected(i);
            else
                setButtonUnselected(i);
        }
    }

    private void setButtonSelected(int index) {
        Utils.setMaterialButtonStyle(buttons[index], Utils.BUTTON_STYLE_CONTAINED);
        selected[index] = true;
    }

    private void setButtonUnselected(int index) {
        Utils.setMaterialButtonStyle(buttons[index], Utils.BUTTON_STYLE_OUTLINE);
        selected[index] = false;
    }

    private void setOthersUnselected(int index) {
        for (int i = 0; i < buttons.length; i++)
            if (i != index)
                setButtonUnselected(i);

    }

    public void setOnSelectListener(GroupButtons.onSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }


    public MaterialButton[] getButtons() {
        return buttons;
    }

    public boolean[] getSelected() {
        return selected;
    }

    public int getFirstSelectedIndex(){
        for (int i = 0; i < selected.length; i++) {
            if (selected[i])
                return i;
        }
        return -1;
    }

    public boolean hasOneSelected(){
        for (boolean select : selected) {
            if (select)
                return true;
        }
        return false;
    }

    public interface onSelectListener {
        void onSelect(GroupButtons groupButtons, int index, boolean reselect);
    }
}
