package com.dreampany.tools.ui.fragment.radio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.databinding.FragmentTabpagerFixedBinding
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.framework.ui.fragment.BaseStateFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.TextUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.StationMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.pref.RadioPref
import com.dreampany.tools.manager.PlayerManager
import com.dreampany.tools.misc.Constants
import javax.inject.Inject

/**
 * Created by roman on 2019-10-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class RadioHomeFragment
@Inject constructor() : BaseStateFragment<BaseFragment>() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    @Inject
    internal lateinit var mapper: StationMapper
    @Inject
    internal lateinit var radioPref: RadioPref
    @Inject
    internal lateinit var player: PlayerManager

    private lateinit var bind: FragmentTabpagerFixedBinding

    private lateinit var state: State
    private lateinit var countryCode: String

    override fun getLayoutId(): Int {
        return R.layout.fragment_tabpager_fixed
    }

/*    override fun getMenuId(): Int {
        return R.menu.menu_radio
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }*/

    override fun getTitleResId(): Int {
        return R.string.title_feature_radio
    }

    override fun getScreen(): String {
        return Constants.radioHome(context!!)
    }

    override fun hasAllPages(): Boolean {
        return true
    }

    override fun hasTabColor(): Boolean {
        return true
    }

    override fun pageTitles(): Array<String> {
        return TextUtilKt.getStrings(context!!, R.string.local, R.string.trends, R.string.popular)
    }

    override fun pageClasses(): Array<Class<BaseFragment>> {
        val local: Class<BaseFragment> = StationsFragment::class.java as Class<BaseFragment>
        val trends: Class<BaseFragment> = StationsFragment::class.java as Class<BaseFragment>
        val popular: Class<BaseFragment> = StationsFragment::class.java as Class<BaseFragment>

        return arrayOf<Class<BaseFragment>>(local, trends, popular)
    }

    override fun pageTasks(): Array<UiTask<*>> {
        val local = UiTask<Station>(
            type = Type.STATION,
            action = Action.OPEN,
            state = State.LOCAL
        )
        val trends = UiTask<Station>(
            type = Type.STATION,
            action = Action.OPEN,
            state = State.TRENDS
        )
        val popular = UiTask<Station>(
            type = Type.STATION,
            action = Action.OPEN,
            state = State.POPULAR
        )
        return arrayOf<UiTask<*>>(local, trends, popular)
    }

/*    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val searchItem = getSearchMenuItem()
        val categoryItem = menu.findItem(R.id.item_category)
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context!!, R.color.material_white),
            null, searchItem, categoryItem
        )
    }*/

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }


/*    private fun initTitleSubtitle() {
        if (context == null) return
        setTitle(R.string.title_feature_radio)
        when (state) {
            State.LOCAL -> {
                val subtitle = getString(
                    R.string.subtitle_radio,
                    state.name.toTitle(),
                    countryCode,
                    adapter.itemCount
                )
                setSubtitle(subtitle)
            }
        }

    }*/

    private fun initUi() {
        bind = super.binding as FragmentTabpagerFixedBinding
    }

    private val serviceUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //updatePlaying()
        }

    }
}