package com.hotix.myhotixplay.helpers;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InputValidation {

    private Context mContext;

    public InputValidation(Context context) {
        this.mContext = context;
    }

    //------------------------

    public boolean isInputEditTextFilled(TextInputEditText editText, TextInputLayout inputLayout, String message) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            inputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        return true;
    }

    //------------------------

    public boolean isInputEditTextShort(TextInputEditText editText, TextInputLayout inputLayout, String message, int length) {
        String value = editText.getText().toString().trim();
        if (value.length() < length) {
            inputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    //------------------------

    public boolean isInputEditTextEmail(TextInputEditText editText, TextInputLayout inputLayout, String message) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            inputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        return true;
    }

    //------------------------

    public boolean isInputEditTextMatches(TextInputEditText editTextOne, TextInputEditText editTextTwo, TextInputLayout inputLayout, String message) {
        String value1 = editTextOne.getText().toString().trim();
        String value2 = editTextTwo.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            inputLayout.setError(message);
            hideKeyboardFrom(editTextTwo);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        return true;
    }

    //------------------------

    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}