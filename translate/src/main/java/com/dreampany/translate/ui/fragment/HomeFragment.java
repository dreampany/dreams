/*
package com.dreampany.translate.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;

import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.translate.R;
import com.dreampany.translate.databinding.ContentRecyclerBinding;
import com.dreampany.translate.databinding.ContentTopStatusBinding;
import com.dreampany.translate.databinding.FragmentHomeBinding;
import com.dreampany.translate.ui.adapter.TranslationAdapter;
import com.jaiselrahman.hintspinner.HintSpinner;

import javax.inject.Inject;


*/
/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

@ActivityScope
public class HomeFragment extends BaseMenuFragment {

    private final String NONE = "none";
    private final String SEARCH = "search";
    private final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentHomeBinding binding;
    private ContentTopStatusBinding bindStatus;
   // private ContentRecyclerBinding bindRecycler;

    private TranslationAdapter adapter;

    @Inject
    public HomeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

*/
/*    @Override
    public int getMenuId() {
        return R.menu.menu_home;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }*//*


    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
    }

    @Override
    protected void onStopUi() {

    }

    private void initView() {
        setTitle(R.string.home);
        binding = (FragmentHomeBinding) super.binding;
        bindStatus = binding.layoutTopStatus;
        //bindRecycler = binding.layoutRecycler;


        binding.stateful.setStateView(NONE, LayoutInflater.from(getContext()).inflate(R.layout.item_none, null));

        binding.fab.setOnClickListener(this);


        */
/*vm = ViewModelProviders.of(this, factory).get(WordViewModelKt.class);
        vm.setUiCallback(this);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutputsOfString(this, this::processResponseOfString);
        vm.observeOutputs(this, this::processResponse);
        vm.observeOutput(this, this::processSingleResponse);*//*



    }

    private void initRecycler() {
*/
/*        binding.setItems(new ObservableArrayList<>());
        adapter = new TranslationAdapter(this);
        adapter.setStickyHeaders(false);*//*

*/
/*        scroller = new OnVerticalScrollListener() {
            @Override
            public void onScrollingAtEnd() {

            }
        };*//*

*/
/*        ViewUtil.setRecycler(
                adapter,
                bindRecycler.recycler,
                new SmoothScrollLinearLayoutManager(getContext()),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_word, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );*//*

    }
}
*/
