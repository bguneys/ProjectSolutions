package com.bguneys.app652020.note

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bguneys.app652020.database.Folder
import com.bguneys.app652020.database.ProjectRepository

class NoteViewModel (
    val repository : ProjectRepository) : ViewModel() {

    val folderList : LiveData<List<Folder>> = repository.folderList
}