package com.dreampany.fit.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dreampany.fit.R;
import com.dreampany.fit.ui.fragment.HomeFragment;
import com.dreampany.fit.ui.fragment.MoreFragment;
import com.dreampany.framework.misc.SmartAd;
import com.dreampany.framework.ui.activity.BaseBottomNavigationActivity;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import dagger.Lazy;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NavigationActivity extends BaseBottomNavigationActivity {

    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 101;

    @Inject
    Lazy<HomeFragment> homeFragment;
    @Inject
    Lazy<MoreFragment> moreFragment;
    @Inject
    SmartAd ad;
//    ActivityNavigationBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_navigation;
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public int getNavigationViewId() {
        return R.id.navigation_view;
    }

    @Override
    public int getDefaultSelectedNavigationItemId() {
        return R.id.item_home;
    }

    @Override
    public boolean isHomeUp() {
        return false;
    }

    @Override
    protected void onStartUi(Bundle state) {
        // binding = (ActivityNavigationBinding) super.binding;
        ad.loadBanner(findViewById(R.id.adview));
        initFit();
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = getCurrentFragment();
        if (fragment != null && fragment.hasBackPressed()) {
            return;
        }
        finish();
    }

    @Override
    protected void onNavigationItem(int navigationItemId) {
        switch (navigationItemId) {
            case R.id.item_home:
                commitFragment(HomeFragment.class, homeFragment, R.id.layout);
                break;
            case R.id.item_more:
                commitFragment(MoreFragment.class, moreFragment, R.id.layout);
                break;
        }
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
               // accessGoogleFit();
                accessSensor();
            }
        }
    }


    private void initFit() {
/*        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            //accessGoogleFit();
            accessSensor();
        }*/
    }

    private void accessSensor() {

        GoogleSignInOptionsExtension fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .build();

/*        GoogleSignInAccount gsa = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        Task<Void> response = Fitness.getSensorsClient(this, gsa)
                .add(new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                .setSamplingRate(1, TimeUnit.MINUTES)  // sample once per minute
                                .build(),
                        new OnDataPointListener() {
                            @Override
                            public void onDataPoint(DataPoint dataPoint) {
                                Timber.v("DataPoint %s", dataPoint.getDataType().getName());
                                Timber.v("DataPoint %s", dataPoint.getDataSource().getStreamName());
                            }
                        });*/
    }

    private void accessGoogleFit() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -1);
        long startTime = cal.getTimeInMillis();


        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();


/*        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Timber.d("onSuccess()");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.d(e, "onFailure()");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        Timber.d("onComplete()");
                    }
                });*/
    }
}
