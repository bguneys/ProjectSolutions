package com.bguneys.app652020.planner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bguneys.app652020.database.Plan
import com.bguneys.app652020.database.PlanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlanViewModel (
    val repository : PlanRepository
) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val planList : LiveData<List<Plan>> = repository.planList

    fun update(plan : Plan) {
        uiScope.launch {
            repository.update(plan)
        }
    }

    fun insert(plan : Plan) {
        uiScope.launch {
            repository.insert(plan)
        }
    }

    fun delete(plan : Plan) {
        uiScope.launch {
            repository.delete(plan)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}