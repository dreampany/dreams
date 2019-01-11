package com.dreampany.vision.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import com.dreampany.frame.ui.activity.BaseMenuActivity;
import com.dreampany.vision.R;
import com.dreampany.vision.ui.fragment.TextOcrFragment;
import dagger.Lazy;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

/**
 * Created by air on 10/22/17.
 */

public class TextOcrActivity extends BaseMenuActivity {

    public static final String AUTO_FOCUS = "auto_focus";
    public static final String USE_FLASH = "use_flash";
    public static final String TEXT_BLOCK_OBJECT = "text_block_object";

    @Inject
    Lazy<TextOcrFragment> textOcrFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_base;
    }



    @Override
    protected void onStartUi(@Nullable Bundle state) {
        commitFragment(TextOcrFragment.class, textOcrFragment, R.id.layout);
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TextOcrFragment fragment = (TextOcrFragment) getCurrentFragment();
        boolean okay = fragment.onTouchEvent(event);
        return okay || super.onTouchEvent(event);
    }
}
