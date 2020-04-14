package com.example.takeanote


import android.app.Activity
import android.content.AsyncQueryHandler
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.takeanote.contentprovider.NoteDbContract
import com.example.takeanote.contentprovider.NotesContentProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference

/**
 * A simple [Fragment] subclass.
 */
class NoteFragment : DialogFragment() {

    companion object {
        fun newInstance(note: NoteClass, add: Boolean): NoteFragment {
            val fragment = NoteFragment()
            val bundle = Bundle()
            bundle.putBoolean("add", add)
            note.let {
                bundle.putInt("id", it.id)
                bundle.putString("title", it.title)
                bundle.putString("content", it.content)
                bundle.putInt("color", it.color)
            }
            fragment.arguments = bundle
            return fragment
        }

        class NoteAsyncHandler(contentResolver: WeakReference<ContentResolver>) :
            AsyncQueryHandler(contentResolver.get()) {

            fun insert(note: NoteClass) {
                val values = ContentValues().apply {
                    this.put(NoteDbContract.NoteDb.COLUMN_NAME_TITLE, note.title)
                    this.put(NoteDbContract.NoteDb.COLUMN_NAME_CONTENT, note.content)
                    this.put(NoteDbContract.NoteDb.COLUMN_NAME_COLOR, note.color)
                }
                Log.d("insert", "async")
                startInsert(0, null, NotesContentProvider.CONTENT_URI, values)
            }

            fun delete(note: NoteClass) {
                startDelete(
                    0,
                    null,
                    Uri.parse("${NotesContentProvider.CONTENT_URI}/${note.id}"),
                    null,
                    null
                )
            }

            fun update(note: NoteClass) {
                val values = ContentValues().apply {
                    this.put(NoteDbContract.NoteDb.COLUMN_NAME_TITLE, note.title)
                    this.put(NoteDbContract.NoteDb.COLUMN_NAME_CONTENT, note.content)
                    this.put(NoteDbContract.NoteDb.COLUMN_NAME_COLOR, note.color)
                    this.put(NoteDbContract.NoteDb.COLUMN_ID, note.id)
                }

                startUpdate(
                    0, null,
                    Uri.parse("${NotesContentProvider.CONTENT_URI}/${note.id}"), values, null, null
                )
            }
        }
    }

    private lateinit var currentNote: NoteClass
    private lateinit var rootView: View
    private var add: Boolean = false
    private lateinit var handler: NoteAsyncHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        handler = NoteAsyncHandler(WeakReference(activity!!.applicationContext.contentResolver))

        rootView = inflater.inflate(R.layout.fragment_note, container, false)
        val title: EditText = rootView.findViewById(R.id.title)
        val content: EditText = rootView.findViewById(R.id.content)

        currentNote.title.let { title.text.insert(title.selectionStart, it) }
        currentNote.content?.let { content.text.insert(content.selectionStart, it) }
        rootView.setBackgroundColor(getColor(context!!, currentNote.color))

        val noteBottomBar =
            rootView.findViewById<BottomNavigationView>(R.id.note_bottomNavigationView)


        if (add) {
            noteBottomBar.menu.getItem(0).isVisible = false
        }
        noteBottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.save -> {
                    currentNote.title = title.text.toString()
                    currentNote.content = content.text.toString()

                    if (add)
                        handler.insert(currentNote)
                    else
                        handler.update(currentNote)
                    this.dismiss()
                    true
                }
                R.id.color -> {
                    val childFragment = ColorPickerFragment.newInstance()
                    childFragment.setTargetFragment(this@NoteFragment, 1)
                    childFragment.show(fragmentManager!!, "COLORPICKER")
                    true
                }
                //TODO Do we need delete action while adding a Note ?
                R.id.delete -> {
                    handler.delete(currentNote)
                    this.dismiss()
                    true
                }
                else -> {
                    false
                }
            }
        }
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            add = it.getBoolean("add")
            currentNote = NoteClass(
                it.getInt("id"),
                it.getString("title")!!,
                it.getString("content"),
                it.getInt("color")
            )
        }
        savedInstanceState?.let {
            currentNote = NoteClass(
                it.getInt("id"),
                it.getString("title")!!,
                it.getString("content"),
                it.getInt("color")
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            currentNote.color = data?.getIntExtra("color", BackgroundColor.random())!!
            rootView.setBackgroundColor(getColor(context!!, currentNote.color))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.let {
            it.putInt("id", currentNote.id)
            it.putString("title", currentNote.title)
            it.putString("content", currentNote.content)
            it.putInt("color", currentNote.color)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

}
