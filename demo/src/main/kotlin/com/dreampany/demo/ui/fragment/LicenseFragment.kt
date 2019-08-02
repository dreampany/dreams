package com.dreampany.demo.ui.fragment

import android.os.Bundle
import com.artitk.licensefragment.model.LicenseID
import com.artitk.licensefragment.support.v4.RecyclerViewLicenseFragment
import com.dreampany.demo.R
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseFragment
import com.dreampany.frame.util.FragmentUtil
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class LicenseFragment @Inject constructor() : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_license
    }

    override fun onStartUi(state: Bundle?) {
        val licenseIds = ArrayList<Int>()
        licenseIds.add(LicenseID.GSON)
        licenseIds.add(LicenseID.OKHTTP)
        licenseIds.add(LicenseID.RETROFIT)


        val fragment: RecyclerViewLicenseFragment? = FragmentUtil.getFragment(this, R.id.fragment)
        fragment?.addLicense(licenseIds)
    }

    override fun onStopUi() {

    }
}