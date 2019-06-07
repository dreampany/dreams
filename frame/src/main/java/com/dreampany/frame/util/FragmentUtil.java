package com.dreampany.frame.util;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dreampany.frame.data.model.Task;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.ui.activity.BaseActivity;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class FragmentUtil {

    private FragmentUtil() {
    }

/*    public static <T extends Fragment> T commitFragment(final AppCompatActivity activity, final Class<T> fragmentClass, final int parentId) {

        final T fragment = getFragment(activity, fragmentClass);

        Runnable commitRunnable = new Runnable() {
            @Override
            public void run() {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                activity.getSupportFragmentManager().
                        beginTransaction().
                        //setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                                replace(parentId, fragment, fragmentClass.getName()).
                        commitAllowingStateLoss();
            }
        };

        AndroidUtil.getUiHandler().postDelayed(commitRunnable, 250L);

        return fragment;
    }*/

/*    public static <T extends Fragment> T commitFragment(final BaseActivity activity, final T fragment, final int parentId) {

        Runnable commitRunnable = () -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(parentId, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        };

        AndroidUtil.getUiHandler().postDelayed(commitRunnable, 250L);
        return fragment;
    }*/

/*    public static <T extends Fragment> T commitFragment(AppExecutors ex, BaseActivity activity, final T fragment, final int parentId) {

        Runnable commitRunnable = () -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(parentId, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        };

        ex.postToUi(commitRunnable);
        return fragment;
    }*/

    public static <T extends Fragment> T commitFragment(AppExecutors ex, BaseActivity activity, final T fragment, final int parentId) {

        Runnable commitRunnable = () -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(parentId, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        };

        ex.postToUi(commitRunnable);
        return fragment;
    }

    public static <T extends Fragment> T getFragment(AppCompatActivity activity, Class<T> fragmentClass) {

        T fragment = getFragmentByTag(activity, fragmentClass.getName());

        if (fragment == null) {
            fragment = newFragment(fragmentClass);
            if (fragment != null /*&& task != null*/) {
                Bundle bundle = new Bundle();
                //bundle.putSerializable(Task.class.getName(), task);
                fragment.setArguments(bundle);
            }
        } /*else if (task != null) {
            if (task instanceof Parcelable) {
                fragment.getArguments().putParcelable(Task.class.getName(), (Parcelable) task);
            } else if (task instanceof Serializable) {
                fragment.getArguments().putSerializable(Task.class.getName(), task);
            }
        }*/

        return fragment;
    }

    public static <F extends Fragment> F getSupportFragment(FragmentManager parent, Class<F> fragmentClass, String tag, Task<?> task) {

        F fragment = getFragmentByTag(parent, tag);

        if (fragment == null) {
            fragment = newFragment(fragmentClass);
            if (fragment != null && task != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Task.class.getSimpleName(), task);
                fragment.setArguments(bundle);
            }
        } else if (task != null) {
            fragment.getArguments().putParcelable(Task.class.getSimpleName(), task);
        }

        return fragment;
    }

    public static <T extends Fragment> T getFragmentByTag(AppCompatActivity activity, String fragmentTag) {
        return (T) activity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    public static <F extends Fragment> F getFragmentByTag(FragmentManager manager, String fragmentTag) {
        return (F) manager.findFragmentByTag(fragmentTag);
    }

    public static <T extends Fragment> T getFragment(Fragment parent, @IdRes int fragmentId) {
        return (T) parent.getChildFragmentManager().findFragmentById(fragmentId);
    }

    public static <T extends Fragment> T newFragment(Class<T> fragmentClass, Task<?> task) {
        T fragment = newFragment(fragmentClass);
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Task.class.getSimpleName(), task);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    public static <T extends Fragment> T newFragment(Class<T> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (Exception ignored) {
        }
        return null;
    }
}
