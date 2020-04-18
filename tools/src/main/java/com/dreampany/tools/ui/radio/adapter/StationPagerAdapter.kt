package com.dreampany.tools.ui.radio.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.common.ui.adapter.BasePagerAdapter

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationPagerAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

    override fun createFragment(position: Int): Fragment {
        return items.get(position)
    }


}