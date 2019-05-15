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

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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

    private enum State {NONE, FAILED, LOADED, OPENED, STARTED, RESUMED, PAUSED, CLICKED, LEFT, COMPLETED, CLOSED}

    private static final long defaultAdDelay = TimeUnit.SECONDS.toMillis(3);

    private Context context;
    private final Map<String, MutablePair<AdView, State>> banners;
    private final Map<String, MutablePair<InterstitialAd, State>> interstitials;
    private final Map<String, MutablePair<RewardedAd, State>> rewardeds;

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
        interstitials = Maps.newConcurrentMap();
        rewardeds = Maps.newConcurrentMap();
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @DebugLog
    public void initAd(@NonNull Context context,
                       @NonNull String screenId,
                       @NonNull AdView banner,
                       @StringRes int interstitialId,
                       @StringRes int rewardedId) {
        if (!config.enabled) {
            return;
        }
        initBanner(screenId, banner);
        initInterstitial(context, screenId, interstitialId);
        initRewarded(context, screenId, rewardedId);
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

    public void loadAd(@NonNull String screenId) {
        boolean loaded = loadInterstitial(screenId);
        if (!loaded) {
            loadBanner(screenId);
        }
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public boolean loadBanner(@NonNull String screenId) {
        if (!pref.isBannerTimeExpired(config.bannerExpireDelay)) {
            return false;
        }
        if (!banners.containsKey(screenId)) {
            return false;
        }
        AdView banner = banners.get(screenId).left;
        banner.loadAd(new AdRequest.Builder().build());
        return true;
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
        State state = banners.get(screenId).right;
        if (state != State.LOADED) {
            return;
        }
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
        State state = banners.get(screenId).right;
        if (state != State.LOADED) {
            return;
        }
        AdView banner = banners.get(screenId).left;
        View view = (View) banner.getParent();
        view.setVisibility(View.GONE);
        banner.pause();
    }

    @DebugLog
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
    public void initInterstitial(@NonNull Context context,
                                 @NonNull String screenId,
                                 @StringRes int adUnitId) {
        InterstitialAd interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(context.getString(adUnitId));
        interstitials.put(screenId, MutablePair.of(interstitial, State.NONE));

        if (interstitial.getAdListener() == null) {
            interstitial.setAdListener(new BannerListener(screenId) {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    interstitials.get(screenId).setRight(State.FAILED);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    interstitials.get(screenId).setRight(State.LOADED);
                    interstitials.get(screenId).getLeft().show();
                    pref.setInterstitialTime(TimeUtil.currentTime());
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    interstitials.get(screenId).setRight(State.OPENED);
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    interstitials.get(screenId).setRight(State.CLICKED);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                    interstitials.get(screenId).setRight(State.LEFT);
                }
            });
        }
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public boolean loadInterstitial(@NonNull String screenId) {
        if (!pref.isInterstitialTimeExpired(config.interstitialExpireDelay)) {
            return false;
        }
        if (!interstitials.containsKey(screenId)) {
            return false;
        }
        InterstitialAd interstitial = interstitials.get(screenId).left;
        interstitial.loadAd(new AdRequest.Builder().build());
        return true;
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void resumeInterstitial(@NonNull String screenId) {
        if (!pref.isInterstitialTimeExpired(config.interstitialExpireDelay)) {
            return;
        }
        if (!interstitials.containsKey(screenId)) {
            return;
        }
        InterstitialAd interstitial = interstitials.get(screenId).left;
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void pauseInterstitial(@NonNull String screenId) {
        if (!pref.isInterstitialTimeExpired(config.interstitialExpireDelay)) {
            return;
        }
        if (!interstitials.containsKey(screenId)) {
            return;
        }
        InterstitialAd interstitial = interstitials.get(screenId).left;
    }

    @SuppressLint("MissingPermission")
    public void destroyInterstitial(@NonNull String screenId) {
        if (!pref.isInterstitialTimeExpired(config.interstitialExpireDelay)) {
            return;
        }
        if (!interstitials.containsKey(screenId)) {
            return;
        }
        InterstitialAd interstitial = interstitials.get(screenId).left;
        //interstitial.d();
    }

    @DebugLog
    public void initRewarded(@NonNull Context context,
                             @NonNull String screenId,
                             @StringRes int adUnitId) {

        RewardedAd rewarded = new RewardedAd(context, context.getString(adUnitId));
        rewardeds.put(screenId, MutablePair.of(rewarded, State.NONE));
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void loadRewarded(@NonNull String screenId) {
        if (!pref.isRewardedTimeExpired(config.rewardedExpireDelay)) {
            //return;
        }
        if (!rewardeds.containsKey(screenId)) {
            return;
        }
        RewardedAd rewarded = rewardeds.get(screenId).left;
        rewarded.loadAd(new AdRequest.Builder().build(), new RewardedListener(screenId) {
            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                super.onRewardedAdFailedToLoad(errorCode);
                rewardeds.get(screenId).setRight(State.FAILED);
            }

            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
                rewardeds.get(screenId).setRight(State.LOADED);
            }
        });
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void resumeRewarded(@NonNull String screenId) {
        if (!pref.isRewardedTimeExpired(config.rewardedExpireDelay)) {
            return;
        }
        if (!rewardeds.containsKey(screenId)) {
            return;
        }
        RewardedAd rewarded = rewardeds.get(screenId).left;
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void pauseRewarded(@NonNull String screenId) {
        if (!pref.isRewardedTimeExpired(config.rewardedExpireDelay)) {
            return;
        }
        if (!rewardeds.containsKey(screenId)) {
            return;
        }
        RewardedAd rewarded = rewardeds.get(screenId).left;
    }

    @DebugLog
    @SuppressLint("MissingPermission")
    public void destroyRewarded(@NonNull String screenId) {
        if (!pref.isRewardedTimeExpired(config.rewardedExpireDelay)) {
            return;
        }
        if (!rewardeds.containsKey(screenId)) {
            return;
        }
        RewardedAd rewarded = rewardeds.get(screenId).left;
    }

    /*@DebugLog
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
    private boolean initAd(RewardedVideoAd rewardedVideoAd, String unitId) {
        if (rewardedVideoAd.getRewardedVideoAdListener() == null) {
            rewardedVideoAd.setRewardedVideoAdListener(new RewardedListener());
        }
        AndroidUtil.getUiHandler().postDelayed(() -> rewardedVideoAd.loadAd(unitId, new AdRequest.Builder().build()), defaultAdDelay);
        return true;
    }

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
*//*            earnPoints(
                    String.valueOf(DataUtil.getSha256()),
                    AdType.REWARDED,
                    PointSubtype.ADD,
                    points * REWARDED_MULTIPLIER,
                    "Rewarded points"
            );*//*
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
    }*/


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

    private static class InterstitialListener extends AdListener {

        @NonNull
        String screenId;

        InterstitialListener(@NonNull String screenId) {
            this.screenId = screenId;
        }
    }

    private static class RewardedListener extends RewardedAdLoadCallback {

        @NonNull
        String screenId;

        RewardedListener(@NonNull String screenId) {
            this.screenId = screenId;
        }
    }












/*
    private void earnPoints(String id, AdType type, PointSubtype subtype, int points, String comment) {
        FrameManager.onInstance(context).trackPoints(id, type.value(), subtype.value(), points, comment);

        AdEvent eventType = new AdEvent();
        eventType.points = points;
        eventType.setType(type);
        eventType.setSubtype(subtype);

        EventManager.post(eventType);
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

/*    private final AdListener interstitialListener = new AdListener() {
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
*//*            earnPoints(
                    String.valueOf(DataUtil.getSha256()),
                    AdType.INTERSTITIAL,
                    PointSubtype.ADD,
                    points * INTERSTITIAL_MULTIPLIER,
                    "Interstitial points"
            );*//*
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
    };*/
}
