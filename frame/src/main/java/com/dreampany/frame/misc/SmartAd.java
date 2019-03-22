package com.dreampany.frame.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.view.View;

import com.dreampany.frame.data.source.pref.AdPref;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.TimeUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;
import hugo.weaving.DebugLog;
import org.apache.commons.lang3.tuple.MutablePair;

/**
 * Created by Hawladar Roman on 2/7/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class SmartAd {

    private enum State {NONE, FAILED, LOADED, OPENED, STARTED, RESUMED, PAUSED, CLICKED, LEFT, CLOSED}

    private static final long defaultAdDelay = TimeUnit.SECONDS.toMillis(3);

    private Context context;
    private final Map<String, MutablePair<AdView, State>> banners;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    private State interstitialState = State.NONE;
    private State rewardedState = State.NONE;

    private static final int BANNER_MULTIPLIER = 1;
    private static final int INTERSTITIAL_MULTIPLIER = 2;
    private static final int REWARDED_MULTIPLIER = 4;

    private int points;
    private Config config;
    private AdPref pref;

    @Inject
    public SmartAd(Context context, AdPref pref) {
        this.context = context;
        this.pref = pref;
        banners = Maps.newConcurrentMap();
    }

    public void setConfig(Config config) {
        this.config = config;
    }

/*    public void setPref(FramePref pref) {
        this.pref = pref;
    }*/

    public void initPoints(int points) {
        this.points = points;
    }

    public boolean isBannerLoaded() {
        //return bannerState == State.LOADED;
        return false;
    }

    public boolean isInterstitialLoaded() {
        return interstitialState == State.LOADED;
    }

    public boolean isRewardedLoaded() {
        return rewardedState == State.LOADED;
    }


    @DebugLog
    public void initAd(@NonNull Context context,
                       @NonNull String screenId,
                       @NonNull AdView banner,
                       @StringRes int interstitial,
                       @StringRes int rewarded) {
        if (!config.enabled) {
            return;
        }
        initBanner(screenId, banner);
    }

    @DebugLog
    public void initBanner(@NonNull String screenId,
                           @NonNull AdView banner) {
        banners.put(screenId, MutablePair.of(banner, State.NONE));
        if (banner.getAdListener() == null) {
            banner.setAdListener(new BannerListener(screenId) {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    banners.get(screenId).setRight(State.FAILED);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    banners.get(screenId).setRight(State.LOADED);
                    View view = (View) banners.get(screenId).getLeft().getParent();
                    view.setVisibility(View.VISIBLE);
                    pref.setBannerTime(TimeUtil.currentTime());
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    banners.get(screenId).setRight(State.OPENED);
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    banners.get(screenId).setRight(State.CLICKED);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                    banners.get(screenId).setRight(State.LEFT);
                }
            });
        }
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void loadBanner(@NonNull String screenId) {
        if (!pref.isBannerTimeExpired(config.bannerExpireDelay)) {
            return;
        }
        if (!banners.containsKey(screenId)) {
            return;
        }
        AdView banner = banners.get(screenId).left;
        banner.loadAd(new AdRequest.Builder().build());
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void resumeBanner(@NonNull String screenId) {
        if (!pref.isBannerTimeExpired(config.bannerExpireDelay)) {
            return;
        }
        if (!banners.containsKey(screenId)) {
            return;
        }
        AdView banner = banners.get(screenId).left;
        banner.resume();
        View view = (View) banner.getParent();
        view.setVisibility(View.VISIBLE);
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void pauseBanner(@NonNull String screenId) {
        if (!pref.isBannerTimeExpired(config.bannerExpireDelay)) {
            return;
        }
        if (!banners.containsKey(screenId)) {
            return;
        }
        AdView banner = banners.get(screenId).left;
        View view = (View) banner.getParent();
        view.setVisibility(View.GONE);
        banner.pause();
    }

    @SuppressLint("MissingPermission")
    public void destroyBanner(@NonNull String screenId) {
        if (!pref.isBannerTimeExpired(config.bannerExpireDelay)) {
            return;
        }
        if (!banners.containsKey(screenId)) {
            return;
        }
        AdView banner = banners.get(screenId).left;
        View view = (View) banner.getParent();
        view.setVisibility(View.GONE);
        banner.destroy();
    }

    @DebugLog
    public boolean loadInterstitial(@NonNull Context context, @StringRes int adUnitId) {
        if (!config.enabled) {
            return false;
        }

        if (!pref.isInterstitialTimeExpired(config.interstitialExpireDelay)) {
            return false;
        }

        //if (interstitialAd == null) {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(adUnitId));
        //}

        return initAd(interstitialAd);
    }

    @DebugLog
    public boolean loadRewarded(@NonNull Context context, @StringRes int adUnitId) {
        if (!config.enabled) {
            return false;
        }

        if (!pref.isRewardedTimeExpired(config.interstitialExpireDelay)) {
            return false;
        }

        //if (rewardedVideoAd == null) {
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedListener());
        //}
        return initAd(rewardedVideoAd, context.getString(adUnitId));
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    private boolean initAd(final AdView adView) {

        return true;
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    private boolean initAd(InterstitialAd interstitialAd) {
        if (interstitialAd.getAdListener() == null) {
            interstitialAd.setAdListener(interstitialListener);
        }
        AndroidUtil.getUiHandler().postDelayed(() -> interstitialAd.loadAd(new AdRequest.Builder().build()), defaultAdDelay);
        return true;
    }

    @DebugLog
    private boolean initAd(RewardedVideoAd rewardedVideoAd, String unitId) {
        if (rewardedVideoAd.getRewardedVideoAdListener() == null) {
            rewardedVideoAd.setRewardedVideoAdListener(new RewardedListener());
        }
        AndroidUtil.getUiHandler().postDelayed(() -> rewardedVideoAd.loadAd(unitId, new AdRequest.Builder().build()), defaultAdDelay);
        return true;
    }

    public void loadBannerTest(AdView adView) {
        if (adView == null) {
            return;
        }

        //bannerAdView = adView;
        loadTest(adView);
    }

    public void resume(@NonNull Context context) {
        //resumeRewarded(context);
    }

    public void pause(@NonNull Context context) {
        //pauseRewarded(context);
    }

    public void destroy(@NonNull Context context) {
        destroyBanner();
        //destroyRewarded(context);
    }

    private void resumeBanner() {

    }

    private void resumeRewarded(Context context) {
        if (rewardedVideoAd != null && rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.resume(context);
        }
    }

    private void pauseRewarded(Context context) {
        if (rewardedVideoAd != null && rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.pause(context);
        }
    }

    public void destroyBanner() {
/*        if (bannerAdView != null && bannerAdView.isShown()) {
            View view = (View) bannerAdView.getParent();
            view.setVisibility(View.GONE);
            bannerAdView.destroy();
        }
        bannerAdView = null;*/
    }

    private void destroyRewarded(Context context) {
        if (rewardedVideoAd != null && rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.destroy(context);
        }
        rewardedVideoAd = null;
    }

    @SuppressLint("MissingPermission")
    private void loadTest(final AdView adView) {
        if (adView.getAdListener() == null) {
            //adView.setAdListener(bannerListener);
        }
        //if (NetworkManager.onInstance(context).hasInternet()) {
        adView.postDelayed(() -> {
            View view = (View) adView.getParent();
            view.setVisibility(View.VISIBLE);
            adView.loadAd(new AdRequest.Builder().addTestDevice("33BE2250B43518CCDA7DE426D04EE232")
                    .build());
        }, defaultAdDelay);
        // }
    }

/*
    private void earnPoints(String id, AdType type, PointSubtype subtype, int points, String comment) {
        FrameManager.onInstance(context).trackPoints(id, type.value(), subtype.value(), points, comment);

        AdEvent event = new AdEvent();
        event.points = points;
        event.setType(type);
        event.setSubtype(subtype);

        EventManager.post(event);
    }
*/

    /*private final AdListener bannerListener = new AdListener() {

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            bannerState = State.CLOSED;
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            bannerState = State.LOADED;
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            bannerState = State.LEFT;

*//*            earnPoints(
                    String.valueOf(DataUtil.getSha256()),
                    AdType.BANNER,
                    PointSubtype.ADD,
                    points * BANNER_MULTIPLIER,
                    "Banner points"
            );*//*
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            bannerState = State.OPENED;
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            bannerState = State.LOADED;
            View view = (View) bannerAdView.getParent();
            view.setVisibility(View.VISIBLE);
            pref.setBannerTime(TimeUtil.currentTime());
        }
    };*/

    private final AdListener interstitialListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            interstitialState = State.CLOSED;
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            interstitialState = State.LOADED;
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            interstitialState = State.LEFT;
            //lastInterstitialAdTime = TimeUtil.currentTime();
/*            earnPoints(
                    String.valueOf(DataUtil.getSha256()),
                    AdType.INTERSTITIAL,
                    PointSubtype.ADD,
                    points * INTERSTITIAL_MULTIPLIER,
                    "Interstitial points"
            );*/
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            interstitialState = State.OPENED;
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            interstitialState = State.LOADED;
            interstitialAd.show();
            pref.setInterstitialTime(TimeUtil.currentTime());
        }
    };

    private final class RewardedListener implements RewardedVideoAdListener {

        @Override
        public void onRewardedVideoAdLoaded() {
            rewardedState = State.LOADED;
            rewardedVideoAd.show();
            pref.setRewardedTime(TimeUtil.currentTime());
        }

        @Override
        public void onRewardedVideoAdOpened() {
            rewardedState = State.OPENED;
        }

        @Override
        public void onRewardedVideoStarted() {
            rewardedState = State.STARTED;
        }

        @Override
        public void onRewardedVideoAdClosed() {
            rewardedState = State.CLOSED;

        }

        @Override
        public void onRewarded(RewardItem rewardItem) {
/*            earnPoints(
                    String.valueOf(DataUtil.getSha256()),
                    AdType.REWARDED,
                    PointSubtype.ADD,
                    points * REWARDED_MULTIPLIER,
                    "Rewarded points"
            );*/
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            rewardedState = State.LEFT;

        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int errorCode) {
            rewardedState = State.FAILED;

        }

        @Override
        public void onRewardedVideoCompleted() {

        }
    }


    public static class Config {

        private long bannerExpireDelay;
        private long interstitialExpireDelay;
        private long rewardedExpireDelay;
        private boolean enabled;

        private Config(long bannerExpireDelay, long interstitialExpireDelay, long rewardedExpireDelay, boolean enabled) {
            this.bannerExpireDelay = bannerExpireDelay;
            this.interstitialExpireDelay = interstitialExpireDelay;
            this.rewardedExpireDelay = rewardedExpireDelay;
            this.enabled = enabled;
        }

        public static class Builder {
            private long bannerExpireDelay;
            private long interstitialExpireDelay;
            private long rewardedExpireDelay;
            private boolean enabled;

            public Builder() {

            }

            public Builder bannerExpireDelay(long bannerExpireDelay) {
                this.bannerExpireDelay = bannerExpireDelay;
                return this;
            }

            public Builder interstitialExpireDelay(long interstitialExpireDelay) {
                this.interstitialExpireDelay = interstitialExpireDelay;
                return this;
            }

            public Builder rewardedExpireDelay(long rewardedExpireDelay) {
                this.rewardedExpireDelay = rewardedExpireDelay;
                return this;
            }

            public Builder enabled(boolean enabled) {
                this.enabled = enabled;
                return this;
            }

            public Config build() {
                Config config = new Config(bannerExpireDelay, interstitialExpireDelay, rewardedExpireDelay, enabled);
                return config;
            }
        }
    }

    /* listeners */
    private static class BannerListener extends AdListener {

        @NonNull
        String screenId;

        BannerListener(@NonNull String screenId) {
            this.screenId = screenId;
        }
    }
}
