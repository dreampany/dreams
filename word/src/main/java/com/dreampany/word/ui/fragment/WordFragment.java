package com.dreampany.word.ui.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Color;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.ColorUtil;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.MenuTint;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.model.Definition;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.databinding.ContentDefinitionBinding;
import com.dreampany.word.databinding.ContentFullWordBinding;
import com.dreampany.word.databinding.ContentRelatedBinding;
import com.dreampany.word.databinding.ContentTopStatusBinding;
import com.dreampany.word.databinding.ContentWordBinding;
import com.dreampany.word.databinding.FragmentWordBinding;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.vm.SearchViewModel;
import com.dreampany.word.vm.WordViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import cz.kinst.jakub.view.StatefulLayout;


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
    private ContentTopStatusBinding bindStatus;
    private ContentFullWordBinding bindFullWord;
    private ContentWordBinding bindWord;
    private ContentRelatedBinding bindRelated;
    private ContentDefinitionBinding bindDef;
    WordViewModel vm;
    SearchViewModel searchVm;
    Word parent;

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
        vm.load(parent, true, true);
    }

    @Override
    protected void onStopUi() {
        processUiState(UiState.HIDE_PROGRESS);
    }

    @Override
    public void onMenuCreated(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        MenuItem shareItem = findMenuItemById(R.id.item_share);
        MenuTint.colorMenuItem(shareItem, ColorUtil.getColor(getContext(), R.color.material_white), null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
/*            case R.id.item_favourite:
                vm.toggle();
                return true;*/
            case R.id.item_share:
                vm.share(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        processUiState(UiState.HIDE_PROGRESS);
    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()) {
            case R.id.image_speak:
            case R.id.text_word:
                speak();
                break;
            case R.id.text_simple_word:
                String text = ViewUtil.getText(v);
                vm.speak(text);
                break;
            case R.id.toggle_definition:
                toggleDefinition();
                break;
            case R.id.toggle_example:
                //toggleExample();
                break;
            case R.id.image_close:
                //closeBottom();
                break;
            case R.id.button_favorite:
                searchVm.toggleFavorite(binding.getItem().getItem());
                break;
        }
    }

    private void initView() {
        setTitle(R.string.word);

        UiTask<Word> uiTask = getCurrentTask(true);
        parent = uiTask.getInput();

        binding = (FragmentWordBinding) super.binding;
        bindStatus = binding.layoutTopStatus;
        bindFullWord = binding.layoutFullWord;
        bindWord = bindFullWord.layoutWord;
        bindRelated = bindFullWord.layoutRelated;
        bindDef = bindFullWord.layoutDefinition;

        ViewUtil.setSwipe(binding.layoutRefresh, this);
        bindDef.toggleDefinition.setOnClickListener(this);
        bindWord.buttonFavorite.setOnClickListener(this);
        bindWord.textWord.setOnClickListener(this);
        bindWord.imageSpeak.setOnClickListener(this);

/*        ViewUtil.setClickListener(binding.textWord, this);
        binding.layoutDefinition.toggleDefinition.setOnClickListener(this);
        binding.layoutExample.toggleExample.setOnClickListener(this);
        binding.layoutBottom.imageClose.setOnClickListener(this);
        ViewUtil.setClickListener(binding.layoutBottom.textSimpleWord, this);
        ViewUtil.setClickListener(binding.textTranslate, this);
        binding.fab.setOnClickListener(this);*/

        Color color = getColor();
        ViewUtil.setBackground(binding.layoutBottom.layoutExpandable, color.getPrimaryId());

        searchVm = ViewModelProviders.of(this, factory).get(SearchViewModel.class);
        vm = ViewModelProviders.of(this, factory).get(WordViewModel.class);
        vm.setTask(uiTask);

        vm.observeUiState(this, this::processUiState);
        vm.observeOutput(this, this::processResponse);
        searchVm.observeUiState(this, this::processUiState);
        searchVm.observeOutput(this, this::processResponse);
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                if (!binding.layoutRefresh.isRefreshing()) {
                    binding.layoutRefresh.setRefreshing(true);
                }
                break;
            case HIDE_PROGRESS:
                if (binding.layoutRefresh.isRefreshing()) {
                    binding.layoutRefresh.setRefreshing(false);
                }
                break;
            case OFFLINE:
                bindStatus.layoutExpandable.expand();
                break;
            case ONLINE:
                bindStatus.layoutExpandable.collapse();
                break;
            case EXTRA:
                //processUiState(adapter.isEmpty() ? UiState.EMPTY : UiState.CONTENT);
                break;
            case SEARCH:
                //binding.stateful.setState(SEARCH);
                break;
            case EMPTY:
                //binding.stateful.setState(SEARCH);
                break;
            case ERROR:
                break;
            case CONTENT:
                binding.stateful.setState(StatefulLayout.State.CONTENT);
                break;
        }
    }


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
/*        if (!parent.equals(item.getItem())) {
            processSimple(item);
            return;
        }*/
        processDetails(item);
    }

    private void processDetails(WordItem item) {
        Word word = item.getItem();
        binding.setItem(item);
        bindWord.layoutWord.setVisibility(View.VISIBLE);
        processRelated(word.getSynonyms(), word.getAntonyms());
        processDefinitions(item.getItem().getDefinitions());
        processUiState(UiState.CONTENT);
/*        binding.layoutWord.setVisibility(View.VISIBLE);
        processFavourite(item);
        processDefinitions(item.getItem().getDefinitions());
        processExamples(item.getItem().getExamples());
        processRelated(word.getSynonyms(), word.getAntonyms());
        closeBottom();*/
    }

    private void processSimple(WordItem item) {
        Word word = item.getItem();
        binding.layoutBottom.textSimpleWord.setText(word.getId());
        binding.layoutBottom.textPartOfSpeech.setText(word.getPartOfSpeech());
        binding.layoutBottom.textPronunciation.setText(word.getPronunciation());
        //showBottom();
    }

    private void processRelated(List<String> synonyms, List<String> antonyms) {
        String synonym = DataUtil.toString(synonyms, Constants.Sep.COMMA_SPACE);
        String antonym = DataUtil.toString(antonyms, Constants.Sep.COMMA_SPACE);

        if (!DataUtil.isEmpty(synonym)) {
            bindRelated.textSynonym.setText(getString(R.string.synonyms, synonym));
            setSpan(bindRelated.textSynonym, synonym, getString(R.string.synonyms_bold));
            bindRelated.textSynonym.setVisibility(View.VISIBLE);
        } else {
            bindRelated.textSynonym.setVisibility(View.GONE);
        }

        if (!DataUtil.isEmpty(antonym)) {
            bindRelated.textAntonym.setText(getString(R.string.antonyms, antonym));
            setSpan(bindRelated.textAntonym, antonym, getString(R.string.antonyms_bold));
            bindRelated.textAntonym.setVisibility(View.VISIBLE);
        } else {
            bindRelated.textAntonym.setVisibility(View.GONE);
        }
        bindRelated.layoutRelated.setVisibility(DataUtil.isEmpty(synonyms) && DataUtil.isEmpty(antonyms) ? View.GONE : View.VISIBLE);
    }

    private void processDefinitions(List<Definition> definitions) {
        StringBuilder singleBuilder = new StringBuilder();
        StringBuilder multipleBuilder = new StringBuilder();

        if (!DataUtil.isEmpty(definitions)) {
            for (int index = 0; index < definitions.size(); index++) {
                Definition def = definitions.get(index);
                if (index == 0) {
                    singleBuilder
                            .append(def.getPartOfSpeech())
                            .append(DataUtil.SEMI)
                            .append(DataUtil.SPACE)
                            .append(def.getText());
                    multipleBuilder
                            .append(def.getPartOfSpeech())
                            .append(DataUtil.SEMI)
                            .append(DataUtil.SPACE)
                            .append(def.getText());
                    continue;
                }
                multipleBuilder
                        .append(DataUtil.NewLine2)
                        .append(def.getPartOfSpeech())
                        .append(DataUtil.SEMI)
                        .append(DataUtil.SPACE)
                        .append(def.getText());
            }
        }

        if (singleBuilder.length() > 0) {
            String text = singleBuilder.toString();
            bindDef.textSingleDefinition.setText(text);
            setSpan(bindDef.textSingleDefinition, text, null);

            text = multipleBuilder.toString();
            bindDef.textMultipleDefinition.setText(text);
            setSpan(bindDef.textMultipleDefinition, text, null);
            bindDef.layoutDefinition.setVisibility(View.VISIBLE);

            if (definitions.size() > 1) {
                bindDef.toggleDefinition.setVisibility(View.VISIBLE);
            } else {
                bindDef.toggleDefinition.setVisibility(View.GONE);
            }

        } else {
            bindDef.layoutDefinition.setVisibility(View.GONE);
        }
    }

    private void setSpan(TextView view, String text, String bold) {
        List<String> items = TextUtil.getWords(text);
        TextUtil.setSpan(view, items, bold, this::searchWord, this::searchWord);
    }

    private void toggleDefinition() {
        if (bindDef.layoutSingleExpandable.isExpanded()) {
            bindDef.layoutSingleExpandable.collapse(true);
            bindDef.layoutMultipleExpandable.expand(true);
            bindDef.toggleDefinition.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        } else {
            bindDef.layoutMultipleExpandable.collapse(true);
            bindDef.layoutSingleExpandable.expand(true);
            bindDef.toggleDefinition.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }
    }

    private void searchWord(String word) {
        //searchView.clearFocus();
        searchVm.find(word.toLowerCase(), true);
        AndroidUtil.speak(word);
    }

    private void speak() {
        WordItem item = bindWord.getItem();
        if (item != null) {
            AndroidUtil.speak(item.getItem().getId());
        }
    }

/*    private void processExamples(List<String> examples) {
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
    }*/

/*    */

/*    private void showSimple(String word) {
        vm.updateUiState(UiState.SHOW_PROGRESS);
        vm.load(word, true, true);
    }

    private void showDetails(String word) {
        vm.updateUiState(UiState.SHOW_PROGRESS);
        parent = vm.toWord(word);
        vm.load(parent, true, true);
    }*/

/*    private void showBottom() {
        binding.layoutBottom.layoutExpandable.expand(true);
        binding.fab.hide();
    }

    private void closeBottom() {
        binding.layoutBottom.layoutExpandable.collapse(true);
        binding.fab.show();
    }*/

/*    private void setSpan(TextView view, String text, String bold) {
        List<String> items = TextUtil.getWords(text);
        TextUtil.setSpan(view, items, bold, this::showSimple, this::showDetails);
    }*/


/*    private void toggleExample() {
        if (binding.layoutExample.layoutSingleExpandable.isExpanded()) {
            binding.layoutExample.layoutSingleExpandable.collapse(true);
            binding.layoutExample.layoutMultipleExpandable.expand(true);
          //  binding.layoutExample.toggleExample.setImageResource(R.drawable.ic_arrow_drop_up_accent_24dp);
        } else {
            binding.layoutExample.layoutMultipleExpandable.collapse(true);
            binding.layoutExample.layoutSingleExpandable.expand(true);
//            binding.layoutExample.toggleExample.setImageResource(R.drawable.ic_arrow_drop_down_accent_24dp);
        }
    }*/
}
