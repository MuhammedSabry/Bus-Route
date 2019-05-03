package com.example.busroute.util;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

/**
 * @author Muhammed Sabry
 */
public class InputTextUtils {

    /**
     * Cancels the errors message on the TextInputLayout when the text changes
     */
    private static void setErrorCanceller(EditText editText, TextInputLayout inputLayout) {
        editText.addTextChangedListener(new ErrorCancelTextWatcher(inputLayout));
    }

    /**
     * Cancels the errors message on the TextInputLayout when the text changes
     */
    public static void setErrorCanceller(TextInputLayout inputLayout) {
        if (inputLayout.getEditText() != null)
            setErrorCanceller(inputLayout.getEditText(), inputLayout);
    }

    public static Editable getTextEditable(TextInputLayout inputLayout) {
        if (inputLayout.getEditText() == null)
            return null;
        return inputLayout.getEditText().getText();
    }

    public static String getText(TextInputLayout inputLayout) {
        if (inputLayout.getEditText() == null)
            return null;
        return TextUtil.getText(inputLayout.getEditText().getText());
    }

    /**
     * Util method to hide the keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Util method to hide the keyboard
     */
    public static void showSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void setText(TextInputLayout layout, String text) {
        if (layout.getEditText() != null)
            layout.getEditText().setText(text);
    }

    public static void setText(TextInputLayout layout, int value) {
        setText(layout, String.valueOf(value));
    }

    public static void setText(TextInputLayout layout, double value) {
        setText(layout, String.valueOf(value));
    }

    /**
     * Class that implements TextWatcher to remove error on editable texts each time after the text changes
     */
    public static class ErrorCancelTextWatcher implements TextWatcher {

        private final TextInputLayout field;

        /**
         * Saves the TextInputLayout associated with the EditableText
         */
        ErrorCancelTextWatcher(TextInputLayout field) {
            this.field = field;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            field.setError(null);
            field.setErrorEnabled(false);
        }
    }
}
