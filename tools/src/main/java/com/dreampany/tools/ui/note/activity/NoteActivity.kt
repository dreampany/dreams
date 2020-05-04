package com.dreampany.tools.ui.note.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.extension.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.misc.func.TextChangeListener
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.enums.note.NoteAction
import com.dreampany.tools.data.enums.note.NoteState
import com.dreampany.tools.data.enums.note.NoteSubtype
import com.dreampany.tools.data.enums.note.NoteType
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.data.source.note.pref.NotePref
import com.dreampany.tools.databinding.NoteActivityBinding
import com.dreampany.tools.databinding.NotesActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.note.adapter.FastNoteAdapter
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
    private lateinit var input: Note
    private lateinit var action: NoteAction

    private var edited: Boolean = false
    private var saved: Boolean = false
    private var noteTitle: String = Constants.Default.STRING
    private var noteDescription: String = Constants.Default.STRING

    override fun homeUp(): Boolean = true

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.note_activity
    override fun menuRes(): Int = R.menu.menu_note
    override fun toolbarId(): Int = R.id.toolbar

    private val isEdit: Boolean
        get() {
            return action == NoteAction.ADD || action == NoteAction.EDIT
        }

    override fun onStartUi(state: Bundle?) {
        val task: UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note> =
            (task ?: return) as UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note>
        input = task.input ?: return
        action = task.action
        initUi()
        initAd()
        onRefresh()
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
        val editItem = findMenuItemById(R.id.item_edit).toTint(this, R.color.material_white)
        val doneItem = findMenuItemById(R.id.item_done).toTint(this, R.color.material_white)

        editItem?.isVisible = !isEdit
        doneItem?.isVisible = isEdit
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_edit -> {
                return true
            }
            R.id.item_done -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

        input.title.let { noteTitle = it }
        input.description?.let { noteDescription = it }

        bind.layoutNote.inputEditTitle.addTextChangedListener(object : TextChangeListener() {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (noteTitle != s.trimValue) {
                    edited = true
                }
                noteTitle = s.trimValue
            }

        })
        bind.layoutNote.inputEditDescription.addTextChangedListener(object : TextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (noteDescription != s.trimValue) {
                    edited = true
                }
                noteDescription = s.trimValue
            }
        })
        resolveUi()
        if (action == NoteAction.EDIT || action == NoteAction.VIEW) {
            input.let {
                //TODO load note
            }
        }
    }

    private fun processResponse(response: Response<NoteType, NoteSubtype, NoteState, NoteAction, NoteItem>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<NoteType, NoteSubtype, NoteState, NoteAction, NoteItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
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

    private fun processResult(result: NoteItem?) {
        if (result != null) {
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
        action = NoteAction.EDIT
        bind.layoutNote.inputEditTitle.setText(input.title)
        bind.layoutNote.inputEditDescription.setText(input.description)
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
        edited = false
        /*val uiTask = getCurrentTask<UiTask<Note>>()!!
        val note = getInput<Note>()
        request(
            id = note?.id,
            state = State.DIALOG,
            action = uiTask.action,
            title = noteTitle,
            description = noteDescription,
            progress = true
        )*/
        return true
    }

    private fun saveDialog() {
        /*MaterialDialog(context!!).show {
            title(R.string.dialog_title_save_note)
            positiveButton(res = R.string.yes, click = {
                saveNote()
            })
            negativeButton(res = R.string.no, click = {
                edited = false
                forResult(saved)
            })
        }*/
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