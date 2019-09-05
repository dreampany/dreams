package com.dreampany.crime.ui.fragment;

import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.dreampany.framework.databinding.FragmentItemsBinding;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.util.SettingsUtil;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.crime.R;
import com.dreampany.crime.ui.adapter.MoreAdapter;
import com.dreampany.crime.ui.model.MoreItem;

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
        setTitle(R.string.more);
        binding = (FragmentItemsBinding) super.binding;
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
            default:
/*                UiTask<?, MoreType, ?, ?> task = new UiTask<>();
                task.setType(item.getType());
                AndroidUtil.openActivity(getParent(), ToolsActivity.class, task);*/
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
