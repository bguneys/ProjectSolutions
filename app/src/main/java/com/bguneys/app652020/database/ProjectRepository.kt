package com.bguneys.app652020.database

import android.content.Context
import androidx.lifecycle.LiveData

class ProjectRepository (
    val context: Context?
    ) {

    lateinit var folderDao : FolderDao

    companion object {
        @Volatile
        private var instance : ProjectRepository? = null

        fun getInstance(context: Context): ProjectRepository? {
            instance ?: synchronized(this) {
                instance ?: ProjectRepository(context).also { instance = it }
            }

            return instance
        }
    }

    init {
        val projectDatabase : ProjectDatabase = ProjectDatabase.getDatabaseInstance(context!!)
        folderDao = projectDatabase.folderDao
    }


    val folderList : LiveData<List<Folder>> = folderDao.getFolderList()

    fun getFolderByName(folderTitle : String) : LiveData<List<Folder>> {
        return folderDao.getFolderByName(folderTitle)
    }

    suspend fun insert(folder : Folder) {
        folderDao.insert(folder)
    }

    suspend fun update(folder : Folder) {
        folderDao.update(folder)
    }

    suspend fun delete(folder : Folder) {
        folderDao.delete(folder)
    }

}