package com.dreampany.stateful

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.*
import com.dreampany.framework.misc.extension.inflate
import com.dreampany.framework.misc.extension.inflater
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.HashSet


/**
 * Created by roman on 5/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StatefulLayout : FrameLayout {

    @Parcelize
    enum class State : Parcelable {
        DEFAULT, EMPTY, LOADING, OFFLINE, CONTENT
    }

    private val SAVED_INSTANCE_STATE = "saved_instance_state"
    private val SAVED_STATE = "saved_state"

    private val states: EnumMap<State, View> = EnumMap(State::class.java)
    private var state: State? = null

    private var initialized = false
    private var dirtyFlag = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SAVED_INSTANCE_STATE, super.onSaveInstanceState())
        saveInstanceState(bundle)
        return bundle
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle) {
            val bundle = state
            if (this.state == null) restoreInstanceState(bundle)
            state = bundle.getParcelable(SAVED_INSTANCE_STATE)
        }
        super.onRestoreInstanceState(state)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!initialized)
            onSetupContentState()
    }

    fun setStateView(state: State, view: View) {
        if (states.containsKey(state)) {
            removeView(states.get(state))
        }
        states.put(state, view)
        if (view.parent == null) {
            addView(view)
        }
        view.visibility = View.GONE
        dirtyFlag = true
    }

    fun setStateView(state: State, @LayoutRes layoutRes: Int) {
        setStateView(state, context.inflate(layoutRes))
    }

    fun setState(state: State) {
        checkNotNull(getStateView(state)) {
            String.format(
                "Cannot switch to state \"%s\". This state was not defined or the view for this state is null.",
                state
            )
        }
        if (this.state != null && state.equals(this.state) && !dirtyFlag) return
        this.state = state
        for (s in states.keys) {
            states.get(s)?.setVisibility(if (s == state) View.VISIBLE else View.GONE)
        }
        //if (mOnStateChangeListener != null) mOnStateChangeListener.onStateChange(state)
        dirtyFlag = false
    }

    fun getStateView(state: State): View? {
        return states.get(state)
    }

    fun clearStates() {
        for (state in HashSet(states.keys)) {
            val view: View? = states.get(state)
            if (state != State.CONTENT) {
                removeView(view)
                states.remove(state)
            }
        }
    }

    @CallSuper
    protected fun onSetupContentState() {
        check(!(childCount != 1 + states.size)) { "Invalid child count. StatefulLayout must have exactly one child." }
        val contentView = getChildAt(states.size)
        removeView(contentView)
        setStateView(State.CONTENT, contentView)
        setState(State.CONTENT)
        initialized = true
    }

    private fun saveInstanceState(outState: Bundle) {
        if (state != null) outState.putParcelable(SAVED_STATE, state)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle): State? {
        val state: State? = savedInstanceState.getParcelable(SAVED_STATE)
        state?.let {
            setState(state)
        }
        return state
    }
}