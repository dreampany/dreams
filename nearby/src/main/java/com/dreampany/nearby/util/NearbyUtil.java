package com.dreampany.nearby.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Hawladar Roman on 6/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class NearbyUtil {

    private static Random random = new Random();

    private NearbyUtil() {
    }

    private static PackageInfo getPackageInfo(Context context, int flags) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), flags);
        } catch (PackageManager.NameNotFoundException nameException) {
            return null;
        }
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
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_PERMISSIONS);
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

    public static long getSha256() {
        String uuid = UUID.randomUUID().toString();
        return getSha256(uuid);
    }

    public static long getSha256(String data) {
        if (Strings.isNullOrEmpty(data)) {
            return 0L;
        }
        return getSha256(data.getBytes());
    }

    public static long getSha256(byte[] data) {
        if (data == null || data.length == 0) {
            return 0L;
        }
        return Math.abs(Hashing.sha256().newHasher().putBytes(data).hash().asLong());
    }

    public static int nextRand(int upper) {
        if (upper <= 0) return -1;
        return random.nextInt(upper);
    }

    public static int nextRand(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static byte[] copy(byte[] src, int from) {
        return Arrays.copyOfRange(src, from, src.length);
    }

    public static ByteBuffer copyToBuffer(byte[] src, int from) {
        byte[] data = copy(src, from);
        return ByteBuffer.wrap(data);
    }
}
