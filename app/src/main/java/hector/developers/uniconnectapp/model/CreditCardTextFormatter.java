package hector.developers.uniconnectapp.model;

import android.text.Editable;
import android.text.TextWatcher;

public class CreditCardTextFormatter implements TextWatcher {
    private String separator = " - ";
    private int divider = 5;

    public CreditCardTextFormatter(String s, int i) {
    }

    @Override
    public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {

    }

    @Override
    public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            return;
        }
        String oldString = s.toString();
        String newString = getNewString(oldString);
        if (!newString.equals(oldString)) {
            s.replace(0, oldString.length(), newString);
        }
    }

    private String getNewString(String value) {
        String newString = value.replace(separator, "");

        int divider = this.divider;
        while (newString.length() >= divider) {
            newString = newString.substring(0, divider - 1)
                    + this.separator
                    + newString.substring(divider - 1);
            divider += this.divider + separator.length() - 1;
        }
        return newString;
    }
}
