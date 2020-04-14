package com.example.takeanote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolBar)

        val addNote: FloatingActionButton = findViewById(R.id.add)
        addNote.setOnClickListener {
            NoteFragment.newInstance(NoteClass(0, "No title", null, BackgroundColor.random()), true)
                .show(supportFragmentManager, "NOTE")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.content_fragment, NotesRecyclerFragment.newInstance())
            .commit()
    }
}
