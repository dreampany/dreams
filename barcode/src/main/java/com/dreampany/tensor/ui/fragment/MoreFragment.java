package com.dreampany.tensor.ui.fragment;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.dreampany.frame.databinding.FragmentItemsBinding;
import com.dreampany.frame.injector.ActivityScoped;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.SettingsUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.tensor.R;
import com.dreampany.tensor.ui.adapter.MoreAdapter;
import com.dreampany.tensor.ui.model.MoreItem;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

@ActivityScoped
public class MoreFragment extends BaseMenuFragment implements
        FlexibleAdapter.OnItemClickListener {

    private FragmentItemsBinding binding;
    private MoreAdapter adapter;

    @Inject
    public MoreFragment() {

    }

    @Override
    protected int getLayoutId() {
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
        binding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                produceItems();
            }
        });
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
                binding.recyclerView,
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

    private void rateUs() {
        SettingsUtil.rateUs(getActivity());
    }

    private void sendFeedback() {
        SettingsUtil.feedback(getActivity());
    }
}
