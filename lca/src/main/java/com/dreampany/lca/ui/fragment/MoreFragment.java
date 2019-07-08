package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.databinding.FragmentRecyclerBinding;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.Constants;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.adapter.MoreAdapter;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.MoreItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.MoreViewModel;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Companion.more(getAppContext());
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
        setSubtitle(null);
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
            case SETTINGS:
                UiTask<?> task = new UiTask<>(false);
                task.setUiType(UiType.MORE);
                task.setSubtype(UiSubtype.SETTINGS);
                openActivity(ToolsActivity.class, task);
                break;
            case APPS:
                vm.moreApps(getParent());
                break;
            case RATE_US:
                vm.rateUs(getParent());
                break;
            case FEEDBACK:
                vm.sendFeedback(getParent());
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
