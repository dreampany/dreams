package com.dreampany.frame.vm

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.dreampany.frame.data.enums.Event
import com.dreampany.frame.data.enums.NetworkState
import com.dreampany.frame.data.enums.UiMode
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.util.AndroidUtil
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.io.IOException
import java.util.*

/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
/**
 * B2 - B1 = Added
 * B1 - B2 = Removed
 * B1 - Removed = Updated
 */
abstract class BaseViewModel<T, X, Y> protected constructor(
    application: Application,
    protected val rx: RxMapper,
    protected val ex: AppExecutors,
    protected val rm: ResponseMapper
) : AndroidViewModel(application), LifecycleOwner/*, Observer<X>*/ {

    private val lifecycleRegistry: LifecycleRegistry

    var uiMap: SmartMap<Long, X>
    var uiCache: SmartCache<Long, X>
    var uiFavorites: Set<T>
    var uiSelects: Set<T>

    var titleOwner: LifecycleOwner? = null
    var subtitleOwner: LifecycleOwner? = null
    var uiModeOwner: LifecycleOwner? = null
    var uiStateOwner: LifecycleOwner? = null
    var eventOwner: LifecycleOwner? = null
    var favoriteOwner: LifecycleOwner? = null
    var singleOwner: LifecycleOwner? = null
    var multipleOwner: LifecycleOwner? = null
    var singleOwners: MutableList<LifecycleOwner> = mutableListOf()
    var multipleOwners: MutableList<LifecycleOwner> = mutableListOf()

    val disposables: CompositeDisposable
    val ioDisposables: CompositeDisposable
    var singleDisposable: Disposable? = null
    var multipleDisposable: Disposable? = null

    val uiMode: SingleLiveEvent<UiMode>
    val uiState: SingleLiveEvent<UiState>
    val event: SingleLiveEvent<Event>
    val favorite: MutableLiveData<X>
    val input: PublishSubject<Response<X>>
    val inputs: PublishSubject<Response<List<X>>>
    val output: MutableLiveData<Response<X>>
    val outputs: MutableLiveData<Response<List<X>>>
    var task: Y? = null
    val liveTitle: SingleLiveEvent<String>
    val liveSubtitle: SingleLiveEvent<String>
    var networkEvent: NetworkState
    val itemOffset: Int = 4
    var focus: Boolean = false

    init {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED);
        //register(this)

        disposables = CompositeDisposable()
        ioDisposables = CompositeDisposable()
        uiMode = SingleLiveEvent()
        uiState = SingleLiveEvent()
        event = SingleLiveEvent()
        liveTitle = SingleLiveEvent()
        liveSubtitle = SingleLiveEvent()
        favorite = MutableLiveData()
        input = PublishSubject.create()
        inputs = PublishSubject.create()
        output = rx.toLiveData(input, ioDisposables)
        outputs = rx.toLiveData(inputs, ioDisposables)
        uiMap = SmartMap.newMap()
        uiCache = SmartCache.newCache()
        uiFavorites = Collections.synchronizedSet<T>(HashSet<T>())
        uiSelects = Collections.synchronizedSet<T>(HashSet<T>())

        networkEvent = NetworkState.NONE
        uiMode.value = UiMode.MAIN
        uiState.value = UiState.NONE
        uiMap.clear()
        uiCache.clear()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    protected open fun getTitle(): Maybe<String> {
        TODO("not implemented")
    }

    protected open fun getSubtitle(): Maybe<String> {
        TODO("not implemented")
    }

/*    protected open fun hasNetCheck(): Boolean {
        return false
    }

    protected open fun onNetworkEvent(event: NetworkState) {
    }*/

    override fun onCleared() {
        clear()
        ioDisposables.clear()
        super.onCleared()
    }

/*    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(networkEvent: NetworkState) {
        if (this.networkEvent != networkEvent) {
            this.networkEvent = networkEvent;
            onNetworkEvent(networkEvent)
            updateUiState(if (networkEvent == NetworkState.ONLINE) UiState.ONLINE else UiState.OFFLINE)
            // this.event.postValue(event)
        }
    }*/

/*    fun processEvent(event: Event?) {

    }*/

    open fun clear() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
        //unregister(this)
        titleOwner?.let { liveTitle.removeObservers(it) }
        subtitleOwner?.let { liveSubtitle.removeObservers(it) }
        uiModeOwner?.let { uiMode.removeObservers(it) }
        uiStateOwner?.let { uiState.removeObservers(it) }
        eventOwner?.let { event.removeObservers(it) }
        favoriteOwner?.let { favorite.removeObservers(it) }
        singleOwner?.let { output.removeObservers(it) }
        multipleOwner?.let { outputs.removeObservers(it) }
        for (owner in singleOwners) {
            owner.let { output.removeObservers(it) }
        }
        for (owner in multipleOwners) {
            owner.let { outputs.removeObservers(it) }
        }
        singleOwners.clear()
        multipleOwners.clear()
        removeSingleSubscription()
        removeMultipleSubscription()
        uiMap.clear()
        uiCache.clear()
        clearUiState()
        //postEmpty(null as X?)
        //postEmpty(null as List<X>?)
    }

    open fun clearUiState() {
        updateUiState(UiState.NONE)
    }

    open fun clearInput() {
        input.onNext(Response.responseEmpty(null))
        inputs.onNext(Response.responseEmpty(null))
    }

    open fun clearOutput() {
        output.value = null
        outputs.value = null
    }

    fun hasSelection(): Boolean {
        return uiSelects.isNotEmpty()
    }

    open fun hasFocus(): Boolean {
        return focus
    }

    fun onFlag(t: X?) {
        favorite.value = t
    }

/*    override fun onChanged(t: X?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/

    fun observeTitle(owner: LifecycleOwner, observer: Observer<String>) {
        titleOwner = owner
        liveTitle.observe(owner, observer)
    }

    fun observeSubtitle(owner: LifecycleOwner, observer: Observer<String>) {
        subtitleOwner = owner
        liveSubtitle.observe(owner, observer)
    }

    fun <T> observe(owner: LifecycleOwner, observer: Observer<T>, event: SingleLiveEvent<T>) {
        event.reObserve(owner, observer)
    }

    fun <T> observe(owner: LifecycleOwner, observer: Observer<T>, event: MutableLiveData<T>) {
        event.reObserve(owner, observer)
    }

    fun observeUiMode(owner: LifecycleOwner, observer: Observer<UiMode>) {
        uiModeOwner = owner
        uiMode.reObserve(owner, observer)
    }

    fun observeUiState(owner: LifecycleOwner, observer: Observer<UiState>) {
        uiStateOwner = owner
        uiState.reObserve(owner, observer)
    }

/*    fun observeEvent() {
        observeEvent(this, Observer { processEvent(it)  })
    }*/

    fun observeEvent(owner: LifecycleOwner, observer: Observer<Event>) {
        eventOwner = owner
        event.reObserve(owner, observer)
    }

    fun observeOutput(owner: LifecycleOwner, observer: Observer<Response<X>>) {
        singleOwners.add(owner)
        output.reObserve(owner, observer)
    }

    fun observeOutputs(owner: LifecycleOwner, observer: Observer<Response<List<X>>>) {
        multipleOwners.add(owner)
        outputs.reObserve(owner, observer)
    }

    fun observeFlag(owner: LifecycleOwner, observer: Observer<X>) {
        favoriteOwner = owner
        favorite.reObserve(owner, observer)
    }

    fun hasDisposable(disposable: Disposable?): Boolean {
        return disposable != null && !disposable.isDisposed
    }

    fun hasSingleDisposable(): Boolean {
        return hasDisposable(singleDisposable)
    }

    fun hasMultipleDisposable(): Boolean {
        return hasDisposable(multipleDisposable)
    }

    fun addSingleSubscription(disposable: Disposable) {
        singleDisposable = disposable
        addSubscription(disposable)
    }

    fun addMultipleSubscription(disposable: Disposable) {
        multipleDisposable = disposable
        addSubscription(disposable)
    }

    fun addSubscription(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun addSubscription(vararg disposables: Disposable) {
        this.disposables.addAll(*disposables)
    }

    fun removeSingleSubscription() {
        removeSubscription(singleDisposable)
    }

    fun removeMultipleSubscription() {
        removeSubscription(multipleDisposable)
    }

    fun removeSubscription(disposable: Disposable?): Boolean {
        disposable?.let {
            if (it.isDisposed) {
                return this.disposables.delete(it)
            } else {
                return this.disposables.remove(it)
            }
        }
        return false;
    }

/*    fun removeSubscription(vararg disposables: Disposable) {
        for (disposable in disposables) {
            removeSubscription(disposable)
        }
    }*/

    fun loadTitle() {
        val disposable = rx.backToMain(getTitle()).subscribe({ liveTitle.setValue(it) })
        addSubscription(disposable)
    }

    fun loadSubtitle() {
        val disposable = rx.backToMain(getSubtitle()).subscribe({ liveSubtitle.setValue(it) })
        addSubscription(disposable)
    }

/*    open fun getItems(): Flowable<List<T>>? {
        return null
    }

    open fun getRepeatWhenSeconds(): Int {
        return 0
    }

    fun loads(): Disposable? {
        if (hasMultipleDisposable()) {
            return multipleDisposable
        }
        var items = getItems()
        val repeatWhen = getRepeatWhenSeconds()
        if (items != null) {
            rxMapper.backToMain<List<T>>(items)
            if (repeatWhen != 0) {
                items.repeatWhen({ completed -> completed.delay(10, TimeUnit.SECONDS) })
            }
            items.doOnSubscribe({ subscription -> postProgressMultiple(true) })
            val disposable = items.subscribe({ this.postResultWithProgress(it) }, { this.postFailureMultiple(it) })
            addSubscription(disposable)
        }
        return null
    }*/

    fun preLoad(fresh: Boolean): Boolean {
        if (fresh) {
            removeSingleSubscription()
        }
        if (hasSingleDisposable()) {
            notifyUiState()
            return false
        }
        return true
    }

    fun preLoads(fresh: Boolean): Boolean {
        if (fresh) {
            removeMultipleSubscription()
        }
        if (hasMultipleDisposable()) {
            notifyUiState()
            return false
        }
        return true
    }

    fun updateUiMode(mode: UiMode?) {
        mode?.let {
            // if (it != uiMode.value) {
            uiMode.value = it
            // }
        }
    }

    fun updateUiState(state: UiState?) {
        state?.let {
            //if (it != UiState.NONE) {
                uiState.value = it
            //}
        }
    }

    fun notifyUiMode() {
        updateUiMode(uiMode.value)
    }

    fun notifyUiState() {
        updateUiState(uiState.value)
    }

/*    fun notifyNetworkState() {
        val state = network.state
        val uiState = if (state == NetworkState.ONLINE) UiState.ONLINE else UiState.OFFLINE
        updateUiState(uiState)
    }*/

    fun postProgress(loading: Boolean) {
        updateUiState(if (loading) UiState.SHOW_PROGRESS else UiState.HIDE_PROGRESS)
    }

    fun processFailure(error: Throwable) {
        if (error is IOException || error.cause is IOException) {
            updateUiState(UiState.OFFLINE)
        } else if (error is EmptyException) {
            updateUiState(UiState.EMPTY)
        } else if (error is ExtraException) {
            updateUiState(UiState.EXTRA)
        } else if (error is MultiException) {
            for (e in error.errors) {
                processFailure(e)
            }
        }
    }

    fun postFailure(error: Throwable) {
        if (!hasSingleDisposable()) {
            //return
        }
        rm.responseWithProgress(input, error)
    }

    fun postFailureMultiple(error: Throwable) {
        if (!hasMultipleDisposable()) {
            //return
        }
        rm.responseWithProgress(inputs, error)
    }

    fun postResult(data: X) {
        rm.response(input, data)
    }

    fun postResult(data: X, withProgress: Boolean) {
        if (withProgress) {
            rm.responseWithProgress(input, data)
        } else {
            rm.response(input, data)
        }
    }

    fun postResult(data: List<X>) {
        rm.response(inputs, data)
    }

    fun postResult(data: List<X>, withProgress: Boolean) {
        if (withProgress) {
            rm.responseWithProgress(inputs, data)
        } else {
            rm.response(inputs, data)
        }
    }

    fun postEmpty(data: X?) {
        rm.responseEmpty(input, data)
    }

    fun postEmpty(data: X?, withProgress: Boolean) {
        if (withProgress) {
            rm.responseEmptyWithProgress(input, data)
        } else {
            rm.responseEmpty(input, data)
        }
    }

    fun postEmpty(data: List<X>?) {
        rm.responseEmpty(inputs, data)
    }

    fun postEmpty(data: List<X>?, withProgress: Boolean) {
        if (withProgress) {
            rm.responseEmptyWithProgress(inputs, data)
        } else {
            rm.responseEmpty(inputs, data)
        }
    }

    fun postFavorite(data: X) {
        favorite.value = data
    }

    fun speak(text: String) {
        AndroidUtil.speak(text);
    }

/*    private fun register(subscriber: Any) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber)
        }
    }

    private fun unregister(subscriber: Any) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }*/

/*    fun post(event: Any) {
        EventBus.getDefault().post(event)
    }*/
}