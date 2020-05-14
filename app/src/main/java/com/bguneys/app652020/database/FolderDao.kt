package com.bguneys.app652020.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(folder : Folder)

    @Update
    fun update(folder : Folder)

    @Delete
    fun delete(folder : Folder)

    @Query("SELECT * FROM notes_table GROUP BY folder_title")
    fun getFolderList() : LiveData<List<Folder>>

    @Query("SELECT * FROM notes_table WHERE folder_title = :folderTitle AND note_title IS NOT NULL")
    fun getFolderByTitle(folderTitle : String) : LiveData<List<Folder>>

    @Query("SELECT * FROM notes_table WHERE folder_title=:folderTitle AND note_title=:noteTitle")
    fun getFolderByFolderTitleAndNoteTitle(folderTitle: String, noteTitle : String) : LiveData<Folder>

    @Query("DELETE FROM notes_table WHERE folder_title=:folderTitle AND note_title=:noteTitle")
    fun deleteByFolderTitleAndNoteTitle(folderTitle: String, noteTitle : String)


}