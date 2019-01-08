package com.dreampany.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import java.util.Arrays;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class PermissionUtil {
    private PermissionUtil() {
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (!hasPermission(context, permission)) {
                    return false;
                }
            }
            return true;
        }
        return false; //when not decided or not expected solution
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasOverlayPermission(Context context) {
        return Settings.canDrawOverlays(context.getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasWriteSettingsPermission(Context context) {
        return Settings.System.canWrite(context.getApplicationContext());
    }

    public static boolean checkOverlayPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!hasOverlayPermission(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkWriteSettingsPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!hasWriteSettingsPermission(activity.getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return hasPermissionInManifest(context, permission);
        }
        return ContextCompat.checkSelfPermission(context.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean hasPermissionInManifest(Context context, String permission) {
        PackageInfo packageInfo = AndroidUtil.getPackageInfo(context, PackageManager.GET_PERMISSIONS);
        if (packageInfo != null) {
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if (requestedPermissions != null) {
                if (Arrays.asList(requestedPermissions).contains(permission)) {
                    return true;
                }
            }
        }
        return false;
    }
}
