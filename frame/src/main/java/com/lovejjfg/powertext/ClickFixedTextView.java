package com.lovejjfg.powertext;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Hawladar Roman on 7/6/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ClickFixedTextView extends AppCompatTextView {

    public boolean linkClicked;

    public ClickFixedTextView(Context context) {
        super(context);
    }

    public ClickFixedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickFixedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        return !linkClicked && super.performClick();
    }
}
