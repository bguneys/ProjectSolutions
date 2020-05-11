package com.bguneys.app652020.database

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    fun getFolderByTitle(folderTitle : String) : LiveData<List<Folder>> {
        return folderDao.getFolderByTitle(folderTitle)
    }

    fun getFolderByFolderTitleAndNoteTitle(folderTitle: String, noteTitle : String) : LiveData<Folder> {
        return folderDao.getFolderByFolderTitleAndNoteTitle(folderTitle, noteTitle)
    }

    suspend fun insert(folder : Folder) {
        withContext(Dispatchers.IO) {
            folderDao.insert(folder)
        }
    }

    suspend fun update(folder : Folder) {
        withContext(Dispatchers.IO) {
            folderDao.update(folder)
        }
    }

    suspend fun delete(folder : Folder) {
        folderDao.delete(folder)
    }

}