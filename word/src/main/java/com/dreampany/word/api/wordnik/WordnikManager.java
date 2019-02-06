package com.dreampany.word.api.wordnik;

import com.dreampany.frame.util.DataUtil;
import com.wordnik.client.api.WordApi;
import com.wordnik.client.api.WordsApi;
import com.wordnik.client.model.Definition;
import com.wordnik.client.model.Example;
import com.wordnik.client.model.ExampleSearchResults;
import com.wordnik.client.model.Related;
import com.wordnik.client.model.SimpleDefinition;
import com.wordnik.client.model.SimpleExample;
import com.wordnik.client.model.TextPron;
import com.wordnik.client.model.WordObject;
import com.wordnik.client.model.WordOfTheDay;
import com.wordnik.client.model.WordSearchResult;
import com.wordnik.client.model.WordSearchResults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 9/3/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class WordnikManager {

    private final String WORDNIK_API_KEY = "api_key";
    private final String WORDNIK_API_KEY_VALUE_ROMANBJIT = "5c9a53f4c0e012d4cf5a66115420c073d7da523b9081dff1f";
    private final String WORDNIK_API_KEY_VALUE_DREAMPANY = "464b0c5a35f469103f3610840dc061f1c768aa1c223ffa447";
    private final String WORDNIK_API_KEY_VALUE_IFTENET = "a6714f04f26b9f14e29a920702e0f03dde4b84e98f94fe6fe";
    private final String[] KEYS = {WORDNIK_API_KEY_VALUE_ROMANBJIT, WORDNIK_API_KEY_VALUE_DREAMPANY, WORDNIK_API_KEY_VALUE_IFTENET};

    private final String RELATED_SYNONYM = "synonym";
    private final String RELATED_ANTONYM = "antonym";

    private final String RELATED_SYNONYM_ANTONYM = "synonym,antonym";

    private final List<WordsApi> wordsApis;
    private final List<WordApi> wordApis;

    private final CircularFifoQueue<Integer> queue;

    @Inject
    WordnikManager() {
        wordsApis = Collections.synchronizedList(new ArrayList<>());
        wordApis = Collections.synchronizedList(new ArrayList<>());
        queue = new CircularFifoQueue<>(KEYS.length);

        for (int index = 0; index < KEYS.length; index++) {
            String key = KEYS[index];
            queue.add(index);

            WordsApi wordsApi = new WordsApi();
            wordsApi.addHeader(WORDNIK_API_KEY, key);
            wordsApis.add(wordsApi);

            WordApi wordApi = new WordApi();
            wordApi.addHeader(WORDNIK_API_KEY, key);
            wordApis.add(wordApi);
        }


    }

    public WordnikWord getWordOfTheDay(String date, int limit) {
        for (WordsApi api : wordsApis) {
            try {
                WordOfTheDay wordOfTheDay = api.getWordOfTheDay(date);
                return getWord(wordOfTheDay, limit);
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return null;
    }

    public WordnikWord getWord(String word, int limit) {
        for (WordApi api : wordApis) {
            try {
                String useCanonical = "true";
                String includeSuggestions = "false";
                WordObject wordObject = api.getWord(word, useCanonical, includeSuggestions);
                return getWord(wordObject, limit);
            } catch (Exception e) {
                Timber.e(e);
            }
        }

        return null;
    }

    public List<WordnikWord> search(String query, int limit) {

        String includePartOfSpeech = null;
        String excludePartOfSpeech = null;
        String caseSensitive = "false";
        int minCorpusCount = 5;
        int maxCorpusCount = -1;
        int minDictionaryCount = 1;
        int maxDictionaryCount = -1;
        int minLength = 1;
        int maxLength = -1;
        int skip = 0;

        for (WordsApi api : wordsApis) {
            try {
                WordSearchResults results = api.searchWords(query,
                        includePartOfSpeech,
                        excludePartOfSpeech,
                        caseSensitive,
                        minCorpusCount,
                        maxCorpusCount,
                        minDictionaryCount,
                        maxDictionaryCount,
                        minLength,
                        maxLength,
                        skip,
                        limit
                );
                if (results != null) {
                    List<WordSearchResult> searches = results.getSearchResults();
                    if (!DataUtil.isEmpty(searches)) {
                        List<WordnikWord> words = new ArrayList<>(searches.size());
                        for (WordSearchResult result : searches) {
                            words.add(new WordnikWord(result.getWord().toLowerCase()));
                        }
                        return words;
                    }
                }

            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return null;
    }

    private WordnikWord getWord(WordOfTheDay from, int limit) {
        WordnikWord word = new WordnikWord(from.getWord().toLowerCase());
        word.setPartOfSpeech(getPartOfSpeech(from));
        word.setPronunciation(getPronunciation(from));
        word.setDefinitions(getDefinitions(from));
        word.setExamples(getExamples(from));

        List<Related> relateds = getRelateds(from.getWord(), RELATED_SYNONYM_ANTONYM, limit);
        word.setSynonyms(getSynonyms(relateds));
        word.setAntonyms(getAntonyms(relateds));
        return word;
    }

    private WordnikWord getWord(WordObject from, int limit) {
        WordnikWord word = new WordnikWord(from.getWord().toLowerCase());

        List<Definition> definitions = getDefinitions(from.getWord(), limit);
        word.setPartOfSpeech(getPartOfSpeech(definitions));
        word.setPronunciation(getPronunciation(from));
        word.setDefinitions(getDefinitions(definitions));

        List<Example> examples = getExamples(from.getWord(), limit);
        word.setExamples(getExamples(examples));

        List<Related> relateds = getRelateds(from.getWord(), RELATED_SYNONYM_ANTONYM, limit);
        word.setSynonyms(getSynonyms(relateds));
        word.setAntonyms(getAntonyms(relateds));

        return word;
    }

    private String getPartOfSpeech(WordOfTheDay word) {
        List<SimpleDefinition> items = word.getDefinitions();
        if (!DataUtil.isEmpty(items)) {
            for (SimpleDefinition item : items) {
                if (!DataUtil.isEmpty(item.getPartOfSpeech())) {
                    return item.getPartOfSpeech();
                }
            }
        }
        return null;
    }

    private String getPartOfSpeech(List<Definition> items) {
        if (!DataUtil.isEmpty(items)) {
            for (Definition item : items) {
                if (!DataUtil.isEmpty(item.getPartOfSpeech())) {
                    return item.getPartOfSpeech();
                }
            }
        }
        return null;
    }

    private String getPronunciation(WordOfTheDay word) {
        return getPronunciation(word.getWord());
    }

    private String getPronunciation(WordObject word) {
        return getPronunciation(word.getWord());
    }

    private List<WordnikDefinition> getDefinitions(WordOfTheDay word) {
        List<SimpleDefinition> items = word.getDefinitions();
        if (!DataUtil.isEmpty(items)) {
            List<WordnikDefinition> definitions = new ArrayList<>(items.size());
            for (SimpleDefinition item : items) {
                definitions.add(new WordnikDefinition(item.getPartOfSpeech(), item.getText()));
            }
            return definitions;
        }
        return null;
    }

    private List<WordnikDefinition> getDefinitions(List<Definition> items) {
        if (!DataUtil.isEmpty(items)) {
            List<WordnikDefinition> definitions = new ArrayList<>(items.size());
            for (Definition item : items) {
                definitions.add(new WordnikDefinition(item.getPartOfSpeech(), item.getText()));
            }
            return definitions;
        }
        return null;
    }

    private List<String> getExamples(WordOfTheDay word) {
        List<SimpleExample> items = word.getExamples();
        if (!DataUtil.isEmpty(items)) {
            List<String> examples = new ArrayList<>(items.size());
            for (SimpleExample item : items) {
                examples.add(item.getText());
            }
            return examples;
        }
        return null;
    }

    private List<String> getExamples(List<Example> items) {
        if (!DataUtil.isEmpty(items)) {
            List<String> examples = new ArrayList<>(items.size());
            for (Example item : items) {
                examples.add(item.getText());
            }
            return examples;
        }
        return null;
    }

    private List<String> getSynonyms(List<Related> relateds) {
        Related related = getRelated(relateds, RELATED_SYNONYM);
        if (related != null) {
            return related.getWords();
        }
        return null;
    }

    private List<String> getAntonyms(List<Related> relateds) {
        Related related = getRelated(relateds, RELATED_ANTONYM);
        if (related != null) {
            return related.getWords();
        }
        return null;
    }


    private String getPronunciation(String word) {
        for (WordApi api : wordApis) {
            try {
                String sourceDictionary = null;
                String typeFormat = null;
                String useCanonical = "true";

                List<TextPron> pronunciations = api.getTextPronunciations(word, sourceDictionary, typeFormat, useCanonical, 3);
                if (!DataUtil.isEmpty(pronunciations)) {
                    String pronunciation = pronunciations.get(0).getRaw();
                    for (int index = 1; index < pronunciations.size(); index++) {
                        if (pronunciation.length() > pronunciations.get(index).getRaw().length()) {
                            pronunciation = pronunciations.get(index).getRaw();
                        }
                    }
                    pronunciation = pronunciation.replaceAll("(?s)<i>.*?</i>", "");
                    return pronunciation;
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return null;
    }

    private List<Definition> getDefinitions(String word, int limit) {
        for (WordApi api : wordApis) {
            try {
                String partOfSpeech = null;
                String sourceDictionaries = null;
                String includeRelated = null;
                String useCanonical = "true";
                List<Definition> definitions = api.getDefinitions(word, partOfSpeech, sourceDictionaries, limit, includeRelated, useCanonical, null);
                return definitions;
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return null;
    }

    public List<Example> getExamples(String word, int limit) {
        for (WordApi api : wordApis) {
            try {
                String includeDuplicates = "true";
                String useCanonical = "true";
                int skip = 0;
                ExampleSearchResults results = api.getExamples(word, includeDuplicates, useCanonical, skip, limit);
                if (results != null) {
                    return results.getExamples();
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return null;
    }

    public List<WordnikWord> query(String query, int limit) {
        for (WordsApi api : wordsApis) {
            String includePartOfSpeech = null;
            String excludePartOfSpeech = null;
            String caseSensitive = "false";
            int minCorpusCount = 5;
            int maxCorpusCount = -1;
            int minDictionaryCount = 1;
            int maxDictionaryCount = -1;
            int minLength = 1;
            int maxLength = -1;
            int skip = 0;

            try {
                WordSearchResults results = api.searchWords(query, includePartOfSpeech, excludePartOfSpeech,
                        caseSensitive, minCorpusCount, maxCorpusCount, minDictionaryCount, maxDictionaryCount, minLength, maxLength, skip, limit
                );

                if (results != null) {
                    List<WordSearchResult> searches = results.getSearchResults();
                    if (!DataUtil.isEmpty(searches)) {
                        List<WordnikWord> words = new ArrayList<>(searches.size());
                        for (WordSearchResult result : searches) {
                            words.add(new WordnikWord(result.getWord().toLowerCase()));
                        }
                        return words;
                    }
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return null;
    }

    private List<Related> getRelateds(String word, String relationshipTypes, int limit) {
        for (WordApi api : wordApis) {
            try {
                String useCanonical = "true";

                List<Related> relateds = api.getRelatedWords(word, relationshipTypes, useCanonical, limit);
                return relateds;

            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return null;
    }


    private Related getRelated(List<Related> relateds, String relationshipType) {
        if (!DataUtil.isEmpty(relateds)) {
            for (Related related : relateds) {
                if (relationshipType.equals(related.getRelationshipType())) {
                    return related;
                }
            }
        }
        return null;
    }
}
