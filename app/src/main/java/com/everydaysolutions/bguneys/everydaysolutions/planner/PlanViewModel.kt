package com.everydaysolutions.bguneys.everydaysolutions.planner

import android.content.Context
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.everydaysolutions.bguneys.everydaysolutions.database.Plan
import com.everydaysolutions.bguneys.everydaysolutions.database.PlanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

        _datePickerResult.value = DateUtils.formatDateTime(context, datePickerMillis.value!!,
            DateUtils.FORMAT_ABBREV_RELATIVE)

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
