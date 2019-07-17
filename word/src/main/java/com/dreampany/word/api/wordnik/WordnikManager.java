package com.dreampany.word.api.wordnik;

import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.word.api.wordnik.core.ClientException;
import com.dreampany.word.api.wordnik.model.Definition;
import com.dreampany.word.api.wordnik.model.Example;
import com.dreampany.word.api.wordnik.model.ExampleSearchResults;
import com.dreampany.word.api.wordnik.model.ExampleUsage;
import com.dreampany.word.api.wordnik.model.Related;
import com.dreampany.word.api.wordnik.model.TextPron;
import com.dreampany.word.api.wordnik.model.WordOfTheDay;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class WordnikManager {

    private final String WORDNIK_API_KEY = "api_key";
    private final String WORDNIK_API_KEY_ROMANBJIT = "5c9a53f4c0e012d4cf5a66115420c073d7da523b9081dff1f";
    private final String WORDNIK_API_KEY_DREAMPANY = "464b0c5a35f469103f3610840dc061f1c768aa1c223ffa447";
    private final String WORDNIK_API_KEY_IFTENET = "a6714f04f26b9f14e29a920702e0f03dde4b84e98f94fe6fe";
    private final String[] KEYS = {WORDNIK_API_KEY_ROMANBJIT, WORDNIK_API_KEY_DREAMPANY, WORDNIK_API_KEY_IFTENET};

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
            wordsApi.setKeyOfApi(key);
            wordsApis.add(wordsApi);

            WordApi wordApi = new WordApi();
            wordApi.setKeyOfApi(key);
            wordApis.add(wordApi);
        }
    }

    public WordnikWord getWordOfTheDay(String date, int limit) {
        for (int index = 0; index < KEYS.length; index++) {
            WordsApi api = getWordsApi();
            try {
                WordOfTheDay wordOfTheDay = api.getWordOfTheDay(date);
                return getWord(wordOfTheDay, limit);
            } catch (Exception e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    public WordnikWord getWord(String word, int limit) {
        for (int index = 0; index < KEYS.length; index++) {
            WordApi api = getWordApi();
            try {
                String useCanonical = "true";
                String includeSuggestions = "false";
                //WordObject wordObject = api.getWord(word, useCanonical, includeSuggestions);
                WordnikWord result = getWordImpl(word, limit);
                return result;
            } catch (Exception e) {
                Timber.e(e);
                iterateQueue();
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

        for (int index = 0; index < KEYS.length; index++) {
            WordsApi api = getWordsApi();
/*            try {
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
                iterateQueue();
            }*/
        }
        return null;
    }

    private void iterateQueue() {
        queue.add(queue.peek());
    }

    private WordApi getWordApi() {
        return wordApis.get(queue.peek());
    }

    private WordsApi getWordsApi() {
        return wordsApis.get(queue.peek());
    }

    private WordnikWord getWord(WordOfTheDay from, int limit) {
        WordnikWord word = new WordnikWord(from.getWord().toLowerCase());
        word.setPartOfSpeech(getPartOfSpeech(from));
        //word.setPronunciation(getPronunciation(from));
        word.setDefinitions(getDefinitions(from));
        word.setExamples(getExamples(from));

        List<Related> relateds = getRelateds(from.getWord(), RELATED_SYNONYM_ANTONYM, limit);
        word.setSynonyms(getSynonyms(relateds));
        word.setAntonyms(getAntonyms(relateds));
        return word;
    }

    private WordnikWord getWordImpl(String from, int limit) {
        WordnikWord word = new WordnikWord(from);

        List<Definition> definitions = getDefinitions(from, limit);

        word.setPartOfSpeech(getPartOfSpeech(definitions));
        word.setPronunciation(getPronunciation(from, limit));
        word.setDefinitions(getDefinitions(definitions));
        word.setExamples(getExamplesBy(definitions));

        //List<String> examples = getExamples(from, limit);
        //word.setExamples(examples);
        //word.setExamples(getExamples(examples));

        List<Related> relateds = getRelateds(from, RELATED_SYNONYM_ANTONYM, limit);

        word.setSynonyms(getSynonyms(relateds));
        word.setAntonyms(getAntonyms(relateds));

        return word;
    }

    private String getPartOfSpeech(WordOfTheDay word) {
        String[] items = word.getDefinitions();
        if (!DataUtil.isEmpty(items)) {
            for (String item : items) {
   /*             if (!DataUtil.isEmpty(item.getPartOfSpeech())) {
                    return item.getPartOfSpeech();
                }*/
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

/*    private String getPronunciation(WordOfTheDay word) {
        return getPronunciation(word.getWord());
    }

    private String getPronunciation(WordObject word) {
        return getPronunciation(word.getWord());
    }*/

    private List<WordnikDefinition> getDefinitions(WordOfTheDay word) {
        String[] items = word.getDefinitions();
        if (!DataUtil.isEmpty(items)) {
            List<WordnikDefinition> definitions = new ArrayList<>(items.length);
            for (String item : items) {
                //definitions.add(new WordnikDefinition(item.getPartOfSpeech(), item.getText()));
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
        String[] items = word.getExamples();
        if (!DataUtil.isEmpty(items)) {
            List<String> examples = new ArrayList<>(items.length);
            for (String item : items) {
                examples.add(item);
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

    private List<String> getExamplesBy(List<Definition> definitions) {
        if (!DataUtil.isEmpty(definitions)) {
            List<String> examples = new ArrayList<>();
            for (Definition def : definitions) {
                if (DataUtil.isEmpty(def.getExampleUses()))
                    continue;
                for (ExampleUsage ex : def.getExampleUses()) {
                    examples.add(ex.getText());
                }
            }
            return examples;
        }
        return null;
    }

    private List<String> getSynonyms(List<Related> relateds) {
        Related related = getRelated(relateds, RELATED_SYNONYM);
        if (related != null) {
            return Arrays.asList(related.getWords());
        }
        return null;
    }

    private List<String> getAntonyms(List<Related> relateds) {
        Related related = getRelated(relateds, RELATED_ANTONYM);
        if (related != null) {
            return Arrays.asList(related.getWords());
        }
        return null;
    }


    private String getPronunciation(String word, int limit) {
        for (int index = 0; index < KEYS.length; index++) {
            WordApi api = getWordApi();
            try {
                String sourceDictionary = null;
                String typeFormat = null;
                String useCanonical = "true";

                TextPron[] result = api.getTextPronunciations(word, useCanonical, sourceDictionary, typeFormat, limit);
                List<TextPron> pronunciations = Arrays.asList(result);
                if (!DataUtil.isEmpty(pronunciations)) {
                    String pronunciation = pronunciations.get(0).getRaw();
                    for (int indexX = 1; indexX < pronunciations.size(); indexX++) {
                        if (pronunciation.length() > pronunciations.get(indexX).getRaw().length()) {
                            pronunciation = pronunciations.get(indexX).getRaw();
                        }
                    }
                    pronunciation = pronunciation.replaceAll("(?s)<i>.*?</i>", "");
                    return pronunciation;
                }
            } catch (Exception e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    private List<Definition> getDefinitions(String word, int limit) {
        for (int index = 0; index < KEYS.length; index++) {
            WordApi api = getWordApi();
            try {
                String partOfSpeech = null;
                String includeRelated = "false";
                String[] sourceDictionaries = null;
                String useCanonical = "true";
                String includeTags = "false";
                Definition[] definitions = api.getDefinitions(word, limit, partOfSpeech, includeRelated, sourceDictionaries, useCanonical, includeTags);
                return Arrays.asList(definitions);
            } catch (Exception error) {
                Timber.e(error);
                if (error instanceof ClientException) {
                    if (error.toString().contains("404")) {
                        word = TextUtil.toTitleCase(word);
                        index--;
                        continue;
                    }
                }
                iterateQueue();
            }
        }
        return null;
    }

    public List<String> getExamples(String word, int limit) {
        for (int index = 0; index < KEYS.length; index++) {
            WordApi api = getWordApi();
            try {
                String includeDuplicates = "true";
                String useCanonical = "true";
                int skip = 0;
                ExampleSearchResults results = api.getExamples(word, includeDuplicates, useCanonical, skip, limit);
                return Arrays.asList(results.getExamples());
            } catch (Exception e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    public List<WordnikWord> query(String query, int limit) {

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

        for (int index = 0; index < KEYS.length; index++) {
            WordsApi api = getWordsApi();

/*            try {
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
                iterateQueue();
            }*/
        }
        return null;
    }

    private List<Related> getRelateds(String word, String relationshipTypes, int limit) {
        for (int index = 0; index < KEYS.length; index++) {
            WordApi api = getWordApi();
            try {
                String useCanonical = "true";

                Related[] relateds = api.getRelatedWords(word, useCanonical, relationshipTypes, limit);
                return Arrays.asList(relateds);

            } catch (Exception error) {
                Timber.e(error);
                if (error instanceof ClientException) {
                    if (error.toString().contains("404")) {
                        word = TextUtil.toTitleCase(word);
                        index--;
                        continue;
                    }
                }
                iterateQueue();
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
