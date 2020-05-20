package com.bguneys.app652020.planner

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    /**LiveData back properties for milli seconds obtained from Date Picker.
     * It is used to calculate time left until the event and inset data model.
     **/
    private var _datePickerMillis = MutableLiveData<Long>()
    val datePickerMillis: LiveData<Long>
        get() = _datePickerMillis

    /**LiveData back properties for result obtained from Date Picker.
    * It is used to show date in AddPlanFragment.
    **/
    private var _datePickerResult = MutableLiveData<String>()
    val datePickerResult: LiveData<String>
        get() = _datePickerResult

    //Creating Job and Scope for Coroutines
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //getting all Plan items from the database
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

    /**
     * calculation from the input taken from DatePickerFragment
     */
    fun getDatePickerResult(context : Context, year : Int, month : Int, day : Int) {
/*
        val yearString = Integer.toString(year)
        val monthString = Integer.toString(month + 1)
        val dayString = Integer.toString(day)

        if (Locale.getDefault().language == "en_US") {
            _datePickerResult.value = "$monthString / $dayString / $yearString"
        } else {
            _datePickerResult.value = "$dayString / $monthString / $yearString"
        }
*/
        _datePickerResult.value = DateUtils.formatDateTime(context, datePickerMillis.value!!, DateUtils.FORMAT_ABBREV_RELATIVE)

    }

    //cancelling job when viewmodel is finished
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //setting chosen date as milliseconds for the current Plan
    fun setDatePickerMillis(millis : Long) {
        _datePickerMillis.value = millis
    }


}