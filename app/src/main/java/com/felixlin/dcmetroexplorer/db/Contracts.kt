package com.felixlin.dcmetroexplorer.db

import android.provider.BaseColumns

val DATABASE_NAME = "landmark.db"
val DATABASE_VERSION = 10

object LandmarkEntry: BaseColumns
{
    val TABLE_NAME = "landmark"
    val _ID = "id"
    val TITLE_COL = "title"
    val RATING_COL = "rating"
    val IMAGE_COL = "image"
}