package com.dreampany.frame.ui.widget;


import android.content.Context;
import android.content.res.Resources;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.AttributeSet;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class SmartSpinner extends AppCompatSpinner {

    public SmartSpinner(Context context) {
        super(context);
    }

    public SmartSpinner(Context context, int mode) {
        super(context, mode);
    }

    public SmartSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmartSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public SmartSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
