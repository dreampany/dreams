package com.dreampany.share.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.annimon.stream.Stream;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.network.data.enums.NetworkType;
import com.dreampany.network.data.model.Network;
import com.dreampany.share.R;
import com.dreampany.share.databinding.FragmentDiscoverBinding;
import com.dreampany.share.ui.adapter.MediaAdapter;
import com.dreampany.share.ui.model.NetworkItem;
import com.dreampany.share.vm.DiscoverViewModel;
import com.dreampany.share.vm.NetworkViewModel;
import com.dreampany.share.vm.UserViewModel;
import com.google.common.collect.Maps;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class DiscoverFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;
    FragmentDiscoverBinding binding;
    NetworkViewModel nvm;
    UserViewModel uvm;
    DiscoverViewModel dvm;
    MediaAdapter adapter;
    OnVerticalScrollListener scroller;
    SwipeRefreshLayout refresh;
    ExpandableLayout expandable;
    RecyclerView recycler;
    MenuItem wifiItem;
    MenuItem btItem;

    @Inject
    public DiscoverFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_discover;
    }

    @Override
    public void onMenuCreated(@NotNull Menu menu) {
        initMenu(menu);
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
    }

    @Override
    protected void onStopUi() {

    }

    private void initView() {
        setTitle(R.string.discover);
        binding = (FragmentDiscoverBinding) super.binding;
        refresh = binding.layoutRefresh;
        expandable = findViewById(R.id.layout_expandable);
        recycler = findViewById(R.id.recycler);

        ViewUtil.setSwipe(refresh, this);
        ViewUtil.setClickListener(this, R.id.button_empty);
        ViewUtil.setClickListener(this, R.id.fab);

        nvm = ViewModelProviders.of(this, factory).get(NetworkViewModel.class);
        uvm = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        dvm = ViewModelProviders.of(this, factory).get(DiscoverViewModel.class);
        nvm.observeOutputs(this, this::processNetwork);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new MediaAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener();
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_apk, dvm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    private void initMenu(Menu menu) {
        wifiItem = menu.findItem(R.id.item_wifi);
        btItem = menu.findItem(R.id.item_bluetooth);
    }


    public void processNetwork(Response<List<NetworkItem>> response) {
        if (response instanceof Response.Result) {
            Response.Result<List<NetworkItem>> result = (Response.Result<List<NetworkItem>>) response;
            processSuccessNetwork(result.getData());
        }
    }

    private void processSuccessNetwork(List<NetworkItem> items) {
        Map<NetworkType, Network> networks = Maps.newHashMap();
        Stream.of(items).forEach(item -> {
            networks.put(item.getItem().getType(), item.getItem());
        });
        int wifiResId = R.drawable.ic_portable_wifi_off_white_24dp;
        if (networks.containsKey(NetworkType.WIFI)) {
            wifiResId = R.drawable.ic_wifi_white_24dp;
        } else if (networks.containsKey(NetworkType.HOTSPOT)) {
            wifiResId = R.drawable.ic_wifi_tethering_white_24dp;
        }
        int btResId = networks.containsKey(NetworkType.BLUETOOTH) ? R.drawable.ic_bluetooth_connected_white_24dp : R.drawable.ic_bluetooth_disabled_white_24dp;

        wifiItem.setIcon(wifiResId);
        btItem.setIcon(btResId);
    }
}
