package com.dreampany.frame.util;

import android.util.SparseArray;

import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class DataUtil {

    private static final String COLLISION_TAG = "collision";
    public static final String NewLine = "\n";
    public static final String NewLine2 = "\n\n";
    public static final String COMMA = ",";
    public static final String SEMI = ":";
    public static final String SPACE = " ";

    private DataUtil() {

    }

    public static long getSha512() {
        String uuid = UUID.randomUUID().toString();
        return getSha512(uuid);
    }

    public static long getSha512(long first, String... data) {
        if (DataUtil.isEmpty(data)) {
            return 0L;
        }
        StringBuilder builder = new StringBuilder(String.valueOf(first));
        for (String item : data) {
            builder.append(item);
        }
        return getSha512(builder.toString());
    }

    public static long getSha512(String... data) {
        if (DataUtil.isEmpty(data)) {
            return 0L;
        }
        StringBuilder builder = new StringBuilder();
        for (String item : data) {
            builder.append(item);
        }
        return getSha512(builder.toString());
    }

    public static long getSha512(String data) {
        if (DataUtil.isEmpty(data)) {
            return 0L;
        }
        return getSha512(data.getBytes());
    }

    public static long getSha512CollisionFree(String data) {
        if (DataUtil.isEmpty(data)) {
            return 0L;
        }
        return getSha512(data, COLLISION_TAG);
    }

    public static long getSha512(byte[] data) {
        if (DataUtil.isEmpty(data)) {
            return 0L;
        }
        return Hashing.sha512().newHasher().putBytes(data).hash().asLong();
    }

    public static long getSha512ByUri(String data) {
        if (DataUtil.isEmpty(data)) {
            return 0L;
        }
        File file = new File(data);
        return getSha512(file);
    }

    public static long getSha512(File file) {
        try {
            HashCode hash = Files.asByteSource(file).hash(Hashing.sha512());
            return hash.asLong();
        } catch (IOException e) {
            return 0L;
        }
    }

    public static <T> List<T> asList(SparseArray<T> array) {
        if (array == null) return null;
        List<T> arrayList = new ArrayList<T>(array.size());
        for (int i = 0; i < array.size(); i++)
            arrayList.add(array.valueAt(i));
        return arrayList;
    }

    public static boolean isEmpty(String item) {
        return Strings.isNullOrEmpty(item);
    }

    public static boolean isEmpty(String[] items) {
        if (items == null || items.length == 0) {
            return true;
        }
        for (String item : items) {
            if (!Strings.isNullOrEmpty(item)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(byte[] data) {
        return data == null || data.length <= 0;
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <T> List<T> sub(List<T> list, int count) {
        if (isEmpty(list) || list.size() <= count) {
            return null;
        }
        return list.subList(0, count);
    }

    public static <T> List<T> sub(List<T> list, int index, int limit) {

        if (isEmpty(list) || index < 0 || limit < 1 || list.size() <= index) {
            return null;
        }
        //limit = getMin(list.size(), limit);
        if ((list.size() - index) < limit) {
            limit = list.size() - index;
        }
        return list.subList(index, index + limit);
    }

    public static <T> List<T> removeFirst(List<T> list, int count) {
        if (count <= 0 || isEmpty(list) || list.size() <= count) {
            return list;
        }
        list.subList(0, count).clear();
        return list;
    }

    public static <T> List<T> removeAll(List<T> list, List<T> sub) {
        list.removeAll(sub);
        return list;
    }

    public static <T> T getRandomItem(List<T> items) {
        if (isEmpty(items)) {
            return null;
        }
        return items.get(NumberUtil.nextRand(0, items.size() - 1));
    }

    public static String getReadableDuration(long duration) {
        long seconds = duration / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        if (h <= 0) {
            return String.format(Locale.ENGLISH, "%02d:%02d", m, s);
        }
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", h, m, s);
    }

    public static String formatReadableSize(long value, boolean si) {
        int unit = si ? 1000 : 1024;
        if (value < unit) return value + " B";
        int exp = (int) (Math.log(value) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %sB", value / Math.pow(unit, exp), pre);
    }

    public static String formatReadableCount(long value, boolean si) {
        int unit = si ? 1000 : 1024;
        if (value < unit) return String.valueOf(value);
        int exp = (int) (Math.log(value) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %s", value / Math.pow(unit, exp), pre);
    }

    public static byte[] copy(byte[] src, int from) {
        return Arrays.copyOfRange(src, from, src.length);
    }

    public static ByteBuffer copyToBuffer(byte[] src, int offset) {
        byte[] data = Arrays.copyOfRange(src, offset, src.length);
        return ByteBuffer.wrap(data);
        //return ByteBuffer.wrap(src, offset, src.length - offset);
    }

    public static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public static String toString(String[] values) {
        String value = null;
        if (!DataUtil.isEmpty(values)) {
            StringBuilder builder = new StringBuilder();
            for (String v : values) {
                if (builder.length() > 0) {
                    builder.append(COMMA);
                }
                builder.append(v);
            }
            value = builder.toString();
        }
        return value;
    }

    public static String toString(List<String> values) {
        String value = null;
        if (!DataUtil.isEmpty(values)) {
            StringBuilder builder = new StringBuilder();
            for (String v : values) {
                if (builder.length() > 0) {
                    builder.append(COMMA);
                }
                builder.append(v);
            }
            value = builder.toString();
        }
        return value;
    }

    public static void joinString(StringBuilder builder, String value, String separator) {
        List<String> values = TextUtil.getWords(value);
        if (!DataUtil.isEmpty(values)) {
            for (String v : values) {
                if (builder.length() > 0) {
                    builder.append(separator);
                }
                builder.append(v);
            }
        }
    }

    public static String[] toStringArray(List<String> list) {
        return list.toArray(new String[0]);
    }

    public static int getMin(int left, int right) {
        return left < right ? left : right;
    }
}
