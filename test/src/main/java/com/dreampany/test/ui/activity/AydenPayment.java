package com.dreampany.test.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.adyen.checkout.core.CheckoutException;
import com.adyen.checkout.ui.CheckoutController;
import com.adyen.checkout.ui.CheckoutSetupParameters;
import com.adyen.checkout.ui.CheckoutSetupParametersHandler;
import com.dreampany.frame.ui.activity.BaseActivity;

import org.jetbrains.annotations.Nullable;

/**
 * Created by roman on 2019-05-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class AydenPayment extends BaseActivity {


    @Override
    protected void onStartUi(@Nullable Bundle state) {

        CheckoutController.startPayment(this, new CheckoutSetupParametersHandler() {
            @Override
            public void onRequestPaymentSession(@NonNull CheckoutSetupParameters checkoutSetupParameters) {

            }

            @Override
            public void onError(@NonNull CheckoutException error) {

            }
        });

    }

    @Override
    protected void onStopUi() {

    }
}
