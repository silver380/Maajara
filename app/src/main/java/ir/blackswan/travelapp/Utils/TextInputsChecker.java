package ir.blackswan.travelapp.Utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import ir.blackswan.travelapp.R;

public class TextInputsChecker {
    ArrayList<InputMessageError> inputsMessagesErrors = new ArrayList<>();


    public void add(EditText editText , Error error){
        InputMessageError ime = new InputMessageError(editText , error);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkError(ime , false);
            }
        });
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                checkError(ime , true);
        });
        inputsMessagesErrors.add(ime);
    }

    public static void prepareShowError(){

    }

    public void add(TextInputEditText inputEditText ){
        add(inputEditText, editText ->{
            if (Utils.getEditableText(editText.getText()).isEmpty())
                return inputEditText.getHint() + " ضروری است.";
            return null;
        });
    }


    public void add(List<TextInputEditText> inputEditTexts){
        for (TextInputEditText input : inputEditTexts) {
            add(input);
        }
    }

    public void removeByIndex(int i){
        inputsMessagesErrors.remove(i);
    }

    public void remove(TextInputEditText inputEditText){
        for (int i = 0; i < inputsMessagesErrors.size(); i++) {
            if(inputsMessagesErrors.get(i).input.equals(inputEditText)) {
                removeByIndex(i);
                return;
            }
        }
    }

    public InputMessageError getImeByEditText(TextInputEditText editText){
        for (int i = 0; i < inputsMessagesErrors.size(); i++) {
            if(inputsMessagesErrors.get(i).input.equals(editText)) {
                return inputsMessagesErrors.get(i);
            }
        }
        return null;
    }

    public interface Error{
        @Nullable String error(EditText editText);
    }

    private boolean checkError(InputMessageError ime , boolean showError){
        String error = ime.error.error(ime.input);
        if (error != null) {
            if (showError)
                ((TextInputLayout)ime.input.getParent().getParent()).setError(error);
            return true;
        }else
            clearError(ime);
        return false;
    }

    public boolean checkAllError(){
        boolean hasError = false;
        for (InputMessageError ime : inputsMessagesErrors) {
            boolean tError = checkError(ime , true);
            if (!hasError)
                hasError = tError;
        }
        return hasError;
    }

    public void clearErrors(){
        for (InputMessageError ime : inputsMessagesErrors) {
            clearError(ime);
        }
    }

    public void clearError(InputMessageError ime){
        ((TextInputLayout)ime.input.getParent().getParent()).setError(null);
    }

    static class InputMessageError {
        final EditText input;
        final Error error;

        public InputMessageError(EditText input,  Error error) {
            this.input = input;
            this.error = error;
        }
    }


}

