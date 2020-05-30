package com.everydaysolutions.bguneys.everydaysolutions.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.everydaysolutions.bguneys.everydaysolutions.database.PlanRepository

class PlanViewModelFactory(
    private val repository: PlanRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PlanViewModel::class.java)) {
            return PlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}