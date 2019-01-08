package com.dreampany.word.data.misc;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.word.api.wordnik.WordnikWord;
import com.dreampany.word.data.model.Definition;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;
import com.dreampany.word.misc.WordAnnote;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by Hawladar Roman on 9/3/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class WordMapper {

    private final SmartMap<Long, Word> map;
    private final SmartCache<Long, Word> cache;

    @Inject
    WordMapper(@WordAnnote SmartMap<Long, Word> map,
               @WordAnnote SmartCache<Long, Word> cache) {
        this.map = map;
        this.cache = cache;
    }

    public boolean isExists(Word item) {
        return map.contains(item.getId());
    }

    public Word toItem(String word) {
        if (DataUtil.isEmpty(word)) {
            return null;
        }
        long id = DataUtil.getSha512(word);
        Word out = map.get(id);
        if (out == null) {
            out = new Word();
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setWord(word);

        if (word == null) {
            Exception e = new NullPointerException();
            Timber.e(e);
        }

        return out;
    }

    public Word toItem(WordnikWord in,  boolean full) {
        if (in == null) {
            return null;
        }

        long id = DataUtil.getSha512(in.getWord());
        Word out = map.get(id);
        if (out == null) {
            out = new Word();
            if (full) {
                map.put(id, out);
            }
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setWord(in.getWord());
        out.setPartOfSpeech(in.getPartOfSpeech());
        out.setPronunciation(in.getPronunciation());
        if (full) {
            out.setDefinitions(getDefinitions(in));
            out.setExamples(getExamples(in));
            out.setSynonyms(getSynonyms(in));
            out.setAntonyms(getAntonyms(in));
        }

        if (in.getWord() == null) {
            Exception e = new NullPointerException();
            Timber.e(e);
        }

        return out;
    }

    public Word toItem(String word, WordnikWord in,  boolean full) {
        if (in == null) {
            return null;
        }

        long id = DataUtil.getSha512(word);
        Word out = map.get(id);
        if (out == null) {
            out = new Word();
            if (full) {
                map.put(id, out);
            }
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setWord(word);
        out.setPartOfSpeech(in.getPartOfSpeech());
        out.setPronunciation(in.getPronunciation());
        if (full) {
            out.setDefinitions(getDefinitions(in));
            out.setExamples(getExamples(in));
            out.setSynonyms(getSynonyms(in));
            out.setAntonyms(getAntonyms(in));
        }

        if (in.getWord() == null) {
            Exception e = new NullPointerException();
            Timber.e(e);
        }

        return out;
    }

    public Word toItem(Flag in, WordDataSource source) {
        Word out = map.get(in.getId());
        if (out == null) {
            out = source.getItem(in.getId());
            map.put(in.getId(), out);
        }
        return out;
    }

    public Word toItem(State in, WordDataSource source) {
        Word out = map.get(in.getId());
        if (out == null) {
            out = source.getItem(in.getId());
            map.put(in.getId(), out);
        }
        return out;
    }

    private List<Definition> getDefinitions(WordnikWord in) {
        if (in.hasDefinition()) {
            List<Definition> result = new ArrayList<>();
            Stream.of(in.getDefinitions()).forEach(item -> {
                Definition def = new Definition();
                def.setPartOfSpeech(item.getPartOfSpeech());
                def.setText(item.getText());
                result.add(def);
            });
            return result;
        }
        return null;
    }

    private List<String> getExamples(WordnikWord in) {
        if (in.hasExample()) {
            List<String> result = new ArrayList<>();
            Stream.of(in.getExamples()).forEach(result::add);
            return result;
        }
        return null;
    }

    private List<String> getSynonyms(WordnikWord in) {
        if (in.hasSynonyms()) {
            List<String> result = new ArrayList<>();
            Stream.of(in.getSynonyms()).forEach(result::add);
            return result;
        }
        return null;
    }

    private List<String> getAntonyms(WordnikWord in) {
        if (in.hasAntonyms()) {
            List<String> result = new ArrayList<>();
            Stream.of(in.getAntonyms()).forEach(result::add);
            return result;
        }
        return null;
    }

}
