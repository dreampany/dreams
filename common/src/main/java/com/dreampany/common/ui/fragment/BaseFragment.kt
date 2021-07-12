package com.dreampany.common.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dreampany.common.misc.func.Executors
import javax.inject.Inject

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class BaseFragment<T> : Fragment() where T : ViewDataBinding {

    @Inject
    protected lateinit var ex: Executors
    protected lateinit var binding: T

    @get:LayoutRes
    open val layoutRes: Int = 0

    @get:MenuRes
    open val menuRes: Int = 0

    @get:StringRes
    open val titleRes: Int = 0

    @get:StringRes
    open val subtitleRes: Int = 0

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(menuRes != 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (layoutRes != 0) {
            binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
            binding.lifecycleOwner = this
            return binding.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onStartUi(savedInstanceState)
    }
}