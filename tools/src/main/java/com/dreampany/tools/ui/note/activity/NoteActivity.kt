package com.dreampany.tools.ui.note.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.misc.func.SimpleTextWatcher
import com.dreampany.framework.misc.util.NotifyUtil
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.note.NoteAction
import com.dreampany.tools.data.enums.note.NoteState
import com.dreampany.tools.data.enums.note.NoteSubtype
import com.dreampany.tools.data.enums.note.NoteType
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.data.source.note.pref.NotePref
import com.dreampany.tools.databinding.NoteActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.note.model.NoteItem
import com.dreampany.tools.ui.note.vm.NoteViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteActivity : InjectActivity() {
    @Inject
    internal lateinit var ad: AdManager

    @Inject
    internal lateinit var notePref: NotePref

    private lateinit var bind: NoteActivityBinding
    private lateinit var vm: NoteViewModel

    private var action: NoteAction = NoteAction.DEFAULT
    private var input: Note? = null

    private var changed: Boolean = false
    private var state: NoteState = NoteState.DEFAULT

    private var noteTitle: String = Constant.Default.STRING
    private var noteDescription: String = Constant.Default.STRING

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.note_activity
    override val menuRes: Int = R.menu.menu_note
    override val toolbarId: Int = R.id.toolbar

    private val isEdit: Boolean
        get() {
            return action == NoteAction.ADD || action == NoteAction.EDIT
        }

    override fun onStartUi(state: Bundle?) {
        resolveTask()
        initUi()
        initAd()
        input?.run {
            ex.postToUi(Runnable {
                vm.loadNote(id)
            })
        }
        ad.loadBanner(this.javaClass.simpleName)
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ad.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onMenuCreated(menu: Menu) {
        val favItem = findMenuItemById(R.id.item_favorite)
        val editItem = findMenuItemById(R.id.item_edit)
        val doneItem = findMenuItemById(R.id.item_done)
        //resolveTask()
        favItem?.isVisible = action != NoteAction.ADD
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
            if (state == NoteState.ADDED || state == NoteState.EDITED) {
                val currentTask: UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note> =
                    (task ?: return) as UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note>
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

    private fun resolveTask() {
        //if (::action.isInitialized) return
        val task: UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note> =
            (task ?: return) as UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note>
        action = task.action
        input = task.input ?: return
    }

    private fun onItemPressed(view: View, item: NoteItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openUi(item)
            }
            R.id.button_favorite -> {
                onFavoriteClicked(item)
            }
            else -> {

            }
        }
    }

    private fun initAd() {
        ad.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun initUi() {
        bind = getBinding()

        vm = createVm(NoteViewModel::class)
        vm.subscribe(this, Observer { this.processResponse(it) })

        val titleRes =
            if (action == NoteAction.ADD) R.string.title_add_note else R.string.title_edit_note
        setTitle(titleRes)

        input?.title?.let { noteTitle = it }
        input?.description?.let { noteDescription = it }

        bind.layoutNote.inputEditTitle.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (noteTitle != s.trimValue) {
                    changed = true
                }
                noteTitle = s.trimValue
            }

        })
        bind.layoutNote.inputEditDescription.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (noteDescription != s.trimValue) {
                    changed = true
                }
                noteDescription = s.trimValue
            }
        })
        resolveUi()
    }

    private fun processResponse(response: Response<NoteType, NoteSubtype, NoteState, NoteAction, NoteItem>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<NoteType, NoteSubtype, NoteState, NoteAction, NoteItem>) {
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

    private fun processResult(state: NoteState, result: NoteItem?) {
        if (result == null) return
        this.state = state
        input = result.input
        changed = false
        when (state) {
            NoteState.FAVORITE -> {
                findMenuItemById(R.id.item_favorite)?.let {
                    it.setIcon(result.favoriteRes)
                    it.toTint(this, R.color.material_white)
                }
            }
            NoteState.LOADED -> {
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
            NoteState.ADDED,
            NoteState.EDITED -> {
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
        if (action == NoteAction.EDIT) return
        action = NoteAction.EDIT
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

    private fun openAddNoteUi() {
        val task = UiTask(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.ADD,
            null as Note?
        )
        //open(CoinActivity::class, task)
    }

    private fun onFavoriteClicked(item: NoteItem) {
        vm.toggleFavorite(item.input)
    }


    private fun openUi(item: NoteItem) {
        val task = UiTask(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.VIEW,
            item.input
        )
        //open(CoinActivity::class, task)
    }

    private fun openFavoritesUi() {
        // open(FavoriteCoinsActivity::class)
    }
}