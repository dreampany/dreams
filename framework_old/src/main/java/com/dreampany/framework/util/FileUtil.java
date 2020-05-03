package com.dreampany.framework.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hawladar Roman on 7/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class FileUtil {
    private FileUtil() {
    }

    public static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) return false;
        return new File(path).exists();
    }

    public static long getFileHash(String path) {
        File file = new File(path);
        return DataUtil.getSha512(file);
    }

    public static String getMimeType(String path) {
        File file = new File(path);
        Uri selectedUri = Uri.fromFile(file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        return mimeType;
    }

    public static long getFileSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.length();
        }
        return -1;
    }

    public static List<String> readAssetsAsStrings(Context context, String path) {
        try {
            InputStream stream = context.getAssets().open(path);

            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            List<String> items = new ArrayList<>();
            String item;
            while ((item = in.readLine()) != null) {
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            return null;
        }
    }


/*    public static List<String> readAssetsAsStrings(Context context, String path) {
        try {
            InputStream stream = context.getAssets().open(path);
            Scanner scanner = new Scanner(stream);
            List<String> items = new ArrayList<>();

            while (scanner.hasNextLine()) {
                items.add(scanner.nextLine());
            }
            return items;
        } catch (IOException e) {
            return null;
        }
    }*/
}
