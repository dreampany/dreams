package com.dreampany.fit.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.dreampany.fit.R;
import com.dreampany.fit.ui.activity.ToolsActivity;
import com.dreampany.fit.ui.adapter.MoreAdapter;
import com.dreampany.fit.ui.enums.UiSubtype;
import com.dreampany.fit.ui.enums.UiType;
import com.dreampany.fit.ui.model.MoreItem;
import com.dreampany.fit.ui.model.UiTask;
import com.dreampany.fit.vm.MoreViewModel;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.databinding.FragmentRecyclerBinding;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.util.ViewUtil;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemAnimator;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;


/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class MoreFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentRecyclerBinding binding;
    private MoreViewModel vm;
    private MoreAdapter adapter;

    @Inject
    public MoreFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void onStartUi(Bundle state) {
        initView();
        initRecycler();
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
    public void onPause() {
        super.onPause();
        vm.removeMultipleSubscription();
    }

    @Override
    public boolean onItemClick(View view, int position) {
        if (position != RecyclerView.NO_POSITION) {
            MoreItem item = adapter.getItem(position);
            showItem(Objects.requireNonNull(item));
            return true;
        }
        return false;
    }

    private void initView() {
        setTitle(R.string.more);
        binding = (FragmentRecyclerBinding) super.binding;

        vm = ViewModelProviders.of(this, factory).get(MoreViewModel.class);
        vm.observeOutputs(this, this::processResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new MoreAdapter(this);
        adapter.setStickyHeaders(false);
        ViewUtil.setRecycler(
                adapter,
                binding.recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_more, 0, 0, 0, 1)
                        //.withBottomEdge(false)
                        .withEdge(true),
                new FlexibleItemAnimator(),
                null,
                null
        );
    }


    private void processResponse(Response<List<MoreItem>> response) {
        if (response instanceof Response.Result) {
            Response.Result<List<MoreItem>> result = (Response.Result<List<MoreItem>>) response;
            processSuccess(result.getData());
        }
    }

    private void processSuccess(List<MoreItem> items) {
        if (adapter.isEmpty()) {
            adapter.addItems(items);
        }
    }

    private void showItem(MoreItem item) {
        switch (item.getItem().getType()) {
            case APPS:
                vm.moreApps(getParent());
                break;
            case RATE_US:
                vm.rateUs(getParent());
                break;
            case FEEDBACK:
                vm.sendFeedback(getParent());
                break;
            case SETTINGS:
                UiTask<?> task = new UiTask<>(false);
                task.setUiType(UiType.MORE);
                task.setSubtype(UiSubtype.SETTINGS);
                openActivity(ToolsActivity.class, task);
                break;
            case LICENSE:
                task = new UiTask<>(false);
                task.setUiType(UiType.MORE);
                task.setSubtype(UiSubtype.LICENSE);
                openActivity(ToolsActivity.class, task);
                break;
            case ABOUT:
            default:
                task = new UiTask<>(false);
                task.setUiType(UiType.MORE);
                task.setSubtype(UiSubtype.ABOUT);
                openActivity(ToolsActivity.class, task);
                break;
        }
    }
}
