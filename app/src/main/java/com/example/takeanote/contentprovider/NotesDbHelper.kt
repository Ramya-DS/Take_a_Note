package com.example.takeanote.contentprovider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object NoteDbContract {

    object NoteDb : BaseColumns {
        const val TABLE_NAME = "AllNotes"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_CONTENT = "content"
        const val COLUMN_NAME_COLOR = "color"
    }
}

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${NoteDbContract.NoteDb.TABLE_NAME} (" +
            "${NoteDbContract.NoteDb.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${NoteDbContract.NoteDb.COLUMN_NAME_TITLE} TEXT DEFAULT 'NO TITLE'," +
            "${NoteDbContract.NoteDb.COLUMN_NAME_CONTENT} TEXT DEFAULT NULL," +
            "${NoteDbContract.NoteDb.COLUMN_NAME_COLOR} TEXT)"

class NoteDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        for (version in oldVersion + 1..newVersion) {
//            when (version) {
//                2 ->  // make changes in version 2 using alter table/necessary statement
//                3 ->  // make changes in version 3 using alter table/necessary statement
//            }
//        }
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "TakeANote.db"
    }

}