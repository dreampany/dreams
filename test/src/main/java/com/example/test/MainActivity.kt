package com.example.test

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.balysv.materialripple.MaterialRippleLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        setWheelPicker()

        /*seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                updateMarker(p0, progress, p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar) {
            }

            override fun onStopTrackingTouch(p0: SeekBar) {
            }

        })

        seek_bar.postDelayed(Runnable {
            updateMarker(seek_bar, progress, seek_bar.progress.toString())

        }, 100)*/

        /* MaterialRippleLayout.on(button)
             .rippleColor(Color.GRAY)
             .create()*/

    }

    private fun setWheelPicker() {
        val list = ArrayList<String>()
        (1..20).forEach {
            list.add(it.toString())
        }
        picker.setDataSet(list)
    }

    private fun updateMarker(
        sb: SeekBar, marker: TextView, message: String
    ) {
        /**
         * According to this question:
         * https://stackoverflow.com/questions/20493577/android-seekbar-thumb-position-in-pixel
         * one can find the SeekBar thumb location in pixels using:
         */
        val width = (sb.width
                - sb.paddingLeft
                - sb.paddingRight)
        val thumbPos = (sb.paddingLeft
                + (width
                * sb.progress
                / sb.max) +  //take into consideration the margin added (in this case it is 10dp)
                Math.round(convertDpToPixel(10f, this@MainActivity))).toInt()
        marker.setText(message)
        marker.post(Runnable {
            val display: Display =
                (this@MainActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).getDefaultDisplay()
            val deviceDisplay = Point()
            display.getSize(deviceDisplay)

            //vArrow always follow seekBar thumb location
            marker.setX((thumbPos - sb.thumbOffset).toFloat())

            /* //unlike vArrow, tvProgress will not always follow seekBar thumb location
             if (thumbPos - marker.getWidth() / 2 - sb.paddingLeft < 0) {
                 //part of the tvProgress is to the left of 0 bound
                 tvProgress.setX(vArrow.getX() - 20)
             } else if (thumbPos + tvProgress.getWidth() / 2 + sb.paddingRight > deviceDisplay.x) {
                 //part of the tvProgress is to the right of screen width bound
                 tvProgress.setX(vArrow.getX() - tvProgress.getWidth() + 20 + vArrow.getWidth())
             } else {
                 //tvProgress is between 0 and screen width bounds
                 tvProgress.setX(thumbPos - tvProgress.getWidth() / 2f)
             }*/
        })
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources: Resources = context.getResources()
        val metrics: DisplayMetrics = resources.getDisplayMetrics()
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}