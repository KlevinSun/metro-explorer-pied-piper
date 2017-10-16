package com.felixlin.dcmetroexplorer.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.felixlin.dcmetroexplorer.db.LandmarkEntry.IMAGE_COL
import com.felixlin.dcmetroexplorer.db.LandmarkEntry.RATING_COL
import com.felixlin.dcmetroexplorer.db.LandmarkEntry.TABLE_NAME
import com.felixlin.dcmetroexplorer.db.LandmarkEntry.TITLE_COL
import com.felixlin.dcmetroexplorer.db.LandmarkEntry._ID
import com.felixlin.dcmetroexplorer.model.Landmark

class LandmarkDbTable(context: Context)
{
    private  val TAG = LandmarkDbTable::class.java.simpleName
    private val dbHelper = LandmarkDB(context)

    fun store(landmark: Landmark): Long
    {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(TITLE_COL, landmark.name)
            put(RATING_COL, landmark.rating)
            put(IMAGE_COL, landmark.imageUrl)
        }

        val id = db.transaction {
            insert(TABLE_NAME, null, values)
        }

        db.close()

        Log.d(TAG, "Stored new landmark to the DB $landmark")

        return id
    }

    private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T): T {
        beginTransaction()
        val result = try {
            val returnValue = function()
            setTransactionSuccessful()

            returnValue
        } finally {
            endTransaction()
        }
        close()

        return result
    }

    fun readAllHabits(): List<Landmark> {

        val columns = arrayOf(_ID, TITLE_COL, RATING_COL, IMAGE_COL)

        val db = dbHelper.readableDatabase

        val order = "$_ID ASC"

        val cursor = db.doQuery(TABLE_NAME, columns, orderBy = order)

        return parseHabitFromCursor(cursor)
    }

    private fun parseHabitFromCursor(cursor: Cursor): MutableList<Landmark> {
        val landmarks = mutableListOf<Landmark>()
        while (cursor.moveToNext()) {
            val title = cursor.getString(TITLE_COL)
            val rating = cursor.getString(RATING_COL)
            val image = cursor.getString(IMAGE_COL)
            landmarks.add(Landmark(title, rating.toDouble(), image))
        }
        cursor.close()

        return landmarks
    }

    private fun SQLiteDatabase.doQuery(table: String, columns: Array<String>, selection: String? = null,
                                       selectionArgs:  Array<String>? = null, groupBy: String? = null,
                                       having: String? = null, orderBy: String? = null): Cursor {

        return query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
    }

    private fun Cursor.getString(columnName: String) = getString(getColumnIndex(columnName))
}