package com.dreampany.share.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;

import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.misc.FragmentScope;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.media.data.model.Media;
import com.dreampany.share.R;
import com.dreampany.share.databinding.FragmentDownloadBinding;
import com.dreampany.share.databinding.FragmentShareBinding;
import com.dreampany.share.ui.adapter.MediaAdapter;
import com.dreampany.share.ui.model.UiTask;
import com.dreampany.share.vm.ShareViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class DownloadFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;
    FragmentDownloadBinding binding;
    ShareViewModel vm;
    MediaAdapter adapter;
    OnVerticalScrollListener scroller;
    SwipeRefreshLayout refresh;
    ExpandableLayout expandable;
    RecyclerView recycler;

    @Inject
    public DownloadFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_download;
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
        setTitle(R.string.download);
        binding = (FragmentDownloadBinding) super.binding;
        refresh = binding.layoutRefresh;
        expandable = binding.layoutTop.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.setSwipe(refresh, this);
        ViewUtil.setClickListener(this, R.id.button_empty);
        ViewUtil.setClickListener(this, R.id.fab);

        vm = ViewModelProviders.of(this, factory).get(ShareViewModel.class);
        UiTask<Media> uiTask = getCurrentTask(true);
        vm.setTask(uiTask);

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
                        .addItemViewType(R.layout.item_apk, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }
}
