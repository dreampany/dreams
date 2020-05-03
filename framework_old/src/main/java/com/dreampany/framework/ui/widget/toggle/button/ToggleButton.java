package com.dreampany.framework.ui.widget.toggle.button;

import android.widget.Checkable;

public interface ToggleButton extends Checkable {

    void setOnCheckedChangeListener(OnCheckedChangeListener listener);

}
