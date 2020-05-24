package com.bguneys.app652020.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class InfoViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _infoList = MutableLiveData<List<User>>()
    val infoList : LiveData<List<User>>
        get() = _infoList

    init {
        //TODO
    }





    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}