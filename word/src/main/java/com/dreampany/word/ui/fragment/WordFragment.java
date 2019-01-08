package com.dreampany.word.ui.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Color;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.ColorUtil;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.model.Definition;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.databinding.FragmentWordBinding;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.vm.WordViewModel;
import com.ethanhua.skeleton.SkeletonScreen;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

/**
 * Created by Hawladar Roman on 9/17/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@ActivityScope
public class WordFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;
    FragmentWordBinding binding;
    SkeletonScreen skeletonScreen;
    WordViewModel vm;
    String parent;

    @Inject
    public WordFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_word;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_word;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
    }

    @Override
    protected void onStopUi() {
        vm.removeSingleSubscription();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_favourite_toggle:
                vm.toggle();
                return true;
            case R.id.item_share:
                vm.share(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()) {
            case R.id.text_word:
            case R.id.fab:
                vm.speak(parent);
                break;
            case R.id.text_simple_word:
                String text = ViewUtil.getText(v);
                vm.speak(text);
                break;
            case R.id.toggle_definition:
                toggleDefinition();
                break;
            case R.id.toggle_example:
                toggleExample();
                break;
            case R.id.image_close:
                closeBottom();
                break;
        }
    }

    private void initView() {
        setTitle(R.string.details);
        binding = (FragmentWordBinding) super.binding;

        ViewUtil.setClickListener(binding.textWord, this);
        binding.layoutDefinition.toggleDefinition.setOnClickListener(this);
        binding.layoutExample.toggleExample.setOnClickListener(this);
        binding.layoutBottom.imageClose.setOnClickListener(this);
        ViewUtil.setClickListener(binding.layoutBottom.textSimpleWord, this);
        ViewUtil.setClickListener(binding.textTranslate, this);
        binding.fab.setOnClickListener(this);

        Color color = getColor();
        ViewUtil.setBackground(binding.layoutBottom.layoutExpandable, color.getPrimaryId());

        UiTask<Word> uiTask = getCurrentTask(true);
        parent = uiTask.getInput().getWord();
        binding = (FragmentWordBinding) super.binding;
        vm = ViewModelProviders.of(this, factory).get(WordViewModel.class);
        vm.setTask(uiTask);

        vm.observeUiState(this, this::processUiState);
        vm.observeFlag(this, this::onFlag);
        vm.observeOutput(this, this::processResponse);

        vm.load(parent);
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                binding.progress.setVisibility(View.VISIBLE);
/*                skeletonScreen = Skeleton.bind(binding.getRoot())
                        .load(R.layout.fragment_word).show();*/
                break;
            case HIDE_PROGRESS:
                binding.progress.setVisibility(View.GONE);
                //skeletonScreen.hide();
                break;
            case OFFLINE:

                break;
            case ONLINE:

                break;
            case EXTRA:

                break;
            case EMPTY:
            case ERROR:
                break;
        }
    }

    @DebugLog
    private void processResponse(Response<WordItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<WordItem> result = (Response.Result<WordItem>) response;
            processSuccess(result.getData());
        }
    }

    public void onFlag(WordItem item) {
        processFavourite(item);
    }

    private void processProgress(boolean loading) {
        if (loading) {
            vm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
            vm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }

    private void processFailure(Throwable error) {
        if (error instanceof IOException || error.getCause() instanceof IOException) {
            vm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            vm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
            vm.updateUiState(UiState.EXTRA);
        } else if (error instanceof MultiException) {
            for (Throwable e : ((MultiException) error).getErrors()) {
                processFailure(e);
            }
        }
    }

    private void processSuccess(WordItem item) {
        if (!parent.equalsIgnoreCase(item.getItem().getWord())) {
            processSimple(item);
            return;
        }
        processDetails(item);
    }

    private void processDetails(WordItem item) {
        Word word = item.getItem();
        binding.setItem(item);
        binding.layoutWord.setVisibility(View.VISIBLE);
        processFavourite(item);
        processDefinitions(item.getItem().getDefinitions());
        processExamples(item.getItem().getExamples());
        processRelated(word.getSynonyms(), word.getAntonyms());
        closeBottom();
    }

    private void processSimple(WordItem item) {
        Word word = item.getItem();
        binding.layoutBottom.textSimpleWord.setText(word.getWord());
        binding.layoutBottom.textPartOfSpeech.setText(word.getPartOfSpeech());
        binding.layoutBottom.textPronunciation.setText(word.getPronunciation());
        showBottom();
    }

    private void processFavourite(WordItem item) {
        int flagImageId = item.isFlagged() ? R.drawable.ic_turned_in_white_24dp : R.drawable.ic_turned_in_not_white_24dp;
        ViewUtil.setIcon(getMenu(), R.id.item_favourite_toggle, flagImageId);
    }

    private void processDefinitions(List<Definition> definitions) {
        StringBuilder singleBuilder = new StringBuilder();
        StringBuilder multipleBuilder = new StringBuilder();

        if (!DataUtil.isEmpty(definitions)) {
            for (int index = 0; index < definitions.size(); index++) {
                if (index == 0) {
                    singleBuilder
                            .append(definitions.get(index).getPartOfSpeech())
                            .append(DataUtil.SEMI)
                            .append(DataUtil.SPACE)
                            .append(definitions.get(index).getText());
                    multipleBuilder
                            .append(definitions.get(index).getPartOfSpeech())
                            .append(DataUtil.SEMI)
                            .append(DataUtil.SPACE)
                            .append(definitions.get(index).getText());
                    continue;
                }
                multipleBuilder
                        .append(DataUtil.NewLine2)
                        .append(definitions.get(index).getPartOfSpeech())
                        .append(DataUtil.SEMI)
                        .append(DataUtil.SPACE)
                        .append(definitions.get(index).getText());
            }
        }

        if (singleBuilder.length() > 0) {
            String text = singleBuilder.toString();
            binding.layoutDefinition.textSingleDefinition.setText(text);
            setSpan(binding.layoutDefinition.textSingleDefinition, text, null);

            text = multipleBuilder.toString();
            binding.layoutDefinition.textMultipleDefinition.setText(text);
            setSpan(binding.layoutDefinition.textMultipleDefinition, text, null);
            binding.layoutDefinition.layoutDefinition.setVisibility(View.VISIBLE);

            if (definitions.size() > 1) {
                binding.layoutDefinition.toggleDefinition.setVisibility(View.VISIBLE);
            } else {
                binding.layoutDefinition.toggleDefinition.setVisibility(View.GONE);
            }

        } else {
            binding.layoutDefinition.layoutDefinition.setVisibility(View.GONE);
        }
    }

    private void processExamples(List<String> examples) {
        StringBuilder singleBuilder = new StringBuilder();
        StringBuilder multipleBuilder = new StringBuilder();

        if (!DataUtil.isEmpty(examples)) {
            for (int index = 0; index < examples.size(); index++) {
                if (index == 0) {
                    singleBuilder
                            .append((index + 1))
                            .append(DataUtil.SEMI)
                            .append(DataUtil.SPACE)
                            .append(examples.get(index));
                    multipleBuilder
                            .append((index + 1))
                            .append(DataUtil.SEMI)
                            .append(DataUtil.SPACE)
                            .append(examples.get(index));
                    continue;
                }
                multipleBuilder
                        .append(DataUtil.NewLine2)
                        .append((index + 1))
                        .append(DataUtil.SEMI)
                        .append(DataUtil.SPACE)
                        .append(examples.get(index));
            }
        }

        if (singleBuilder.length() > 0) {
            String text = singleBuilder.toString();
            binding.layoutExample.textSingleExample.setText(text);
            setSpan(binding.layoutExample.textSingleExample, text, null);

            text = multipleBuilder.toString();
            binding.layoutExample.textMultipleExample.setText(text);
            setSpan(binding.layoutExample.textMultipleExample, text, null);
            binding.layoutExample.layoutExample.setVisibility(View.VISIBLE);

            if (examples.size() > 1) {
                binding.layoutExample.toggleExample.setVisibility(View.VISIBLE);
            } else {
                binding.layoutExample.toggleExample.setVisibility(View.GONE);
            }

        } else {
            binding.layoutExample.layoutExample.setVisibility(View.GONE);
        }
    }

    private void processRelated(List<String> synonyms, List<String> antonyms) {
        String synonym = DataUtil.toString(synonyms);
        String antonym = DataUtil.toString(antonyms);

        if (!DataUtil.isEmpty(synonym)) {
            binding.textSynonym.setText("Synonyms: " + synonym);
            setSpan(binding.textSynonym, synonym, "Synonyms:");
            binding.textSynonym.setVisibility(View.VISIBLE);
        } else {
            binding.textSynonym.setVisibility(View.GONE);
        }

        if (!DataUtil.isEmpty(antonym)) {
            binding.textAntonym.setText("Antonyms: " + antonym);
            setSpan(binding.textAntonym, antonym, "Antonyms:");
            binding.textAntonym.setVisibility(View.VISIBLE);
        } else {
            binding.textAntonym.setVisibility(View.GONE);
        }

        binding.layoutRelated.setVisibility(DataUtil.isEmpty(synonyms) && DataUtil.isEmpty(antonyms) ? View.GONE : View.VISIBLE);
    }

    private void showSimple(String word) {
        vm.updateUiState(UiState.SHOW_PROGRESS);
        vm.load(word);
    }

    private void showDetails(String word) {
        vm.updateUiState(UiState.SHOW_PROGRESS);
        parent = word;
        vm.load(word);
    }

    private void showBottom() {
        binding.layoutBottom.layoutExpandable.expand(true);
        binding.fab.hide();
    }

    private void closeBottom() {
        binding.layoutBottom.layoutExpandable.collapse(true);
        binding.fab.show();
    }

    private void setSpan(TextView view, String text, String bold) {
        List<String> items = TextUtil.getWords(text);
        TextUtil.setSpan(view, items, bold, this::showSimple, this::showDetails);
    }

    private void toggleDefinition() {
        if (binding.layoutDefinition.layoutSingleExpandable.isExpanded()) {
            binding.layoutDefinition.layoutSingleExpandable.collapse(true);
            binding.layoutDefinition.layoutMultipleExpandable.expand(true);
            binding.layoutDefinition.toggleDefinition.setImageResource(R.drawable.ic_arrow_drop_up_accent_24dp);
        } else {
            binding.layoutDefinition.layoutMultipleExpandable.collapse(true);
            binding.layoutDefinition.layoutSingleExpandable.expand(true);
            binding.layoutDefinition.toggleDefinition.setImageResource(R.drawable.ic_arrow_drop_down_accent_24dp);
        }
    }

    private void toggleExample() {
        if (binding.layoutExample.layoutSingleExpandable.isExpanded()) {
            binding.layoutExample.layoutSingleExpandable.collapse(true);
            binding.layoutExample.layoutMultipleExpandable.expand(true);
            binding.layoutExample.toggleExample.setImageResource(R.drawable.ic_arrow_drop_up_accent_24dp);
        } else {
            binding.layoutExample.layoutMultipleExpandable.collapse(true);
            binding.layoutExample.layoutSingleExpandable.expand(true);
            binding.layoutExample.toggleExample.setImageResource(R.drawable.ic_arrow_drop_down_accent_24dp);
        }
    }
}
