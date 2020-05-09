package com.bguneys.app652020.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FolderDao {

    @Insert
    suspend fun insert(folder : Folder)

    @Update
    suspend fun update(folder : Folder)

    @Delete
    suspend fun delete(folder : Folder)

    @Query("SELECT * FROM notes_table GROUP BY folder_title")
    fun getFolderList() : LiveData<List<Folder>>

    @Query("SELECT * FROM notes_table WHERE folder_title = :folderTitle")
    fun getFolderByName(folderTitle : String) : LiveData<List<Folder>>

}