package com.example.takeanote.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log


class NotesContentProvider : ContentProvider() {

    private lateinit var dbHelper: NoteDbHelper

    companion object {
        private const val AUTHORITY = "com.example.takeanote.contentprovider"
        val CONTENT_URI: Uri =
            Uri.parse("content://" + AUTHORITY + "/" + NoteDbContract.NoteDb.TABLE_NAME)
        private const val ALL_NOTES = 1
        const val SINGLE_NOTE = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(
                "com.example.takeanote.contentprovider",
                NoteDbContract.NoteDb.TABLE_NAME,
                ALL_NOTES
            )
            //TODO this URI should be matching single notes, right ?
            addURI(
                "com.example.takeanote.contentprovider",
                NoteDbContract.NoteDb.TABLE_NAME + "/#",
                SINGLE_NOTE
            )
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val newRowId = db?.insert(NoteDbContract.NoteDb.TABLE_NAME, null, values)
        Log.d("insert", "insert")
        context?.contentResolver?.notifyChange(uri, null)

        return Uri.parse("$CONTENT_URI/$newRowId")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.writableDatabase
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = NoteDbContract.NoteDb.TABLE_NAME
        when (uriMatcher.match(uri)) {
            SINGLE_NOTE -> {
                val id = "_ID ${uri.lastPathSegment}"
                queryBuilder.appendWhere(id)
            }
        }
        Log.d("query", "$uri")
        val cursor =
            queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun onCreate(): Boolean {
        dbHelper = NoteDbHelper(context!!)
        return false
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val db = dbHelper.writableDatabase
        val id = "${NoteDbContract.NoteDb.COLUMN_ID}=${uri.lastPathSegment}"
        val updateCount = db.update(NoteDbContract.NoteDb.TABLE_NAME, values, id, null)
        context?.contentResolver?.notifyChange(uri, null)
        Log.i("update", "$uri  $updateCount")
        return updateCount
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        val id = "${NoteDbContract.NoteDb.COLUMN_ID}=${uri.lastPathSegment}"
        val deleteCount = db.delete(NoteDbContract.NoteDb.TABLE_NAME, id, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        Log.i("delete", "$uri  $deleteCount")
        return deleteCount
    }

    override fun getType(uri: Uri): String? =
        when (uriMatcher.match(uri)) {
            SINGLE_NOTE -> "vnd.android.cursor.item/vnd.com.example.takeanote.contentprovider.${NoteDbContract.NoteDb.TABLE_NAME}"
            ALL_NOTES -> "vnd.android.cursor.item/vnd.com.example.takeanote.contentprovider.${NoteDbContract.NoteDb.TABLE_NAME}"
            else ->
                throw  IllegalArgumentException("Unsupported URI: $uri")
        }
}