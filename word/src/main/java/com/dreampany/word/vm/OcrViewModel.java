package com.dreampany.word.vm;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import com.dreampany.frame.data.source.repository.StoreRepository;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.enums.ItemType;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.github.oliveiradev.lib.Rx2Photo;
import com.github.oliveiradev.lib.shared.TypeRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import hugo.weaving.DebugLog;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 9/27/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class OcrViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private static final long initialDelay = Constants.Time.INSTANCE.getWordPeriod();
    private static final long period = Constants.Time.INSTANCE.getWordPeriod();
    private static final int retry = 3;

    private final WordRepository repo;
    private final StoreRepository storeRepo;
    private final StateMapper stateMapper;
    private SmartAdapter.Callback<WordItem> uiCallback;
    private Disposable updateDisposable, updateVisibleItemsDisposable;

    @Inject
    OcrViewModel(Application application,
                 RxMapper rx,
                 AppExecutors ex,
                 ResponseMapper rm,
                 WordRepository repo,
                 StoreRepository storeRepo,
                 StateMapper stateMapper) {
        super(application, rx, ex, rm);
        this.repo = repo;
        this.storeRepo = storeRepo;
        this.stateMapper = stateMapper;
    }

    @Override
    public void clear() {
        //network.deObserve(this::onResult, true);
        this.uiCallback = null;
        removeSubscription(updateDisposable);
        removeSubscription(updateVisibleItemsDisposable);
        super.clear();
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    public void removeUpdateVisibleItemsDisposable() {
        removeSubscription(updateVisibleItemsDisposable);
    }

    public void setUiCallback(SmartAdapter.Callback<WordItem> callback) {
        this.uiCallback = callback;
    }

    @DebugLog
    public void loads(boolean fresh) {
        if (!preLoads(fresh)) {
            updateVisibleItems();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getOcrItemsRx(fresh))
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                })
                .subscribe(                        result -> {
                    postProgress(false);
                    //postResult(result);
                }, error -> {
                    //postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
        updateVisibleItems();
    }

    public void updateVisibleItems() {
        if (hasDisposable(updateVisibleItemsDisposable)) {
            return;
        }
 /*       updateVisibleItemsDisposable = getRx()
                .backToMain(getVisibleItemsRx())
                .subscribe(this::postResult, error -> {

                });
        addSubscription(updateVisibleItemsDisposable);*/
    }

    @DebugLog
    public void loadOcrOfCamera(Activity parent, boolean fresh) {
        Dexter.withActivity(parent)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            loadOcrOfCameraImpl(fresh);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    @DebugLog
    public void loadOcrOfImage(boolean fresh) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsOfImage())
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                })
                .subscribe(                        result -> {
                    postProgress(false);
                    //postResult(result);
                }, error -> {
                    //postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void update() {
        if (hasDisposable(updateDisposable)) {
            Timber.v("Updater Running...");
            return;
        }
        updateDisposable = getRx()
                .backToMain(updateItemInterval())
                .subscribe(                        result -> {
                    postProgress(false);
                    //postResult(result);
                }, this::postFailure);
        addSubscription(updateDisposable);
    }


    public void toggle(Word word) {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(word))
                .subscribe(this::postFavorite, this::postFailure);
        addSingleSubscription(disposable);
    }

    private Maybe<List<WordItem>> getOcrItemsRx(boolean fresh) {
        return getWordsOfOcr()
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) words -> getItemsRx(words, fresh));
    }

    private Flowable<WordItem> updateItemInterval() {
        Flowable<WordItem> flowable = Flowable
                .interval(initialDelay, period, TimeUnit.MILLISECONDS, getRx().io())
                .map(tick -> {
                    WordItem next = null;
                    if (uiCallback != null) {
                        List<WordItem> items = uiCallback.getVisibleItems();
                        if (!DataUtil.isEmpty(items)) {
                            for (WordItem item : items) {
                                /*if (!repo.hasState(item.getItemRx(), ItemState.STATE, ItemSubstate.FULL)) {
                                    Timber.d("Next Item to updateVisibleItemIf %s", item.getItemRx().getWord());
                                    getEx().postToUi(() -> postProgress(true));
                                    next = updateItemRx(item.getItemRx()).blockingGet();
                                    break;
                                }*/
                            }
                        }
                    }
                    return next;
                }).retry(retry);
        return flowable;
    }

    private Maybe<List<WordItem>> getVisibleItemsRx() {
        return Maybe.fromCallable(() -> {
            List<WordItem> items = uiCallback.getVisibleItems();
            if (!DataUtil.isEmpty(items)) {
                for (WordItem item : items) {
                    item.setItem(repo.getItem(item.getItem().getWord(), false));
                    adjustState(item);
                    adjustFlag(item);
                }
            }
            return items;
        });
    }

    private void loadOcrOfCameraImpl(boolean fresh) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsOfCamera())
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                })
                .subscribe(                        result -> {
                    postProgress(false);
//                    postResult(result);
                }, error -> {
                //    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            //repo.toggleFlag(word);
            return getItem(word);
        });
    }

    private Maybe<List<WordItem>> getItemsOfCamera() {
        return Rx2Photo.with(getApplication())
                .requestBitmap(TypeRequest.CAMERA)
                .toFlowable(BackpressureStrategy.LATEST)
                .map(bitmap -> repo.getItemsRx(bitmap).blockingGet())
                .map(this::getItems)
                .firstElement();
    }

    private Maybe<List<WordItem>> getItemsOfImage() {
        return Rx2Photo.with(getApplication())
                .requestBitmap(TypeRequest.GALLERY)
                .toFlowable(BackpressureStrategy.LATEST)
                .map(bitmap -> repo.getItemsRx(bitmap).blockingGet())
                .map(this::getItems)
                .firstElement();
    }

    private Maybe<WordItem> updateItemRx(Word item) {
        return repo.getItemRx(item.getWord(), false).map(this::getItem);
    }

    @DebugLog
    private List<WordItem> getItems(List<Word> items) {
        return Flowable.fromIterable(items)
                .map(this::getItem)
                .toList()
                .blockingGet();
    }

    @DebugLog
    private List<Word> getItemsOf(List<String> items) {
        return Flowable.fromIterable(items)
                .map(s -> repo.getItemOf(s, false))
                .toList()
                .blockingGet();
    }

    private WordItem getItem(Word word) {
        SmartMap<Long, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.getSimpleItem(word);
            map.put(word.getId(), item);
        }
        item.setItem(word);
        adjustState(item);
        adjustFlag(item);
        return item;
    }

    private void adjustState(WordItem item) {
        //List<State> states = repo.getStates(item.getItemRx());
        //Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState()), stateMapper.toSubstate(state.getSubstate())));
    }

    private void adjustFlag(WordItem item) {
       // boolean flagged = repo.isFlagged(item.getItemRx());
        //item.setFlagged(flagged);
    }

    private Maybe<List<Word>> getWordsOfOcr() {
        return storeRepo.getItemsOfRx(ItemType.WORD.name(), ItemSubtype.OCR.name(), ItemState.RAW.name(), Constants.Limit.WORD_OCR)
                .map(this::getItemsOf);
    }

    private Maybe<List<WordItem>> getItemsRx(List<Word> items, boolean fresh) {
        Timber.v("Stored OCR words %d", items.size());
        return Flowable.fromIterable(items)
                .map(this::getItem)
                .toList()
                .toMaybe();
    }
}
