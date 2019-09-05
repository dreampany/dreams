package com.dreampany.play2048.ui.fragment;

import android.os.Bundle;

import com.artitk.licensefragment.model.LicenseID;
import com.artitk.licensefragment.support.v4.RecyclerViewLicenseFragment;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.util.FragmentUtil;
import com.dreampany.play2048.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 3/8/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@ActivityScope
public class LicenseFragment extends BaseFragment {

    @Inject
    public LicenseFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_license;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        ArrayList<Integer> licenseIds = new ArrayList<>();
        licenseIds.add(LicenseID.GSON);
        licenseIds.add(LicenseID.OKHTTP);
        licenseIds.add(LicenseID.RETROFIT);


        RecyclerViewLicenseFragment fragment = FragmentUtil.getFragment(this, R.id.fragment);
        fragment.addLicense(licenseIds);
    }

    @Override
    protected void onStopUi() {

    }
}
