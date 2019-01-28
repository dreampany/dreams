package com.dreampany.word.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.databinding.FragmentHomeBinding;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.vm.RecentViewModel;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import net.cachapa.expandablelayout.ExpandableLayout;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class HomeFragment extends BaseMenuFragment implements SmartAdapter.Callback<WordItem> {

    private final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentHomeBinding binding;

    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;

    RecentViewModel vm;
    WordAdapter adapter;

    @Inject
    public HomeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_home;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
        vm.start();
    }

    @Override
    protected void onStopUi() {
        vm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.loads(false);
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        BaseFragment fragment = getCurrentFragment();
        return fragment != null && fragment.onQueryTextChange(newText);
    }

    private void initView() {
        setTitle(R.string.home);

        binding = (FragmentHomeBinding) super.binding;
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_recent_words);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        vm = ViewModelProviders.of(this, factory).get(RecentViewModel.class);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new WordAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener() {
            @Override
            public void onScrolling() {
                vm.updateVisibleItemIf();
            }
        };
        //adapter.setEndlessScrollListener(this, CoinItem.getProgressItem());
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(getContext()),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_word, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    @Nullable
    @Override
    public ArrayList<WordItem> getVisibleItems() {
        return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public WordItem getVisibleItem() {
        return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public List<WordItem> getItems() {
        return null;
    }
}
