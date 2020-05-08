package com.bguneys.app652020.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bguneys.app652020.database.ProjectRepository

class NoteViewModelFactory(
    private val repository: ProjectRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}