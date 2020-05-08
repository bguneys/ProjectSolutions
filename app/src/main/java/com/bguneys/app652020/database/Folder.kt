package com.bguneys.app652020.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Constants.NOTE_TABLE_NAME)
data class Folder (
    @ColumnInfo(name = Constants.COLUMN_ITEM_ID)
    @PrimaryKey(autoGenerate = true)
    var itemId : Int = 0,

    @ColumnInfo(name = Constants.COLUMN_FOLDER_TITLE)
    var folderTitle : String = "",

    @ColumnInfo(name = Constants.COLUMN_NOTE_TITLE)
    var noteTitle : String = "",

    @ColumnInfo(name = Constants.COLUMN_NOTE_TEXT)
    var noteText : String = ""
)