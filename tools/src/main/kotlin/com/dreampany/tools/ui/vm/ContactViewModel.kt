package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.api.theme.ColorManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.framework.util.DataUtilKt
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.ContactMapper
import com.dreampany.tools.data.model.Contact
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.BlockPref
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.ContactRepository
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.misc.ContactRequest
import com.dreampany.tools.ui.misc.WordRequest
import com.dreampany.tools.ui.model.ContactItem
import com.dreampany.tools.ui.model.WordItem
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ContactViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val colors: ColorManager,
    private val network: NetworkManager,
    private val pref: Pref,
    private val blockPref: BlockPref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: ContactMapper,
    private val repo: ContactRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Contact, ContactItem, UiTask<Contact>>(application, rx, ex, rm),
    NetworkManager.Callback {

    private val blocks: SmartMap<String, Boolean>

    init {
        blocks = SmartMap.newMap()
    }

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: ContactRequest) {
        if (request.single) {
            requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestSingle(request: ContactRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(state = request.state,
                        action = request.action,
                        loading = true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(state = request.state,
                        action = request.action,
                        loading = false)
                }
                postResult(state = request.state,
                    action = request.action,
                    data = result)
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state,
                        action = request.action,
                        loading = false)
                }
                postFailures(state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    private fun requestMultiple(request: ContactRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemsRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(state = request.state,
                        action = request.action,
                        loading = true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(state = request.state,
                        action = request.action,
                        loading = false)
                }
                postResult(state = request.state,
                    action = request.action,
                    data = result)
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state,
                        action = request.action,
                        loading = false)
                }
                postFailures(state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemRx(request: ContactRequest): Maybe<ContactItem> {
        return requestItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun requestUiItemsRx(request: ContactRequest): Maybe<List<ContactItem>> {
        //if (true) return getDummy()
        if (request.state == State.BLOCKED) {
            return storeRepo
                .getItemsRx(Type.CONTACT, Subtype.DEFAULT, State.BLOCKED)
                .flatMap { getUiItemsOfStoresRx(request, it) }
        }
        return repo.getItemsRx().flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemRx(request: ContactRequest): Maybe<Contact> {
        if (request.action == Action.BLOCK || request.action == Action.UNBLOCK) {
            return toggleBlock(request)
        }
        return repo.getItemRx(request.id!!)
    }

    private fun toggleBlock(request: ContactRequest): Maybe<Contact> {
        return Maybe.create { emitter ->
            var contact: Contact? = request.input
            if (contact == null) {
                contact = mapper.getItem(request.countryCode, request.phoneNumber, repo)
            }
            if (contact != null) {
                val blocked = toggleBlock(contact.id)
            }
            if (emitter.isDisposed) return@create
            if (contact == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(contact)
            }
        }
    }

    /* get methods */
    private fun getUiItemRx(request: ContactRequest, item: Contact): Maybe<ContactItem> {
        return Maybe.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            val uiItem = getUiItem(request, item)
            emitter.onSuccess(uiItem)
        }
    }

    private fun getUiItemsOfStoresRx(
        request: ContactRequest,
        items: List<Store>
    ): Maybe<List<ContactItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItemsRx(
        request: ContactRequest,
        items: List<Contact>
    ): Maybe<List<ContactItem>> {
        Timber.v("For UI items %d", items.size)
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: ContactRequest, item: Contact): ContactItem {
        var uiItem: ContactItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = ContactItem.getItem(colors, item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        adjustFavorite(item, uiItem)
        adjustBlock(item, uiItem)
        return uiItem
    }

    private fun getUiItem(request: ContactRequest, store: Store): ContactItem {
        val contact = mapper.getItem(store, repo)
        val item = getUiItem(request, contact!!)
        item.time = store.time
        return item
    }

    private fun adjustFavorite(contact: Contact, item: ContactItem) {
        item.favorite = isFavorite(contact)
    }

    private fun adjustBlock(contact: Contact, item: ContactItem) {
        item.block = isBlocked(contact)
    }

    private fun toggleFavorite(id: String): Boolean {
        val favorite = hasStore(id, Type.CONTACT, Subtype.DEFAULT, State.FAVORITE)
        if (favorite) {
            removeStore(id, Type.CONTACT, Subtype.DEFAULT, State.FAVORITE)
            favorites.put(id, false)
        } else {
            putStore(id, Type.CONTACT, Subtype.DEFAULT, State.FAVORITE)
            favorites.put(id, true)
        }
        return favorites.get(id)
    }

    private fun toggleBlock(id: String): Boolean {
        val blocked = hasStore(id, Type.CONTACT, Subtype.DEFAULT, State.BLOCKED)
        if (blocked) {
            removeStore(id, Type.CONTACT, Subtype.DEFAULT, State.BLOCKED)
            return false
        } else {
            putStore(id, Type.CONTACT, Subtype.DEFAULT, State.BLOCKED)
            return true
        }
    }

    private fun isFavorite(contact: Contact): Boolean {
        Timber.v("Checking favorite")
        if (!favorites.contains(contact.id)) {
            val favorite = hasStore(contact.id, Type.CONTACT, Subtype.DEFAULT, State.FAVORITE)
            Timber.v("Favorite of %s %s", contact.id, favorite)
            favorites.put(contact.id, favorite)
        }
        return favorites.get(contact.id)
    }

    private fun isBlocked(contact: Contact): Boolean {
        Timber.v("Checking Block Status")
        if (!blocks.contains(contact.id)) {
            val blocked = hasStore(contact.id, Type.CONTACT, Subtype.DEFAULT, State.BLOCKED)
            Timber.v("Block of [%s - %s]", contact.id, blocked)
            blocks.put(contact.id, blocked)
        }
        return blocks.get(contact.id)
    }

    private fun hasStore(id: String, type: Type, subtype: Subtype, state: State): Boolean {
        return storeRepo.isExists(id, type, subtype, state)
    }

    private fun putStore(id: String, type: Type, subtype: Subtype, state: State): Long {
        val store = storeMapper.getItem(id, type, subtype, state)
        return storeRepo.putItem(store)
    }

    private fun removeStore(id: String, type: Type, subtype: Subtype, state: State): Int {
        val store = storeMapper.getItem(id, type, subtype, state)
        return storeRepo.delete(store)
    }

/*    private fun getDummy(): Maybe<List<ContactItem>> {
        return Maybe.create { emitter ->
            val list = arrayListOf<ContactItem>()
            val contact = Contact(DataUtilKt.getRandId())
            list.add(ContactItem.getItem(contact))
            emitter.onSuccess(list)
        }
    }*/
}