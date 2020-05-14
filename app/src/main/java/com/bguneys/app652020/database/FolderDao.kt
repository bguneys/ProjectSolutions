package com.bguneys.app652020.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder : Folder)

    @Update
    suspend fun update(folder : Folder)

    @Delete
    suspend fun delete(folder : Folder)

    @Query("SELECT * FROM notes_table GROUP BY folder_title")
    fun getFolderList() : LiveData<List<Folder>>

    @Query("SELECT * FROM notes_table WHERE folder_title = :folderTitle AND note_title IS NOT NULL")
    fun getFolderByTitle(folderTitle : String) : LiveData<List<Folder>>

    @Query("SELECT * FROM notes_table WHERE folder_title=:folderTitle AND note_title=:noteTitle")
    fun getFolderByFolderTitleAndNoteTitle(folderTitle: String, noteTitle : String) : LiveData<Folder>

    @Query("DELETE FROM notes_table WHERE folder_title=:folderTitle AND note_title=:noteTitle")
    suspend fun deleteByFolderTitleAndNoteTitle(folderTitle: String, noteTitle : String)


}