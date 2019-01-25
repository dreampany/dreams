package com.dreampany.radio.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.radio.R;
import com.dreampany.radio.databinding.FragmentStationBinding;
import org.jetbrains.annotations.Nullable;
import timber.log.Timber;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 1/8/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@ActivityScope
public class StationFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory factory;
    FragmentStationBinding binding;

    @Inject
    public StationFragment() {
        Timber.v("StationFragment created");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_station;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView(state);
    }

    @Override
    protected void onStopUi() {

    }

    private void initView(Bundle state) {
        setTitle(R.string.app_name);
        binding = (FragmentStationBinding) super.binding;

    }
}
