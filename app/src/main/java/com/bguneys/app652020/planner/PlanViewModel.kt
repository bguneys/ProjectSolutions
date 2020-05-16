package com.bguneys.app652020.planner

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bguneys.app652020.database.Plan
import com.bguneys.app652020.database.PlanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class PlanViewModel (
    val repository : PlanRepository
) : ViewModel() {

    var _datePickerMillis = MutableLiveData<Long>()
    val datePickerMillis: LiveData<Long>
        get() = _datePickerMillis

    var _datePickerResult = MutableLiveData<String>()
    val datePickerResult: LiveData<String>
        get() = _datePickerResult

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

    /**
     * calculation from the input taken from DatePickerFragment
     */
    fun getDatePickerResult(year : Int, month : Int, day : Int) {
        val yearString = Integer.toString(year)
        val monthString = Integer.toString(month + 1)
        val dayString = Integer.toString(day)

        if (Locale.getDefault().language == "en_US") {
            _datePickerResult.value = "$monthString / $dayString / $yearString"
        } else {
            _datePickerResult.value = "$dayString / $monthString / $yearString"
        }

        Log.i("PlanViewModel", "getDaePickerResult() called. Result: ${_datePickerResult.value} ")
    }




}