package com.dreampany.frame.util

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.frame.R
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper
import java.util.*


/**
 * Created by Roman-372 on 7/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ViewUtil {
    companion object {
        fun <T : View> getViewById(fragment: Fragment, viewId: Int): T? {
            return getViewById(fragment.view, viewId)
        }

        fun <T : View> getViewById(parentView: View?, viewId: Int): T? {
            return if (parentView == null || viewId <= 0) null else parentView.findViewById(viewId)
        }

        fun visible(parent: Fragment, @IdRes viewId: Int) {
            val view = getViewById<View>(parent, viewId)
            if (view != null) {
                ViewUtil.visible(view)
            }
        }

        fun visible(view: View) {
            if (view != null) {
                view.visibility = View.VISIBLE
            }
        }

        fun hide(parent: Fragment, @IdRes viewId: Int) {
            val view = getViewById<View>(parent, viewId)
            ViewUtil.hide(view!!)
        }

        fun hide(view: View) {
            if (view != null) {
                view.visibility = View.GONE
            }
        }

        fun setClickListener(parentView: View, viewId: Int, clickListener: View.OnClickListener) {
            setClickListener(getViewById(parentView, viewId), clickListener)
        }

        fun setClickListener(view: View?, clickListener: View.OnClickListener) {
            view?.setOnClickListener(clickListener)
        }


        fun setClickListener(fragment: Fragment, @IdRes viewId: Int) {
            setClickListener(getViewById(fragment.view, viewId), fragment as View.OnClickListener)
        }

        fun setText(fragment: Fragment, @IdRes viewId: Int, @StringRes textId: Int) {
            setText(fragment, viewId, fragment.getString(textId))
        }

        fun setFancyText(fragment: Fragment, @IdRes viewId: Int, @StringRes textId: Int) {
            setFancyText(fragment, viewId, fragment.getString(textId))
        }

        fun setText(fragment: Fragment, @IdRes viewId: Int, text: String) {
            val view = getViewById<TextView>(fragment.view, viewId)
            setText(view, text)
        }

        fun setFancyText(fragment: Fragment, @IdRes viewId: Int, text: String) {
            //MaterialFancyButton view = getViewById(fragment.getView(), viewId);
            //setFancyText(view, text);
        }

        fun setText(view: TextView?, text: String) {
            if (view != null) {
                view.text = text
            }
        }

        fun setTextColor(view: TextView?, @ColorInt color: Int) {
            view?.setTextColor(color)
        }

/*    public static void setFancyText(MaterialFancyButton view, String text) {
        if (view != null) {
            view.setText(text);
        }
    }*/

        fun setFabBackgroundTint(fab: FloatingActionButton, color: Int) {
            val fabColorStateList = ColorStateList(
                arrayOf(intArrayOf()),
                intArrayOf(color)
            )
            fab.backgroundTintList = fabColorStateList
        }


        fun setBackground(view: View, colorId: Int) {
            if (FloatingActionButton::class.java.isInstance(view)) {
                val runnable = {
                    (view as FloatingActionButton).backgroundTintList =
                        ColorStateList.valueOf(ColorUtil.getColor(view.getContext(), colorId))
                }
                view.post(runnable)
            } else if (ImageView::class.java.isInstance(view)) {
                /*Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ((ImageView) view).setImageResource(resourceId);
                }
            };
            AndroidUtil.post(runnable);*/
            } else if (View::class.java.isInstance(view)) {
                view.setBackgroundColor(ColorUtil.getColor(view.context, colorId))
            }
        }

        fun setSwipe(swipe: SwipeRefreshLayout?, listener: SwipeRefreshLayout.OnRefreshListener) {
            if (swipe != null) {
                swipe.setColorSchemeResources(
                    R.color.colorPrimary,
                    R.color.colorAccent,
                    R.color.colorPrimaryDark
                )
                swipe.setOnRefreshListener(listener)
            }
        }

        fun setRecycler(
            adapter: SmartAdapter<*>,
            recycler: RecyclerView,
            layout: RecyclerView.LayoutManager
        ) {
            ViewUtil.setRecycler(adapter, recycler, layout, null)
        }

        fun setRecycler(
            adapter: SmartAdapter<*>,
            recycler: RecyclerView,
            layout: RecyclerView.LayoutManager,
            decoration: RecyclerView.ItemDecoration?
        ) {
            ViewUtil.setRecycler(adapter, recycler, layout, decoration, null, null, null)
        }

        fun setRecycler(
            adapter: SmartAdapter<*>,
            recycler: RecyclerView,
            layout: RecyclerView.LayoutManager,
            decoration: RecyclerView.ItemDecoration?,
            animator: RecyclerView.ItemAnimator?,
            scroller: RecyclerView.OnScrollListener?
        ) {

            ViewUtil.setRecycler(adapter, recycler, layout, decoration, animator, scroller, null, null, null)
        }

        fun setRecycler(
            adapter: SmartAdapter<*>,
            recycler: RecyclerView,
            layout: RecyclerView.LayoutManager,
            decoration: RecyclerView.ItemDecoration?,
            animator: RecyclerView.ItemAnimator?,
            scroller: RecyclerView.OnScrollListener?,
            empty: View?
        ) {

            ViewUtil.setRecycler(adapter, recycler, layout, decoration, animator, scroller, empty, null, null)
        }

        fun setRecycler(
            adapter: SmartAdapter<*>,
            recycler: RecyclerView,
            layout: RecyclerView.LayoutManager,
            decoration: RecyclerView.ItemDecoration?,
            animator: RecyclerView.ItemAnimator?,
            scroller: RecyclerView.OnScrollListener?,
            empty: View?,
            filter: View?,
            emptyListener: EmptyViewHelper.OnEmptyViewListener?
        ) {
            ViewUtil.setRecycler(
                adapter,
                recycler,
                layout,
                decoration,
                animator,
                scroller,
                empty,
                null,
                null,
                false,
                false
            )
        }

        fun setRecycler(
            adapter: SmartAdapter<*>,
            recycler: RecyclerView,
            layout: RecyclerView.LayoutManager,
            decoration: RecyclerView.ItemDecoration?,
            animator: RecyclerView.ItemAnimator?,
            scroller: RecyclerView.OnScrollListener?,
            empty: View?,
            filter: View?,
            emptyListener: EmptyViewHelper.OnEmptyViewListener?,
            dragEnabled: Boolean,
            swipeEnabled: Boolean
        ) {
            layout.isItemPrefetchEnabled = false
            recycler.setHasFixedSize(true)
            recycler.adapter = adapter
            recycler.layoutManager = layout

            if (decoration != null && recycler.itemDecorationCount == 0) {
                recycler.addItemDecoration(decoration)
            }

            if (animator != null) {
                recycler.itemAnimator = animator
            } else {
                (Objects.requireNonNull(recycler.itemAnimator) as DefaultItemAnimator).supportsChangeAnimations = false
                //recycler.setItemAnimator(null);
            }

            recycler.clearOnScrollListeners()
            if (scroller != null) {
                recycler.addOnScrollListener(scroller)
            }

            if (empty != null && filter != null) {
                EmptyViewHelper.create(adapter, empty, filter, emptyListener)
            } else if (empty != null) {
                EmptyViewHelper.create(adapter, empty)
            }
            adapter.isLongPressDragEnabled = dragEnabled
            adapter.isSwipeEnabled = swipeEnabled
        }

        fun showSnackbar(view: View, @StringRes textId: Int) {
            Snackbar.make(view, textId, Snackbar.LENGTH_LONG).show()
        }

        fun showSnackbar(view: View, text: String) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
        }

        fun blink(view: TextView, startColorId: Int, endColorId: Int) {
            val startColor = ColorUtil.getColor(view.context, startColorId)
            val endColor = ColorUtil.getColor(view.context, endColorId)
            val animator = ObjectAnimator.ofInt(view, "textColor", startColor, endColor, startColor)
            animator.duration = 1500
            animator.setEvaluator(ArgbEvaluator())
            animator.repeatMode = ValueAnimator.REVERSE
            animator.repeatCount = ValueAnimator.RESTART
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    ViewUtil.setTextColor(view, endColor)
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            animator.start()

        }

        fun getText(context: Context, @StringRes keyResId: Int, value: Int): String {
            return String.format(
                context.getString(keyResId),
                value
            )
        }

        fun getText(view: View): String? {
            if (TextView::class.java.isInstance(view)) {
                val textView = view as TextView
                return textView.text.toString().trim { it <= ' ' }
            }
            return null
        }

        fun setIcon(menu: Menu?, menuItemId: Int, @ColorRes iconRes: Int) {
            if (menu != null) {
                val item = menu.findItem(menuItemId)
                item?.setIcon(iconRes)
            }
        }

        fun setIconColor(context: Context, menuItem: MenuItem, @ColorRes colorRes: Int) {
            val drawable = menuItem.icon
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(ColorUtil.getColor(context, colorRes), PorterDuff.Mode.SRC_ATOP)
            }
        }

/*    mPopupWindow = new PopupWindow();
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setContentView(popupView);
        if (android.os.Build.VERSION.SDK_INT >=24) {
        int[] a = new int[2];
        anchorView.getLocationInWindow(a);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, 0 , a[1]+anchorView.getHeight());
    } else{
        mPopupWindow.showAsDropDown(anchorView);
    }
        if (Build.VERSION.SDK_INT != 24) {
        mPopupWindow.update(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    } else  {
        mPopupWindow.update();
    }*/



        fun createDatePicker(
            context: Context,
            listener: DatePickerDialog.OnDateSetListener?,
            day: Int,
            month: Int,
            year: Int
        ) : DatePickerDialog {
            val dialog = object : DatePickerDialog(context, listener, year, month, day) {
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    val year = context.resources.getIdentifier("android:id/year", null, null)
                    if (year != 0) {
                        val yearPicker = findViewById<View>(year)
                        yearPicker?.setVisibility(View.GONE)
                    }
                }
            }
            return dialog
        }
    }
}