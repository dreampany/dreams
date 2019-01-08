package com.dreampany.toggle.button;

import android.widget.Checkable;

public interface ToggleButton extends Checkable {

    void setOnCheckedChangeListener(OnCheckedChangeListener listener);

}
