/*
package com.dreampany.word.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.dreampany.frame.databinding.ActivityWebBinding;
import com.dreampany.frame.ui.activity.BaseActivity;
import com.dreampany.word.R;
import com.dreampany.word.ui.model.UiTask;

import org.jetbrains.annotations.Nullable;

import im.delight.android.webview.AdvancedWebView;

*/
/**
 * Created by Roman on 1/23/2019
 * Copyright (c) 2019 Dreampany. All rights reserved.
 * dreampanymail@gmail.com
 * Last modified $file.lastModified
 *//*

public class WebActivity extends BaseActivity implements AdvancedWebView.Listener {

    ActivityWebBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        binding = (ActivityWebBinding) super.binding;
        binding.webview.setListener(this, this);

        UiTask<?> task = getCurrentTask(false);
        if (task == null) {
            finish();
            return;
        }
        String url = task.getComment();
        binding.webview.loadUrl(url);
    }

    @Override
    protected void onStopUi() {
        binding.webview.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webview.onResume();
    }

    @Override
    protected void onPause() {
        binding.webview.onPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!binding.webview.onBackPressed()) { return; }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        binding.webview.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
*/
