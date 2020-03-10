package com.dreampany.pair.ui.tutorial

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.pair.databinding.TutorialItemBinding

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TutorialAdapter :  RecyclerView.Adapter<TutorialAdapter.ViewHolder>() {

    private val items: MutableList<>

    override fun getItemCount(): Int {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    inner class ViewHolder(bind: TutorialItemBinding) : RecyclerView.ViewHolder(bind.root) {

        fun bind() {

        }
    }
}