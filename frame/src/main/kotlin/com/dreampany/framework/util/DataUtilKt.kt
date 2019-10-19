package com.dreampany.framework.util

import android.util.SparseArray
import androidx.core.util.PatternsCompat
import com.dreampany.framework.misc.Constants
import com.google.common.base.Strings
import com.google.common.hash.Hashing
import com.google.common.io.Files
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Roman-372 on 7/25/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DataUtilKt {
    companion object {

        private val COLLISION_TAG = "collision"
        val NewLine = "\n"
        val NewLine2 = "\n\n"
        val COMMA = ","
        val SEMI = ":"
        val SPACE = " "

        fun getRandId() :String {
            return UUID.randomUUID().toString()
        }

        fun isEmpty(item: String?): Boolean {
            return item.isNullOrEmpty()
        }

        fun isEmpty(vararg items: String): Boolean {
            if (items.isEmpty()) {
                return true
            }
            for (item in items) {
                if (!Strings.isNullOrEmpty(item)) {
                    return false
                }
            }
            return true
        }

        fun isAnyEmpty(vararg items: String?): Boolean {
            if (items.isEmpty()) {
                return true
            }
            for (item in items) {
                if (Strings.isNullOrEmpty(item)) {
                    return true
                }
            }
            return false
        }

        fun join(vararg items: Int): String {
            val builder = StringBuilder()
            for (item in items) {
                builder.append(item)
            }
            return builder.toString()
        }

        fun join(vararg items: String): String {
            val builder = StringBuilder()
            for (item in items) {
                builder.append(item)
            }
            return builder.toString()
        }

        fun getFirstPart(value: String, sep: String): String {
            return value.split(sep).first()
        }

        fun isValidUrl(url: String): Boolean {
            return PatternsCompat.WEB_URL.matcher(url).matches()
        }

        fun isValidImageUrl(url: String): Boolean {
            val pattern = Constants.Pattern.IMAGE_PATTERN
            return pattern.matcher(url).matches()
        }

        fun joinPrefixIf(url: String, prefix: String): String {
            return if (!url.startsWith(prefix)) prefix.plus(url) else url
        }

        fun formatReadableSize(value: Long): String {
            return formatReadableSize(value, false)
        }

        fun formatReadableSize(value: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (value < unit) return "$value B"
            val exp = (Math.log(value.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else ""
            return String.format(
                Locale.ENGLISH,
                "%.1f %sB",
                value / Math.pow(unit.toDouble(), exp.toDouble()),
                pre
            )
        }

/*        fun isEmpty(collection: Collection<*>?): Boolean {
            return collection == null || collection.isEmpty()
        }*/

        fun <T> sub(list: MutableList<T>?, count: Int): MutableList<T>? {
            var count = count
            if (list.isNullOrEmpty()) {
                return null
            }
            count = if (list.size < count) list.size else count
            return ArrayList(list.subList(0, count))
        }

        fun <T> takeFirst(list: MutableList<T>?, count: Int): MutableList<T>? {
            if (list.isNullOrEmpty()) {
                return null
            }

            val result = sub(list, count)
            removeAll(list, result)
            return result
        }

        fun <T> takeFirst(list: ArrayList<String>?) : String? {
            return list?.first()
        }

        fun <T> takeRandom(list: ArrayList<String>?) : String? {
            return list?.random()
        }

        fun <T> removeAll(list: MutableList<T>, sub: MutableList<T>?): MutableList<T>? {
            sub?.run {
                list.removeAll(this)
            }
            return list
        }

/*        fun formatReadableSize(value: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (value < unit) return value.toString()
            val exp = (Math.log(value.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
            return String.format(
                Locale.ENGLISH,
                "%.1f %s",
                value / Math.pow(unit.toDouble(), exp.toDouble()),
                pre
            )
        }*/

/*        fun join(vararg data: String): String {
            return StringUtils.join(*data)
        }*/

        fun getSha512(): Long {
            val uuid = UUID.randomUUID().toString()
            return getSha512(uuid)
        }

        fun <T> getSha512(vararg data: T): Long {
            if (DataUtil.isEmpty(data)) {
                return 0L
            }
            val builder = StringBuilder()
            for (item in data) {
                builder.append(item)
            }
            return getSha512(builder.toString())
        }

        fun getSha512(vararg data: String): Long {
            if (DataUtil.isEmpty(*data)) {
                return 0L
            }
            val builder = StringBuilder()
            for (item in data) {
                builder.append(item)
            }
            return getSha512(builder.toString())
        }

        fun getSha512(data: String): Long {
            return if (DataUtil.isEmpty(data)) {
                0L
            } else getSha512(data.toByteArray())
        }

        fun getSha512(data: ByteArray): Long {
            return if (DataUtil.isEmpty(data)) {
                0L
            } else Hashing.sha512().newHasher().putBytes(data).hash().asLong()
        }

        fun getSha512ByUri(data: String): Long {
            if (DataUtil.isEmpty(data)) {
                return 0L
            }
            val file = File(data)
            return getSha512(file)
        }

        fun getSha512(file: File): Long {
            try {
                val hash = Files.asByteSource(file).hash(Hashing.sha512())
                return hash.asLong()
            } catch (e: IOException) {
                return 0L
            }

        }

        fun <T> asList(array: SparseArray<T>?): List<T>? {
            if (array == null) return null
            val arrayList = java.util.ArrayList<T>(array.size())
            for (i in 0 until array.size())
                arrayList.add(array.valueAt(i))
            return arrayList
        }

        fun isEmpty(data: Any?): Boolean {
            return data == null
        }

/*        fun isEmpty(item: String): Boolean {
            return Strings.isNullOrEmpty(item)
        }*/

/*    public static boolean isEmpty(String[] items) {
        if (items == null || items.length == 0) {
            return true;
        }
        for (String item : items) {
            if (!Strings.isNullOrEmpty(item)) {
                return false;
            }
        }
        return true;
    }*/

/*        fun isEmpty(vararg items: String): Boolean {
            if (items == null || items.size == 0) {
                return true
            }
            for (item in items) {
                if (!Strings.isNullOrEmpty(item)) {
                    return false
                }
            }
            return true
        }*/

        fun isEmpty(data: ByteArray?): Boolean {
            return data == null || data.size <= 0
        }

        fun isEmpty(collection: Collection<*>?): Boolean {
            return collection == null || collection.isEmpty()
        }

        fun isNotEmpty(collection: Collection<*>): Boolean {
            return !isEmpty(collection)
        }

        fun getSize(collection: Collection<*>?): Int {
            return collection?.size ?: 0
        }

        fun isEquals(left: String?, right: String?): Boolean {
            if (!left.isNullOrEmpty()) {
                return left.equals(right)
            }
            if (!right.isNullOrEmpty()) {
                return right.equals(left)
            }
            return true
        }

        fun isEquals(left: Collection<*>, right: Collection<*>): Boolean {
            return getSize(left) == getSize(right)
        }

        fun <T> pullLast(list: List<T>): T? {
            return if (isEmpty(list)) {
                null
            } else list[list.size - 1]
        }

/*        fun <T> takeFirst(list: MutableList<T>, count: Int): List<T>? {
            if (isEmpty(list)) {
                return null
            }

            val result = sub<T>(list, count)
            removeAll(list, result)
            return result
        }*/

/*        fun <T> sub(list: List<T>, count: Int): List<T>? {
            var count = count
            if (isEmpty(list)) {
                return null
            }
            count = if (list.size < count) list.size else count
            return java.util.ArrayList(list.subList(0, count))
        }*/

        fun <T> sub(list: List<T>, index: Int, limit: Int): List<T>? {
            var limit = limit
            if (isEmpty(list) || index < 0 || limit < 1 || list.size <= index) {
                return null
            }
            if (list.size - index < limit) {
                limit = list.size - index
            }
            return list.subList(index, index + limit)
        }

/*        fun <T> removeFirst(list: List<T>, count: Int): List<T>? {
            if (count <= 0 || isEmpty(list) || list.size <= count) {
                return list
            }
            list.subList(0, count).clear()
            return list
        }*/

/*        fun <T> removeAll(list: MutableList<T>, sub: List<T>?): List<T> {
            list.removeAll(sub!!)
            return list
        }*/

        fun <T> getRandomItem(items: List<T>): T? {
            return if (isEmpty(items)) {
                null
            } else items[NumberUtil.nextRand(0, items.size - 1)]
        }

        fun getReadableDuration(duration: Long): String {
            val seconds = duration / 1000
            val s = seconds % 60
            val m = seconds / 60 % 60
            val h = seconds / (60 * 60) % 24
            return if (h <= 0) {
                String.format(Locale.ENGLISH, "%02d:%02d", m, s)
            } else String.format(Locale.ENGLISH, "%02d:%02d:%02d", h, m, s)
        }

/*        fun formatReadableSize(value: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (value < unit) return "$value B"
            val exp = (Math.log(value.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
            return String.format(
                Locale.ENGLISH,
                "%.1f %sB",
                value / Math.pow(unit.toDouble(), exp.toDouble()),
                pre
            )
        }*/

/*    public static String formatReadableCount(long value, boolean si) {
        int unit = si ? 1000 : 1024;
        if (value < unit) return String.valueOf(value);
        int exp = (int) (Math.log(value) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %s", value / Math.pow(unit, exp), pre);
    }*/

        fun copy(src: ByteArray, from: Int): ByteArray {
            return Arrays.copyOfRange(src, from, src.size)
        }

        fun copyToBuffer(src: ByteArray, offset: Int): ByteBuffer {
            val data = Arrays.copyOfRange(src, offset, src.size)
            return ByteBuffer.wrap(data)
            //return ByteBuffer.wrap(src, offset, src.length - offset);
        }

        fun isAlpha(name: String): Boolean {
            return name.matches("[a-zA-Z]+".toRegex())
        }

        fun joinString(values: Array<String>): String? {
            var value: String? = null
            if (!DataUtil.isEmpty(*values)) {
                val builder = StringBuilder()
                for (v in values) {
                    if (builder.length > 0) {
                        builder.append(COMMA)
                    }
                    builder.append(v)
                }
                value = builder.toString()
            }
            return value
        }

        fun joinString(values: List<String>): String? {
            var value: String? = null
            if (!DataUtil.isEmpty(values)) {
                val builder = StringBuilder()
                for (v in values) {
                    if (builder.length > 0) {
                        builder.append(COMMA)
                    }
                    builder.append(v)
                }
                value = builder.toString()
            }
            return value
        }

        fun joinString(values: List<String>, sep: String): String? {
            var value: String? = null
            if (!DataUtil.isEmpty(values)) {
                val builder = StringBuilder()
                for (v in values) {
                    if (builder.length > 0) {
                        builder.append(sep)
                    }
                    builder.append(v)
                }
                value = builder.toString()
            }
            return value
        }

        fun joinString(builder: StringBuilder, value: String) {
            joinString(builder, value, COMMA)
        }

        fun joinString(builder: StringBuilder, value: String, separator: String) {
            val values = TextUtil.getWords(value)
            if (!DataUtil.isEmpty(values)) {
                for (v in values) {
                    if (builder.length > 0) {
                        builder.append(separator)
                    }
                    builder.append(v)
                }
            }
        }

        fun toIntArrayOf(list: List<Int>): Array<Int> {
            return list.toTypedArray()
        }

        fun toStringArray(list: List<String>): Array<String> {
            return list.toTypedArray()
        }

        fun getMin(left: Int, right: Int): Int {
            return if (left < right) left else right
        }
    }
}