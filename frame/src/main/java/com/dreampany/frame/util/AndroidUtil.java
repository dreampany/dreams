package com.dreampany.frame.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.Surface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dreampany.frame.R;
import com.dreampany.frame.data.model.Task;
import com.dreampany.frame.misc.Constants;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class AndroidUtil {
    private AndroidUtil() {
    }

    private static int colorPrimary = -1;
    private static int colorPrimaryDark = -1;
    private static int colorAccent = -1;

    private static Handler uiHandler;
    private static Handler backHandler;

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static Handler getUiHandler() {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
        return uiHandler;
    }

    public static boolean isOnUiThread() {
        if (hasMarshmallow()) {
            return Looper.getMainLooper().isCurrentThread();
        }
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    @Nullable
    public static String getPackageName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return null;
        }
        return packageInfo.packageName;
    }

    public static String getLastApplicationId(Context context) {
        String applicationId = getPackageName(context.getApplicationContext());
        if (DataUtil.isEmpty(applicationId)) {
            return null;
        }
        return Iterables.getLast(Splitter.on(Constants.Sep.DOT).trimResults().split(applicationId));
    }

    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return 0;
        }
        return packageInfo.versionCode;
    }

    @Nullable
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return null;
        }
        return packageInfo.versionName;
    }

    public static PackageInfo getPackageInfo(Context context) {
        return getPackageInfo(context, 0);
    }

    public static PackageInfo getPackageInfo(Context context, int flags) {
        try {
            Context appContext = context.getApplicationContext();
            return appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), flags);
        } catch (PackageManager.NameNotFoundException nameException) {
            return null;
        }
    }

    public static List<ApplicationInfo> getInstalledApps(Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        return pm.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    public static boolean isSystemApp(ApplicationInfo info) {
        return ((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static boolean isValid(PackageManager pm, ApplicationInfo info) {
        return (pm.getLaunchIntentForPackage(info.packageName) != null);
    }

    public static Drawable getApplicationIcon(Context context, String packageName) {
        try {
            return context.getApplicationContext().getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException ignored) {

        }
        return null;
    }

    public static boolean isDebug(Context context) {
        boolean debuggable = false;

        Context appContext = context.getApplicationContext();
        PackageManager pm = appContext.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(appContext.getPackageName(), 0);
            debuggable = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException ignored) {
            /*debuggable variable will remain false*/
        }

        return debuggable;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getColorPrimary(Context context) {
        if (colorPrimary < 0) {
            int primaryAttr = FlexibleUtils.hasLollipop() ? android.R.attr.colorPrimary : R.attr.colorPrimary;
            TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[]{primaryAttr});
            colorPrimary = androidAttr.getColor(0, 0xFFFFFFFF); //Default: material_deep_teal_500
            androidAttr.recycle();
        }
        return colorPrimary;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getColorPrimaryDark(Context context) {
        if (colorPrimaryDark < 0) {
            int primaryDarkAttr = FlexibleUtils.hasLollipop() ? android.R.attr.colorPrimaryDark : R.attr.colorPrimaryDark;
            TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[]{primaryDarkAttr});
            colorPrimaryDark = androidAttr.getColor(0, 0xFFFFFFFF); //Default: material_deep_teal_500
            androidAttr.recycle();
        }
        return colorPrimaryDark;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getColorAccent(Context context) {
        if (colorAccent < 0) {
            int accentAttr = FlexibleUtils.hasLollipop() ? android.R.attr.colorAccent : R.attr.colorAccent;
            TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[]{accentAttr});
            colorAccent = androidAttr.getColor(0, 0xFFFFFFFF); //Default: material_deep_teal_500
            androidAttr.recycle();
        }
        return colorAccent;
    }

    /**
     * Show Soft Keyboard with new Thread
     *
     * @param activity
     */
    public static void hideSoftInput(final Activity activity) {
        if (activity.getCurrentFocus() != null) {
            ((Runnable) () -> {
                InputMethodManager imm =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
            }).run();
        }
    }

    /**
     * Hide Soft Keyboard from Dialogs with new Thread
     *
     * @param context
     * @param view
     */
    public static void hideSoftInputFrom(final Context context, final View view) {
        ((Runnable) () -> {
            InputMethodManager imm =
                    (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }).run();
    }

    /**
     * Show Soft Keyboard with new Thread
     *
     * @param context
     * @param view
     */
    public static void showSoftInput(final Context context, final View view) {
        ((Runnable) () -> {
            InputMethodManager imm =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }).run();
    }

    public static int getScreenOrientation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }

    public static boolean isAlive(Context context) {
        if (Activity.class.isInstance(context)) {
            return isAlive((Activity) context);
        }
        return false;
    }

    /**
     * To check activeness of activity
     *
     * @return true if host activity is not destroyed or finished otherwise false
     */
    public static boolean isAlive(Activity activity) {
        if (activity == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !(activity.isFinishing() || activity.isDestroyed());
        }
        return !activity.isFinishing();
    }

    public static boolean isAlive(Fragment fragment) {
        if (fragment != null) {
            return fragment.isVisible();
        }
        return false;
    }


    public static <T extends Context> Intent createIntent(T source, Class<?> target) {
        return createIntent(source, target, null);
    }

    public static <T extends Context> Intent createIntent(T source, Class<?> target, @Nullable Task<?> task) {
        Intent intent = new Intent(source, target);
        if (task != null) {
            intent.putExtra(Task.class.getSimpleName(), (Parcelable) task);
        }
        return intent;
    }

    /* ui opening section */
    public static <T extends Activity> void openActivity(T source, Class<?> target) {
        openActivity(source, target, false);
    }

    public static <T extends Fragment> void openActivity(T source, Class<?> target) {
        openActivity(source, target, false);
    }

    public static <T extends Activity> void openActivity(T source, Class<?> target, boolean finish) {
        if (source != null) {
            source.startActivity(new Intent(source, target));
            if (finish) {
                source.finish();
            }
            Animato.animateSlideLeft(source);
        }
    }

    public static <T extends Fragment> void openActivity(T source, Class<?> target, boolean finish) {
        if (source != null) {
            source.startActivity(new Intent(source.getActivity(), target));
            if (finish) {
                source.getActivity().finish();
            }
            Animato.animateSlideLeft(source.getActivity());
        }
    }

    public static <T extends Activity> void openActivity(T source, Class<?> target, int requestCode) {
        if (source != null) {
            source.startActivityForResult(new Intent(source, target), requestCode);
            Animato.animateSlideLeft(source);
        }
    }

    public static <T extends Fragment> void openActivity(T source, Class<?> target, int requestCode) {
        if (source != null) {
            source.startActivityForResult(new Intent(source.getActivity(), target), requestCode);
            Animato.animateSlideLeft(source.getActivity());
        }
    }

    public static <T extends Activity, X extends Parcelable> void openActivity(T source, Class<?> target, Task<X> task) {
        openActivity(source, target, task, false);
    }

    public static <T extends Activity, X extends Parcelable> void openActivity(T source, Class<?> target, Task<X> task, boolean finish) {
        if (source != null) {
            Intent intent = new Intent(source, target);
            intent.putExtra(Task.class.getSimpleName(), (Parcelable) task);
            source.startActivity(intent);
        }
        if (finish) {
            source.finish();
        }
        Animato.animateSlideLeft(source);
    }

    public static <T extends Fragment, X extends Parcelable> void openActivity(T source, Class<?> target, Task<X> task) {
        if (source != null) {
            Intent intent = new Intent(source.getActivity(), target);
            if (task != null) {
                intent.putExtra(Task.class.getSimpleName(), (Parcelable) task);
            }
            source.startActivity(intent);
            Animato.animateSlideLeft(source.getActivity());
        }
    }

    public static <T extends Fragment, X extends Parcelable> void openActivity(T source, Class<?> target, Task<X> task, int requestCode) {
        if (source != null) {
            Intent intent = new Intent(source.getActivity(), target);
            intent.putExtra(Task.class.getSimpleName(), (Parcelable) task);
            source.startActivityForResult(intent, requestCode);
            Animato.animateSlideLeft(source.getActivity());
        }
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

/*    public static String getDeviceName() {
        return DeviceName.getDeviceName();
    }*/


    private static TextToSpeech tts;

    public static void initTts(Context context) {
        if (tts == null) {
            try {
                tts = new TextToSpeech(context.getApplicationContext(), status -> {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.ENGLISH);
                    }
                });
            } catch (IllegalArgumentException e) {
                Timber.e("Error in tts: %s", e.toString());
            }
        }
    }

    public static void speak(String text) {
        if (tts != null && text != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle params = new Bundle();
                params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1f);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1");
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public static void silentSpeak() {
        if (tts != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak("", TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                tts.speak("", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }


    public static void stopTts() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    public static void backupDatabase(Context context, String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            String packageName = context.getApplicationInfo().packageName;

            if (sd.canWrite()) {
                String currentDBPath = String.format("//data//%s//databases//%s",
                        packageName, databaseName);
                String backupDBPath = String.format("debug_%s.sqlite", packageName);
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static void share(Fragment fragment, String subject, String text) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        fragment.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    public static void share(Activity activity, String subject, String text) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startService(Context context, Class<?> serviceClass) {
        AndroidUtil.startService(context, new Intent(context, serviceClass));
    }

    public static void startService(Context context, Intent serviceIntent) {
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
