package com.dreampany.lca.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.artitk.licensefragment.model.CustomUI;
import com.artitk.licensefragment.model.LicenseID;
import com.artitk.licensefragment.support.v4.RecyclerViewLicenseFragment;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.util.FragmentUtil;
import com.dreampany.lca.R;

import com.dreampany.frame.misc.Constants;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Screen.license(getAppContext());
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        setTitle(R.string.license);
        CustomUI customUI = new CustomUI()
                .setTitleBackgroundColor(Color.parseColor("#7fff7f"))
                .setTitleTextColor(getResources().getColor(android.R.color.holo_green_dark))
                .setLicenseBackgroundColor(Color.rgb(127, 223, 127))
                .setLicenseTextColor(Color.DKGRAY);

        ArrayList<Integer> licenseIds = new ArrayList<>();
        licenseIds.add(LicenseID.GSON);
        licenseIds.add(LicenseID.OKHTTP);
        licenseIds.add(LicenseID.RETROFIT);

        RecyclerViewLicenseFragment fragment = FragmentUtil.getFragment(this, R.id.fragment);
        //fragment.setCustomUI(customUI);
        fragment.addLicense(licenseIds);


    }

    @Override
    protected void onStopUi() {

    }
}
