package com.everydaysolutions.bguneys.everydaysolutions.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.everydaysolutions.bguneys.everydaysolutions.database.Folder
import com.everydaysolutions.bguneys.everydaysolutions.database.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteViewModel (
    val repository : ProjectRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val folderList : LiveData<List<Folder>> = repository.folderList

    fun getFolderByTitle(folderTitle : String) : LiveData<List<Folder>> {
        return repository.getFolderByTitle(folderTitle)
    }

    fun getFolderByFolderTitleAndNoteTitle(folderTitle: String, noteTitle : String) : LiveData<Folder> {
        return repository.getFolderByFolderTitleAndNoteTitle(folderTitle, noteTitle)
    }

    fun deleteByFolderTitleAndNoteTitle(folderTitle: String, noteTitle : String) {
        uiScope.launch {
            repository.deleteByFolderTitleAndNoteTitle(folderTitle, noteTitle)
        }
    }

    fun deleteByFolderTitle(folderTitle: String) {
        uiScope.launch {
            repository.deleteByFolderTitle(folderTitle)
        }
    }

    fun update(folder : Folder) {
        uiScope.launch {
            repository.update(folder)
        }
    }

    fun insert(folder : Folder) {
        uiScope.launch {
            repository.insert(folder)
        }
    }

    fun delete(folder : Folder) {
        uiScope.launch {
            repository.delete(folder)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
