package com.dreampany.fit.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.dreampany.fit.R;
import com.dreampany.fit.misc.Constants;
import com.dreampany.fit.service.AppService;
import com.dreampany.framework.api.service.ServiceManager;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class HomeFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    ServiceManager service;

    @Inject
    public HomeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_home;
    }

/*    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }*/

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        BaseFragment fragment = getCurrentFragment();
        return fragment != null && fragment.onQueryTextChange(newText);
    }

    @Override
    public void onPermissionGranted(@Nullable PermissionGrantedResponse response) {
        //TODO action to start listening sensors events using AppService
        checkAccount();
    }

    @Override
    public void onPermissionDenied(@Nullable PermissionDeniedResponse response) {
        //TODO show a perfect message to user
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.Code.GoogleFit) {
                startFit();
            }
        }
    }

    private void initView() {
        setTitle(R.string.home);

        Dexter.withActivity(getParent())
                .withPermission(Constants.Permission.Location)
                .withListener(this)
                .check();
    }

    private void checkAccount() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getContext()), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    Constants.Code.GoogleFit,
                    GoogleSignIn.getLastSignedInAccount(getAppContext()),
                    fitnessOptions);
        } else {
            startFit();
        }
    }

    private void startFit() {
        Intent intent = new Intent(getAppContext(), AppService.class);
        intent.setAction(Constants.Action.StartFit);
        service.openService(intent);
    }

    private void stopFit() {
        Intent intent = new Intent(getAppContext(), AppService.class);
        intent.setAction(Constants.Action.StopFit);
        service.openService(intent);
    }
}
