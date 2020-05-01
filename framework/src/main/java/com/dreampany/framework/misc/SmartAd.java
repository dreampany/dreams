package com.dreampany.framework.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.dreampany.framework.data.source.pref.AdPref;
import com.dreampany.framework.util.TimeUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 2/7/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class SmartAd {

    private enum State {DEFAULT, FAILED, LOADED, OPENED, STARTED, RESUMED, PAUSED, CLICKED, LEFT, COMPLETED, CLOSED}

    private static final long defaultAdDelay = TimeUnit.SECONDS.toMillis(3);

    private Context context;
    private final Map<String, MutablePair<AdView, State>> banners;
    private final Map<String, MutablePair<InterstitialAd, State>> interstitials;
    private final Map<String, MutablePair<RewardedAd, State>> rewardeds;

    private State interstitialState = State.DEFAULT;
    private State rewardedState = State.DEFAULT;

    private static final int BANNER_MULTIPLIER = 1;
    private static final int INTERSTITIAL_MULTIPLIER = 2;
    private static final int REWARDED_MULTIPLIER = 3;

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


    public void initBanner(@NonNull String screenId,
                           @NonNull AdView banner) {
        banners.put(screenId, MutablePair.of(banner, State.DEFAULT));
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



    public void initInterstitial(@NonNull Context context,
                                 @NonNull String screenId,
                                 @StringRes int adUnitId) {
        InterstitialAd interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(context.getString(adUnitId));
        interstitials.put(screenId, MutablePair.of(interstitial, State.DEFAULT));

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


    public void initRewarded(@NonNull Context context,
                             @NonNull String screenId,
                             @StringRes int adUnitId) {

        RewardedAd rewarded = new RewardedAd(context, context.getString(adUnitId));
        rewardeds.put(screenId, MutablePair.of(rewarded, State.DEFAULT));
    }



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
}
