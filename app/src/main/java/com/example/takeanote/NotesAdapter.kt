package com.example.takeanote

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.takeanote.contentprovider.NoteDbContract

class NotesAdapter(
    private var cursor: Cursor?,
    private val context: Context,
    val mOnNoteSelectedListener: OnNoteSelectedListener
) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(noteView: View) : RecyclerView.ViewHolder(noteView),
        View.OnClickListener {
        var note: NoteClass? = null
        val title: TextView = noteView.findViewById(R.id.title)

        init {
            noteView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mOnNoteSelectedListener.onNoteSelected(note!!)
            Log.i("Onclick", "${note?.id}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.notes_recycler_layout,
                parent,
                false
            )
        )

    override fun getItemCount() = cursor?.count ?: -1

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        cursor?.let {
            if (it.moveToPosition(position)) {

                holder.note = NoteClass(
                    it.getInt(it.getColumnIndex(NoteDbContract.NoteDb.COLUMN_ID)),
                    it.getString(it.getColumnIndex(NoteDbContract.NoteDb.COLUMN_NAME_TITLE)),
                    it.getString(it.getColumnIndex(NoteDbContract.NoteDb.COLUMN_NAME_CONTENT)),
                    it.getInt(it.getColumnIndex(NoteDbContract.NoteDb.COLUMN_NAME_COLOR))
                )



                holder.note?.title.let {title->
                    holder.title.text=title
                    Log.d("inside $position","${holder.title.text}")
                }

                holder.title.setBackgroundColor(
                    getColor(
                        context,
                        holder.note?.color!!
                    )
                )

            }
        }
    }

    fun swapCursor(newCursor: Cursor?) {
        cursor = newCursor
        notifyDataSetChanged()

    }
}