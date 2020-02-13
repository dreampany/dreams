package com.dreampany.tools.ui.vm.resume

import android.app.Application
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.model.resume.*
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.ResumeRepository
import com.dreampany.tools.ui.misc.ResumeRequest
import com.dreampany.tools.ui.model.resume.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResumeViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: ResumeMapper,
    private val repo: ResumeRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Resume, ResumeItem, UiTask<Resume>>(application, rx, ex, rm),
    NetworkManager.Callback {

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: ResumeRequest) {
        if (request.single) {
            requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestSingle(request: ResumeRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }
        val disposable = rx
            .backToMain(requestUiItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(
                        type = request.type,
                        subtype = request.subtype,
                        state = request.state,
                        action = request.action,
                        loading = true
                    )
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(
                        type = request.type,
                        subtype = request.subtype,
                        state = request.state,
                        action = request.action,
                        loading = false
                    )
                }
                postResult(
                    type = request.type, subtype = request.subtype,
                    state = request.state,
                    action = request.action,
                    data = result
                )
            }, { error ->
                if (request.progress) {
                    postProgress(
                        type = request.type,
                        subtype = request.subtype,
                        state = request.state,
                        action = request.action,
                        loading = false
                    )
                }
                postFailures(
                    type = request.type, subtype = request.subtype,
                    state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addSingleSubscription(disposable)
    }

    private fun requestMultiple(request: ResumeRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemsRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(
                        type = request.type,
                        subtype = request.subtype,
                        state = request.state,
                        action = request.action,
                        loading = true
                    )
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(
                        type = request.type,
                        subtype = request.subtype,
                        state = request.state,
                        action = request.action,
                        loading = false
                    )
                }
                postResult(
                    type = request.type, subtype = request.subtype, state = request.state,
                    action = request.action,
                    data = result
                )
            }, { error ->
                if (request.progress) {
                    postProgress(
                        type = request.type,
                        subtype = request.subtype,
                        state = request.state,
                        action = request.action,
                        loading = false
                    )
                }
                postFailures(
                    type = request.type, subtype = request.subtype,
                    state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemRx(request: ResumeRequest): Maybe<ResumeItem> {
        if (request.state == State.UI && request.action == Action.GET) {
            request.input?.let {
                return getUiItemRx(request, it)
            }
        }

        when (request.action) {
            Action.ADD -> {
                return addItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.EDIT -> {
                return editItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            /*
            Action.FAVORITE -> {
                return favoriteItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.ARCHIVE -> {
                return archiveItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.TRASH -> {
                return trashItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.DELETE -> {
                return deleteItemRx(request).flatMap { getUiItemRx(request, it) }
            }*/
        }
        return getItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun requestUiItemsRx(request: ResumeRequest): Maybe<List<ResumeItem>> {
/*        if (request.action == Action.FAVORITE) {
            return storeRepo
                .getItemsRx(Type.SERVER, Subtype.DEFAULT, State.FAVORITE)
                .flatMap { getUiItemsOfStoresRx(request, it) }
        }*/
        return requestItemsRx(request).flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemsRx(request: ResumeRequest): Maybe<List<Resume>> {
        return repo.getItemsRx()
    }

    private fun getItemRx(request: ResumeRequest): Maybe<Resume> {
        return repo.getItemRx(request.id!!)
    }

    private fun addItemRx(request: ResumeRequest): Maybe<Resume> {
        return Maybe.create { emitter ->
            // need to add only skill
            var resume: Resume? = null
            if (request.subtype == Subtype.SKILL) {
                resume = request.input
                resume?.run {
                    mapper.applySkills(this, request.skills)
                }
            } else if (request.subtype == Subtype.EXPERIENCE) {
                resume = request.input
                resume?.run {
                    mapper.applyExperiences(this, request.experiences)
                }
            } else if (request.subtype == Subtype.PROJECT) {
                resume = request.input
                resume?.run {
                    mapper.applyProjects(this, request.projects)
                }
            } else if (request.subtype == Subtype.SCHOOL) {
                resume = request.input
                resume?.run {
                    mapper.applySchools(this, request.schools)
                }
            } else {
                resume = mapper.getItem(
                    request.id,
                    profile = request.profile,
                    skills = request.skills,
                    experiences = request.experiences,
                    projects = request.projects,
                    schools = request.schools
                )
            }
            if (resume == null) {
                emitter.onError(EmptyException())
            } else {
                repo.putItem(resume)
                emitter.onSuccess(resume)
            }
        }
    }

    private fun editItemRx(request: ResumeRequest): Maybe<Resume> {
        return Maybe.create { emitter ->
            val resume: Resume? = /*if (request.state == State.UI) request.input else */
                mapper.getItem(
                    request.input?.id,
                    profile = request.profile,
                    skills = request.skills,
                    experiences = request.experiences,
                    projects = request.projects,
                    schools = request.schools
                )
            if (resume == null) {
                emitter.onError(EmptyException())
            } else {
                repo.putItem(resume)
                emitter.onSuccess(resume)
            }
        }
    }

    private fun getUiItemRx(request: ResumeRequest, item: Resume): Maybe<ResumeItem> {
        return Maybe.create { emitter ->
            /*            if (request.action == Action.FAVORITE) {
                            toggleFavorite(item.id)
                        }*/
            val uiItem = getUiItem(request, item)
            item.skills?.forEach {
                getUiItem(request, it).run {
                    if (!uiItem.skills.contains(this)) {
                        uiItem.skills.add(this)
                    }
                }
            }
            item.experiences?.forEach {
                getUiItem(request, it).run {
                    if (!uiItem.experiences.contains(this)) {
                        uiItem.experiences.add(this)
                    }
                }
            }
            item.projects?.forEach {
                getUiItem(request, it).run {
                    if (!uiItem.projects.contains(this)) {
                        uiItem.projects.add(this)
                    }
                }
            }
            item.schools?.forEach {
                getUiItem(request, it).run {
                    if (!uiItem.schools.contains(this)) {
                        uiItem.schools.add(this)
                    }
                }
            }
            emitter.onSuccess(uiItem)
        }
    }

    private fun getUiItemsRx(
        request: ResumeRequest,
        items: List<Resume>
    ): Maybe<List<ResumeItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: ResumeRequest, item: Resume): ResumeItem {
        var uiItem: ResumeItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = ResumeItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        //adjustTranslate(request, uiItem)
        return uiItem
    }

    private fun getUiItem(request: ResumeRequest, item: Skill): SkillItem {
        var uiItem: SkillItem? = mapper.getSkillUiItem(item.id)
        if (uiItem == null) {
            uiItem = SkillItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        //adjustTranslate(request, uiItem)
        return uiItem
    }

    private fun getUiItem(request: ResumeRequest, item: Experience): ExperienceItem {
        var uiItem: ExperienceItem? = mapper.getExperienceUiItem(item.id)
        if (uiItem == null) {
            uiItem = ExperienceItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        //adjustTranslate(request, uiItem)
        return uiItem
    }

    private fun getUiItem(request: ResumeRequest, item: Project): ProjectItem {
        var uiItem: ProjectItem? = mapper.getProjectUiItem(item.id)
        if (uiItem == null) {
            uiItem = ProjectItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        //adjustTranslate(request, uiItem)
        return uiItem
    }

    private fun getUiItem(request: ResumeRequest, item: School): SchoolItem {
        var uiItem: SchoolItem? = mapper.getSchoolUiItem(item.id)
        if (uiItem == null) {
            uiItem = SchoolItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        //adjustTranslate(request, uiItem)
        return uiItem
    }
}