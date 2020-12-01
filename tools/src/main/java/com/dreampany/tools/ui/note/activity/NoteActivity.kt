package com.dreampany.tools.ui.note.activity

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SimpleTextWatcher
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.misc.util.NotifyUtil
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.databinding.NoteActivityBinding
import com.dreampany.tools.manager.AdsManager
import com.dreampany.tools.ui.note.model.NoteItem
import com.dreampany.tools.ui.note.vm.NoteViewModel
import timber.log.Timber
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteActivity : InjectActivity() {
    @Inject
    internal lateinit var ads: AdsManager

    private lateinit var bind: NoteActivityBinding
    private lateinit var vm: NoteViewModel

    private var action: Action = Action.DEFAULT
    private var input: Note? = null

    private var changed: Boolean = false
    private var state: State = State.DEFAULT

    private var noteTitle: String = Constant.Default.STRING
    private var noteDescription: String = Constant.Default.STRING

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.note_activity
    override val menuRes: Int = R.menu.menu_note
    override val toolbarId: Int = R.id.toolbar

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, Constant.Param.screen(this))

            params.put(Constant.Event.key(this), param)
            return params
        }

    private val isEdit: Boolean
        get() {
            return action == Action.ADD || action == Action.EDIT
        }

    override fun onStartUi(state: Bundle?) {
        val task =
            (task ?: return) as UiTask<Type, Subtype, State, Action, Note>
        action = task.action
        input = task.input
        initUi()
        initAd()
        input?.run {
            ex.postToUi({
                vm.loadNote(id)
            })
        }
        ads.loadBanner(this.javaClass.simpleName)
        ads.showInHouseAds(this)
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        ads.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ads.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onMenuCreated(menu: Menu) {
        val favItem = findMenuItemById(R.id.item_favorite)
        val editItem = findMenuItemById(R.id.item_edit)
        val doneItem = findMenuItemById(R.id.item_done)

        favItem?.isVisible = action != Action.ADD
        editItem?.isVisible = !isEdit
        //doneItem?.isVisible = isEdit
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorite -> {
                input?.let { vm.toggleFavorite(it) }
                return true
            }
            R.id.item_edit -> {
                switchToEdit()
                return true
            }
            R.id.item_done -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isEdit) {
            if (changed) {
                saveDialog()
                return
            }
            if (state == State.ADDED || state == State.EDITED) {
                val currentTask = (task ?: return) as UiTask<Type, Subtype, State, Action, Note>
                val task = UiTask(
                    currentTask.type,
                    currentTask.subtype,
                    state,
                    currentTask.action,
                    input
                )
                close(task)
            }
            close()
            return
        }
        super.onBackPressed()
    }

    private fun initAd() {
        ads.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        vm = createVm(NoteViewModel::class)

        vm.subscribe(this, { this.processResponse(it) })

        val titleRes =
            if (action == Action.ADD) R.string.title_add_note else R.string.title_edit_note
        setTitle(titleRes)

        input?.title?.let { noteTitle = it }
        input?.description?.let { noteDescription = it }

        bind.layoutNote.inputEditTitle.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                if (noteTitle != text.trimValue) {
                    changed = true
                }
                noteTitle = text.trimValue
            }
        })
        bind.layoutNote.inputEditDescription.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                if (noteDescription != text.trimValue) {
                    changed = true
                }
                noteDescription = text.trimValue
            }
        })
        resolveUi()
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, NoteItem>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, NoteItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.state, response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResult(state: State, result: NoteItem?) {
        if (result == null) return
        this.state = state
        input = result.input
        changed = false
        when (state) {
            State.FAVORITE -> {
                findMenuItemById(R.id.item_favorite)?.let {
                    it.setIcon(result.favoriteRes)
                    it.toTint(this, R.color.material_white)
                }
            }
            State.LOADED -> {
                if (isEdit) {
                    bind.layoutNote.inputEditTitle.setText(result.input.title)
                    bind.layoutNote.inputEditDescription.setText(result.input.description)
                } else {
                    bind.layoutNote.textTitle.setText(result.input.title)
                    bind.layoutNote.textDescription.setText(result.input.description)
                }
                findMenuItemById(R.id.item_favorite)?.let {
                    it.setIcon(result.favoriteRes)
                    it.toTint(this, R.color.material_white)
                }
            }
            State.ADDED,
            State.EDITED -> {
                NotifyUtil.shortToast(this, R.string.dialog_saved_note)
                hideKeyboard()
                input = result.input
            }
        }
    }

    private fun resolveUi() {
        if (isEdit) {
            bind.layoutNote.layoutView.gone()
            bind.layoutNote.layoutEdit.visible()
        } else {
            bind.layoutNote.layoutEdit.gone()
            bind.layoutNote.layoutView.visible()
        }
    }

    private fun switchToEdit() {
        if (action == Action.EDIT) return
        action = Action.EDIT
        bind.layoutNote.inputEditTitle.setText(input?.title)
        bind.layoutNote.inputEditDescription.setText(input?.description)
        resolveUi()
    }

    private fun saveNote(): Boolean {
        if (noteTitle.isEmpty()) {
            bind.layoutNote.inputEditTitle.error = getString(R.string.error_title_note)
            return false
        }
        if (noteDescription.isEmpty()) {
            bind.layoutNote.inputEditDescription.error = getString(R.string.error_description_note)
            return false
        }
        vm.saveNote(input?.id, noteTitle, noteDescription)
        return true
    }

    private fun saveDialog() {
        showDialogue(
            R.string.dialog_title_save_note,
            onPositiveClick = {
                saveNote()
            },
            onNegativeClick = {
                onBackPressed()
            }
        )
    }

    private fun onFavoriteClicked(item: NoteItem) {
        vm.toggleFavorite(item.input)
    }
}