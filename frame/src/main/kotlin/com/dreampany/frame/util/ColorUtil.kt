package com.dreampany.frame.util

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.dreampany.frame.R
import com.dreampany.frame.data.model.Color
import java.util.*


/**
 * Created by roman on 2019-08-02
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ColorUtil {


    companion object {
        private val random: Random = Random(System.currentTimeMillis())
        private val defaultColorCodes: MutableList<Int> = mutableListOf()
        private val materialColorCodes: MutableList<Int> = mutableListOf()

        init {
            defaultColorCodes.addAll(
                listOf(
                    0xfff16364,
                    0xfff58559,
                    0xfff9a43e,
                    0xffe4c62e,
                    0xff67bf74,
                    0xff59a2be,
                    0xff2093cd,
                    0xffad62a7,
                    0xff805781
                ) as Collection<Int>
            )
            materialColorCodes.addAll(
                listOf(
                    0xffe57373,
                    0xfff06292,
                    0xffba68c8,
                    0xff9575cd,
                    0xff7986cb,
                    0xff64b5f6,
                    0xff4fc3f7,
                    0xff4dd0e1,
                    0xff4db6ac,
                    0xff81c784,
                    0xffaed581,
                    0xffff8a65,
                    0xffd4e157,
                    0xffffd54f,
                    0xffffb74d,
                    0xffa1887f,
                    0xff90a4ae
                ) as Collection<Int>
            )
        }

        fun getDefaultRandomColor(): Int {
            return defaultColorCodes.get(random.nextInt(defaultColorCodes.size))
        }

        fun getDefaultColor(key: Any): Int {
            return defaultColorCodes.get(Math.abs(key.hashCode()) % defaultColorCodes.size)
        }

        fun getMaterialRandomColor(): Int {
            return materialColorCodes.get(random.nextInt(materialColorCodes.size))
        }

        fun getMaterialColor(key: Any): Int {
            return materialColorCodes.get(Math.abs(key.hashCode()) % materialColorCodes.size)
        }

        fun getStatusBarColor(primaryColor: Int): Int {
            val arrayOfFloat = FloatArray(3)
            android.graphics.Color.colorToHSV(primaryColor, arrayOfFloat)
            arrayOfFloat[2] *= 0.9f
            return android.graphics.Color.HSVToColor(arrayOfFloat)
        }

        fun createColor(primary: Int, primaryDark: Int, accent: Int): Color {
            return Color(primary, primaryDark, accent)
        }

        fun createRedColor(): Color {
            return Color(R.color.material_red500, R.color.material_red700, R.color.material_redA700)
        }

        fun getColor(context: Context, colorId: Int): Int {
            return ContextCompat.getColor(context, colorId)
        }

        private val colors = ArrayList<Color>()

        fun getRandColor(position: Int): Color {
            if (colors.isEmpty()) {
                /*          Color redColor = new Color(R.color.colorRed500, R.color.colorRed700).setAccentId(R.color.colorRed900);
            Color pinkColor = new Color(R.color.colorPink500, R.color.colorPink700).setAccentId(R.color.colorPink900);
            Color purpleColor = new Color(R.color.colorPurple500, R.color.colorPurple700).setAccentId(R.color.colorPurple900);

            Color deepPurpleColor = new Color(R.color.colorDeepPurple500, R.color.colorDeepPurple700).setAccentId(R.color.colorDeepPurple900);
            Color indigoColor = new Color(R.color.colorIndigo500, R.color.colorIndigo700).setAccentId(R.color.colorIndigo900);
            Color blueColor = new Color(R.color.colorBlue500, R.color.colorBlue700).setAccentId(R.color.colorBlue900);

            Color lightBlueColor = new Color(R.color.colorLightBlue600, R.color.colorLightBlue700).setAccentId(R.color.colorLightBlue900);
            Color cyanColor = new Color(R.color.colorCyan600, R.color.colorCyan700).setAccentId(R.color.colorCyan900);
            Color tealColor = new Color(R.color.colorTeal500, R.color.colorTeal700).setAccentId(R.color.colorTeal900);

            Color greenColor = new Color(R.color.colorGreen600, R.color.colorGreen700).setAccentId(R.color.colorGreen900);
            Color lightGreenColor = new Color(R.color.colorLightGreen600, R.color.colorLightGreen700).setAccentId(R.color.colorLightGreen900);
            Color limeColor = new Color(R.color.colorLime800, R.color.colorLime900).setAccentId(R.color.colorAccent);

            Color yellowColor = new Color(R.color.colorYellow600, R.color.colorYellow800).setAccentId(R.color.colorYellow900);
            Color amberColor = new Color(R.color.colorAmber600, R.color.colorAmber800).setAccentId(R.color.colorAmber900);
            Color orangeColor = new Color(R.color.colorOrange600, R.color.colorOrange800).setAccentId(R.color.colorOrange900);

            Color deepOrangeColor = new Color(R.color.colorDeepOrange600, R.color.colorDeepOrange800).setAccentId(R.color.colorDeepOrange900);
            Color brownColor = new Color(R.color.colorBrown600, R.color.colorBrown800).setAccentId(R.color.colorBrown900);
            Color greyColor = new Color(R.color.colorGrey600, R.color.colorGrey800).setAccentId(R.color.colorGrey900);

            Color blueGreyColor = new Color(R.color.colorBlueGrey600, R.color.colorBlueGrey800).setAccentId(R.color.colorBlueGrey900);

            colors.add(redColor);
            colors.add(pinkColor);
            colors.add(purpleColor);

            colors.add(deepPurpleColor);
            colors.add(indigoColor);
            colors.add(blueColor);

            colors.add(lightBlueColor);
            colors.add(cyanColor);
            colors.add(tealColor);

            colors.add(greenColor);
            colors.add(lightGreenColor);
            colors.add(limeColor);

            colors.add(yellowColor);
            colors.add(amberColor);
            colors.add(orangeColor);

            colors.add(deepOrangeColor);
            colors.add(brownColor);
            colors.add(greyColor);

            colors.add(blueGreyColor);*/
            }

            if (position == -1) {
                val min = 1
                val max = colors.size

                val r = Random()
                val rand = r.nextInt(max - min + 1) + min

                return colors[rand - 1]
            }

            val size = colors.size

            return colors[position % size]

        }


        fun getRandColor(): Color {
            return getRandColor(-1)
        }

        fun getRandCompatColor(context: Context): Int {
            val (primaryId) = getRandColor()
            return ContextCompat.getColor(context, primaryId)
        }


        fun getRandColor(context: Context, position: Int): Int {
            val (_, primaryDarkId) = getRandColor(position)
            return ContextCompat.getColor(context, primaryDarkId)
        }

        private val particleColors: IntArray? = null

        fun getParticleColors(context: Context): IntArray? {
            if (particleColors == null) {
                /*            int goldDark = ColorUtil.getColor(context, R.color.gold_dark);
            int goldMed = ColorUtil.getColor(context, R.color.gold_med);
            int gold = ColorUtil.getColor(context, R.color.gold);
            int goldLight = ColorUtil.getColor(context, R.color.gold_light);
            particleColors = new int[]{goldDark, goldMed, gold, goldLight};*/
            }
            return particleColors
        }

        fun lighter(color: Int, factor: Float): Int {
            val red =
                ((android.graphics.Color.red(color).toFloat() * (1.0f - factor) / 255.0f + factor) * 255.0f).toInt()
            val green =
                ((android.graphics.Color.green(color).toFloat() * (1.0f - factor) / 255.0f + factor) * 255.0f).toInt()
            val blue =
                ((android.graphics.Color.blue(color).toFloat() * (1.0f - factor) / 255.0f + factor) * 255.0f).toInt()
            return android.graphics.Color.argb(
                android.graphics.Color.alpha(color),
                red,
                green,
                blue
            )
        }

        fun lighter(color: ColorStateList, factor: Float): Int {
            return lighter(color.defaultColor, factor)
        }

        fun alpha(color: Int, alpha: Int): Int {
            return android.graphics.Color.argb(
                alpha,
                android.graphics.Color.red(color),
                android.graphics.Color.green(color),
                android.graphics.Color.blue(color)
            )
        }

        fun isColorDark(color: Int): Boolean {
            val darkness =
                1.0 - (0.2126 * android.graphics.Color.red(color).toDouble() + 0.7152 * android.graphics.Color.green(
                    color
                ).toDouble() + 0.0722 * android.graphics.Color.blue(color).toDouble()) / 255.0
            return darkness >= 0.5
        }

        fun getThemeAccentColor(context: Context): Int {
            val value = TypedValue()
            context.theme.resolveAttribute(R.attr.colorAccent, value, true)
            return value.data
        }
    }
}