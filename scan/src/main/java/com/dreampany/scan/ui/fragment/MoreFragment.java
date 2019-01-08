package com.dreampany.scan.ui.fragment;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.dreampany.frame.databinding.FragmentItemsBinding;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.SettingsUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.scan.R;
import com.dreampany.scan.ui.activity.ToolsActivity;
import com.dreampany.scan.ui.adapter.MoreAdapter;
import com.dreampany.scan.ui.enums.UiSubtype;
import com.dreampany.scan.ui.enums.UiType;
import com.dreampany.scan.ui.model.MoreItem;
import com.dreampany.scan.ui.model.UiTask;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class MoreFragment extends BaseMenuFragment implements
        FlexibleAdapter.OnItemClickListener {

    private FragmentItemsBinding binding;
    private MoreAdapter adapter;

    @Inject
    public MoreFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_items;
    }

    @Override
    protected void onMenuCreated(Menu menu) {

    }

    @Override
    protected void onStartUi(Bundle state) {
        initView();
        initRecycler();
        binding.getRoot().post(() -> produceItems());
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public boolean onItemClick(View view, int position) {
        if (position != RecyclerView.NO_POSITION) {
            MoreItem item = adapter.getItem(position);
            showItem(item);
            return true;
        }
        return false;
    }

    private void initView() {
        setTitle(R.string.more);
        binding = (FragmentItemsBinding) super.binding;
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new MoreAdapter(this);
        ViewUtil.setRecycler(
                binding.recycler,
                adapter,
                new SmoothScrollLinearLayoutManager(getContext()),
                null,
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_more, 0, 0, 0, 1)
                        //.withBottomEdge(false)
                        .withEdge(true)
        );
    }

    private void produceItems() {
        adapter.load();
    }

    private void showItem(MoreItem item) {
        switch (item.getType()) {
            case APPS:
                moreApps();
                break;
            case RATE_US:
                rateUs();
                break;
            case FEEDBACK:
                sendFeedback();
                break;
            case ABOUT_US:
                UiTask<?> task = new UiTask<>(false);
                task.setUiType(UiType.MORE);
                task.setSubtype(UiSubtype.ABOUT_US);
                openActivity(ToolsActivity.class, task);
                break;
            case SETTINGS:
                task = new UiTask<>(false);
                task.setUiType(UiType.MORE);
                task.setSubtype(UiSubtype.SETTINGS);
                openActivity(ToolsActivity.class, task);
                break;
            default:
                break;
        }
    }

    private void moreApps() {
        SettingsUtil.moreApps(getActivity());
    }

    private void rateUs() {
        SettingsUtil.rateUs(getActivity());
    }

    private void sendFeedback() {
        SettingsUtil.feedback(getActivity());
    }
}
