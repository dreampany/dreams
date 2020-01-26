package com.dreampany.framework.ui.vm

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.dreampany.framework.data.enums.*
import com.dreampany.framework.ui.enums.UiMode
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.data.model.Event
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.misc.extension.reObserve
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.network.data.model.Network
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
 *
 * T = Model
 * X = Ui Model Item
 * Y = UiTask<T>
 */
abstract class BaseViewModel<T, X, Y>
protected constructor(
    application: Application,
    protected val rx: RxMapper,
    protected val ex: AppExecutor,
    protected val rm: ResponseMapper
) : AndroidViewModel(application), LifecycleOwner/*, Observer<X>*/ {

    private val lifecycleRegistry: LifecycleRegistry

    var uiMap: SmartMap<String, X>
    var uiCache: SmartCache<String, X>
    var uiFavorites: Set<T>
    var uiSelects: Set<T>

    var titleOwner: LifecycleOwner? = null
    var subtitleOwner: LifecycleOwner? = null
    var uiModeOwner: LifecycleOwner? = null
    var uiStateOwner: LifecycleOwner? = null
    var eventOwner: LifecycleOwner? = null
    var favoriteOwner: LifecycleOwner? = null
    var selectOwner: LifecycleOwner? = null
    var singleOwner: LifecycleOwner? = null
    var multipleOwner: LifecycleOwner? = null
    var singleOwners: MutableList<LifecycleOwner> = mutableListOf()
    var multipleOwners: MutableList<LifecycleOwner> = mutableListOf()
    var multipleOwnersOfString: MutableList<LifecycleOwner> = mutableListOf()
    var multipleOwnersOfNetwork: MutableList<LifecycleOwner> = mutableListOf()

    val disposables: CompositeDisposable
    val ioDisposables: CompositeDisposable
    var singleDisposable: Disposable? = null
    var multipleDisposable: Disposable? = null
    var multipleDisposableOfString: Disposable? = null

    val liveTitle: SingleLiveEvent<String>
    val liveSubtitle: SingleLiveEvent<String>
    val uiMode: SingleLiveEvent<UiMode>
    val uiResponse: SingleLiveEvent<Response.UiResponse>
    val event: SingleLiveEvent<Event>
    val favorite: MutableLiveData<X>
    val select: MutableLiveData<X>
    val input: PublishSubject<Response<X>>
    val inputs: PublishSubject<Response<List<X>>>
    val inputsOfString: PublishSubject<Response<List<String>>>
    val inputsOfNetwork: PublishSubject<Response<List<Network>>>
    val output: MutableLiveData<Response<X>>
    val outputs: MutableLiveData<Response<List<X>>>
    val outputsOfString: MutableLiveData<Response<List<String>>>
    val outputsOfNetwork: MutableLiveData<Response<List<Network>>>
    var task: Y? = null
    var networkEvent: NetworkState
    val itemOffset: Int = 4
    var focus: Boolean = false

    init {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED)
        //register(this)

        disposables = CompositeDisposable()
        ioDisposables = CompositeDisposable()
        uiMode = SingleLiveEvent()
        uiResponse = SingleLiveEvent<Response.UiResponse>()
        event = SingleLiveEvent<Event>()
        liveTitle = SingleLiveEvent<String>()
        liveSubtitle = SingleLiveEvent<String>()
        favorite = MutableLiveData<X>()
        select = MutableLiveData<X>()
        input = PublishSubject.create()
        inputs = PublishSubject.create()
        inputsOfString = PublishSubject.create()
        inputsOfNetwork = PublishSubject.create()
        output = rx.toLiveData(input, ioDisposables)
        outputs = rx.toLiveData(inputs, ioDisposables)
        outputsOfString = rx.toLiveData(inputsOfString, ioDisposables)
        outputsOfNetwork = rx.toLiveData(inputsOfNetwork, ioDisposables)
        uiMap = SmartMap.newMap()
        uiCache = SmartCache.newCache()
        uiFavorites = Collections.synchronizedSet<T>(HashSet<T>())
        uiSelects = Collections.synchronizedSet<T>(HashSet<T>())

        networkEvent = NetworkState.NONE
        uiMode.value = UiMode.MAIN
        uiResponse.value = Response.response(
            Type.DEFAULT,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            UiState.DEFAULT
        )
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

    override fun onCleared() {
        clear()
        ioDisposables.clear()
        super.onCleared()
    }

    open fun clear() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED)
        titleOwner?.let { liveTitle.removeObservers(it) }
        subtitleOwner?.let { liveSubtitle.removeObservers(it) }
        uiModeOwner?.let { uiMode.removeObservers(it) }
        uiStateOwner?.let { uiResponse.removeObservers(it) }
        eventOwner?.let { event.removeObservers(it) }
        favoriteOwner?.let { favorite.removeObservers(it) }
        selectOwner?.let { select.removeObservers(it) }
        singleOwner?.let { output.removeObservers(it) }
        multipleOwner?.let { outputs.removeObservers(it) }
        for (owner in singleOwners) {
            owner.let { output.removeObservers(it) }
        }
        for (owner in multipleOwners) {
            owner.let { outputs.removeObservers(it) }
        }
        for (owner in multipleOwnersOfString) {
            owner.let { outputsOfString.removeObservers(it) }
        }
        for (owner in multipleOwnersOfNetwork) {
            owner.let { outputsOfNetwork.removeObservers(it) }
        }
        singleOwners.clear()
        multipleOwners.clear()
        multipleOwnersOfString.clear()
        multipleOwnersOfNetwork.clear()
        removeSingleSubscription()
        removeMultipleSubscription()
        removeMultipleSubscriptionOfString()
        uiMap.clear()
        uiCache.clear()
        clearUiState()
    }

    open fun clearUiState() {
        updateUiState()
    }

    open fun clearInput() {
        input.onNext(Response.responseEmpty(data = null))
        inputs.onNext(Response.responseEmpty(data = null))
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

    fun onFavorite(t: X?) {
        favorite.value = t
    }

    fun onSelect(t: X?) {
        select.value = t
    }

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

    fun observeUiState(owner: LifecycleOwner, observer: Observer<Response.UiResponse>) {
        uiStateOwner = owner
        uiResponse.reObserve(owner, observer)
    }

    fun observeEvent(owner: LifecycleOwner, observer: Observer<Event>) {
        eventOwner = owner
        event.reObserve(owner, observer)
    }

    fun observeOutput(owner: LifecycleOwner, observer: Observer<Response<X>>) {
        postEmpty(data = null as X)
        singleOwners.add(owner)
        output.reObserve(owner, observer)
    }

    fun observeOutputs(owner: LifecycleOwner, observer: Observer<Response<List<X>>>) {
        postEmpty(data = null as List<X>?)
        multipleOwners.add(owner)
        outputs.reObserve(owner, observer)
    }

    fun observeOutputsOfString(owner: LifecycleOwner, observer: Observer<Response<List<String>>>) {
        multipleOwnersOfString.add(owner)
        outputsOfString.reObserve(owner, observer)
    }

    fun observeOutputsOfNetwork(
        owner: LifecycleOwner,
        observer: Observer<Response<List<Network>>>
    ) {
        multipleOwnersOfNetwork.add(owner)
        outputsOfNetwork.reObserve(owner, observer)
    }

    fun observeFavorite(owner: LifecycleOwner, observer: Observer<X>) {
        favoriteOwner = owner
        favorite.reObserve(owner, observer)
    }

    fun observeSelect(owner: LifecycleOwner, observer: Observer<X>) {
        selectOwner = owner
        select.reObserve(owner, observer)
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

    fun addMultipleSubscriptionOfString(disposable: Disposable) {
        multipleDisposableOfString = disposable
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

    fun removeMultipleSubscriptionOfString() {
        removeSubscription(multipleDisposableOfString)
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

    fun loadTitle() {
        val disposable = rx.backToMain(getTitle()).subscribe({ liveTitle.setValue(it) })
        addSubscription(disposable)
    }

    fun loadSubtitle() {
        val disposable = rx.backToMain(getSubtitle()).subscribe({ liveSubtitle.setValue(it) })
        addSubscription(disposable)
    }

/*    open fun getItemsWithoutId(): Flowable<List<T>>? {
        return null
    }

    open fun getRepeatWhenSeconds(): Int {
        return 0
    }

    fun loads(): Disposable? {
        if (hasMultipleDisposable()) {
            return multipleDisposable
        }
        var items = getItemsWithoutId()
        val repeatWhen = getRepeatWhenSeconds()
        if (items != null) {
            rxMapper.backToMain<List<T>>(items)
            if (repeatWhen != 0) {
                items.repeatWhen({ completed -> completed.delay(10, TimeUnit.SECONDS) })
            }
            items.doOnSubscribe({ subscription -> postProgressMultiple(true) })
            val disposable = items.subscribe({ this.postResultWithProgress(it) }, { this.postFailures(it) })
            addSubscription(disposable)
        }
        return null
    }*/

    fun takeAction(important: Boolean, disposable: Disposable?): Boolean {
        if (important) {
            removeSubscription(disposable)
        }
        if (hasDisposable(disposable)) {
            notifyUiState()
            return false
        }
        return true
    }

    fun updateUiMode(mode: UiMode?) {
        mode?.let {
            uiMode.value = it
        }
    }

    fun updateUiState(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        uiState: UiState = UiState.DEFAULT
    ) {
        this.uiResponse.value = Response.response(type, subtype, state, action, uiState)
    }

    fun notifyUiMode() {
        updateUiMode(uiMode.value)
    }

    fun notifyUiState() {
        uiResponse.value?.run {
            updateUiState(type, subtype, state, action, uiState)
        }
    }

    fun postProgress(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        loading: Boolean
    ) {
        updateUiState(
            type,
            subtype,
            state,
            action,
            if (loading) UiState.SHOW_PROGRESS else UiState.HIDE_PROGRESS
        )
    }

    fun postFailure(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        if (!hasSingleDisposable()) {
        }
        rm.response(input, type, subtype, state, action, error)
    }

    fun postFailure(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable,
        withProgress: Boolean
    ) {
        if (!hasSingleDisposable()) {
        }
        if (withProgress) {
            rm.responseWithProgress(input, type, subtype, state, action, error)
        } else {
            rm.response(input, type, subtype, state, action, error)
        }
    }

    fun postFailures(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        if (!hasMultipleDisposable()) {
        }
        rm.response(inputs, type, subtype, state, action, error)
    }

    fun postFailures(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable,
        withProgress: Boolean
    ) {
        if (!hasMultipleDisposable()) {
        }
        if (withProgress) {
            rm.responseWithProgress(inputs, type, subtype, state, action, error)
        } else {
            rm.response(inputs, type, subtype, state, action, error)
        }
    }

    fun postResult(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT, data: X
    ) {
        rm.response(input, type, subtype, state, action, data)
    }

    fun postResult(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: X,
        withProgress: Boolean
    ) {
        if (withProgress) {
            rm.responseWithProgress(input, type, subtype, state, action, data)
        } else {
            rm.response(input, type, subtype, state, action, data)
        }
    }

    fun postResult(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT, data: List<X>
    ) {
        rm.response(inputs, type, subtype, state, action, data)
    }

    fun postResultOfString(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: List<String>
    ) {
        rm.response(inputsOfString, type, subtype, state, action, data)
    }

    fun postResult(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: List<X>,
        withProgress: Boolean
    ) {
        if (withProgress) {
            rm.responseWithProgress(inputs, type, subtype, state, action, data)
        } else {
            rm.response(inputs, type, subtype, state, action, data)
        }
    }

    fun postEmpty(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT, data: X?
    ) {
        rm.responseEmpty(input, type, subtype, state, action, data)
    }

    fun postEmpty(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: X?,
        withProgress: Boolean
    ) {
        if (withProgress) {
            rm.responseEmptyWithProgress(input, type, subtype, state, action, data)
        } else {
            rm.responseEmpty(input, type, subtype, state, action, data)
        }
    }

    fun postEmpty(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT, data: List<X>?
    ) {
        rm.responseEmpty(inputs, type, subtype, state, action, data)
    }

    fun postEmpty(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: List<X>?,
        withProgress: Boolean
    ) {
        if (withProgress) {
            rm.responseEmptyWithProgress(inputs, type, subtype, state, action, data)
        } else {
            rm.responseEmpty(inputs, type, subtype, state, action, data)
        }
    }

    fun postFavorite(data: X) {
        favorite.value = data
    }

    fun processProgress(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        loading: Boolean
    ) {
        if (loading) {
            updateUiState(type, subtype, state, action, UiState.SHOW_PROGRESS)
        } else {
            updateUiState(type, subtype, state, action, UiState.HIDE_PROGRESS)
        }
    }

    fun processFailure(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        if (error is IOException || error.cause is IOException) {
            updateUiState(type, subtype, state, action, UiState.OFFLINE)
        } else if (error is EmptyException) {
            updateUiState(type, subtype, state, action, UiState.EMPTY)
        } else if (error is ExtraException) {
            updateUiState(type, subtype, state, action, UiState.EXTRA)
        } else if (error is MultiException) {
            for (e in error.errors) {
                processFailure(type, subtype, state, action, e)
            }
        }
    }

    fun speak(text: String) {
        AndroidUtil.speak(text);
    }
}