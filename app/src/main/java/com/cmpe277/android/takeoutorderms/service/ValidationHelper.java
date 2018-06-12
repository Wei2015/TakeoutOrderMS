package com.cmpe277.android.takeoutorderms.service;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by weiyao on 4/16/18.
 */

public class ValidationHelper {

    private Context context;

    public ValidationHelper(Context context) {
        this.context = context;
    }

    /**
     * method to check EditText filled .
     * @param editText
     * @param textInputLayout
     * @param message
     * @return
     */
    public Boolean isEditTextFilled(EditText editText, TextInputLayout textInputLayout, String message) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * method to check preparation time is <100
     */
    public Boolean isTimeValid(EditText editText, TextInputLayout textInputLayout, String message){
        String value = editText.getText().toString().trim();
        if (value.isEmpty() || value.length()>=3) {
            textInputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        }else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * methoc to check radio button is selected
     */
    public Boolean isRadioButtonSelected(RadioButton[] buttons, TextInputLayout textInputLayout, String message) {
        Boolean isSelected = false;
        for (RadioButton button: buttons) {
            if (button.isChecked()) {
                isSelected = true;
                break;
            }
        }
        if (!isSelected) {
            textInputLayout.setError(message);
        }else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }


    /**
     * method to Hide keyboard
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}


