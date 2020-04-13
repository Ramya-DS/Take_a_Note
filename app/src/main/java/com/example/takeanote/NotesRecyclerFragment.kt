package com.example.takeanote


import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takeanote.contentprovider.NotesContentProvider


/**
 * A simple [Fragment] subclass.
 */
class NotesRecyclerFragment : Fragment(), OnNoteSelectedListener,
    LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        fun newInstance(): NotesRecyclerFragment {
            val fragment = NotesRecyclerFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var notesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_notes_recycler, container, false)

        activity?.supportLoaderManager?.initLoader(0, null, this)

        notesRecyclerView = rootView.findViewById(R.id.notes_recycler)
        notesRecyclerView.layoutManager = LinearLayoutManager(context)
        notesRecyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                context?.applicationContext,
                R.anim.recycler_dropdown
            )
        notesRecyclerView.setHasFixedSize(false)
        notesRecyclerView.adapter = NotesAdapter(getAllNotes(), context!!, this)

        return rootView

    }

    private fun getAllNotes() =
        activity?.contentResolver?.query(NotesContentProvider.CONTENT_URI, null, null, null, null)

    override fun onNoteSelected(note: NoteClass) {
        val fragment = NoteFragment.newInstance(note, false)
        fragment.show(fragmentManager!!, "NOTE")
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(context!!, NotesContentProvider.CONTENT_URI, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        data?.moveToFirst()
        (notesRecyclerView.adapter as NotesAdapter).swapCursor(data)
        notesRecyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_dropdown)
        notesRecyclerView.scheduleLayoutAnimation()

    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        (notesRecyclerView.adapter as NotesAdapter).swapCursor(null)
    }
}
