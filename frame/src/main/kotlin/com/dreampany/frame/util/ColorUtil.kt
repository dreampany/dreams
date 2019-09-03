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

        fun createGreyColor(): Color {
            return Color(R.color.material_grey600, R.color.material_grey800, R.color.material_grey500)
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
                val redColor =
                    Color(R.color.material_red500, R.color.material_red700, R.color.material_red900)
                val pinkColor = Color(
                    R.color.material_pink500,
                    R.color.material_pink700,
                    R.color.material_pink900
                )
                val purpleColor = Color(
                    R.color.material_purple500,
                    R.color.material_purple700,
                    R.color.material_purple900
                );

                val deepPurpleColor = Color(
                    R.color.material_deeppurple500,
                    R.color.material_deeppurple700
                    , R.color.material_deeppurple900
                )
                val indigoColor = Color(
                    R.color.material_indigo500,
                    R.color.material_indigo700
                    , R.color.material_indigo900
                )
                val blueColor = Color(
                    R.color.material_blue500,
                    R.color.material_blue700,
                    R.color.material_blue900
                )

                val lightBlueColor = Color(
                    R.color.material_lightblue600,
                    R.color.material_lightblue700, R.color.material_lightblue900
                )
                val cyanColor = Color(
                    R.color.material_cyan600,
                    R.color.material_cyan700,
                    R.color.material_cyan900
                )
                val tealColor = Color(
                    R.color.material_teal500,
                    R.color.material_teal700,
                    R.color.material_teal900
                )

                val greenColor = Color(
                    R.color.material_green600,
                    R.color.material_green700, R.color.material_green900
                )
                val lightGreenColor = Color(
                    R.color.material_lightgreen600,
                    R.color.material_lightgreen700, R.color.material_lightgreen900
                )
                val limeColor = Color(
                    R.color.material_lime800, R.color.material_lime900,
                    R.color.colorAccent
                )

                val yellowColor = Color(
                    R.color.material_yellow600,
                    R.color.material_yellow800
                    , R.color.material_yellow900
                )
                val amberColor = Color(
                    R.color.material_amber600,
                    R.color.material_amber800
                    , R.color.material_amber900
                )
                val orangeColor = Color(
                    R.color.material_orange600,
                    R.color.material_orange800,
                    R.color.material_orange900
                )

                val deepOrangeColor = Color(
                    R.color.material_deeporange600,
                    R.color.material_deeporange800,
                    R.color.material_deeporange900
                )
                val brownColor = Color(
                    R.color.material_brown600,
                    R.color.material_brown800,
                    R.color.material_brown900
                )
                val greyColor = Color(
                    R.color.material_grey600,
                    R.color.material_grey800,
                    R.color.material_grey900
                )

                val blueGreyColor = Color(
                    R.color.material_bluegrey600,
                    R.color.material_bluegrey800, R.color.material_bluegrey900
                )

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

                colors.add(blueGreyColor);
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
            val color = getRandColor(position)
            return ContextCompat.getColor(context, color.primaryDarkId)
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