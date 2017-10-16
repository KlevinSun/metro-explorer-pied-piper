package com.felixlin.dcmetroexplorer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LandmarkDB(context:Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    private val SQL_CREATE_ENTRIES = "CREATE TABLE ${LandmarkEntry.TABLE_NAME} (" +
            "${LandmarkEntry._ID} INTEGER PRIMARY KEY," +
            "${LandmarkEntry.TITLE_COL} TEXT,"+
            "${LandmarkEntry.RATING_COL} TEXT,"+
            "${LandmarkEntry.IMAGE_COL} BLOB"+
            ")"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${LandmarkEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

}
