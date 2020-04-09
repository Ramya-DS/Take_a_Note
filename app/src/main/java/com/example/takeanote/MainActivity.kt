package com.example.takeanote

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.takeanote.contentprovider.NoteDbContract
import com.example.takeanote.contentprovider.NotesContentProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var toolBar: Toolbar
    lateinit var addNote: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolBar)

        addNote = findViewById(R.id.add)
        addNote.setOnClickListener {
            NoteFragment.newInstance(NoteClass(0, "No title", null, BackgroundColor.random()), true)
                .show(supportFragmentManager, "NOTE")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.content_fragment, NotesRecyclerFragment.newInstance())
            .commit()
    }
}
